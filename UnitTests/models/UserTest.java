package models;

import core.ApplicationManager;
import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest{
    private static User user;

    @BeforeAll
    static void setUp() {
        ApplicationManager am = new ApplicationManager();

        //ASSUMPTION: There is a user in the database with username="testusername", password="AbC*1f", name="Test User" and email="testuser@gmail.com".
        //If this is not true, all tests will fail.
        user = am.login("testusername", "AbC*1f");
    }

    @Test
    void testChangePassword() {
        assertThrows(IllegalArgumentException.class, () -> user.changePassword("12345"), "Passwords should not be able to be less then 6 chars. (Currently: 5) ");
        assertThrows(IllegalArgumentException.class, () -> user.changePassword("12345678"), "Passwords should not be able to contain only numbers");
        assertThrows(IllegalArgumentException.class, () -> user.changePassword(""), "Passwords should never be able to be empty.");
        assertThrows(IllegalArgumentException.class, () -> user.changePassword("A&cdefg"), "Passwords should contain at least 1 number");
        assertThrows(IllegalArgumentException.class, () -> user.changePassword("Abcdef3"), "Passwords should contain at least 1 special char");
        assertThrows(IllegalArgumentException.class, () -> user.changePassword("ab*cdef3"), "Passwords should contain at least 1 uppercase letter.");
        assertThrows(IllegalArgumentException.class, () -> user.changePassword("AB*CDEF3"), "Passwords should contain at least 1 lowercase letter.");
        assertThrows(IllegalArgumentException.class, () -> user.changePassword("ab*cdEf3Wolfeschlegelsteinhausenb"), "Passwords should not exceed 32 chars");

        assertTrue(user.changePassword("AbC*1f"), "Tried to change to a correct password (AbC*1f), but got false in return.");
    }

    @Test
    void testGetUsername() {
        assertEquals("testusername", user.getUsername(), "Username getter is not working properly.");
        assertEquals(user.getUsername(), user.getProfile().getUsername(), "Username in profile does not match the one in user.");
    }

    @Test
    void testGetName() {
        assertEquals("Test User", user.getName(), "Full name getter is not working properly");
        assertEquals(user.getName(), user.getProfile().getName(), "Full name in profile does not match the one in user.");
    }

    @Test
    void testSetName() {
        user.setName("Barnaby Marmaduke Aloysius Benjy Cobweb Dartagnan Egbert Felix G");
        assertEquals("Barnaby Marmaduke Aloysius Benjy Cobweb Dartagnan Egbert Felix G", user.getUsername(), "Username should be able to contain 64 chars");
        //Todo: DAL implementation to check if username actually changed.
        assertThrows(IllegalArgumentException.class, () -> user.setName("Barnaby Marmaduke Aloysius Benjy Cobweb Dartagnan Egbert Felix Gaspar"), "Full name should not be able to exceed 64 chars. Currently: 69");
        assertThrows(IllegalArgumentException.class, () -> user.setName("T3st User"), "Full name should not be able to contain numbers");
        assertThrows(IllegalArgumentException.class, () -> user.setName("T&st User"), "Full name should not be able to contain special characters");
        assertThrows(IllegalArgumentException.class, () -> user.setName("T*st User"), "Full name should not be able to contain special characters");
    }

    @Test
    void testGetEmail() {
        assertEquals("testuser@gmail.com", user.getEmail(), "Email getter is not working properly");
    }

    @Test
    void testSetEmail() {
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("JamesDrNoFromRussiawithLoveGoldfingerThunderballYouOnlyLiveTwiceOnHerMajestysSecretServiceDiamondsAreForeverLiveandLetDieTheManwiththeGoldenGunTheSpyWhoLovedMeMoonrakerForYourEyesOnlyOctopussyAViewtoaKillTheLivingDaylightsLicencetoKillGoldenEye123@gmail.com"),
                "Email should not be able to exceed 255 chars. Currently: 256");
        user.setEmail("JamesDrNoFromRussiawithLoveGoldfingerThunderballYouOnlyLiveTwiceOnHerMajestysSecretServiceDiamondsAreForeverLiveandLetDieTheManwiththeGoldenGunTheSpyWhoLovedMeMoonrakerForYourEyesOnlyOctopussyAViewtoaKillTheLivingDaylightsLicencetoKillGoldenEye123@gmail.com");
        assertEquals("JamesDrNoFromRussiawithLoveGoldfingerThunderballYouOnlyLiveTwiceOnHerMajestysSecretServiceDiamondsAreForeverLiveandLetDieTheManwiththeGoldenGunTheSpyWhoLovedMeMoonrakerForYourEyesOnlyOctopussyAViewtoaKillTheLivingDaylightsLicencetoKillGoldenEye12@gmail.com", user.getEmail(),
                "Username should be able to contain 255 chars.");

        assertThrows(IllegalArgumentException.class, () -> user.setEmail("testuserwithoutatemail.com"), "Email should always contain an '@'.");
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("testuserwith@gmailcom"), "Email should always end with a valid domain name.");
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("testuserwith@bladieduiaaf.aiusdhfuias"), "Email should always end with a valid domain name.");
    }

    @Deprecated
    void testSetPhoto() {
        //Todo: Fix it. Pls. Help :(        (Iets met een mockingframework)
        Image image = new Image("/testavatarCorrect.jpg");
        user.setPhoto(image);
        assertSame(image, user.getProfile().getPhoto(), "Photo is not properly set.");

        Image badImage = new Image("/testavatarIncorrect.jpg");
        assertThrows(IllegalArgumentException.class, () -> user.setPhoto(badImage), "Photo should not be larger then 300x300. (Currently");

        Image badImage2 = new Image("/testavatarIncorrect2.jpg");
        assertThrows(IllegalArgumentException.class, () -> user.setPhoto(badImage2), "Photo should not be smaller then 50x50. (Currently: 49x49)");
    }

    @AfterAll
    static void cleanUp() {
        user.setName("Test User");
        user.setEmail("testuser@gmail.com");
    }
}