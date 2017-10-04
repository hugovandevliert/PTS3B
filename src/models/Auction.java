package models;

import javafx.scene.image.Image;
import utilities.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for keeping information about an auction.
 * */
public class Auction {
    private double startBid, minimum;
    private boolean isPremium;
    private LocalDateTime expirationDate, openingDate, creationDate;
    private String title;
    private Status status;
    private ArrayList<Image> images;
    private ArrayList<Bid> bids;
    private Profile creator;

    /**
     * Constructor for an auction.
     * @param startBid: Minimum value of the first bid.
     * @param minimum: Minimum bid the auction must have reached before the seller actually sells the item.
     * @param isPremium: Indicates if a user paid to boost his auction.
     * @param expirationDate: Date/time when the auction is planned to close.
     * @param openingDate: Date/time when the auction is planned to open.
     * @param title: Title of the auction.
     * @param status: Status of the auction. Currently: CLOSED or OPEN.
     * @param images: All images added to the auction.
     * @param bids: All bids on this auction.
     * @param creator: The user's profile that created this auction.
     * */
    Auction(double startBid, double minimum, boolean isPremium, LocalDateTime expirationDate, LocalDateTime openingDate, String title, Status status, ArrayList<Image> images, ArrayList<Bid> bids, Profile creator) {
        this.startBid = startBid;
        this.minimum = minimum;
        this.isPremium = isPremium;
        this.expirationDate = expirationDate;
        this.openingDate = openingDate;
        this.creationDate = LocalDateTime.now(); //todo: Is this smart? When loading a bid from the database it's date will reset.
        this.title = title;
        this.status = status;
        this.images = images;
        this.bids = bids;
        this.creator = creator;
    }

    /**
     * Method to add a bid to an auction.
     * @param amount: Amount that was bid.
     * @param profile: profile that placed the bid.
     * */
    public void addBid(double amount, Profile profile) {
    }

    /**
     * Method to end the auction
     * @return: Return true if succeeded, false if failed to close.
     * */
    public boolean endAuction() {
        return false;
    }

    public double getStartBid() {
        return startBid;
    }

    public double getMinimum() {
        return minimum;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getTitle() {
        return title;
    }

    public List<Image> getImages() { return Collections.unmodifiableList(images);
    }

    public List<Bid> getBids() { return Collections.unmodifiableList(bids); }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Profile getCreator() { return creator; }
}
