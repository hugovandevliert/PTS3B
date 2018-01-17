package models;

import java.time.LocalDateTime;

public class Feedback {

    private LocalDateTime date;
    private Profile author;
    private String message;
    private boolean isPositive;

    /**
     * Constructor for making a new Feedback-object
     * @param author:       The author of the Feedback
     * @param date:         The creationDate of this feedback.
     * @param isPositive:   Whether the given Feedback was positive
     * @param message:      The String message that belongs to the Feedback
     */
    public Feedback(final Profile author, final LocalDateTime date, final boolean isPositive, final String message) {
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
