package data.contexts;

import data.interfaces.IFeedbackContext;
import models.Feedback;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class FeedbackMySqlContext implements IFeedbackContext {

    @Override
    public ArrayList<Feedback> getFeedbacks(int profileId) {
        throw new NotImplementedException();
    }
}
