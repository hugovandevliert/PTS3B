package data.interfaces;

import models.Auction;
import models.Profile;

public interface IProfileContext {

    boolean addVisitedAuction(Profile profile, Auction auction);
    boolean addFavoriteAuction(Profile profile, Auction auction);
    boolean removeFavoriteAuction(Profile profile, Auction auction);
}
