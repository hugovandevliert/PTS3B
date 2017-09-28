package data.interfaces;

import models.Auction;
import models.Profile;
import utilities.enums.Status;

import java.util.ArrayList;

public interface IAuctionContext {

    ArrayList<Auction> getAuctions(String searchTerm);
    boolean addAuction(Profile profile, Auction auction);
    boolean setStatus(Status status, int auctionId);
    boolean endAuction(int auctionId);
}
