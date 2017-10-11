package data.interfaces;

import models.Auction;
import models.Profile;
import java.sql.SQLException;

public interface IProfileContext {

    Profile getProfileForId(final int userId) throws SQLException;
    boolean addVisitedAuction(final Profile profile, final Auction auction);
    boolean addFavoriteAuction(final Profile profile, final Auction auction);
    boolean removeFavoriteAuction(final Profile profile, final Auction auction);
}
