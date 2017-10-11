package logic.repositories;

import data.interfaces.IUserContext;
import javafx.scene.image.Image;
import models.Profile;
import models.User;

public class UserRepository {

    private final IUserContext context;

    public UserRepository(final IUserContext context) {
        this.context = context;
    }

    public boolean setPhoto(final Profile profile, final Image photo) {
        return context.setPhoto(profile, photo);
    }

    public String[] getSaltAndHash(final String username){
        return context.getSaltAndHash(username);
    }

    public User getUserByUsername(final String username){
        return context.getUserByUsername(username);
    }

    public boolean registerUser(final String username, final String password, final String salt, final String email, final String name) { return context.registerUser(username, password, salt, email, name); }
}
