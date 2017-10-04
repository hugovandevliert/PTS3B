package data.interfaces;

import javafx.scene.image.Image;
import models.Profile;

public interface IUserContext {

    boolean setPhoto(Profile profile, Image photo);
}
