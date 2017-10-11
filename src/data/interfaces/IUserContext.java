package data.interfaces;

import javafx.scene.image.Image;
import models.Profile;
import models.User;

public interface IUserContext {

    boolean registerUser(final String username, final String password, final String salt, final String email, final String name);
    String[] getSaltAndHash(final String username);
    boolean setPhoto(final Profile profile, final Image photo);
    User getUserByUsername(final String username);
}
