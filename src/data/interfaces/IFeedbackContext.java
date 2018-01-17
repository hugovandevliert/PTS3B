package data.interfaces;

import models.Auction;
import models.Feedback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IFeedbackContext {

    ArrayList<Feedback> getFeedbacks(final int profileId) throws SQLException, IOException, ClassNotFoundException;

    boolean addFeedback(final boolean isPositive, final String message, final int accountId, final int authorId, final Auction auction);
}
