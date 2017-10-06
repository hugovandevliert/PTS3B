package models;

import javafx.scene.image.Image;
import utilities.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Auction {

    private int id;
    private double startBid, minimum;
    private boolean isPremium;
    private LocalDateTime expirationDate, openingDate, creationDate;
    private String title, description;
    private Status status;
    private ArrayList<Image> images;
    private ArrayList<Bid> bids;
    private Profile creator;

    public Auction(final int id, final String title, final String description, final double startBid, final ArrayList<Image> images) {
        this.id = id;
        this.startBid = startBid;
        this.title = title;
        this.description = description;
        this.images = images;
    }

    public double getId() { return id; }

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

    public String getDescription() { return description; }

    public List<Image> getImages() {
        return Collections.unmodifiableList(images);
    }

    public List<Bid> getBids() {
        return Collections.unmodifiableList(bids);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void addBid(final double amount, final Profile profile) { }

    public boolean endAuction() {
        return false;
    }

    public Profile getCreator() {
        return creator;
    }
}
