package models;

import javafx.scene.image.Image;
import utilities.enums.Status;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class AuctionTest {

    private Profile profile1;
    private Profile profile2;
    private Auction auction1;
    private Auction auction2;

    @BeforeEach
    void setUp() throws SQLException {
        profile1 = new Profile(1, "MyProfile");
        profile2 = new Profile(2, "MySecondProfile");
        auction1 = new Auction("MyTitle", "MyDescription", 0, 5, LocalDateTime.of(2016, 9, 10, 12, 33), LocalDateTime.of(2017, 10, 11, 13, 34), false, profile1, null);
        auction2 = new Auction(0, "MySecondTitle", "MySecondDescription", 10, LocalDateTime.of(2017, 10, 11, 13, 38), profile1, new ArrayList<>(), new ArrayList<>(), 0, 0);
    }

    @Test
    void getId() {
        assertEquals(0, auction2.getId(), "The auction ID should've been 0");
    }

    @Test
    void getStartBid() {
        assertEquals(0, auction1.getStartBid(), "Start bid should've been 0");
        assertEquals(10, auction2.getStartBid(), "Start bid should've been 10");
    }

    @Test
    void getMinimum() {
        assertEquals(5, auction1.getMinimum(), "Minimum should've been 5");
    }

    @Test
    void isPremium() {
        assertEquals(false, auction1.isPremium(), "Should've been false");
    }

    @Test
    void getExpirationDate() {
        assertEquals(LocalDateTime.of(2017, 10, 11, 13, 34), auction1.getExpirationDate(), "This should've been 11-10-2017 13:34");
        assertEquals(LocalDateTime.of(2017, 10, 11, 13, 38), auction2.getExpirationDate(), "This should've been 11-10-2017 13:38");
    }

    @Test
    void getOpeningDate() {
        assertEquals(LocalDateTime.of(2016, 9, 10, 12, 33), auction1.getOpeningDate(), "This should've been 10-9-2016 12:33");
    }

    @Test
    void getTitle() {
        assertEquals("MyTitle", auction1.getTitle(), "This should've been MyTitle");
        assertEquals("MySecondTitle", auction2.getTitle(), "This should've been MySecondTitle");
    }

    @Test
    void getDescription() {
        assertEquals("MyDescription", auction1.getDescription(), "This should've been MyDescription");
        assertEquals("MySecondDescription", auction2.getDescription(), "This should've been MySecondDescription");
    }

    @Test
    void getImages() {
        assertNull(auction1.getImages());
        assertEquals(new ArrayList<Image>(), auction2.getImages());
    }

    @Test
    void getBids() {
        assertEquals(new ArrayList<Bid>(), auction2.getBids());
    }

    @Test
    void getStatus() {
        assertEquals(Status.OPEN, auction1.getStatus());
        assertNull(auction2.getStatus());
    }

    @Test
    void getCreator() {
        assertEquals(profile1, auction1.getCreator());
        assertEquals(profile1, auction2.getCreator());
    }

    @Test
    void setStatus() {
        auction1.setStatus(Status.CLOSED);
        assertEquals(Status.CLOSED, auction1.getStatus());
        auction1.setStatus(Status.OPEN);
        assertEquals(Status.OPEN, auction1.getStatus());
    }

//    Not completely implemented yet.
//    @Test
//    void addBid() {
//        try{
//            auction1.addBid(10.00, profile2);
//        }
//        catch(Exception exception){
//            fail("Expected no exception, but got: " + exception.getStackTrace());
//        }
//        assertThrows(IllegalArgumentException.class, () -> auction1.addBid(10, profile1), "Can not bid on your own auction.");
//    }
}