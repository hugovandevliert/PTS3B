package models;

import core.ApplicationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ASSUMPTION: There is a user in the database with username="testusername", password="AbC*1f", name="Test User" and email="testuser@gmail.com".
 * ASSUMPTION: There is a user in the database with username="testusername2" and password="AbC*1f"
 * If these are not true, all tests will fail.
 */
class FeedbackTest {
    static private ApplicationManager applicationManager;

    @BeforeAll
    static void setUp() {
        applicationManager = new ApplicationManager();
    }

    @Test
    void testGetDate() {
        Feedback f = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), false, "Testfeedback");
        assertNotNull(f.getDate());
        //Can't really test the value of this getter since the value is unknown.
        //Todo: Look into delta's
    }

    @Test
    void testGetAuthor() {
        Profile author = applicationManager.login("testusername2", "AbC*2f").getProfile();
        Feedback f = new Feedback(author, false, "Testfeedback");
        assertSame(author, f.getAuthor(), "Author getter is not working properly.");
    }

    @Test
    void testGetMessage() {
        Feedback f = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), false, "Testfeedback");
        assertEquals("Testfeedback", f.getMessage());
    }

    @Test
    void testIsPositive() {
        Feedback f = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), false, "Testfeedback");
        assertFalse(f.isPositive(), "Positivity getter is not working properly for negative value.");

        f = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), true, "Testfeedback");
        assertTrue(f.isPositive(), "Positivity getter is not working properly for positive value.");
    }

}