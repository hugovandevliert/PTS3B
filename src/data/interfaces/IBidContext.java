package data.interfaces;

import modelslibrary.Bid;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IBidContext {

    ArrayList<Bid> getBids(final int auctionId) throws SQLException, IOException, ClassNotFoundException;
    Bid getMostRecentBidForAuctionWithId(final int auctionId) throws SQLException, IOException, ClassNotFoundException;
}
