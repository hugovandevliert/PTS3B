package models;

import data.contexts.AuctionMySqlContext;
import javafx.scene.image.Image;
import logic.repositories.AuctionRepository;
import java.io.File;
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

    private ArrayList<Auction> auctions, visitedAuctions, favoriteAuctions;
    private ArrayList<Feedback> feedbacks;

    private LocalDateTime creationDate;

    private AuctionRepository auctionRepository;

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
        visitedAuctions = new ArrayList<>();
        favoriteAuctions = new ArrayList<>();
        feedbacks = new ArrayList<>();

        auctionRepository = new AuctionRepository(new AuctionMySqlContext());

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
    public Profile(final int profileId, final String username, final LocalDateTime creationDate, final Image photo, final ArrayList<Auction> auctions, final ArrayList<Feedback> feedbacks) {
        this(profileId, username);
        this.creationDate = creationDate;
        this.photo = photo;
        this.auctions = auctions;
        this.feedbacks = feedbacks;
    }

    /**
     * Method for creating a new auction.
     * @param startBid:       Minimum value of the first bid. Must be >0.
     * @param minimum:        Minimum bid the auction must have reached before the seller actually sells the item. Must be >0.
     * @param expirationDate: Date/time when the auction is planned to close. Should be later then the current date.
     * @param openingDate:    Date/time when the auction is planned to open. Should be earlier then the the expirationdate, and can't be earlier then today.
     * @param isPremium:      Indicates if a user paid to boost his auction.
     * @param title:          Title of the auction. Can't contain more then 64 characters.
     * @param fileImages:     The file's which represent the images added to this auction.
     */
    public boolean addAuction(final double startBid, final double minimum, final LocalDateTime expirationDate, final LocalDateTime openingDate, final boolean isPremium, final String title, final String description, final ArrayList<File> fileImages) throws SQLException {
        if (startBid <= 0){
            throw new IllegalArgumentException("Startbid should be higher than 0.");
        }
        else if (minimum <= 0){
            throw new IllegalArgumentException("Minimum bid should be higher than 0.");
        }
        else if (expirationDate == null){
            throw new IllegalArgumentException("Expiration date can not be empty.");
        }
        else if (expirationDate.compareTo(LocalDateTime.now()) < 0){
            throw new IllegalArgumentException("Expiration date should be in the future");
        }
        else if (openingDate == null){
            throw new IllegalArgumentException("Opening date can not be empty");
        }
        else if (openingDate.compareTo(LocalDateTime.now()) < 0){
            throw new IllegalArgumentException("Opening date can not be before today");
        }
        else if (expirationDate.compareTo(openingDate) < 1){
            throw new IllegalArgumentException("Opening date must be before the expiration date");
        }
        else if (title.length() > 64){
            throw new IllegalArgumentException("Title can not be longer than 64 characters.");
        }

        final Auction auction = new Auction(title, description, startBid, minimum, openingDate, expirationDate, isPremium, this, fileImages);

        if (auctionRepository.addAuction(auction)) {
            auctions.add(auction);
            return true;
        }
        return false;
    }

    /**
     * Method used for adding an auction to this Profile-object for testing purposes.
     * @param auction: The auction to be added to this profile.
     */
    public void addAuction(final Auction auction) throws SQLException {
        auctions.add(auction);
        auctionRepository.addAuction(auction);
    }

    /**
     * Method for adding an auction to a profile's visited auctions.
     * @param auction: The auction to add.
     */
    public void addVisitedAuction(final Auction auction) {

    }

    /**
     * Method for adding an auction to a profile's favorite auctions.
     * @param auction: The auction to add.
     */
    public void addFavoriteAuction(final Auction auction) {

    }

    /**
     * Method for removing an auction to a profile's favorite auctions.
     * @param auction: The auction to remove.
     */
    public void removeFavoriteAuction(final Auction auction) {

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
