package models;

import javafx.scene.image.Image;
import utilities.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Auction {

    private int ID;
    private double startBid, minimum;
    private boolean isPremium;
    private LocalDateTime expirationDate, openingDate, creationDate;
    private String title;
    private Status status;
    private ArrayList<Image> images;
    private ArrayList<Bid> bids;
    private Profile creator;

    Auction() {
        images = new ArrayList<>();
        bids = new ArrayList<>();
    }

    public double getID() { return ID; }

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

    public List<Image> getImages() {
        return Collections.unmodifiableList(images);
    }

    public List<Bid> getBids() {
        return Collections.unmodifiableList(bids);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addBid(double amount, Profile profile) {
    }

    public boolean endAuction() {
        return false;
    }

    public Profile getCreator() {
        return creator;
    }
}
