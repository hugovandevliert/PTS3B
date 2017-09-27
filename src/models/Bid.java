package models;

import java.time.LocalDateTime;

public class Bid {

    private LocalDateTime date;
    private Profile profile;
    private double amount;

    Bid(Profile profile, double amount) {
        //time hier implementeren
        this.profile = profile;
        this.date = date;
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Profile getProfile() { return profile; }

    public double getAmount() { return amount; }
}
