package data.contexts;

import data.interfaces.IFeedbackContext;
import models.Feedback;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeedbackMySqlContext implements IFeedbackContext {
    PreparedStatement preparedStatement;

    @Override
    public ArrayList<Feedback> getFeedbacks(int profileId) {
        Database.getData("SELECT * FROM Feedback WHERE Account_ID = ?", new String[]{ Integer.toString(profileId)});
        return null;
    }
}
