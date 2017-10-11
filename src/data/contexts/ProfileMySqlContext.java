package data.contexts;

import data.interfaces.IProfileContext;
import models.Auction;
import models.Profile;
import utilities.database.Database;
import utilities.enums.ProfileLoadingType;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMySqlContext implements IProfileContext {

    @Override
    public Profile getProfileForId(final int userId) throws SQLException {
        final String query = "SELECT username FROM Account WHERE id = ?";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(userId) });

        if (resultSet != null){
            if (resultSet.next()){
                return getProfileFromResultSet(resultSet, ProfileLoadingType.FOR_AUCTION_PAGE);
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

    private Profile getProfileFromResultSet(final ResultSet resultSet, final ProfileLoadingType profileLoadingType) throws SQLException {
        switch(profileLoadingType){
            case FOR_AUCTION_PAGE:
                return new Profile(resultSet.getString("username"));
            default:
                return null;
        }
    }
}
