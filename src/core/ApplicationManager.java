package core;

import models.Auction;
import models.Profile;
import models.User;

import java.util.ArrayList;

public class ApplicationManager {

    public ArrayList<Auction> loadedAuctions;
    public Profile loadedProfile;
    public User currentUser;

    public ApplicationManager() {
        loadedAuctions = new ArrayList<>();
    }

    public User login(String username, String password) {
        currentUser = new User(username, username, username);
        return currentUser;
    }

    public boolean registerUser(String username, String password, String email, String name) {
        return false;
    }

    public boolean logout() {
        return false;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
