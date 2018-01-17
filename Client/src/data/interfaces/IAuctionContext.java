package data.interfaces;

import models.Auction;
import utilities.enums.AuctionLoadingType;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IAuctionContext {

    ArrayList<Auction> getAuctionsForSearchTerm(final String searchTerm) throws SQLException, IOException, ClassNotFoundException;

    ArrayList<Auction> getAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException;

    ArrayList<Auction> getFavoriteAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException;

    ArrayList<Auction> getWonAuctionsWithoutFeedbackForProfile(final int auctionCreatorId, final int feedbackAuthorId) throws SQLException, IOException, ClassNotFoundException;

    Auction getAuctionForId(final int auctionId, final AuctionLoadingType auctionLoadingType) throws SQLException, IOException, ClassNotFoundException;

    boolean addAuction(final Auction auction) throws SQLException, ConnectException;

    boolean addBid(final double amount, final int accountId, final int auctionId, final LocalDateTime localDateTime);

    boolean manuallyEndAuction(final int auctionId);

    boolean auctionIsClosed(final int auctionId) throws SQLException, ConnectException;

    boolean auctionIsFavoriteForUser(final int auctionId, final int userId) throws SQLException, ConnectException;

    int getLastInsertedAuctionId() throws SQLException, ConnectException;
}
