package logic.clients;

import core.javafx.menu.MenuController;
import utilities.Constants;
import utilities.enums.AlertType;
import utilities.interfaces.ITimeServer;

import java.net.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeClient {

    private ITimeServer timeServer;

    public TimeClient(final Registry registry) throws ConnectException {
        if (registry != null){
            try {
                timeServer = (ITimeServer) registry.lookup(Constants.CHANGED_PROPERTY_TIME_SERVER);
            } catch (RemoteException e) {
                MenuController.showAlertMessage("ConnectException: Could not connect to Time Server!", AlertType.ERROR, 5000);
                Logger.getLogger(TimeClient.class.getName()).log(Level.SEVERE, e.toString());
            } catch (NotBoundException e) {
                MenuController.showAlertMessage("ConnectException: Could not connect to Time Server!", AlertType.ERROR, 5000);
                Logger.getLogger(TimeClient.class.getName()).log(Level.SEVERE, e.toString());
            }
        }else{
            throw new ConnectException("Could not connect to registry");
        }
    }

    public LocalDateTime getTime() {
        try {
            return timeServer.getTime();
        } catch (RemoteException e) {
            MenuController.showAlertMessage("ConnectException: Could not connect to Time Server!", AlertType.ERROR, 5000);
            Logger.getLogger(TimeClient.class.getName()).log(Level.SEVERE, e.toString());
        }
        return null;
    }
}
