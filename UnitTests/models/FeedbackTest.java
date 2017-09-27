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

    private static ApplicationManager applicationManager;

    @BeforeAll
    void setUp() {
        applicationManager = new ApplicationManager();
    }

    @Test
    void getDate() {
        Feedback f = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), false, "Testfeedback");
        f.getDate();
        //Can't really test this getter since the value is unknown.
    }

    @Test
    void getAuthor() {
        Profile author = applicationManager.login("testusername2", "AbC*2f").getProfile();
        Feedback f = new Feedback(author, false, "Testfeedback");
        assertSame(author, f.getAuthor(), "Author getter is not working properly.");
    }

    @Test
    void getMessage() {
        Feedback f = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), false, "Testfeedback");
        assertEquals("Testfeedback", f.getMessage());
    }

    @Test
    void isPositive() {
        Feedback f = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), false, "Testfeedback");
        assertFalse(f.isPositive(), "Positivity getter is not working properly for negative value.");

        Feedback f2 = new Feedback(applicationManager.login("testusername2", "AbC*2f").getProfile(), true, "Testfeedback");
        assertTrue(f2.isPositive(), "Positivity getter is not working properly for positive value.");
    }

}