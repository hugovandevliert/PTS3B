package models;

import core.ApplicationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    private static ApplicationManager applicationManager;

    @BeforeAll
    void setUp() {
        applicationManager = new ApplicationManager();
        User user = applicationManager.login("testuser", "AbC*1f");
        Profile profile = new ApplicationManager().login("testuser", "AbC*1f").getProfile();
    }

    @Test
    void getDate() {
        Feedback f = new Feedback(applicationManager.login("testuser2", "AbC*2f").getProfile(), false, "Testfeedback");
        //Can't really test this getter since the value is unknown.
    }

    @Test
    void getAuthor() {
        Profile author = applicationManager.login("testuser2", "AbC*2f").getProfile();
        Feedback f = new Feedback(author, false, "Testfeedback");
        assertSame(author, f.getAuthor(), "Author getter is not working properly.");
    }

    @Test
    void getMessage() {
        Feedback f = new Feedback(applicationManager.login("testuser2", "AbC*2f").getProfile(), false, "Testfeedback");
        assertEquals("Testfeedback", f.getMessage());
    }

    @Test
    void isPositive() {
        Feedback f = new Feedback(applicationManager.login("testuser2", "AbC*2f").getProfile(), false, "Testfeedback");
        assertFalse(f.isPositive(), "Positivity getter is not working properly for negative value.");

        Feedback f2 = new Feedback(applicationManager.login("testuser2", "AbC*2f").getProfile(), true, "Testfeedback");
        assertTrue(f2.isPositive(), "Positivity getter is not working properly for positive value.");
    }

}