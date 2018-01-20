package utilities.publisher;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RemotePublisher extends UnicastRemoteObject implements IRemotePublisherForListener, IRemotePublisherForDomain {

    Publisher publisher;

    public RemotePublisher() throws RemoteException {
        this.publisher = new Publisher();
    }

    public RemotePublisher(String[] properties) throws RemoteException {
        this.publisher = new Publisher(properties);
    }

    public void subscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {
        this.publisher.subscribeRemoteListener(listener, property);
    }

    public void subscribeLocalListener(ILocalPropertyListener listener, String property) {
        this.publisher.subscribeLocalListener(listener, property);
    }

    public void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {
        this.publisher.unsubscribeRemoteListener(listener, property);
    }

    public void unsubscribeLocalListener(ILocalPropertyListener listener, String property) {
        this.publisher.unsubscribeLocalListener(listener, property);
    }

    public void registerProperty(String property) throws RemoteException {
        this.publisher.registerProperty(property);
    }

    public void unregisterProperty(String property) throws RemoteException {
        this.publisher.unregisterProperty(property);
    }

    public void inform(String property, Object oldValue, Object newValue) throws RemoteException {
        this.publisher.inform(property, oldValue, newValue);
    }

    public List<String> getProperties() throws RemoteException {
        return this.publisher.getProperties();
    }
}
