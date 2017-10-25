package logic.repositories;

import data.interfaces.IAuctionContext;
import models.Auction;
import utilities.enums.AuctionLoadingType;
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

    public ArrayList<Auction> getAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionsForProfile(profileId);
    }

    public ArrayList<Auction> getFavoriteAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        return context.getFavoriteAuctionsForProfile(profileId);
    }

    public Auction getAuctionForId(final int auctionId, final AuctionLoadingType auctionLoadingType) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionForId(auctionId, auctionLoadingType);
    }

    public boolean addAuction(final Auction auction) throws SQLException {
        return context.addAuction(auction);
    }

    public boolean addBid(final double amount, final int accountId, final int auctionId) {
        return context.addBid(amount, accountId, auctionId);
    }

    public boolean manuallyEndAuction(final int auctionId) {
        return context.manuallyEndAuction(auctionId);
    }

    public boolean auctionIsClosed(final int auctionId) throws SQLException { return context.auctionIsClosed(auctionId); }

    public boolean auctionIsFavoriteForUser(final int auctionId, final int userId) throws SQLException { return context.auctionIsFavoriteForUser(auctionId, userId); }
}
