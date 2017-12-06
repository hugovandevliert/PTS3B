package data.contexts;

import data.interfaces.IFeedbackContext;
import logic.repositories.ProfileRepository;
import modelslibrary.Auction;
import modelslibrary.Feedback;
import utilities.database.Database;
import utilities.enums.FeedbackLoadingType;
import utilities.enums.ProfileLoadingType;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FeedbackMySqlContext implements IFeedbackContext {

    private final ProfileRepository profileRepository;

    public FeedbackMySqlContext(){
        this.profileRepository = new ProfileRepository(new ProfileMySqlContext());
    }

    @Override
    public ArrayList<Feedback> getFeedbacks(final int profileId) throws SQLException, IOException, ClassNotFoundException{
        final String query = "SELECT * FROM Feedback WHERE `account_id` = ?";
        final ResultSet resultSet = Database.getData(query, new String[] { Integer.toString(profileId)});
        final ArrayList<Feedback> feedbacks = new ArrayList<>();

        if (resultSet != null){
            while (resultSet.next()){
                feedbacks.add(getFeedbackFromResultSet(resultSet, FeedbackLoadingType.FOR_PROFILE_PAGE));
            }
        }
        return feedbacks;
    }

    @Override
    public boolean addFeedback(final boolean isPositive, final String message, final int accountId, final int authorId, final Auction auction) {
        final String query = "INSERT INTO MyAuctions.Feedback (ispositive, date, message, account_id, author_id, auction_id) values (?, ?, ?, ?, ?, ?)";

        return 1 == Database.setData(query, new String[]{ String.valueOf(isPositive), String.valueOf(LocalDateTime.now()), message, String.valueOf(accountId), String.valueOf(authorId), String.valueOf(auction.getId()) }, true);
    }

    private Feedback getFeedbackFromResultSet(final ResultSet resultSet, final FeedbackLoadingType feedbackLoadingType) throws SQLException, IOException, ClassNotFoundException {
        if (feedbackLoadingType == FeedbackLoadingType.FOR_PROFILE_PAGE) {
            return new Feedback
                    (
                            profileRepository.getProfileForId(resultSet.getInt("author_id"), ProfileLoadingType.FOR_AUCTION_PAGE),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getBoolean("ispositive"),
                            resultSet.getString("message")
                    );
        } else {
            return null;
        }
    }
}
