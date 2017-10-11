package models;

import javafx.scene.image.Image;
import utilities.enums.Status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* Class for keeping information about an auction.
**/
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

     /**
     * Constructor used for creating a new auction.
     * @param title: Title of the auction.
     * @param description: Description of the auction
     * @param startBid: Minimum value of the first bid.
     * @param minimum: Minimum bid the auction must have reached before the seller actually sells the item.
     * @param openingDate: Date/time when the auction is planned to open.
     * @param expirationDate: Date/time when the auction is planned to close.
     * @param isPremium: Indicates if a user paid to boost his auction.
     * @param creator: The user's profile that created this auction.
     * @param images: All images added to the auction.
     **/
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

        bids = new ArrayList<Bid>();
    }

    /**
     * Constructor used for listing the auctions.
     * @param id: The ID of the auction.
     * @param title: Title of the auction.
     * @param description: Description of the auction
     * @param startBid: Minimum value of the first bid.
     * @param images: All images added to the auction.
     **/
    public Auction(final int id, final String title, final String description, final double startBid, final ArrayList<Image> images) {
        this.id = id;
        this.startBid = startBid;
        this.title = title;
        this.description = description;
        this.images = images;

        bids = new ArrayList<Bid>();
    }

    /**
     * Constructor used for auction page (viewing an auction)
     * @param id: The ID of the auction.
     * @param title: Title of the auction.
     * @param description: Description of the auction
     * @param startBid: Minimum value of the first bid.
     * @param expirationDate: Date/time when the auction is planned to close.
     * @param creator: The user's profile that created this auction.
     * @param images: All images added to the auction.
     * @param bids: All bids placed on the auction.
     **/
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

    /**
     * Method to get the id of this auction.
     * @return Returns the id of this auction.
     */
    public int getId() { return id; }

    /**
     * Method to get the minimum amount to start bidding of this auction.
     * @return Returns the start bid of this auction.
     */
    public double getStartBid() {
        return startBid;
    }

    /**
     * Method to get the minimum of this auction.
     * Minimum is the minumum amount of money for which the creator of this auction will sell it.
     * The minimum is only visible to the creator himself.
     * @return Returns the minimum of this auction.
     */
    public double getMinimum() {
        return minimum;
    }

    /**
     * Method to check if this auction is a premium auction.
     * @return Returns true if this auction is indeed a premium auction. Otherwise returns false.
     */
    public boolean isPremium() {
        return isPremium;
    }

    /**
     * Method to get the expiration date of this auction.
     * @return Returns the expiration date of this auction.
     */
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Method to get the opening date of this auction.
     * @return Returns the opening date of this auction.
     */
    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    /**
     * Method to get the creation date of this auction.
     * @return Returns the creation date of this auction.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Method to get the title of this auction.
     * @return Returns the title of this auction.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method to get the description of this auction.
     * @return Returns the description of this auction.
     */
    public String getDescription() { return description; }

    /**
     * Method to get all the images of this auction.
     * @return Returns a list of images containing all the images of this auction.
     */
    public List<Image> getImages() {
        return Collections.unmodifiableList(images);
    }

    /**
     * Method to get all the bids placed on this auction.
     * @return Returns a list containing all the bids placed on this auction.
     */
    public List<Bid> getBids() {
        Collections.sort(bids);
        return Collections.unmodifiableList(bids);
    }

    /**
     * Method to get the status of this auction.
     * @return Returns the status of this auction.
     */
    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    /**
     * Method to add a bid to an auction.
     * @param amount: Amount that was bid.
     * @param profile: profile that placed the bid.
     **/
    public void addBid(final double amount, final Profile profile) { }

    /**
    * Method to end the auction
    * @return: Return true if succeeded, false if failed to close.
    **/
    public boolean endAuction() {
        return false;
    }


    /**
     * Method to get the creator of this auction.
     * @return Returns the profile of the user who created this auction.
     */
    public Profile getCreator() {
        return creator;
    }
}
