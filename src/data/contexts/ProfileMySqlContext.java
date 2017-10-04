package data.contexts;

import data.interfaces.IProfileContext;
import models.Auction;
import models.Profile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;

import java.sql.PreparedStatement;

public class ProfileMySqlContext implements IProfileContext {
    PreparedStatement preparedStatement;

    @Override
    public boolean addVisitedAuction(Profile profile, Auction auction) {
        preparedStatement =  Database.getConnection().prepareStatement("INSERT INTO VisitedAuction (Account_ID, Auction_ID) VALUES (?, ?)");
        preparedStatement.setInt(1, profile.getProfileId());
        preparedStatement.setInt(2, auction.getID());
    }

    @Override
    public boolean addFavoriteAuction(Profile profile, Auction auction) {
        preparedStatement =  Database.getConnection().prepareStatement("INSERT INTO FavoriteAuction (Account_ID, Auction_ID) VALUES (?, ?)");
        preparedStatement.setInt(1, profile.getProfileId());
        preparedStatement.setInt(2, auction.getID());
    }

    @Override
    public boolean removeFavoriteAuction(Profile profile, Auction auction) {
        preparedStatement =  Database.getConnection().prepareStatement("DELETE FROM FavoriteAuction WHERE Account_ID = ? AND Auction_ID = ?");
        preparedStatement.setInt(1, profile.getProfileId());
        preparedStatement.setInt(2, auction.getID());
    }
}
