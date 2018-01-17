package server;

import models.Bid;
import server.timeserver.TimeServer;
import utilities.Constants;
import utilities.interfaces.IBidServer;
import utilities.interfaces.ITimeServer;
import utilities.publisher.IRemotePublisherForDomain;
import utilities.publisher.RemotePublisher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements IBidServer {

    private Registry registry = null;
    private IRemotePublisherForDomain publisher;
    private ITimeServer timeServer;

    protected RMIServer() throws RemoteException {
        super();

        System.setProperty("java.rmi.server.hostname", Constants.SERVER_IP);

        publisher = new RemotePublisher();
        this.publisher.registerProperty(Constants.CHANGED_PROPERTY_BID_SERVER);
        System.out.println("Started publisher and registered " + Constants.CHANGED_PROPERTY_BID_SERVER + " property");

        registry = LocateRegistry.createRegistry(Constants.PORT_NUMBER);
        System.out.println("Created registry on port " + Constants.PORT_NUMBER);

        registry.rebind(Constants.SERVER_NAME_THAT_PUSHES_TO_CLIENTS, publisher);
        System.out.println("Rebinded " + Constants.SERVER_NAME_THAT_PUSHES_TO_CLIENTS + " to publisher for message pushing towards clients");

        registry.rebind(Constants.SERVER_NAME_THAT_RECEIVES_FROM_CLIENTS, this);
        System.out.println("Rebinded " + Constants.SERVER_NAME_THAT_RECEIVES_FROM_CLIENTS + " to publisher for message receiving from clients");

        timeServer = new TimeServer();
        System.out.println("Created Time Server");

        registry.rebind(Constants.CHANGED_PROPERTY_TIME_SERVER, timeServer);
        System.out.println("Rebinded " + Constants.CHANGED_PROPERTY_TIME_SERVER + " for Time Server");
    }

    public static void main(String[] args) {
        try {
            final RMIServer rmiServer = new RMIServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendBid(final Bid bid) throws RemoteException {
        // A client has sent a new bid. We should send this bid to all the clients that are registered for this auction
        publisher.inform(Constants.CHANGED_PROPERTY_BID_SERVER, null, bid);
    }
}
