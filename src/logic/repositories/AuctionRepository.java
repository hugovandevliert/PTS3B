package logic.repositories;

import data.interfaces.IAuctionContext;
import models.Auction;
import models.Profile;
import utilities.enums.Status;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuctionRepository {

    final private IAuctionContext context;

    public AuctionRepository(final IAuctionContext context) {
        this.context = context;
    }

    public ArrayList<Auction> getAuctionsForSearchTerm(final String searchTerm) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionsForSearchTerm(searchTerm);
    }

    public Auction getAuctionForId(final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionForId(auctionId);
    }

    public boolean addAuction(final Auction auction) throws SQLException {
        return context.addAuction(auction);
    }

    public boolean setStatus(final Status status, final int auctionId) {
        return context.setStatus(status, auctionId);
    }

    public boolean endAuction(final int auctionId) {
        return context.endAuction(auctionId);
    }
}
