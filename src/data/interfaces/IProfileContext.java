package data.interfaces;

import models.Auction;
import models.Profile;
import utilities.enums.ProfileLoadingType;
import java.io.IOException;
import java.sql.SQLException;

public interface IProfileContext {

    Profile getProfileForId(final int userId,  final ProfileLoadingType loadingType) throws SQLException, IOException, ClassNotFoundException;
    boolean addVisitedAuction(final Profile profile, final Auction auction);
    boolean addFavoriteAuction(final Profile profile, final int auctionId);
    boolean removeFavoriteAuction(final Profile profile, final Auction auction);
}
