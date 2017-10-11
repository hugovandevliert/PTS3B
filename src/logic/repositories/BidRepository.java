package logic.repositories;

import data.interfaces.IBidContext;
import models.Bid;
import java.sql.SQLException;
import java.util.ArrayList;

public class BidRepository {

    final private IBidContext context;

    public BidRepository(final IBidContext context) {
        this.context = context;
    }

    public ArrayList<Bid> getBids(final int auctionId) throws SQLException {
        return context.getBids(auctionId);
    }
}
