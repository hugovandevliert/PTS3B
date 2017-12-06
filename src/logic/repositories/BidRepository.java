package logic.repositories;

import data.interfaces.IBidContext;
import modelslibrary.Bid;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class BidRepository {

    private final IBidContext context;

    public BidRepository(final IBidContext context) {
        this.context = context;
    }

    public ArrayList<Bid> getBids(final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        return context.getBids(auctionId);
    }

    public Bid getMostRecentBidForAuctionWithId(final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        return context.getMostRecentBidForAuctionWithId(auctionId);
    }
}
