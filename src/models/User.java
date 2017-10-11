package models;

import javafx.scene.image.Image;

/**
 * User class, used for authenticating users in the application.
 * */
public class User {

    private String username, name, email;
    private int id;
    private Profile profile;

    /**
     * Constructor of the User-object which is used for creating an instance, it requires the following input parameters: Username, Name en Email
     * @param username: The Username of an User-object, Username is an unique String value
     * @param name: The Name of the User
     * @param email: The Email of the User, this value should be unique. It should always end with @[Valid domain name]
     */
    public User(final int id, final String username, final String name, final String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    /**
     * Method for changing the password of an User, it requires a String value that holds the new password and will return a boolean value back
     * @param password: The new password that the User's password should be changed to. Password must have at least: 6 characters, 1 lowercase, 1 uppercase, 1 symbol, 1 integer
     * @return: Depending on whether the new password is allowed and the outcome of the check, the method will return true when password is successfully changed
     */
    public boolean changePassword(final String password) { return false; }

    /**
     * Method for getting the Username of this User-object
     * @return: Will return the Username value of this User-object
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method for getting the Name of this User-object
     * @return: Will return the Name value of this User-object
     */
    public String getName() {
        return name;
    }

    /**
     * Method for changing the Name of this User-object, this requires a String parameter Name that consist of the new Name it should be changed to
     * @param name: String value of the new Name this User-object should hold
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Method for getting the Email of this User-object
     * @return: Will return the Email value of this User-object
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method for changing the Email of this User-object, it requires a String input of the new Email value it should hold
     * @param email: The new Email value that this User-object should hold. Requirements: Max. 255 characters, '@', '.' and a legitimate domain (ex.: .NL)
     * @return: Depending on whether the new Email is allowed and accepted it will return a boolean value. The method will return true when it is succesfully changed
     */
    public boolean setEmail(final String email) {
        this.email = email;
        return false;
    }

    /**
     * Method for changing the Photo of this User object, it requires a Image input of the new Photo it should hold
     * @param photo: The new Image value that this User-object should hold as photo. Value may not be null or return value will be false
     * @return: Depending on whether the Image is accepted it will reutrn a boolean value. The method will return true when it s succesfully changed
     */
    public boolean setPhoto(final Image photo) {
        return false;
    }

    /**
     * Method for getting the Profile-object that belongs to this User-object
     * @return: Returns the Profile-object of this User-object
     */
    public Profile getProfile() {
        return null;
    }

    public int getId() {
        return id;
    }
}
