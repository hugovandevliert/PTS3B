package models;

import logic.managers.ApplicationManager;
import data.contexts.AuctionMySqlContext;
import data.contexts.ProfileMySqlContext;
import logic.repositories.AuctionRepository;
import logic.repositories.ProfileRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ASSUMPTION: There is a user in the database with username="user1", password="User1!", name="Mohamed Ali" and email="User1@gmail.com".
 * ASSUMPTION: There is a user in the database with username="user2" and password="User2!"
 * ASSUMPTION: There is a user in the database with username="user3" and password="User3!"
 * ASSUMPTION: There is an auction in the database with id="1"
 * If these are not true, all tests will fail.*/
public class ProfileTest {
    final ProfileRepository profileRepository = new ProfileRepository(new ProfileMySqlContext());
    final AuctionRepository auctionRepository = new AuctionRepository(new AuctionMySqlContext());
    static private ApplicationManager applicationManager;

    @Before
    public void setUp()
    {
        applicationManager = new ApplicationManager();
    }

    @Test
    public void testAddAuction() { }

//    @Test
//    public void testAddVisitedAuction() throws SQLException, IOException, ClassNotFoundException {
//        Profile profile = applicationManager.login("user1", "User1!").getProfile();
//        Auction auction = auctionRepository.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);
//
//        profileRepository.addVisitedAuction(profile, auction);
//
//        assertTrue(profile.getVisitedAuctions().contains(auction));
//    }

//    @Test
//    public void testAddFavoriteAuction() throws SQLException, IOException, ClassNotFoundException {
//        Profile profile = applicationManager.login("user1", "User1!").getProfile();
//        Auction auction = auctionRepository.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);
//
//        profileRepository.addFavoriteAuction(profile, auction.getId());
//
//        assertTrue(profile.getFavoriteAuctions().contains(auction));
//    }
//
//    @Test
//    public void testRemoveFavoriteAuction() throws SQLException, IOException, ClassNotFoundException {
//        Profile profile = applicationManager.login("user1", "User1!").getProfile();
//        Auction auction = auctionRepository.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);
//
//        assertTrue(profile.getFavoriteAuctions().contains(auction));
//
//        profileRepository.removeFavoriteAuction(profile, auction);
//
//        assertFalse(profile.getFavoriteAuctions().contains(auction));
//    }

//    @Test
//    public void testAddFeedback() throws SQLException, IOException, ClassNotFoundException {
//        Profile profile = applicationManager.login("user1", "User1!").getProfile();
//        Profile author = applicationManager.login("user2", "User2!").getProfile();
//        Feedback feedback = new Feedback(author, LocalDateTime.now(), true, "Product goed ontvangen, snelle service.");
//
//        profile.addFeedback(feedback);
//
//        assertTrue(profile.getFeedbacks().contains(feedback));
//    }

    @Test
    public void testGetUsername() throws Exception {
        applicationManager.login("user1", "User1!");
        Profile profile = applicationManager.getCurrentUser().getProfile();

        assertEquals("User1", profile.getUsername());
    }

    @Test
    public void testGetName() throws Exception {
        Profile profile = new Profile(null, null,  "Mohamed Ali", null, 1);
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

    /*@Test
    public void testGetAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = new Profile(1, "user1", "Test User", LocalDateTime.now(), null, "test@gmail.com", new ArrayList<Auction>(), null);
        profile.addAuction(1, 1, 1, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(1), false, "AbCdE", "", null);

        assertEquals("AbCdE", profile.getAuctions().get(0).getTitle());
    }*/

//    @Test
//    public void testGetVisitedAuctions() throws SQLException, IOException, ClassNotFoundException {
//        Profile profile = applicationManager.login("user1", "User1!").getProfile();
//        Auction auction = auctionRepository.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);
//
//        profile.addVisitedAuction(auction);
//
//        assertTrue(profile.getVisitedAuctions().contains(auction));
//    }

//    @Test
//    public void testGetFavoriteAuctions() throws SQLException, IOException, ClassNotFoundException {
//        Profile profile = applicationManager.login("user1", "User1!").getProfile();
//        Auction auction = auctionRepository.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);
//
//        profile.addFavoriteAuction(auction.getId());
//
//        assertTrue(profile.getFavoriteAuctions().contains(auction));
//    }

//    @Test
//    public void testGetFeedbacks() throws SQLException, IOException, ClassNotFoundException {
//        Profile profile = applicationManager.login("user1", "User1!").getProfile();
//        Profile author = applicationManager.login("user3", "User3!").getProfile();
//        Feedback feedback = new Feedback(author, LocalDateTime.now(), false, "Slechte service, duurde 3 weken voor ontvangst!");
//
//        profile.addFeedback(feedback);
//
//        assertTrue(profile.getFeedbacks().contains(feedback));
//    }
}