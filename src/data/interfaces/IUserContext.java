package data.interfaces;

import javafx.scene.image.Image;
import models.Profile;
import models.User;

import java.io.File;
import java.sql.SQLException;

public interface IUserContext {

    boolean registerUser(final String username, final String password, final String salt, final String email, final String name);
    String[] getSaltAndHash(final String username) throws SQLException;
    boolean setPhoto(final Profile profile, final Image photo);
    User getUserByUsername(final String username) throws SQLException;
}
