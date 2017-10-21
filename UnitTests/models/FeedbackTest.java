package models;

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
 * ASSUMPTION: There is a user in the database with username="testusername", password="AbC*1f", name="Test User" and email="testuser@gmail.com".
 * ASSUMPTION: There is a user in the database with username="testusername2" and password="AbC*1f"
 * If these are not true, all tests will fail.*/
class FeedbackTest {
    static private ApplicationManager applicationManager;

    @BeforeAll
    static void setUp() {
        applicationManager = new ApplicationManager();
    }

    @Test
    void testGetDate() throws SQLException, IOException, ClassNotFoundException {
        final LocalDateTime date = LocalDateTime.now();
        final Feedback feedback = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), date, false, "Testfeedback");
        assertEquals(date, feedback.getDate(), "Creation date is not working properly.");
    }

    @Test
    void testGetAuthor() throws SQLException, IOException, ClassNotFoundException {
        final Profile author = applicationManager.login("testusername2", "AbC*2f").getProfile();
        final Feedback feedback = new Feedback(author, LocalDateTime.now(), false, "Testfeedback");
        assertSame(author, feedback.getAuthor(), "Author getter is not working properly.");
    }

    @Test
    void testGetMessage() throws SQLException, IOException, ClassNotFoundException {
        final Feedback feedback = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), LocalDateTime.now(), false, "Testfeedback");
        assertEquals("Testfeedback", feedback.getMessage());
    }

    @Test
    void testIsPositive() throws SQLException, IOException, ClassNotFoundException {
        Feedback feedback = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), LocalDateTime.now(), false, "Testfeedback");
        assertFalse(feedback.isPositive(), "Positivity getter is not working properly for negative value.");

        feedback = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), LocalDateTime.now(), true, "Testfeedback");
        assertTrue(feedback.isPositive(), "Positivity getter is not working properly for positive value.");
    }

}