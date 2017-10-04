package data.contexts;

import data.interfaces.IFeedbackContext;
import models.Feedback;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class FeedbackMySqlContext implements IFeedbackContext {
    PreparedStatement preparedStatement;

    @Override
    public ArrayList<Feedback> getFeedbacks(int profileId) {
        preparedStatement =  Database.getConnection().prepareStatement("SELECT * FROM Feedback WHERE Account_ID = ?");
        preparedStatement.setInt(1, profileId);
    }
}
