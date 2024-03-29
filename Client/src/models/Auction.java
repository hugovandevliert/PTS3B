package models;

import javafx.scene.image.Image;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* Class for keeping information about an auction.
**/
public class Auction {

    private int id;
    private double startBid;
    private double minimum;
    private double incrementation;
    private boolean isPremium;
    private LocalDateTime expirationDate, openingDate, creationDate;
    private String title, description;
    private ArrayList<Image> images;
    private ArrayList<File> fileImages;
    private ArrayList<Bid> bids;
    private Profile creator;

     /**
     * Constructor used for creating a new auction.
     * @param title:            Title of the auction.
     * @param description:      Description of the auction
     * @param startBid:         Minimum value of the first bid.
     * @param minimum:          Minimum bid the auction must have reached before the seller actually sells the item.
     * @param openingDate:      Date/time when the auction is planned to open.
     * @param expirationDate:   Date/time when the auction is planned to close.
     * @param isPremium:        Indicates if a user paid to boost his auction.
     * @param creator:          The user's profile that created this auction.
     * @param fileImages:       The file's which represent the images added to this auction.
     **/
    public Auction(final String title, final String description, final double startBid, final double minimum, final LocalDateTime openingDate, final LocalDateTime expirationDate, final boolean isPremium, final Profile creator, final ArrayList<File> fileImages, final double incrementation) {
        this.title = title;
        this.description = description;
        this.startBid = startBid;
        this.minimum = minimum;
        this.creationDate = LocalDateTime.now();
        this.openingDate = openingDate;
        this.expirationDate = expirationDate;
        this.isPremium = isPremium;
        this.creator = creator;
        this.fileImages = fileImages;
        this.incrementation = incrementation;

        bids = new ArrayList<>();
    }

    /**
     * Constructor used for listing the auctions.
     * @param id:           The ID of the auction.
     * @param title:        Title of the auction.
     * @param description:  Description of the auction
     * @param startBid:     Minimum value of the first bid.
     * @param images:       All images added to the auction.
     * @param bids:         This arraylist will be filled with the most recent bid.
     **/
    public Auction(final int id, final String title, final String description, final double startBid, final ArrayList<Image> images, final ArrayList<Bid> bids) {
        this.id = id;
        this.startBid = startBid;
        this.title = title;
        this.description = description;
        this.images = images;
        this.bids = bids;
    }

    /**
     * Constructor used by the AuctionCountdownTimer
     * @param id:               The ID of the auction.
     * @param openingDate       Date/time when the auction is allowed to be opened.
     * @param expirationDate:   Date/time when the auction is planned to close.
     */
    public Auction(final int id, final LocalDateTime openingDate, final LocalDateTime expirationDate) {
        this.id = id;
        this.openingDate = openingDate;
        this.expirationDate = expirationDate;
    }

    /**
     * Constructor used for auction page (viewing an auction)
     * @param id:               The ID of the auction.
     * @param title:            Title of the auction.
     * @param description:      Description of the auction
     * @param startBid:         Minimum value of the first bid.
     * @param expirationDate:   Date/time when the auction is planned to close.
     * @param creator:          The user's profile that created this auction.
     * @param images:           All images added to the auction.
     * @param bids:             All bids placed on the auction.
     **/
    public Auction(final int id, final String title, final String description, final double startBid, final LocalDateTime expirationDate, final Profile creator, final ArrayList<Image> images, final ArrayList<Bid> bids, final double minimum, final double incrementation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startBid = startBid;
        this.expirationDate = expirationDate;
        this.creator = creator;
        this.images = images;
        this.bids = bids;
        this.minimum = minimum;
        this.incrementation = incrementation;
    }

    public int getId() { return id; }

    public double getStartBid() {
        return startBid;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getIncrementation() { return incrementation; }

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
        if (images == null) return null;
        return Collections.unmodifiableList(images);
    }

    public List<File> getFileImages() {
        if (fileImages == null) return null;
        return Collections.unmodifiableList(fileImages);
    }

    public List<Bid> getBids() {
        if (bids == null) return null;
        Collections.sort(bids);
        return Collections.unmodifiableList(bids);
    }

    public Profile getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        return title;
    }
}
