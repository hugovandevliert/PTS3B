package models;

import java.util.Date;

/**
 * Class used for keeping information about a bid on an auction.
 * */
public class Bid {

    private Date date;
    private Profile profile;
    private double amount;

    /**
     * Constructor for Bid.
     * @param profile: The user's profile that placed the bid.
     * @param amount: The amount of money.
     * */
    public Bid(final Profile profile, final double amount, final Date date) {
        this.profile = profile;
        this.date = date;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public Profile getProfile() { return profile; }

    public double getAmount() { return amount; }
}
