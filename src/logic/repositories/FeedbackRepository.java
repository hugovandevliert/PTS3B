package logic.repositories;

import data.interfaces.IFeedbackContext;
import modelslibrary.Feedback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeedbackRepository {

    final private IFeedbackContext context;

    public FeedbackRepository(final IFeedbackContext context) {
        this.context = context;
    }

    public ArrayList<Feedback> getFeedbacks(final int profileId) throws SQLException, IOException, ClassNotFoundException {
        return context.getFeedbacks(profileId);
    }
}
