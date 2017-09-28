package data.contexts;

import data.interfaces.IProfileContext;
import models.Auction;
import models.Profile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ProfileMySqlContext implements IProfileContext {

    @Override
    public boolean addVisitedAuction(Profile profile, Auction auction) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addFavoriteAuction(Profile profile, Auction auction) {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeFavoriteAuction(Profile profile, Auction auction) {
        throw new NotImplementedException();
    }
}
