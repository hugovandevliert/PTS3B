package core;

import models.Auction;
import models.Profile;
import models.User;

import java.util.ArrayList;

public class ApplicationManager {

    private ArrayList<Auction> loadedAuctions;
    private Profile loadedProfile;
    private User currentUser;

    public ApplicationManager() {
        loadedAuctions = new ArrayList<>();
    }

    public User login(String username, String password) {
        return null;
    }

    public boolean registerUser(String username, String password, String email, String name) {
        return false;
    }

    public boolean logout() {
        return false;
    }
}
