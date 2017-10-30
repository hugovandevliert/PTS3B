package models;

import com.sun.javaws.exceptions.InvalidArgumentException;
import core.ApplicationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * ASSUMPTION: There is a user in the database with username="user1", password="User1!", name="Test User" and email="testuser@gmail.com".
 * ASSUMPTION: There is a user in the database with username="user2" and password="User2!"
 * If these are not true, all tests will fail.*/
class FeedbackTest {
    static private ApplicationManager applicationManager;

    @BeforeAll
    static void setUp() {
        applicationManager = new ApplicationManager();
    }

    @Test
    void testGetAuthor() throws Exception {
        final Profile author = applicationManager.login("user2", "User2!").getProfile();
        final Feedback feedback = new Feedback(author, LocalDateTime.now(), false, "Testfeedback");
        assertSame(author, feedback.getAuthor(), "Author getter is not working properly.");
    }

    @Test
    void testGetMessage() throws Exception {
        final Feedback feedback = new Feedback(applicationManager.login("user2", "User2!").getProfile(), LocalDateTime.now(), false, "Testfeedback");
        assertEquals("Testfeedback", feedback.getMessage());
    }

    @Test
    void testIsPositive() throws Exception {
        Feedback feedback = new Feedback(applicationManager.login("user1", "User1!").getProfile(), LocalDateTime.now(), false, "Testfeedback");
        assertFalse(feedback.isPositive(), "Positivity getter is not working properly for negative value.");

        feedback = new Feedback(applicationManager.login("user2", "User2!").getProfile(), LocalDateTime.now(), true, "Testfeedback");
        assertTrue(feedback.isPositive(), "Positivity getter is not working properly for positive value.");
    }

}