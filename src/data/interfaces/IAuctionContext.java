package data.interfaces;

import models.Auction;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IAuctionContext {

    ArrayList<Auction> getAuctionsForSearchTerm(final String searchTerm) throws SQLException, IOException, ClassNotFoundException;
    Auction getAuctionForId(final int auctionId) throws SQLException, IOException, ClassNotFoundException;
    boolean addAuction(final Auction auction) throws SQLException;
    boolean manuallyEndAuction(final int auctionId);
    boolean auctionIsClosed(final int auctionId) throws SQLException;
}
