package utilities.interfaces;

import custompublisher.IRemotePropertyListener;

import java.rmi.RemoteException;

public interface IBidClient extends IRemotePropertyListener {

    int getAuctionId() throws RemoteException;
}
