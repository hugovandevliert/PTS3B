package data.interfaces;

import models.Feedback;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IFeedbackContext {

    ArrayList<Feedback> getFeedbacks(final int profileId) throws SQLException, IOException, ClassNotFoundException;
}
