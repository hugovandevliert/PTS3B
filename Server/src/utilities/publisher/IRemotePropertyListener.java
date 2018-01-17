package utilities.publisher;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemotePropertyListener extends IPropertyListener, Remote {

    void propertyChange(PropertyChangeEvent var1) throws RemoteException;
}
