package models;

import java.io.IOException;
import java.sql.SQLException;

/**
 * User class, used for authenticating users in the application.
 * */
public class User {

    private String username;
    private String name;
    private String email;
    private int id;
    private Profile profile;

    /**
     * Constructor of the User-object which is used for creating an instance, it requires the following input parameters: Username, Name en Email
     * @param username: The Username of an User-object, Username is an unique String value
     * @param name:     The Name of the User
     * @param email:    The Email of the User, this value should be unique. It should always end with @[Valid domain name]
     */
    public User(final int id, final String username, final String name, final String email, final Profile profile) throws IOException, SQLException, ClassNotFoundException{
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    /**
     * Method for changing the Name of this User-object, this requires a String parameter Name that consist of the new Name it should be changed to
     * @param name: String value of the new Name this User-object should hold
     */
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
        if (email.length() > 255){throw new IllegalArgumentException("Email should not exceed 255 characters");}
        if(!email.contains("@")){throw new IllegalArgumentException("Email should conntain '@'");}
        if(!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$")){throw new IllegalArgumentException("Email should end with a valid domain name");}
        this.email = email;
        return true;
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
