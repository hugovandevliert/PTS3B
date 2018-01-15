package data.interfaces;

import modelslibrary.Auction;
import modelslibrary.Feedback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IFeedbackContext {

    ArrayList<Feedback> getFeedbacks(final int profileId) throws SQLException, IOException, ClassNotFoundException;

    boolean addFeedback(final boolean isPositive, final String message, final int accountId, final int authorId, final Auction auction);
}
