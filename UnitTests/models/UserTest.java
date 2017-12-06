package models;

import logic.managers.ApplicationManager;
import modelslibrary.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ASSUMPTION: There is a user in the database with username="user1", password="User1!", name="Mohamed Ali" and email="User1@gmail.com".
 * If this is not true, all tests will fail..
 */
class UserTest {
    private static User user;

    @BeforeAll
    static void setUp() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, IOException {
        ApplicationManager applicationManager = new ApplicationManager();
        applicationManager.login("user1", "User1!");
        user = applicationManager.getCurrentUser();
    }

    @Test
    void testSetName() {
        user.setName("Barnaby Marmaduke Aloysius Benjy Cobweb Dartagnan Egbert Felix G");
        assertEquals("Barnaby Marmaduke Aloysius Benjy Cobweb Dartagnan Egbert Felix G", user.getName(), "Username should be able to contain 64 chars");
        assertThrows(IllegalArgumentException.class, () -> user.setName("Barnaby Marmaduke Aloysius Benjy Cobweb Dartagnan Egbert Felix Gaspar"), "Full name should not be able to exceed 64 chars. Currently: 69");
        assertThrows(IllegalArgumentException.class, () -> user.setName("T3st User"), "Full name should not be able to contain numbers");
        assertThrows(IllegalArgumentException.class, () -> user.setName("T&st User"), "Full name should not be able to contain special characters");
        assertThrows(IllegalArgumentException.class, () -> user.setName("T*st User"), "Full name should not be able to contain special characters");
    }

    @Test
    void testSetEmail() {
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("JamesDrNoFromRussiawithLoveGoldfingerThunderballYouOnlyLiveTwiceOnHerMajestysSecretServiceDiamondsAreForeverLiveandLetDieTheManwiththeGoldenGunTheSpyWhoLovedMeMoonrakerForYourEyesOnlyOctopussyAViewtoaKillTheLivingDaylightsLicencetoKillGoldenEye123@gmail.com"),
                "Email should not be able to exceed 255 chars. Currently: 256");
        user.setEmail("JamesNoFromRussiawithLoveGoldfingerThunderballYouOnlyLiveTwiceOnHerMajestysSecretServiceDiamondsAreForeverLiveandLetDieTheManwiththeGoldenGunTheSpyWhoLovedMeMoonrakerForYourEyesOnlyOctopussyAViewtoaKillTheLivingDaylightsLicencetoKillGoldenEye123@gmail.com");
        assertEquals("JamesNoFromRussiawithLoveGoldfingerThunderballYouOnlyLiveTwiceOnHerMajestysSecretServiceDiamondsAreForeverLiveandLetDieTheManwiththeGoldenGunTheSpyWhoLovedMeMoonrakerForYourEyesOnlyOctopussyAViewtoaKillTheLivingDaylightsLicencetoKillGoldenEye123@gmail.com", user.getEmail(),
                "Email should be able to contain 255 chars.");

        assertThrows(IllegalArgumentException.class, () -> user.setEmail("testuserwithoutatemail.com"), "Email should always contain an '@'.");
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("testuserwith@gmailcom"), "Email should always end with a valid domain name.");
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("testuserwith@bladieduiaaf.aiusdhfuias"), "Email should always end with a valid domain name.");
    }

    @Test
    void testGetProfile() {
        assertEquals("User1",user.getProfile().getUsername(), "getProfile does not work properly");
    }


    @Test
    void testGetId() {
        assertEquals(1, user.getProfile().getProfileId(), "getId does not work properly");
    }

    @Test
    void testGetEmail() {
        assertEquals("User1@gmail.com", user.getEmail(), "Email getter is not working properly");
    }


    @Test
    void testGetUsername() {
        assertEquals("User1", user.getUsername(), "Username getter is not working properly.");
        assertEquals(user.getUsername(), user.getProfile().getUsername(), "Username in profile does not match the one in user.");
    }

    @Test
    void testGetName() {
        assertEquals("Mohamed Ali", user.getName(), "Full name getter is not working properly");
    }


    @AfterAll
    static void cleanUp() throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        user.setName("Mohamed Ali");
        user.setEmail("User1@gmail.com");
    }
}