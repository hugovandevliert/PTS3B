package models;

import data.contexts.ProfileMySqlContext;
import data.contexts.UserMySqlContext;
import logic.algorithms.Sha256HashCalculator;
import logic.repositories.ProfileRepository;
import logic.repositories.UserRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.provider.SHA;
import utilities.enums.ProfileLoadingType;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * User class, used for authenticating users in the application.
 * */
public class User {

    private String username, name, email;
    private int id;
    private Profile profile;

    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    /**
     * Constructor of the User-object which is used for creating an instance, it requires the following input parameters: Username, Name en Email
     * @param username: The Username of an User-object, Username is an unique String value
     * @param name:     The Name of the User
     * @param email:    The Email of the User, this value should be unique. It should always end with @[Valid domain name]
     */
    public User(final int id, final String username, final String name, final String email) throws IOException, SQLException, ClassNotFoundException{
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;

        profileRepository = new ProfileRepository(new ProfileMySqlContext());
        userRepository = new UserRepository(new UserMySqlContext());

        this.profile = profileRepository.getProfileForId(id, ProfileLoadingType.FOR_AUCTION_PAGE);
    }

    /**
     * Method for changing the password of an User, it requires a String value that holds the new password and will return a boolean value back
     * @param currentPassword:  The current password used for an extra security check.
     * @param newPassword:      The new password that the User's password should be changed to. Password must have at least: 6 characters, 1 lowercase, 1 uppercase, 1 symbol, 1 integer
     * @return: Depending on whether the new password is allowed and the outcome of the check, the method will return true when password is successfully changed
     */
    public boolean changePassword(final String currentPassword, final String newPassword) throws SQLException {
        if (newPassword.length() < 6){
            throw new IllegalArgumentException("Password should be at least 6 characters");
        }
        if (newPassword.matches("^[0-9]*$")) {
            throw new IllegalArgumentException("Password should not only contain numbers");}
        if (newPassword.length() > 32) {
            throw new IllegalArgumentException("Password should not exceed 32 characters");
        }
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,}")) {
            throw new IllegalArgumentException("Password doesn't contain Upper/Lower case letter or at least one number or one special character");
        }

        final String[] saltAndHash = userRepository.getSaltAndHash(this.username);
        final Sha256HashCalculator sha256HashCalculator = new Sha256HashCalculator();

        //Check if the currentPassword is correct.
        if(sha256HashCalculator.hashString(currentPassword, saltAndHash[0]) == saltAndHash[1]){
            return userRepository.setPassword(sha256HashCalculator.hashString(newPassword, saltAndHash[0]), this.username);
        }

        return false;
    }

    /**
     * Method for changing the Name of this User-object, this requires a String parameter Name that consist of the new Name it should be changed to
     * @param name: String value of the new Name this User-object should hold
     */
    //TODO: Shouldn't this return a boolean as well? So we can check if it was actually updated on the database without any SQLExceptions etc?
    public void setName(final String name) {
        if(name.length() > 64){throw new IllegalArgumentException("Name can't be longer then 64 characters"); }
        if(name.matches(".*[0-9].*")){throw new IllegalArgumentException("Full name can't contain numbers");}
        if(!name.matches("[A-z ]+")){throw new IllegalArgumentException("Full name should only contain letters. No other characters accepted");}
        this.name = name;
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
    public boolean setPhoto(final File photo) {
        return userRepository.setPhoto(this, photo);
    }

    public Profile getProfile() {
        return profile;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }
}
