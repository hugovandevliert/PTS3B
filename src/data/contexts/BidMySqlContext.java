package data.contexts;

import data.interfaces.IBidContext;
import models.Bid;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class BidMySqlContext implements IBidContext {

    @Override
    public ArrayList<Bid> getBids(int auctionId) {
        throw new NotImplementedException();
    }
}
