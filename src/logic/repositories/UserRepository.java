package logic.repositories;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;

public class UserRepository {

    private IUserContext context;

    public UserRepository(IUserContext context) {
        this.context = context;
    }

    public boolean setPhoto(Profile profile, Image photo) {
        return context.setPhoto(profile, photo);
    }
}
