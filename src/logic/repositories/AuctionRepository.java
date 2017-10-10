package logic.repositories;

import data.interfaces.IAuctionContext;
import models.Auction;
import models.Profile;
import utilities.enums.Status;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuctionRepository {

    private IAuctionContext context;

    public AuctionRepository(IAuctionContext context) {
        this.context = context;
    }

    public ArrayList<Auction> getAuctionsForSearchTerm(String searchTerm) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionsForSearchTerm(searchTerm);
    }

    public Auction getAuctionForId(int auctionId) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionForId(auctionId);
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
