package utilities.interfaces;

import models.Bid;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBidServer extends Remote {

    void sendBid(final Bid bid) throws RemoteException;
}
