package data.contexts;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;
import models.User;
import utilities.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMySqlContext implements IUserContext {

    @Override
    public boolean registerUser(final String username, final String password, final String salt, final String email, final String name) {
        final String query = "INSERT INTO Account (`Username`, `Password`, `Salt`, `Email`, `Name`) VALUES (?, ?, ?, ?, ?)";

        return 1 == Database.setData(query, new String[]{username, password, salt, email, name}, true);
    }

    @Override
    public String[] getSaltAndHash(final String username) throws SQLException {
        final String query = "SELECT salt, password FROM Account WHERE username = ?";
        final ResultSet resultSet = Database.getData(query, new String[]{username});

        if (resultSet.next()) {
            //Return the username's saltAndHash
            return new String[]{resultSet.getString(1), resultSet.getString(2)};
        }
        //There is no user with this username
        return null;
    }

    @Override
    public boolean setPhoto(final Profile profile, final Image photo) {
        final String query = "UPDATE Account SET image = ? WHERE id = ?";

        return 1 == Database.setDataWithImages(query, new String[]{ Integer.toString(profile.getProfileId()) }, new Image[]{ photo }, true);
    }

    @Override
    public User getUserByUsername(final String username) throws SQLException {
        final String query = "SELECT id, username, name, email FROM MyAuctions.Account WHERE username = ?";
        final ResultSet resultSet = Database.getData(query, new String[]{username});

        if (resultSet.next()) {
            return new User
                    (
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
        }
        return null;
    }
}
