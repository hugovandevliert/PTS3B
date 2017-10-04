package logic.repositories;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;
import models.User;

public class UserRepository {

    private IUserContext context;

    public UserRepository(IUserContext context) {
        this.context = context;
    }

    public boolean setPhoto(Profile profile, Image photo) {
        return context.setPhoto(profile, photo);
    }

    public String[] getSaltAndHash(String username){
        return context.getSaltAndHash(username);
    }

    public User getUserByUsername(String username){
        return context.getUserByUsername(username);
    }
}
