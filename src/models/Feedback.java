package models;

import java.time.LocalDateTime;

public class Feedback {

    private LocalDateTime date;
    private Profile author;
    private String message;
    private boolean isPositive;

    Feedback(Profile author, LocalDateTime date, boolean isPositive, String message) {
        this.author = author;
        this.date = date;
        this.isPositive = isPositive;
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Profile getAuthor() { return author; }

    public String getMessage() { return message; }

    public boolean isPositive() {
        return isPositive;
    }
}
