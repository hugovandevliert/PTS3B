package logic.repositories;

import data.interfaces.IAuctionContext;
import models.Auction;
import utilities.enums.AuctionLoadingType;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuctionRepository {

    private final IAuctionContext context;

    public AuctionRepository(final IAuctionContext context) {
        this.context = context;
    }

    public List<Auction> getAuctionsForSearchTerm(final String searchTerm) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionsForSearchTerm(searchTerm);
    }

    public List<Auction> getAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionsForProfile(profileId);
    }

    public List<Auction> getFavoriteAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        return context.getFavoriteAuctionsForProfile(profileId);
    }

    public List<Auction> getWonAuctionsWithoutFeedbackForProfile(final int auctionCreatorId, final int feedbackAuthorId) throws SQLException, IOException, ClassNotFoundException {
        return context.getWonAuctionsWithoutFeedbackForProfile(auctionCreatorId, feedbackAuthorId);
    }

    public Auction getAuctionForId(final int auctionId, final AuctionLoadingType auctionLoadingType) throws SQLException, IOException, ClassNotFoundException {
        return context.getAuctionForId(auctionId, auctionLoadingType);
    }

    public boolean addAuction(final Auction auction) throws SQLException, ConnectException {
        return context.addAuction(auction);
    }

    public boolean addBid(final double amount, final int accountId, final int auctionId) {
        return context.addBid(amount, accountId, auctionId);
    }

    public int getLastInsertedAuctionId() throws SQLException, ConnectException {
        return context.getLastInsertedAuctionId();
    }

    public boolean manuallyEndAuction(final int auctionId) {
        return context.manuallyEndAuction(auctionId);
    }

    public boolean auctionIsClosed(final int auctionId) throws SQLException, ConnectException {
        return context.auctionIsClosed(auctionId);
    }

    public boolean auctionIsFavoriteForUser(final int auctionId, final int userId) throws SQLException, ConnectException {
        return context.auctionIsFavoriteForUser(auctionId, userId);
    }
}
