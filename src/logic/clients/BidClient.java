package logic.clients;

import core.javaFX.auction.AuctionController;
import core.javaFX.menu.MenuController;
import custompublisher.IRemotePropertyListener;
import custompublisher.IRemotePublisherForListener;
import ibidserver.IBidServer;
import javafx.application.Platform;
import logic.managers.RMIClientsManager;
import modelslibrary.Bid;
import utilities.Constants;
import utilities.enums.AlertType;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BidClient extends UnicastRemoteObject implements IRemotePropertyListener {

    private IRemotePublisherForListener messageListener;
    private IBidServer server;

    private final int auctionId;
    private final AuctionController auctionController;

    public BidClient(final Registry registry, final int auctionId, final RMIClientsManager rmiClientsManager, final AuctionController auctionController) throws IOException, NotBoundException {
        super();
        this.auctionId = auctionId;
        this.auctionController = auctionController;

        /* This is needed to assure we will not get a connection refused. The server also has this line of code. When the IP changes it should be edited in the server as well */
        System.setProperty("java.rmi.server.hostname", Constants.SERVER_IP);

        messageListener = (IRemotePublisherForListener) registry.lookup(Constants.SERVER_NAME_THAT_PUSHES_TO_CLIENTS);
        messageListener.subscribeRemoteListener(this, Constants.CHANGED_PROPERTY);
        rmiClientsManager.addBidServerMessageListener(messageListener, this);
        System.out.println("Subscribed message listener for receiving bids from server");

        server = (IBidServer) registry.lookup(Constants.SERVER_NAME_THAT_RECEIVES_FROM_CLIENTS);
        System.out.println("Connected to server for sending bids towards server");
    }

    public void sendBid(final Bid bid) {
        try {
            server.sendBid(bid);
        } catch (RemoteException e) {
            e.printStackTrace();
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    @Override
    public int getAuctionId() throws RemoteException {
        return this.auctionId;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
        // The server sent us a new bid. We should display it.
        final Bid bid = (Bid) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            auctionController.addBidToList(bid);
            auctionController.addBidsToInterface(true);
        });
    }
}
