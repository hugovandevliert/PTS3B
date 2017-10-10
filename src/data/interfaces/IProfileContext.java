package data.interfaces;

import models.Auction;
import models.Profile;

import java.sql.SQLException;

public interface IProfileContext {

    Profile getProfileForId(int userId) throws SQLException;
    boolean addVisitedAuction(Profile profile, Auction auction);
    boolean addFavoriteAuction(Profile profile, Auction auction);
    boolean removeFavoriteAuction(Profile profile, Auction auction);
}
