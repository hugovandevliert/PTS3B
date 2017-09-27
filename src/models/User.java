package models;

import javafx.scene.image.Image;

public class User {

    private String username, name, email;
    private Profile profile;

    User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public boolean changePassword(String newPassword) {
        return false;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        this.email = email;
        return false;
    }

    public boolean setPhoto(Image photo) {
        return false;
    }

    public Profile getProfile() {
        return null;
    }
}
