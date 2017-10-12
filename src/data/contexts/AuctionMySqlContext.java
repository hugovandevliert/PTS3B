package data.contexts;

import data.interfaces.IAuctionContext;
import javafx.scene.image.Image;
import logic.algorithms.ImageConverter;
import logic.repositories.BidRepository;
import logic.repositories.ProfileRepository;
import models.Auction;
import utilities.database.Database;
import utilities.enums.AuctionLoadingType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public Auction getAuctionForId(final int auctionId, final AuctionLoadingType auctionLoadingType) throws SQLException, IOException, ClassNotFoundException {
        String query = "";

        if (auctionLoadingType.equals(AuctionLoadingType.FOR_AUCTION_PAGE)){
            query = "SELECT * FROM MyAuctions.Auction WHERE id = ?;";
        }
        else if (auctionLoadingType.equals(AuctionLoadingType.FOR_COUNTDOWN_TIMER)){
            query = "SELECT id, EndDate FROM MyAuctions.Auction WHERE id = ?;";
        }

        final ResultSet resultSet = Database.getData(query, new String[]{String.valueOf(auctionId)});

        if (resultSet != null) {
            if (resultSet.next()) {
                return getAuctionFromResultSet(resultSet, auctionLoadingType);
            }
        }
        return null;
    }

    @Override
    public boolean addAuction(final Auction auction) throws SQLException {
        final String query = "INSERT INTO Auction (Title, Description, StartingBid, Minimum, CreationDate, OpeningDate, EndDate, `Status`, isPremium, Creator_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        final int result = Database.setData(query, new String[]{
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

        //TODO: Title is not unique, so we need to retrieve the ID in a different way.
        final ResultSet resultSet = Database.getData(
                "SELECT id FROM Auction WHERE title = '?'",
                new String[]{ auction.getTitle() }
        );

        return result == 1 && addAuctionImages(auction.getImages(), resultSet.getInt("id"));
    }

    @Override
    public boolean addBid(double amount, int accountId, int auctionId) {
        final String query = "INSERT INTO MyAuctions.Bid (amount, date, account_id, auction_id) " +
                             "VALUES (?, ?, ?, ?);";

        return 1 == Database.setData(query, new String[]
                {
                    String.valueOf(amount),
                    String.valueOf(LocalDateTime.now().toLocalDate() + " " + LocalDateTime.now().toLocalTime()),
                    String.valueOf(accountId), String.valueOf(auctionId)
                }, true);
    }

    public boolean addAuctionImages(final List<Image> images, final int auctionID) {
        final String query = "INSERT INTO Image (`image`, `auction_id`) (?, ?)";
        int resultCorrect = 0;

        for (Image image : images) {
            resultCorrect += Database.setDataWithImages(
                    query,
                    new String[]{ String.valueOf(auctionID) },
                    new Image[]{ image },
                    false
            );
        }
        return resultCorrect == images.size();
    }

    @Override
    public boolean manuallyEndAuction(final int auctionId) {
        final String query = "UPDATE Auction SET `status` = 'CLOSED' WHERE `ID` = ?";

        return 1 == Database.setData(query, new String[] { Integer.toString(auctionId) }, true);
    }

    @Override
    public boolean auctionIsClosed(int auctionId) throws SQLException {
        final String query = "SELECT status FROM MyAuctions.Auction WHERE id = ?;";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(auctionId) });

        if (resultSet != null){
            if (resultSet.next()){
                return resultSet.getString("status").trim().equals("CLOSED");
            }
        }
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
                                bidRepository.getBids(resultSet.getInt("id")),
                                resultSet.getDouble("minimum"),
                                resultSet.getDouble("minIncrementation")
                        );
            case FOR_COUNTDOWN_TIMER:
                return new Auction
                        (
                                resultSet.getInt("id"),
                                resultSet.getTimestamp("endDate").toLocalDateTime()
                        );
            default:
                return null;
        }
    }

    private ArrayList<Image> getImagesForAuctionWithId(final AuctionLoadingType auctionLoadingType, final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        final ArrayList<Image> images = new ArrayList<>();
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
