package models;

import core.ApplicationManager;
import javafx.scene.image.Image;
import logic.repositories.AuctionRepository;
import logic.repositories.ProfileRepository;
import org.junit.jupiter.api.Test;
import utilities.enums.AuctionLoadingType;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * ASSUMPTION: There is a user in the database with username="testusername", password="AbC*1f", name="Test User" and email="testuser@gmail.com".
 * ASSUMPTION: There is a user in the database with username="testusername2" and password="AbC*2f"
 * ASSUMPTION: There is a user in the database with username="testusername3" and password="AbC*3f"
 * ASSUMPTION: There is an auction in the database with id="1"
 * If these are not true, all tests will fail.*/
class ProfileTest {
    final ProfileRepository profileRepository = new ProfileRepository(null);
    final AuctionRepository auctionRepository = new AuctionRepository(null);
    static private ApplicationManager applicationManager;

    @Test
    void testAddAuction() { }

    @Test
    void testAddVisitedAuction() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = auctionRepository.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);

        profileRepository.addVisitedAuction(profile, auction);

        assertTrue(profile.getVisitedAuctions().contains(auction));
    }

    @Test
    void testAddFavoriteAuction() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = auctionRepository.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);

        profileRepository.addFavoriteAuction(profile, auction);

        assertTrue(profile.getFavoriteAuctions().contains(auction));
    }

    @Test
    void testRemoveFavoriteAuction() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = auctionRepository.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);

        assertTrue(profile.getFavoriteAuctions().contains(auction));

        profileRepository.removeFavoriteAuction(profile, auction);

        assertFalse(profile.getFavoriteAuctions().contains(auction));
    }

    @Test
    void testAddFeedback() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Profile author = applicationManager.login("testusername2", "AbC*2f").getProfile();
        Feedback feedback = new Feedback(author, LocalDateTime.now(), true, "Product goed ontvangen, snelle service.");

        profile.addFeedback(feedback);

        assertTrue(profile.getFeedbacks().contains(feedback));
    }

    @Test
    void testNotifyUser() {
    }

    @Test
    void testGetPhoto() throws SQLException, IOException, ClassNotFoundException {
        File img = new File("src/TestImages/2.jpg");
        User user = applicationManager.login("testusername", "AbC*1f");
        Profile profile = user.getProfile();

        user.setPhoto(img);

        assertEquals(profile.getPhoto(), img);
    }

    @Test
    void testGetUsername() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();

        assertEquals(profile.getUsername(), "testusername");
    }

    @Test
    void testGetName() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();

        assertEquals(profile.getName(), "Test User");
    }

    @Test
    void testGetEmail() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();

        assertEquals(profile.getEmail(), "testuser@gmail.com");
    }

    @Test
    void testGetProfileId() {
    }

    @Test
    void testGetAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = auctionRepository.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);

        profile.addAuction(auction);

        assertTrue(profile.getAuctions().contains(auction));
    }

    @Test
    void testGetVisitedAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = auctionRepository.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);

        profile.addVisitedAuction(auction);

        assertTrue(profile.getVisitedAuctions().contains(auction));
    }

    @Test
    void testGetFavoriteAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = auctionRepository.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);

        profile.addFavoriteAuction(auction);

        assertTrue(profile.getFavoriteAuctions().contains(auction));
    }

    @Test
    void testGetFeedbacks() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Profile author = applicationManager.login("testusername3", "AbC*3f").getProfile();
        Feedback feedback = new Feedback(author, LocalDateTime.now(), false, "Slechte service, duurde 3 weken voor ontvangst!");

        profile.addFeedback(feedback);

        assertTrue(profile.getFeedbacks().contains(feedback));
    }
}