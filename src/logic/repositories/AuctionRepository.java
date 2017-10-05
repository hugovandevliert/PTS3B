package logic.repositories;

import data.interfaces.IAuctionContext;
import models.Auction;
import models.Profile;
import utilities.enums.Status;

import java.sql.SQLException;
import java.util.ArrayList;

public class AuctionRepository {

    private IAuctionContext context;

    public AuctionRepository(IAuctionContext context) {
        this.context = context;
    }

    public ArrayList<Auction> getAuctionsForSearchTerm(String searchTerm) throws SQLException {
        return context.getAuctionsForSearchTerm(searchTerm);
    }

    public boolean addAuction(Profile profile, Auction auction) {
        return context.addAuction(profile, auction);
    }

    public boolean setStatus(Status status, int auctionId) {
        return context.setStatus(status, auctionId);
    }

    public boolean endAuction(int auctionId) {
        return context.endAuction(auctionId);
    }
}
