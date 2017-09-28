package data.interfaces;

import models.Feedback;

import java.util.ArrayList;

public interface IFeedbackContext {

    ArrayList<Feedback> getFeedbacks(int profileId);
}
