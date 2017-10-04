package logic.repositories;

import data.interfaces.IProfileContext;
import models.Auction;
import models.Profile;

public class ProfileRepository {

    private IProfileContext context;

    public ProfileRepository(IProfileContext context) {
        this.context = context;
    }

    public boolean addVisitedAuction(Profile profile, Auction auction) {
        return context.addVisitedAuction(profile, auction);
    }

    public boolean addFavoriteAuction(Profile profile, Auction auction) {
        return context.addFavoriteAuction(profile, auction);
    }

    public boolean removeFavoriteAuction(Profile profile, Auction auction) {
        return context.removeFavoriteAuction(profile, auction);
    }
}
