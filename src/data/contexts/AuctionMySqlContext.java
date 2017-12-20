package data.contexts;

import data.interfaces.IAuctionContext;
import javafx.scene.image.Image;
import logic.algorithms.ImageConverter;
import logic.repositories.BidRepository;
import logic.repositories.ProfileRepository;
import modelslibrary.Auction;
import utilities.database.Database;
import utilities.enums.AuctionLoadingType;
import utilities.enums.ImageLoadingType;
import utilities.enums.ProfileLoadingType;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionMySqlContext implements IAuctionContext {

    private final ImageConverter imageConverter;
    private final ProfileRepository profileRepository;
    private final BidRepository bidRepository;

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
    public ArrayList<Auction> getAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT * FROM MyAuctions.Auction WHERE Auction.status = 'OPEN' " +
                "AND Auction.endDate > curdate() AND creator_id = ?;";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(profileId) });
        final ArrayList<Auction> auctions = new ArrayList<>();

        if (resultSet != null) {
            while (resultSet.next()) {
                auctions.add(getAuctionFromResultSet(resultSet, AuctionLoadingType.FOR_LISTED_AUCTIONS));
            }
        }
        return auctions;
    }

    @Override
    public ArrayList<Auction> getFavoriteAuctionsForProfile(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT auction_id FROM FavoriteAuction WHERE account_id = ?;";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(profileId) });
        final ArrayList<Auction> auctions = new ArrayList<>();

        if (resultSet != null) {
            while (resultSet.next()) {
                auctions.add(getAuctionForId(resultSet.getInt("auction_id"), AuctionLoadingType.FOR_LISTED_AUCTIONS));
            }
        }
        return auctions;
    }

    @Override
    public ArrayList<Auction> getWonAuctionsWithoutFeedbackForProfile(final int auctionCreatorId, final int feedbackAuthorId) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT DISTINCT au.id, au.title, au.description, au.startingBid " +
                "FROM Account a INNER JOIN Auction au  ON a.id = au.creator_id INNER JOIN Bid b ON au.id = b.auction_id " +
                "LEFT JOIN Feedback f ON au.id = f.auction_id " +
                "WHERE (au.endDate <= curdate() OR au.status = 'CLOSED') AND f.auction_id IS NULL AND a.id = ? AND ((SELECT b.account_id FROM Bid b WHERE b.auction_id = au.id ORDER BY b.amount DESC LIMIT 1) = ?)";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(auctionCreatorId), String.valueOf(feedbackAuthorId) });
        final ArrayList<Auction> auctions = new ArrayList<>();

        if (resultSet != null) {
            while (resultSet.next()) {
                auctions.add(getAuctionFromResultSet(resultSet, AuctionLoadingType.FOR_FEEDBACK_ADDING));
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
        else if (auctionLoadingType.equals(AuctionLoadingType.FOR_LISTED_AUCTIONS)){
            query = "SELECT * FROM MyAuctions.Auction WHERE id = ?;";
        }
        else if (auctionLoadingType.equals(AuctionLoadingType.FOR_COUNTDOWN_TIMER)){
            query = "SELECT id, OpeningDate, EndDate FROM MyAuctions.Auction WHERE id = ?;";
        }
        else return null;

        final ResultSet resultSet = Database.getData(query, new String[]{String.valueOf(auctionId)});

        if (resultSet != null && resultSet.next()) {
            return getAuctionFromResultSet(resultSet, auctionLoadingType);
        }
        return null;
    }

    @Override
    public boolean addAuction(final Auction auction) throws SQLException {
        final String query = "INSERT INTO Auction (Title, Description, StartingBid, Minimum, CreationDate, OpeningDate, " +
                "EndDate, `Status`, isPremium, Creator_ID, minimumincrement) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        final int result = Database.setData(query, new String[]{
                auction.getTitle(),
                auction.getDescription(),
                String.valueOf(auction.getStartBid()),
                String.valueOf(auction.getMinimum()),
                auction.getCreationDate().toString(),
                auction.getOpeningDate().toString(),
                auction.getExpirationDate().toString(),
                "OPEN",
                String.valueOf(auction.isPremium()),
                String.valueOf(auction.getCreator().getProfileId()),
                String.valueOf(auction.getIncrementation())
        }, true);

        return result == 1 && addAuctionImages(auction.getFileImages(), getLastInsertedAuctionId());
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

    public boolean addAuctionImages(final List<File> images, final int auctionId) {
        if (images == null) return true;
        final String query = "INSERT INTO Image (`auction_id`, `image`) VALUES (?, ?)";
        int resultCorrect = 0;

        for (File image : images) {
            resultCorrect += Database.setDataWithImages(
                    query,
                    new String[]{ String.valueOf(auctionId) },
                    new File[]{ image },
                    true
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
    public boolean auctionIsClosed(final int auctionId) throws SQLException {
        final String query = "SELECT status FROM MyAuctions.Auction WHERE id = ?;";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(auctionId) });

        if (resultSet != null && resultSet.next()){
            return resultSet.getString("status").trim().equals("CLOSED");
        }
        return false;
    }

    @Override
    public boolean auctionIsFavoriteForUser(final int auctionId, final int userId) throws SQLException {
        final String query = "SELECT auction_id FROM MyAuctions.FavoriteAuction WHERE auction_id = ? AND account_id = ?;";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(auctionId), String.valueOf(userId) });

        return resultSet.next();
    }

    public int getLastInsertedAuctionId() throws SQLException {
        final ResultSet resultSet = Database.getData(
                "SELECT MAX(id) FROM Auction",
                null
        );
        resultSet.next();

        return resultSet.getInt("MAX(id)");
    }

    public Auction getAuctionFromResultSet(final ResultSet resultSet, final AuctionLoadingType auctionLoadingType) throws SQLException, IOException, ClassNotFoundException {
        final String id = "id";
        final String title = "title";
        final String description = "description";
        final String startingBid = "startingBid";
        switch (auctionLoadingType) {
            case FOR_LISTED_AUCTIONS:
                return new Auction
                        (
                                resultSet.getInt(id),
                                resultSet.getString(title),
                                resultSet.getString(description),
                                resultSet.getDouble(startingBid),
                                getImagesForAuctionWithId(auctionLoadingType, resultSet.getInt("id")),
                                bidRepository.getBids(resultSet.getInt("id"))
                        );
            case FOR_AUCTION_PAGE:
                return new Auction
                        (
                                resultSet.getInt(id),
                                resultSet.getString(title),
                                resultSet.getString(description),
                                resultSet.getDouble(startingBid),
                                resultSet.getTimestamp("endDate").toLocalDateTime(),
                                profileRepository.getProfileForId(resultSet.getInt("creator_id"), ProfileLoadingType.FOR_AUCTION_PAGE),
                                getImagesForAuctionWithId(auctionLoadingType, resultSet.getInt("id")),
                                bidRepository.getBids(resultSet.getInt("id")),
                                resultSet.getDouble("minimum"),
                                resultSet.getDouble("minimumincrement")
                        );
            case FOR_COUNTDOWN_TIMER:
                return new Auction
                        (
                                resultSet.getInt("id"),
                                resultSet.getTimestamp("openingDate").toLocalDateTime(),
                                resultSet.getTimestamp("endDate").toLocalDateTime()
                        );
            case FOR_FEEDBACK_ADDING:
                return new Auction
                        (
                                resultSet.getInt("id"),
                                resultSet.getString("title"),
                                resultSet.getString("description"),
                                resultSet.getDouble("startingBid"),
                                null,
                                null
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
            images.add(imageConverter.getImageFromInputStream(inputStream, ImageLoadingType.valueOf(auctionLoadingType.toString())));
        }
        return images;
    }
}
