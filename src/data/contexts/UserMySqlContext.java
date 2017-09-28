package data.contexts;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UserMySqlContext implements IUserContext {

    @Override
    public boolean setPhoto(Profile profile, Image photo) {
        throw new NotImplementedException();
    }
}
