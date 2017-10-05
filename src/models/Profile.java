package models;

import javafx.scene.image.Image;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Profile class, used for interaction between users.
 * */
public class Profile {

    private Image photo;
    private String username, name, email;
    private int profileId;

    private ArrayList<Auction> auctions, visitedAuctions, favoriteAuctions;
    private ArrayList<Feedback> feedbacks;

    /**
     * Constructor for a profile.
     * @param photo: User's profile picture.
     * @param username: User's username.
     * @param name: User's full name.
     * @param email: User's email address.
     * @param profileId: The ID of this profile.
     * */
    public Profile(Image photo, String username, String name, String email, int profileId) {
        auctions = new ArrayList<>();
        visitedAuctions = new ArrayList<>();
        favoriteAuctions = new ArrayList<>();
        feedbacks = new ArrayList<>();

        this.photo = photo;
        this.username = username;
        this.name = name;
        this.email = email;
        this.profileId = profileId;
    }

    /**
     * Method for creating a new auction.
     * @param startBid: Minimum value of the first bid. Must be >0.
     * @param minimum: Minimum bid the auction must have reached before the seller actually sells the item. Must be >0.
     * @param expirationDate: Date/time when the auction is planned to close. Should be later then the current date.
     * @param openingDate: Date/time when the auction is planned to open. Should be earlier then the the expirationdate, and can't be earlier then today.
     * @param isPremium: Indicates if a user paid to boost his auction.
     * @param title: Title of the auction. Can't contain more then 64 characters.
     * @param images: All images added to the auction.
     * */
    public void addAuction(double startBid, double minimum, LocalDateTime expirationDate, LocalDateTime openingDate, boolean isPremium, String title, ArrayList<Image> images) {

    }


    /**
     * Method for adding an auction to a profile's visited auctions.
     * @param auction: The auction to add.
     * */
    public void addVisitedAuction(Auction auction) {

    }

    /**
     * Method for adding an auction to a profile's favorite auctions.
     * @param auction: The auction to add.
     * */
    public void addFavoriteAuction(Auction auction) {

    }

    /**
     * Method for removing an auction to a profile's favorite auctions.
     * @param auction: The auction to remove.
     * */
    public void removeFavoriteAuction(Auction auction) {

    }

    /**
     * Method for adding feedback to this profile
     * @param author: Profile that gave the feedback.
     * @param isPositive: Feedback can be positive (true), or negative (false)
     * @param message: Message added to the feedback.
     * */
    public void addFeedback(Profile author, boolean isPositive, String message) {

    }

    /**
     * Method for notifying the user. //Todo: How does this method know what to notify the user of?
     * */
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

    public List<Auction> getAuctions() {
        return Collections.unmodifiableList(auctions);
    }

    public List<Auction> getVisitedAuctions() {
        return Collections.unmodifiableList(visitedAuctions);
    }

    public List<Auction> getFavoriteAuctions() {
        return Collections.unmodifiableList(favoriteAuctions);
    }

    public List<Feedback> getFeedbacks() {
        return Collections.unmodifiableList(feedbacks);
    }
}
