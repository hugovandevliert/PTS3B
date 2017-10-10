package data.interfaces;

import models.Bid;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IBidContext {

    ArrayList<Bid> getBids(int auctionId) throws SQLException;
}
