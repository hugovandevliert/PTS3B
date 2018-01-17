package utilities.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface ITimeServer extends Remote {

    LocalDateTime getTime() throws RemoteException;
}
