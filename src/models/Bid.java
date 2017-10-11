package models;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Class used for keeping information about a bid on an auction.
 * */
public class Bid implements Comparable<Bid> {

    private LocalDateTime date;
    private Profile profile;
    private double amount;

    /**
     * Constructor for Bid. This constructor is being used for auctions
     * @param profile: The user's profile that placed the bid.
     * @param amount: The amount of money.
     * */
    public Bid(final Profile profile, final double amount, final LocalDateTime date) {
        this.profile = profile;
        this.date = date;
        this.amount = amount;
    }

    /**
     * Constructor for Bid. This constructor is being used for using the most recent bid
     * @param amount: the amount of money.
     */
    public Bid(final double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Profile getProfile() { return profile; }

    public double getAmount() { return amount; }

    @Override
    public int compareTo(Bid o) {
        if (this.amount > o.getAmount()) return -1;
        else if (this.amount < o.getAmount()) return 1;
        return 0;
    }
}
