package models;

import javafx.scene.image.Image;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Profile {

    private Image photo;
    private String username, name, email;
    private int profileId;

    private ArrayList<Auction> auctions, visitedAuctions, favoriteAuctions;
    private ArrayList<Feedback> feedbacks;

    Profile(Image photo, String username, String name, String email, int profileId) {
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

    public void addAuction(Profile profile, double startBid, double minimum, LocalDateTime expirationDate, LocalDateTime openingDate, boolean isPremium, String title, ArrayList<Image> images) {

    }

    public void addVisitedAuction(Auction auction) {

    }

    public void addFavoriteAuction(Auction auction) {

    }

    public void removeFavoriteAuction(Auction auction) {

    }

    public void addFeedback(Feedback feedback, Profile author) {

    }

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

    public List<Auction> getAuctions() { return Collections.unmodifiableList(auctions); }

    public List<Auction> getVisitedAuctions() { return Collections.unmodifiableList(visitedAuctions); }

    public List<Auction> getFavoriteAuctions() { return Collections.unmodifiableList(favoriteAuctions); }

    public List<Feedback> getFeedbacks() { return Collections.unmodifiableList(feedbacks); }
}
