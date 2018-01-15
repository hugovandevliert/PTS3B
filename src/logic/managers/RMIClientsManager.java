package logic.managers;

import core.javafx.menu.MenuController;
import custompublisher.IRemotePropertyListener;
import custompublisher.IRemotePublisherForListener;
import logic.clients.BidClient;
import utilities.Constants;
import utilities.enums.AlertType;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClientsManager {

    private Registry bidsRegistry;

    private BidClient bidClient;
    private IRemotePublisherForListener bidServerMessageListener;
    private IRemotePropertyListener bidRemotePropertyListener;

    RMIClientsManager() {
        try {
            bidsRegistry = LocateRegistry.getRegistry("localhost", Constants.PORT_NUMBER);
        } catch (RemoteException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    public void addBidClient(final BidClient bidClient) {
        if (bidClient != null) {
            this.bidClient = bidClient;
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
                bidServerMessageListener.unsubscribeRemoteListener(bidRemotePropertyListener, Constants.CHANGED_PROPERTY);
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

    public Registry getBidsRegistry() {
        return bidsRegistry;
    }
}
