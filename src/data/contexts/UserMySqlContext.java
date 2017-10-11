package data.contexts;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;
import models.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMySqlContext implements IUserContext {

    @Override
    public String[] getSaltAndHash(String username) {
        ResultSet resultSet = Database.getData("SELECT salt, password FROM Account WHERE username = ?", new String[] { username });

        try{
            if (resultSet.next()){
                //Return the username's saltAndHash
                return new String[] { resultSet.getString(1), resultSet.getString(2) };
            }
            //There is no user with this username
            return null;
        }
        catch (SQLException ex){
            ex.printStackTrace();
            //TODO: Proper error handling
            return null;
        }
    }

    @Override
    public boolean setPhoto(Profile profile, Image photo) {
        throw new NotImplementedException();
    }

    @Override
    public User getUserByUsername(String username) {
        User currentUser = null;

        try {
            ResultSet resultSet = Database.getData("SELECT username, name, email FROM MyAuctions.Account WHERE username = ?", new String[]{ username });

            if (resultSet.next()){
                currentUser = new User
                        (
                                resultSet.getString("username"),
                                resultSet.getString("name"),
                                resultSet.getString("email")
                        );
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            //TODO: Proper error handling
        }
        return currentUser;
    }
}
