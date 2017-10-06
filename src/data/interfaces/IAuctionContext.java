package data.interfaces;

import models.Auction;
import models.Profile;
import utilities.enums.Status;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IAuctionContext {

    ArrayList<Auction> getAuctionsForSearchTerm(String searchTerm) throws SQLException, IOException, ClassNotFoundException;
    boolean addAuction(Profile profile, Auction auction);
    boolean setStatus(Status status, int auctionId);
    boolean endAuction(int auctionId);
}
