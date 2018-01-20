package logic.managers;

import core.javafx.menu.MenuController;
import logic.clients.BidClient;
import logic.clients.TimeClient;
import utilities.Constants;
import utilities.enums.AlertType;
import utilities.publisher.IRemotePropertyListener;
import utilities.publisher.IRemotePublisherForListener;

import java.net.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIClientsManager {

    private Registry bidsRegistry;

    private BidClient bidClient;
    private TimeClient timeClient;
    private IRemotePublisherForListener bidServerMessageListener;
    private IRemotePropertyListener bidRemotePropertyListener;

    RMIClientsManager() {
        try {
            bidsRegistry = LocateRegistry.getRegistry("localhost", Constants.PORT_NUMBER);

            addTimeClient(new TimeClient(bidsRegistry));
        } catch (RemoteException | ConnectException e) {
            Logger.getLogger(RMIClientsManager.class.getName()).log(Level.SEVERE, e.toString());
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    public void addBidClient(final BidClient bidClient) {
        if (bidClient != null) {
            this.bidClient = bidClient;
        }
    }

    private void addTimeClient(final TimeClient timeClient) {
        if (timeClient != null){
            this.timeClient = timeClient;
        }
    }

    public void addBidServerMessageListener(final IRemotePublisherForListener serverMessageListener, final IRemotePropertyListener remotePropertyListener) {
        this.bidServerMessageListener = serverMessageListener;
        this.bidRemotePropertyListener = remotePropertyListener;
    }

    public void unsubscribeRemoteListeners() {
        /* This is needed to assure we can re-subscribe ourselves without getting connection refused from the server */
        try {
            if (bidServerMessageListener != null) {
                bidServerMessageListener.unsubscribeRemoteListener(bidRemotePropertyListener, Constants.CHANGED_PROPERTY_BID_SERVER);
            }

            this.bidServerMessageListener = null;
            this.bidRemotePropertyListener = null;
            this.bidClient = null;
        } catch (RemoteException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    public BidClient getBidClient() {
        return bidClient;
    }

    public TimeClient getTimeClient() {
        return timeClient;
    }

    public Registry getBidsRegistry() {
        return bidsRegistry;
    }
}
