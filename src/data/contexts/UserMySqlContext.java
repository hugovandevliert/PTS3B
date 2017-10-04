package data.contexts;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UserMySqlContext implements IUserContext {
    PreparedStatement preparedStatement;

    @Override
    public boolean setPhoto(Profile profile, Image photo) {
        preparedStatement =  Database.getConnection().prepareStatement("UPDATE Account SET Image = ?");
        preparedStatement.setObject(1, photo);
    }
}
