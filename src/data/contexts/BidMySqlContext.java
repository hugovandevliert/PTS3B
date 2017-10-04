package data.contexts;

import data.interfaces.IBidContext;
import models.Bid;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class BidMySqlContext implements IBidContext {
    PreparedStatement preparedStatement;

    @Override
    public ArrayList<Bid> getBids(int auctionId) {
        try {
            Database.getData("SELECT * FROM Bid WHERE Auction_ID = ?", new String[]{ Integer.toString(auctionId)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
