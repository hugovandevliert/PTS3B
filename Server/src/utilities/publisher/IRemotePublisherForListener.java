package utilities.publisher;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemotePublisherForListener extends Remote {

    void subscribeRemoteListener(IRemotePropertyListener var1, String var2) throws RemoteException;

    void unsubscribeRemoteListener(IRemotePropertyListener var1, String var2) throws RemoteException;
}