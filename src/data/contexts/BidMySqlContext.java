package data.contexts;

import data.interfaces.IBidContext;
import logic.repositories.ProfileRepository;
import models.Bid;
import utilities.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class BidMySqlContext implements IBidContext {

    private ProfileRepository profileRepository;

    public BidMySqlContext() { profileRepository = new ProfileRepository(new ProfileMySqlContext()); }

    @Override
    public ArrayList<Bid> getBids(final int auctionId) throws SQLException {
        ArrayList<Bid> bids = new ArrayList<>();
        final String query = "SELECT b.* FROM MyAuctions.Bid b " +
                             "INNER JOIN MyAuctions.Account a ON a.id = b.account_id WHERE Auction_ID = ?";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(auctionId) });

        if (resultSet != null){
            while (resultSet.next()){
                bids.add(getBidFromResultSet(resultSet));
            }
        }
        return bids;
    }

    private Bid getBidFromResultSet(final ResultSet resultSet) throws SQLException {
        return new Bid
                (
                        profileRepository.getProfileForId(resultSet.getInt("account_id")),
                        resultSet.getDouble("amount"),
                        new Date(resultSet.getDate("date").getTime())
                );
    }
}
