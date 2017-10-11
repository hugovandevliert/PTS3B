package data.interfaces;

import models.Auction;
import models.Profile;
import utilities.enums.Status;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IAuctionContext {

    ArrayList<Auction> getAuctionsForSearchTerm(final String searchTerm) throws SQLException, IOException, ClassNotFoundException;
    Auction getAuctionForId(final int auctionId) throws SQLException, IOException, ClassNotFoundException;
    boolean addAuction(final Auction auction) throws SQLException;
    boolean setStatus(final Status status, int auctionId);
    boolean endAuction(final int auctionId);
}
