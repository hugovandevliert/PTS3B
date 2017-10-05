package models;

import java.time.LocalDateTime;

/**
 * Class used for keeping information about a bid on an auction.
 * */
public class Bid {

    private LocalDateTime date;
    private Profile profile;
    private double amount;

    /**
     * Constructor for Bid.
     * @param profile: The user's profile that placed the bid.
     * @param amount: The amount of money.
     * */
    public Bid(Profile profile, double amount) {
        this.profile = profile;
        this.date = LocalDateTime.now(); //todo: Is this smart? When loading a bid from the database it's date will reset. :thinking:
        this.amount = amount;
    }

    public Bid(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Profile getProfile() { return profile; }

    public double getAmount() { return amount; }
}
