package data.contexts;

import data.interfaces.IProfileContext;
import logic.algorithms.ImageConverter;
import logic.repositories.AuctionRepository;
import logic.repositories.FeedbackRepository;
import models.Auction;
import models.Profile;
import utilities.database.Database;
import utilities.enums.ImageLoadingType;
import utilities.enums.ProfileLoadingType;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMySqlContext implements IProfileContext {

    private ImageConverter imageConverter;

    public ProfileMySqlContext() {
        imageConverter = new ImageConverter();
    }

    @Override
    public Profile getProfileForId(final int userId, final ProfileLoadingType loadingType) throws SQLException, IOException, ClassNotFoundException {
        String query = "";

        if (loadingType.equals(ProfileLoadingType.FOR_AUCTION_PAGE)) query = "SELECT id, username FROM Account WHERE id = ?";
        else if (loadingType.equals(ProfileLoadingType.FOR_PROFILE_PAGE)) query = "SElECT id, username, creationDate, Image FROM Account WHERE id = ?";

        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(userId) });

        if (resultSet != null){
            if (resultSet.next()){
                return getProfileFromResultSet(resultSet, loadingType);
            }
        }
        return null;
    }

    @Override
    public boolean addVisitedAuction(final Profile profile, final Auction auction) {
        final String query = "INSERT INTO VisitedAuction (`account_id`, `auction_id`) VALUES (?, ?)";

        return 1 == Database.setData(query, new String[] { Integer.toString(profile.getProfileId()), Integer.toString(auction.getId()) }, false);
    }

    @Override
    public boolean addFavoriteAuction(final Profile profile, final Auction auction) {
        final String query = "INSERT INTO FavoriteAuction (`account_id`, `auction_id`) VALUES (?, ?)";

        return 1 == Database.setData(query, new String[] { Integer.toString(profile.getProfileId()), Integer.toString(auction.getId()) }, false);
    }

    @Override
    public boolean removeFavoriteAuction(final Profile profile, final Auction auction) {
        final String query = "DELETE FROM FavoriteAuction WHERE `account_id` = ? AND `auction_id` = ?";

        return 1 == Database.setData(query, new String[] { Integer.toString(profile.getProfileId()), Integer.toString(auction.getId()) }, false);
    }

    private Profile getProfileFromResultSet(final ResultSet resultSet, final ProfileLoadingType profileLoadingType) throws SQLException, IOException, ClassNotFoundException {
        switch(profileLoadingType){
            case FOR_AUCTION_PAGE:
                return new Profile
                        (
                                resultSet.getInt("id"),
                                resultSet.getString("username")
                        );
            case FOR_PROFILE_PAGE:
                final AuctionRepository auctionRepository = new AuctionRepository(new AuctionMySqlContext()); //This has to be declared here because otherwise there will be a loop of inits and cause errors
                final FeedbackRepository feedbackRepository = new FeedbackRepository(new FeedbackMySqlContext());

                return new Profile
                        (
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getTimestamp("creationDate").toLocalDateTime(),
                                imageConverter.getImageFromInputStream(resultSet.getBinaryStream("image"), ImageLoadingType.FOR_PROFILE_PAGE),
                                auctionRepository.getAuctionsForProfile(resultSet.getInt("id")),
                                feedbackRepository.getFeedbacks(resultSet.getInt("id"))
                        );
            default:
                return null;
        }
    }
}
