package data.contexts;

import data.interfaces.IUserContext;
import logic.repositories.ProfileRepository;
import models.User;
import utilities.database.Database;
import utilities.enums.ProfileLoadingType;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMySqlContext implements IUserContext {

    private final ProfileRepository profileRepository;

    public UserMySqlContext() {
        this.profileRepository = new ProfileRepository(new ProfileMySqlContext());
    }

    @Override
    public boolean registerUser(final String username, final String password, final String salt, final String email, final String name) {
        final String query = "INSERT INTO Account (`Username`, `Password`, `Salt`, `Email`, `Name`, `CreationDate`) VALUES (?, ?, ?, ?, ?, curDate())";

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
        return new String[]{};
    }

    @Override
    public boolean setPhoto(final User profile, final File photo) {
        return 1 == Database.setProfilePicture(profile.getId(), photo);
    }

    @Override
    public boolean setPassword(final String newPassword, final String username) {
        final String query = "UPDATE Account SET `Password` = ? WHERE username = ?";

        return 1 == Database.setData(query, new String[]{newPassword, username}, true);
    }

    @Override
    public User getUserByUsername(final String username) throws SQLException, IOException, ClassNotFoundException {
        final String query = "SELECT id, username, name, email FROM Account WHERE username = ?";
        final ResultSet resultSet = Database.getData(query, new String[]{username});

        if (resultSet != null && resultSet.next()) {
            return new User
                    (
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            profileRepository.getProfileForId(resultSet.getInt("id"), ProfileLoadingType.FOR_AUCTION_PAGE)
                    );
        }
        return null;
    }
}
