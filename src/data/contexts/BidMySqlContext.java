package data.contexts;

import data.interfaces.IBidContext;
import logic.repositories.ProfileRepository;
import modelslibrary.Bid;
import utilities.database.Database;
import utilities.enums.BidLoadingType;
import utilities.enums.ProfileLoadingType;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BidMySqlContext implements IBidContext {

    final private ProfileRepository profileRepository;

    public BidMySqlContext() { profileRepository = new ProfileRepository(new ProfileMySqlContext()); }

    @Override
    public ArrayList<Bid> getBids(final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT b.* FROM MyAuctions.Bid b " +
                "INNER JOIN MyAuctions.Account a ON a.id = b.account_id WHERE Auction_ID = ?";
        final ArrayList<Bid> bids = new ArrayList<>();
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(auctionId) });

        if (resultSet != null){
            while (resultSet.next()){
                bids.add(getBidFromResultSet(resultSet, BidLoadingType.FOR_AUCTION));
            }
        }
        return bids;
    }

    @Override
    public Bid getMostRecentBidForAuctionWithId(final int auctionId) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT amount FROM MyAuctions.Bid WHERE auction_id = ? ORDER BY amount DESC LIMIT 1";
        final ResultSet resultSet = Database.getData(query, new String[]{ String.valueOf(auctionId) });

        if (resultSet != null && resultSet.next()){
            return getBidFromResultSet(resultSet, BidLoadingType.FOR_MOST_RECENT_BID);
        }
        return null;
    }

    private Bid getBidFromResultSet(final ResultSet resultSet, final BidLoadingType bidLoadingType) throws SQLException, IOException, ClassNotFoundException {
        switch(bidLoadingType){
            case FOR_AUCTION:
                return new Bid
                        (
                                profileRepository.getProfileForId(resultSet.getInt("account_id"), ProfileLoadingType.FOR_AUCTION_PAGE),
                                resultSet.getDouble("amount"),
                                resultSet.getTimestamp("date").toLocalDateTime()
                        );
            case FOR_MOST_RECENT_BID:
                return new Bid
                        (
                          resultSet.getDouble("amount")
                        );
                default:
                    return null;
        }
    }
}
