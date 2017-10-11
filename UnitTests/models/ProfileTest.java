package models;
import core.ApplicationManager;
import data.interfaces.IAuctionContext;
import data.interfaces.IProfileContext;
import javafx.scene.image.Image;
import logic.repositories.AuctionRepository;
import logic.repositories.ProfileRepository;
import org.junit.Assert;
import org.junit.Test;
import utilities.enums.AuctionLoadingType;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

/**
 * ASSUMPTION: There is a user in the database with username="testusername", password="AbC*1f", name="Test User" and email="testuser@gmail.com".
 * ASSUMPTION: There is a user in the database with username="testusername2" and password="AbC*2f"
 * ASSUMPTION: There is a user in the database with username="testusername3" and password="AbC*3f"
 * ASSUMPTION: There is an auction in the database with id="1"
 * If these are not true, all tests will fail.. */

class ProfileTest {
    IProfileContext pc = null;
    IAuctionContext ac = null;
    ProfileRepository pr = new ProfileRepository(pc);
    AuctionRepository ar = new AuctionRepository(ac);
    static private ApplicationManager applicationManager;

    @Test
    void testAddAuction() {
    }

    @Test
    void testAddVisitedAuction() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = ar.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);

        pr.addVisitedAuction(profile, auction);

        Assert.assertTrue(profile.getVisitedAuctions().contains(auction));
    }

    @Test
    void testAddFavoriteAuction() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = ar.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);

        pr.addFavoriteAuction(profile, auction);

        Assert.assertTrue(profile.getFavoriteAuctions().contains(auction));
    }

    @Test
    void testRemoveFavoriteAuction() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = ar.getAuctionForId(1, AuctionLoadingType.FOR_AUCTION_PAGE);

        Assert.assertTrue(profile.getFavoriteAuctions().contains(auction));

        pr.removeFavoriteAuction(profile, auction);

        Assert.assertFalse(profile.getFavoriteAuctions().contains(auction));
    }

    @Test
    void testAddFeedback() throws SQLException, IOException, ClassNotFoundException {
        Date date = new Date(2017, 9, 29);
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Profile author = applicationManager.login("testusername2", "AbC*2f").getProfile();
        Feedback feedback = new Feedback(author, date, true, "Product goed ontvangen, snelle service.");

        profile.addFeedback(feedback);

        Assert.assertTrue(profile.getFeedbacks().contains(feedback));
    }

    @Test
    void testNotifyUser() {
    }

    @Test
    void testGetPhoto() throws SQLException {
        Image img = new Image("src/TestImages/2.jpg");
        User user = applicationManager.login("testusername", "AbC*1f");
        Profile profile = user.getProfile();

        user.setPhoto(img);

        Assert.assertEquals(profile.getPhoto(), img);
    }

    @Test
    void testGetUsername() throws SQLException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();

        Assert.assertEquals(profile.getUsername(), "testusername");
    }

    @Test
    void testGetName() throws SQLException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();

        Assert.assertEquals(profile.getName(), "Test User");
    }

    @Test
    void testGetEmail() throws SQLException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();

        Assert.assertEquals(profile.getEmail(), "testuser@gmail.com");
    }

    @Test
    void testGetProfileId() {
    }

    @Test
    void testGetAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = ar.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);

        profile.addAuction(auction);

        Assert.assertTrue(profile.getAuctions().contains(auction));
    }

    @Test
    void testGetVisitedAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = ar.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);

        profile.addVisitedAuction(auction);

        Assert.assertTrue(profile.getVisitedAuctions().contains(auction));
    }

    @Test
    void testGetFavoriteAuctions() throws SQLException, IOException, ClassNotFoundException {
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Auction auction = ar.getAuctionForId(2, AuctionLoadingType.FOR_AUCTION_PAGE);

        profile.addFavoriteAuction(auction);

        Assert.assertTrue(profile.getFavoriteAuctions().contains(auction));
    }

    @Test
    void testGetFeedbacks() throws SQLException, IOException, ClassNotFoundException {
        Date date = new Date(2017, 10, 2);
        Profile profile = applicationManager.login("testusername", "AbC*1f").getProfile();
        Profile author = applicationManager.login("testusername3", "AbC*3f").getProfile();
        Feedback feedback = new Feedback(author, date, false, "Slechte service, duurde 3 weken voor ontvangst!");

        profile.addFeedback(feedback);

        Assert.assertTrue(profile.getFeedbacks().contains(feedback));
    }
}