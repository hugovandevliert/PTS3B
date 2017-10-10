package logic.repositories;

import data.interfaces.IBidContext;
import models.Bid;

import java.sql.SQLException;
import java.util.ArrayList;

public class BidRepository {

    private IBidContext context;

    public BidRepository(IBidContext context) {
        this.context = context;
    }

    public ArrayList<Bid> getBids(int auctionId) throws SQLException {
        return context.getBids(auctionId);
    }
}
