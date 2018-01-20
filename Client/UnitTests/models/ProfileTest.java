package models;

import logic.managers.ApplicationManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ASSUMPTION: There is a user in the database with username="user1", password="User1!", name="Mohamed Ali" and email="User1@gmail.com".
 * ASSUMPTION: There is a user in the database with username="user2" and password="User2!"
 * ASSUMPTION: There is a user in the database with username="user3" and password="User3!"
 * ASSUMPTION: There is an auction in the database with id="1"
 * If these are not true, all tests will fail.
 */
public class ProfileTest {
    private ApplicationManager applicationManager;

    @Before
    public void setUp() {
        applicationManager = new ApplicationManager();
    }

    @Test
    public void testAddAuction() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, IOException {
        applicationManager.login("user1", "User1!");
        Profile profile = applicationManager.getCurrentUser().getProfile();
        profile.addAuction(new Auction("testAuction123", "testdescription", 0, 1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), false, null, null, 1));

        assertEquals(profile.getAuctions().get(0).getTitle(), "testAuction123", "Problem adding auction..");
    }

    @Ignore //Can't test because domain method is empty
    public void testAddFeedback() throws SQLException, IOException, ClassNotFoundException, NoSuchAlgorithmException {
        applicationManager.login("user1", "User1!");
        Profile profile = applicationManager.getCurrentUser().getProfile();

        ApplicationManager applicationManager2 = new ApplicationManager();
        applicationManager2.login("user2", "User2!");
        Profile author = applicationManager2.getCurrentUser().getProfile();

        Feedback feedback = new Feedback(author, LocalDateTime.now(), true, "Product goed ontvangen, snelle service.");

        profile.addFeedback(feedback);

        assertTrue(profile.getFeedbacks().contains(feedback));
    }

    @Test
    public void testGetUsername() throws Exception {
        applicationManager.login("user1", "User1!");
        Profile profile = applicationManager.getCurrentUser().getProfile();

        assertEquals("User1", profile.getUsername());
    }

    @Test
    public void testGetName() throws Exception {
        Profile profile = new Profile(null, null, "Mohamed Ali", null, 1);
        assertEquals("Mohamed Ali", profile.getName());
    }

    @Test
    public void testGetEmail() throws Exception {
        Profile p = new Profile(null, null, null, "testuser@email.com", 1);
        assertEquals("testuser@email.com", p.getEmail());
    }

    @Test
    public void testGetProfileId() {
        Profile p = new Profile(1, "Thomas");
        assertEquals(1, p.getProfileId());
    }

    @Test
    public void testGetCreationDate() {
        LocalDateTime ldt = LocalDateTime.now();
        Profile p = new Profile(1, "testuser", "test", ldt, null, "testuser@email.com", null, null);

        assertSame(ldt, p.getCreationDate());
    }

    @Test
    public void testGetAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = new Profile(1, "user1", "Test User", LocalDateTime.now(), null, "test@gmail.com", new ArrayList<Auction>(), null);
        profile.addAuction(new Auction("AbCdE", "beschrijving", 0, 0, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), false, profile, null, 1));

        assertEquals("AbCdE", profile.getAuctions().get(0).getTitle());
    }

    @Ignore //Can't test because add method is empty
    public void testGetFeedbacks() throws SQLException, IOException, ClassNotFoundException, NoSuchAlgorithmException {
        applicationManager.login("user1", "User1!");
        Profile profile = applicationManager.getCurrentUser().getProfile();

        ApplicationManager applicationManager2 = new ApplicationManager();
        applicationManager2.login("user3", "User3!");
        Profile author = applicationManager2.getCurrentUser().getProfile();

        Feedback feedback = new Feedback(author, LocalDateTime.now(), false, "Slechte service, duurde 3 weken voor ontvangst!");

        profile.addFeedback(feedback);

        assertTrue(profile.getFeedbacks().contains(feedback));
    }
}