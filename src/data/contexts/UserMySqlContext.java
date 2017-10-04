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
    public String[] getSaltAndHash(String username){
        ResultSet resultSet = Database.getData("SELECT salt, password FROM Account WHERE username = ?", new String[] {username});
        try{
            if(!resultSet.next()){
                //There is no user with this username
                return null;
            }
            else{
                //Return the username's his saltAndHash
                return new String[] {resultSet.getString(1), resultSet.getString(2)};
            }
        }
        catch (SQLException ex){
            //TODO: Proper error handling
            return null;
        }
    }

    @Override
    public boolean setPhoto(Profile profile, Image photo) {
        throw new NotImplementedException();
    }

    @Override
    public User getUserByUsername(String username){
        throw new NotImplementedException();
    }
}
