package data.contexts;

import data.interfaces.IAuctionContext;
import models.Auction;
import models.Profile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.enums.Status;

import java.util.ArrayList;

public class AuctionMySqlContext implements IAuctionContext {

    @Override
    public ArrayList<Auction> getAuctions(String searchTerm) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAuction(Profile profile, Auction auction) {
        throw new NotImplementedException();
    }

    @Override
    public boolean setStatus(Status status, int auctionId) {
        throw new NotImplementedException();
    }

    @Override
    public boolean endAuction(int auctionId) {
        throw new NotImplementedException();
    }
}
