package data.interfaces;

import models.Auction;
import models.Profile;
import utilities.enums.ProfileLoadingType;

import java.sql.SQLException;

public interface IProfileContext {

    Profile getProfileForId(final int userId,  final ProfileLoadingType loadingType) throws SQLException;
    boolean addVisitedAuction(final Profile profile, final Auction auction);
    boolean addFavoriteAuction(final Profile profile, final Auction auction);
    boolean removeFavoriteAuction(final Profile profile, final Auction auction);
}
