package data.contexts;

import data.interfaces.IProfileContext;
import models.Auction;
import models.Profile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;
import utilities.enums.ProfileLoadingType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMySqlContext implements IProfileContext {

    @Override
    public Profile getProfileForId(int userId) throws SQLException {
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
    public boolean addVisitedAuction(Profile profile, Auction auction) {
//        preparedStatement =  Database.getConnection().prepareStatement("INSERT INTO VisitedAuction (Account_ID, Auction_ID) VALUES (?, ?)");
//        preparedStatement.setInt(1, profile.getProfileId());
//        preparedStatement.setInt(2, auction.getID());
        return false;
    }

    @Override
    public boolean addFavoriteAuction(Profile profile, Auction auction) {
//        preparedStatement =  Database.getConnection().prepareStatement("INSERT INTO FavoriteAuction (Account_ID, Auction_ID) VALUES (?, ?)");
//        preparedStatement.setInt(1, profile.getProfileId());
//        preparedStatement.setInt(2, auction.getID());
        return false;
    }

    @Override
    public boolean removeFavoriteAuction(Profile profile, Auction auction) {
//        preparedStatement =  Database.getConnection().prepareStatement("DELETE FROM FavoriteAuction WHERE Account_ID = ? AND Auction_ID = ?");
//        preparedStatement.setInt(1, profile.getProfileId());
//        preparedStatement.setInt(2, auction.getID());
        return false;
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
