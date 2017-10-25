package models;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class BidTest {
    Profile profile;
    LocalDateTime time;
    Bid bid;

    @Before
    public void setUp() {
        profile = new Profile(null, "User1", "Mohamed Ali", "User1@gmail.com", 1);
        time = LocalDateTime.now();
        bid = new Bid(profile, 300, time);
    }

    @Test
    public void testGetDate() {
        assertSame(time, bid.getDate());
    }

    @Test
    public void testGetProfile() {
        assertSame(profile, bid.getProfile());
    }

    @Test
    public void testGetAmount() {
        assertEquals(300, bid.getAmount());
    }

    @Test
    public void testCompareTo() {
        Bid bid2 = new Bid(profile, 400, time);
        assertEquals(-1, bid2.compareTo(bid));

        Bid bid3 = new Bid(profile, 200, time);
        assertEquals(1, bid3.compareTo(bid));

        Bid bid4 = new Bid(profile, 300, time);
        assertEquals(0, bid4.compareTo(bid));
    }

}