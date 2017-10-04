package data.contexts;

import data.interfaces.IAuctionContext;
import models.Auction;
import models.Profile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;
import utilities.enums.Status;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuctionMySqlContext implements IAuctionContext {

    @Override
    public ArrayList<Auction> getAuctions(String searchTerm) {
        Database.getData("SELECT * FROM Auction WHERE `STATUS` = 'OPEN' AND EndDate > curdate()", null);
        return null;
    }

    @Override
    public boolean addAuction(Profile profile, Auction auction) {
//        preparedStatement =  Database.getConnection().prepareStatement("INSERT INTO Auction (Title, StartingBid, Minimum, CreationDate, OpeningDate, EndDate, `Status`, isPremium, Creator_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//        preparedStatement.setString(1, auction.getTitle());
//        preparedStatement.setDouble(2, auction.getStartBid());
//        preparedStatement.setString(3, auction.getMinimum());
//        preparedStatement.setDate(4, auction.getCreationDate());
//        preparedStatement.setDate(5, auction.getOpeningDate());
//        preparedStatement.setDate(6, auction.getExpirationDate());
//        preparedStatement.setString(7, auction.getStatus());
//        preparedStatement.setBoolean(8, auction.isPremium());
//        preparedStatement.setInt(9, profile.getProfileId());
        return false;
    }

    @Override
    public boolean setStatus(Status status, int auctionId) {
//        preparedStatement =  Database.getConnection().prepareStatement("UPDATE Auctions SET `Status` = ? WHERE ID = ?");
//        preparedStatement.setString(1, status);
//        preparedStatement.setInt(2, auctionId);
        return false;

    }

    @Override
    public boolean endAuction(int auctionId) {
//        preparedStatement =  Database.getConnection().prepareStatement("UPDATE Auctions SET `Status` = 'CLOSED' WHERE ID = ?");
//        preparedStatement.setInt(1, auctionId);
        return false;

    }
}
