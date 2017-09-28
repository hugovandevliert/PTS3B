package logic.repositories;

import data.interfaces.IBidContext;
import models.Bid;

import java.util.ArrayList;

public class BidRepository {

    private IBidContext context;

    public BidRepository(IBidContext context) {
        this.context = context;
    }

    public ArrayList<Bid> getBids(int auctionId) {
        return context.getBids(auctionId);
    }
}
