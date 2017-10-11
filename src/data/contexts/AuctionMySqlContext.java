package data.contexts;

import data.interfaces.IAuctionContext;
import javafx.scene.image.Image;
import logic.algorithms.ImageConverter;
import logic.repositories.BidRepository;
import logic.repositories.ProfileRepository;
import models.Auction;
import utilities.database.Database;
import utilities.enums.AuctionLoadingType;
import utilities.enums.Status;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class AuctionMySqlContext implements IAuctionContext {

    final private ImageConverter imageConverter;
    final private ProfileRepository profileRepository;
    final private BidRepository bidRepository;

    public AuctionMySqlContext() {
        imageConverter = new ImageConverter();
        profileRepository = new ProfileRepository(new ProfileMySqlContext());
        bidRepository = new BidRepository(new BidMySqlContext());
    }

    @Override
    public ArrayList<Auction> getAuctionsForSearchTerm(final String searchTerm) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT * FROM MyAuctions.Auction WHERE Auction.status = 'OPEN' " +
                "AND Auction.endDate > curdate() AND Auction.title LIKE ?;";
        final ResultSet resultSet = Database.getDataForSearchTerm(query, searchTerm);
        final ArrayList<Auction> auctions = new ArrayList<>();

        if (resultSet != null) {
            while (resultSet.next()) {
                auctions.add(getAuctionFromResultSet(resultSet, AuctionLoadingType.FOR_LISTED_AUCTIONS));
            }
        }
        return auctions;
    }

    @Override
    public Auction getAuctionForId(final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT * FROM MyAuctions.Auction WHERE id = ?;";
        final ResultSet resultSet = Database.getData(query, new String[]{String.valueOf(auctionId)});

        if (resultSet != null) {
            if (resultSet.next()) {
                return getAuctionFromResultSet(resultSet, AuctionLoadingType.FOR_AUCTION_PAGE);
            }
        }
        return null;
    }

    @Override
    public boolean addAuction(final Auction auction) throws SQLException {
        Database.setData("INSERT INTO Auction (Title, Description, StartingBid, Minimum, CreationDate, OpeningDate, EndDate, `Status`, isPremium, Creator_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[] {
                auction.getTitle(),
                auction.getDescription(),
                String.valueOf(auction.getStartBid()),
                String.valueOf(auction.getMinimum()),
                auction.getCreationDate().toString(),
                auction.getOpeningDate().toString(),
                auction.getExpirationDate().toString(),
                auction.getStatus().toString(),
                String.valueOf(auction.isPremium()),
                String.valueOf(auction.getCreator().getProfileId())
        }, false);
        return true;
    }

    @Override
    public boolean setStatus(final Status status, final int auctionId) {
//        preparedStatement =  Database.getConnection().prepareStatement("UPDATE Auctions SET `Status` = ? WHERE ID = ?");
//        preparedStatement.setString(1, status);
//        preparedStatement.setInt(2, auctionId);
        return false;

    }

    @Override
    public boolean endAuction(final int auctionId) {
//        preparedStatement =  Database.getConnection().prepareStatement("UPDATE Auctions SET `Status` = 'CLOSED' WHERE ID = ?");
//        preparedStatement.setInt(1, auctionId);
        return false;

    }

    public Auction getAuctionFromResultSet(final ResultSet resultSet, final AuctionLoadingType auctionLoadingType) throws SQLException, IOException, ClassNotFoundException {
        switch (auctionLoadingType) {
            case FOR_LISTED_AUCTIONS:
                return new Auction
                        (
                                resultSet.getInt("id"),
                                resultSet.getString("title"),
                                resultSet.getString("description"),
                                resultSet.getDouble("startingBid"),
                                getImagesForAuctionWithId(auctionLoadingType, resultSet.getInt("id"))
                        );
            case FOR_AUCTION_PAGE:
                return new Auction
                        (
                                resultSet.getInt("id"),
                                resultSet.getString("title"),
                                resultSet.getString("description"),
                                resultSet.getDouble("startingBid"),
                                resultSet.getTimestamp("endDate").toLocalDateTime(),
                                profileRepository.getProfileForId(resultSet.getInt("creator_id")),
                                getImagesForAuctionWithId(auctionLoadingType, resultSet.getInt("id")),
                                bidRepository.getBids(resultSet.getInt("id"))
                        );
            default:
                return null;
        }
    }

    private ArrayList<Image> getImagesForAuctionWithId(final AuctionLoadingType auctionLoadingType, final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        ArrayList<Image> images = new ArrayList<>();
        String query = "SELECT image FROM MyAuctions.Image i INNER JOIN MyAuctions.Auction a ON " +
                "a.id = i.auction_id WHERE i.auction_id = ?";

        if (auctionLoadingType.equals(AuctionLoadingType.FOR_LISTED_AUCTIONS)) query += "LIMIT 1";

        final ResultSet resultSet = Database.getData(query, new String[]{String.valueOf(auctionId)});

        while (resultSet.next()) {
            final InputStream inputStream = resultSet.getBinaryStream("image");
            images.add(imageConverter.getImageFromInputStream(inputStream));
        }
        return images;
    }
}
