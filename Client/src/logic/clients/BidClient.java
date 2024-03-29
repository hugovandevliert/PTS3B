package logic.clients;

import core.javafx.auction.AuctionController;
import core.javafx.menu.MenuController;
import javafx.application.Platform;
import logic.algorithms.MusicPlayer;
import logic.managers.RMIClientsManager;
import models.Bid;
import utilities.Constants;
import utilities.enums.AlertType;
import utilities.interfaces.IBidClient;
import utilities.interfaces.IBidServer;
import utilities.publisher.IRemotePublisherForListener;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BidClient extends UnicastRemoteObject implements IBidClient {

    private final int auctionId;
    private final int currentUserId;
    private transient final AuctionController auctionController;
    private transient final MusicPlayer musicPlayer;
    private transient IBidServer server;

    public BidClient(final Registry registry, final int auctionId, final int currentUserId, final RMIClientsManager rmiClientsManager, final AuctionController auctionController) throws IOException, NotBoundException {
        super();
        this.auctionId = auctionId;
        this.currentUserId = currentUserId;
        this.auctionController = auctionController;
        this.musicPlayer = new MusicPlayer(Constants.BEEP_NEW_BID_MP3);

        /* This is needed to assure we will not get a connection refused. The server also has this line of code. When the IP changes it should be edited in the server as well */
        System.setProperty("java.rmi.server.hostname", Constants.SERVER_IP);

        IRemotePublisherForListener messageListener = (IRemotePublisherForListener) registry.lookup(Constants.SERVER_NAME_THAT_PUSHES_TO_CLIENTS);
        messageListener.subscribeRemoteListener(this, Constants.CHANGED_PROPERTY_BID_SERVER);
        rmiClientsManager.addBidServerMessageListener(messageListener, this);

        server = (IBidServer) registry.lookup(Constants.SERVER_NAME_THAT_RECEIVES_FROM_CLIENTS);
    }

    public void sendBid(final Bid bid) {
        try {
            server.sendBid(bid);
        } catch (RemoteException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    @Override
    public int getAuctionId() {
        return this.auctionId;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        // The server sent us a new bid. We should display it.
        final Bid bid = (Bid) propertyChangeEvent.getNewValue();

        /* We do want to display that a new bid has been added to the user, if the user did not just place this bid himself. */
        if (!isCurrentUser(bid.getProfile().getProfileId())) {
            Platform.runLater(() -> MenuController.showAlertMessage("A new bid has been added!", AlertType.MESSAGE));
            musicPlayer.playSound();
        }

        Platform.runLater(() -> {
            auctionController.addBidToList(bid);
            auctionController.addBidsToInterface();
            auctionController.setBidTextfieldPromptText(bid);
        });
    }

    private boolean isCurrentUser(final int userId) {
        return userId == this.currentUserId;
    }
}
