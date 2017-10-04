package data.contexts;

import data.interfaces.IBidContext;
import models.Bid;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class BidMySqlContext implements IBidContext {
    PreparedStatement preparedStatement;

    @Override
    public ArrayList<Bid> getBids(int auctionId) {
        preparedStatement =  Database.getConnection().prepareStatement("SELECT * FROM Bid WHERE Auction_ID = ?");
        preparedStatement.setInt(1, auctionId);
    }
}
