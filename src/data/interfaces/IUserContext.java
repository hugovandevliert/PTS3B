package data.interfaces;

import models.User;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public interface IUserContext {

    boolean registerUser(final String username, final String password, final String salt, final String email, final String name);
    String[] getSaltAndHash(final String username) throws SQLException;
    boolean setPhoto(final User profile, final File photo);
    User getUserByUsername(final String username) throws SQLException, IOException, ClassNotFoundException;
}
