package logic.repositories;

import data.interfaces.IFeedbackContext;
import modelslibrary.Auction;
import modelslibrary.Feedback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeedbackRepository {

    private final IFeedbackContext context;

    public FeedbackRepository(final IFeedbackContext context) {
        this.context = context;
    }

    public ArrayList<Feedback> getFeedbacks(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        return context.getFeedbacks(profileId);
    }

    public boolean addFeedback(final boolean isPositive, final String message, final int accountId, final int authorId, final Auction auction) {
        return context.addFeedback(isPositive, message, accountId, authorId, auction);
    }
}
