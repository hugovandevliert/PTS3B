package models;

import javafx.scene.image.Image;
import modelslibrary.Auction;
import modelslibrary.Bid;
import modelslibrary.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuctionTest {

    private Profile profile1;
    private Auction auction1;
    private Auction auction2;

    @BeforeEach
    void setUp() throws SQLException {
        profile1 = new Profile(1, "MyProfile");
        auction1 = new Auction("MyTitle", "MyDescription", 0, 5, LocalDateTime.of(2016, 9, 10, 12, 33), LocalDateTime.of(2017, 10, 11, 13, 34), false, profile1, null, 5);
        auction2 = new Auction(0, "MySecondTitle", "MySecondDescription", 10, LocalDateTime.of(2017, 10, 11, 13, 38), profile1, new ArrayList<>(), new ArrayList<>(), 0, 5);
    }

    @Test
    void testGetId() {
        assertEquals(0, auction2.getId(), "The auction ID should've been 0");
    }

    @Test
    void testGetStartBid() {
        assertEquals(10, auction2.getStartBid());
    }

    @Test
    void testGetMinimum() {
        assertEquals(5, auction1.getMinimum(), "Minimum should've been 5");
    }

    @Test
    void testGetIncrementation() {
        assertEquals(5, auction2.getIncrementation());
    }

    @Test
    void testIsPremium() {
        assertEquals(false, auction1.isPremium(), "Should've been false");
    }

    @Test
    void testGetExpirationDate() {
        assertEquals(LocalDateTime.of(2017, 10, 11, 13, 34), auction1.getExpirationDate(), "This should've been 11-10-2017 13:34");
        assertEquals(LocalDateTime.of(2017, 10, 11, 13, 38), auction2.getExpirationDate(), "This should've been 11-10-2017 13:38");
    }

    @Test
    void testGetOpeningDate() {
        assertEquals(LocalDateTime.of(2016, 9, 10, 12, 33), auction1.getOpeningDate(), "This should've been 10-9-2016 12:33");
    }

    @Test
    void testGetCreationDate() {
        //Can't test this :@
    }

    @Test
    void testGetTitle() {
        assertEquals("MyTitle", auction1.getTitle(), "This should've been MyTitle");
        assertEquals("MySecondTitle", auction2.getTitle(), "This should've been MySecondTitle");
    }

    @Test
    void testGetDescription() {
        assertEquals("MyDescription", auction1.getDescription(), "This should've been MyDescription");
        assertEquals("MySecondDescription", auction2.getDescription(), "This should've been MySecondDescription");
    }

    @Test
    void testGetImages() {
        assertNull(auction1.getImages());
        assertEquals(new ArrayList<Image>(), auction2.getImages());
    }

    @Test
    void testGetFileImages() {
        assertEquals(0, auction1.getFileImages().size());
    }

    @Test
    void testGetBids() {
        assertEquals(new ArrayList<Bid>(), auction2.getBids());
    }

    @Test
    void testGetCreator() {
        assertEquals(profile1, auction1.getCreator());
        assertEquals(profile1, auction2.getCreator());
    }
}