package data.contexts;

import data.interfaces.IAuctionContext;
import javafx.scene.image.Image;
import logic.algorithms.ImageConverter;
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

    private ImageConverter imageConverter;

    public AuctionMySqlContext() {
        imageConverter = new ImageConverter();
    }

    @Override
    public ArrayList<Auction> getAuctionsForSearchTerm(String searchTerm) throws SQLException {
        final String query = "SELECT * FROM MyAuctions.Auction WHERE Auction.status = 'OPEN' " +
                       "AND Auction.endDate > curdate() AND Auction.title LIKE ?;";
        final ResultSet resultSet = Database.getDataForSearchTerm(query, searchTerm);
        final ArrayList<Auction> auctions = new ArrayList<>();

        if (resultSet != null){
            while (resultSet.next()){
                auctions.add(getAuctionFromResultSet(resultSet));
            }
        }
        return auctions;
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

    public Auction getAuctionFromResultSet(ResultSet resultSet) throws SQLException {
        /*To grab the image, please do the following:
        get the resultset from any getData method in the Database Class
        in this resultset, grab the byte array as an object, which goes as follows;
        byte[] byteArray = (byte[]) resultSet.getObject(1)
        to make sure we have efficient code, we would then convert our image like so;

        try {
            final Image image = imageConverter.byteArrayToImage((byte[])resultSet.getObject(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return new Auction
                (
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDouble("startingBid")
                );
    }
}
