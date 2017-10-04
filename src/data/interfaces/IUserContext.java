package data.interfaces;

import javafx.scene.image.Image;
import models.Profile;
import models.User;

public interface IUserContext {
    String[] getSaltAndHash(String username);
    boolean setPhoto(Profile profile, Image photo);
    User getUserByUsername(String username);
}
