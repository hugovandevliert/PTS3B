package models;

import javafx.scene.image.Image;
import utilities.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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


    //Constructor used for creating a new Auction.
    public Auction(String title, String description, double startBid, double minimum, LocalDateTime openingDate, LocalDateTime expirationDate, boolean isPremium, Profile creator, ArrayList<Image> images) {
        this.title = title;
        this.description = description;
        this.startBid = startBid;
        this.minimum = minimum;
        this.creationDate = LocalDateTime.now();
        this.openingDate = openingDate;
        this.expirationDate = expirationDate;
        this.isPremium = isPremium;
        this.creator = creator;
        this.images = images;
        this.status = Status.OPEN;
    }

    //Constructor used for listed auctions
    public Auction(final int id, final String title, final String description, final double startBid, final ArrayList<Image> images) {
        this.id = id;
        this.startBid = startBid;
        this.title = title;
        this.description = description;
        this.images = images;

        bids = new ArrayList<Bid>();
    }

    //Constructor used for auction page
    public Auction(final int id, final String title, final String description, final double startBid, final LocalDateTime expirationDate, final Profile creator, final ArrayList<Image> images, final ArrayList<Bid> bids) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startBid = startBid;
        this.expirationDate = expirationDate;
        this.creator = creator;
        this.images = images;
        this.bids = bids;
    }

    public int getId() { return id; }

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
        Collections.sort(bids);
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
