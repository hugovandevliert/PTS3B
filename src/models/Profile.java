package models;

import javafx.scene.image.Image;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Profile class, used for interaction between users.
 */
public class Profile {

    private Image photo;
    private String username, name, email;
    private int profileId;

    private ArrayList<Auction> auctions;
    private ArrayList<Feedback> feedbacks;

    private LocalDateTime creationDate;

    /**
     * Default Constructor.
     * @param photo:     User's profile picture.
     * @param username:  User's username.
     * @param name:      User's full name.
     * @param email:     User's email address.
     * @param profileId: The ID of this profile.
     */
    public Profile(final Image photo, final String username, final String name, final String email, final int profileId) {
        this(profileId, username);

        auctions = new ArrayList<>();
        feedbacks = new ArrayList<>();

        this.photo = photo;
        this.name = name;
        this.email = email;
    }

    /**
     * This constructor is used for setting default properties.
     * This constructor is also used for loading profiles for FOR_AUCTION_PAGE.
     * @param profileId:     The ID of this profile.
     * @param username:      User's username.
     */
    public Profile(final int profileId, final String username) {
        auctions = new ArrayList<>();
        feedbacks = new ArrayList<>();

        this.username = username;
        this.profileId = profileId;
    }

    /**
     * This constructor is used for loading profiles for FOR_PROFILE_PAGE.
     * @param profileId:     The ID of this profile.
     * @param username:      User's username.
     * @param creationDate:  The register date of this user/profile.
     * @param photo:         The profile picture of this profile.
     * @param auctions:      The current running auctions that this profile is the creator of.
     */
    public Profile(final int profileId, final String username, final String name, final LocalDateTime creationDate, final Image photo, final String email, final ArrayList<Auction> auctions, final ArrayList<Feedback> feedbacks) {
        this(profileId, username);
        this.creationDate = creationDate;
        this.name = name;
        this.photo = photo;
        this.email = email;
        this.auctions = auctions;
        this.feedbacks = feedbacks;
    }

    /**
     * Method used for adding an auction to this Profile-object for testing purposes.
     * @param auction: The auction to be added to this profile.
     */
    public void addAuction(final Auction auction) throws SQLException {
        auctions.add(auction);
    }

    /**
     * Method for adding feedback to this Profile-object
     * @param author:     Profile that gave the feedback.
     * @param isPositive: Feedback can be positive (true), or negative (false)
     * @param message:    Message added to the feedback.
     */
    public void addFeedback(final Profile author, final boolean isPositive, final String message) {

    }

    /**
     * Method for adding feedback to this Profile-object for testing purposes.
     * @param feedback: The Feedback-object to add to this Profile-object
     */
    public void addFeedback(final Feedback feedback) {

    }

    /**
     * Method for notifying the user. //Todo: How does this method know what to notify the user of?
     */
    public void notifyUser() {

    }

    public Image getPhoto() {
        return photo;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getProfileId() {
        return profileId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public List<Auction> getAuctions() {
        return Collections.unmodifiableList(auctions);
    }

    public List<Feedback> getFeedbacks() {
        return Collections.unmodifiableList(feedbacks);
    }
}
