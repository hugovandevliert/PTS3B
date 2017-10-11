package data.contexts;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;
import models.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMySqlContext implements IUserContext {

    @Override
    public boolean registerUser(final String username, final String password, final String salt, final String email, final String name){
        return 1 == Database.setData("INSERT INTO Account (`Username`, `Password`, `Salt`, `Email`, `Name`) values (?, ?, ?, ?, ?)", new String[] { username, password, salt, email, name }, false);
    }

    @Override
    public String[] getSaltAndHash(final String username) {
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
    public boolean setPhoto(final Profile profile, final Image photo) {
        throw new NotImplementedException();
    }

    @Override
    public User getUserByUsername(final String username) {
        User currentUser = null;

        try {
            ResultSet resultSet = Database.getData("SELECT id, username, name, email FROM MyAuctions.Account WHERE username = ?", new String[]{ username });

            if (resultSet.next()){
                currentUser = new User
                        (
                                resultSet.getInt("id"),
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
