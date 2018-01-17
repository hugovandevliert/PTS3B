package utilities.interfaces;


import utilities.publisher.IRemotePropertyListener;

import java.rmi.RemoteException;

public interface IBidClient extends IRemotePropertyListener {

    int getAuctionId() throws RemoteException;
}
