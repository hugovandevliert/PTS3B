package logic.repositories;

import data.interfaces.IProfileContext;
import models.Auction;
import models.Profile;
import utilities.enums.ProfileLoadingType;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileRepository {

    final private IProfileContext context;

    public ProfileRepository(final IProfileContext context) {
        this.context = context;
    }

    public Profile getProfileForId(final int userId,  final ProfileLoadingType loadingType) throws SQLException, IOException, ClassNotFoundException {
        return context.getProfileForId(userId, loadingType);
    }

    public boolean addVisitedAuction(final Profile profile, Auction auction) {
        return context.addVisitedAuction(profile, auction);
    }

    public boolean addFavoriteAuction(final Profile profile, Auction auction) {
        return context.addFavoriteAuction(profile, auction);
    }

    public boolean removeFavoriteAuction(final Profile profile, Auction auction) {
        return context.removeFavoriteAuction(profile, auction);
    }
}
