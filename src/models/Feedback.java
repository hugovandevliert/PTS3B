package models;

import java.time.LocalDateTime;

public class Feedback {

    private LocalDateTime date;
    private Profile author;
    private String message;
    private boolean isPositive;

    /**
     * Constructor for making a new Feedback-object, it requires the following parameter input: Author, Date, isPositive, Message.
     * @param author: The author of the Feedback
     * @param isPositive: Whether the given Feedback was positive
     * @param message: The String message that belongs to the Feedback
     */
    public Feedback(final Profile author, final LocalDateTime date, final boolean isPositive, final String message) {
        this.author = author;
        this.date = date;
        this.isPositive = isPositive;
        this.message = message;
    }

    /**
     * Method that gets the Date of when the Feedback was given
     * @return: Returns the Date of when the Feedback was made
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Method that gets the Profile-object that belongs to the Author that made the Feedback
     * @return: Returns the Profile-object that belongs to the Author
     */
    public Profile getAuthor() { return author; }

    /**
     * Method that gets the Message that belongs to this Feedback-object
     * @return: Returns the Message that belongs to this Feedback
     */
    public String getMessage() { return message; }

    /**
     * Method that gets the attribute belonging to this Feedback-object, whether the author is positive or not. It returns a boolean output
     * @return: Returns the boolean value of whether the Feedback is positive or not
     */
    public boolean isPositive() {
        return isPositive;
    }
}
