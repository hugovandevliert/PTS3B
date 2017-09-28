package logic.repositories;

import data.interfaces.IFeedbackContext;
import models.Feedback;

import java.util.ArrayList;

public class FeedbackRepository {

    private IFeedbackContext context;

    public FeedbackRepository(IFeedbackContext context) {
        this.context = context;
    }

    public ArrayList<Feedback> getFeedbacks(int profileId) {
        return context.getFeedbacks(profileId);
    }
}
