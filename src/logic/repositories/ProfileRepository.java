package logic.repositories;

import data.interfaces.IProfileContext;
import modelslibrary.Auction;
import modelslibrary.Profile;
import utilities.enums.ProfileLoadingType;
import java.io.IOException;
import java.sql.SQLException;

public class ProfileRepository {

    private final IProfileContext context;

    public ProfileRepository(final IProfileContext context) {
        this.context = context;
    }

    public Profile getProfileForId(final int userId, final ProfileLoadingType loadingType) throws SQLException, IOException, ClassNotFoundException {
        return context.getProfileForId(userId, loadingType);
    }

    public boolean addVisitedAuction(final Profile profile, Auction auction) {
        return context.addVisitedAuction(profile, auction);
    }

    public boolean addFavoriteAuction(final Profile profile, final int auctionId) {
        return context.addFavoriteAuction(profile, auctionId);
    }

    public boolean removeFavoriteAuction(final Profile profile, Auction auction) {
        return context.removeFavoriteAuction(profile, auction);
    }
}
