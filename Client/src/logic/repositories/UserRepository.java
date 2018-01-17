package logic.repositories;

import data.interfaces.IUserContext;
import models.User;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;

public class UserRepository {

    private final IUserContext context;

    public UserRepository(final IUserContext context) {
        this.context = context;
    }

    public boolean setPhoto(final User profile, final File photo) {
        return context.setPhoto(profile, photo);
    }

    public boolean setPassword(final String newPassword, final String username) {
        return context.setPassword(newPassword, username);
    }

    public String[] getSaltAndHash(final String username) throws SQLException, ConnectException {
        return context.getSaltAndHash(username);
    }

    public User getUserByUsername(final String username) throws SQLException, IOException, ClassNotFoundException {
        return context.getUserByUsername(username);
    }

    public boolean registerUser(final String username, final String password, final String salt, final String email, final String name) {
        return context.registerUser(username, password, salt, email, name);
    }
}
