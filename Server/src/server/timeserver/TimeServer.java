package server.timeserver;

import utilities.interfaces.ITimeServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class TimeServer extends UnicastRemoteObject implements ITimeServer {

    public TimeServer() throws RemoteException {
        super();
    }

    @Override
    public LocalDateTime getTime() throws RemoteException {
        return LocalDateTime.now();
    }
}
