package utilities.publisher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemotePublisherForDomain extends Remote {

    void registerProperty(String var1) throws RemoteException;

    void unregisterProperty(String var1) throws RemoteException;

    void inform(String var1, Object var2, Object var3) throws RemoteException;

    List<String> getProperties() throws RemoteException;
}
