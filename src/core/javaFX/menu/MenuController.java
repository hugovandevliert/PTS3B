package core.javaFX.menu;

import logic.managers.ApplicationManager;
import core.javaFX.favorites.FavoritesController;
import core.javaFX.profile.ProfileController;
import core.javaFX.useralert.UserAlertController;
import data.contexts.ProfileMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import logic.repositories.ProfileRepository;
import modelslibrary.Profile;
import utilities.database.Database;
import utilities.enums.AlertType;
import utilities.enums.ProfileLoadingType;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("Duplicates")
public class MenuController implements Initializable {

    @FXML public Pane paneMenu;
    @FXML public Pane paneContent;
    @FXML public Label lblClose;

    @FXML public ImageView imgviewProfile;
    @FXML public ImageView imgviewAuctions;
    @FXML public ImageView imgviewFavorites;
    @FXML public ImageView imgviewAddAuction;

    @FXML private Pane paneAlert;

    protected final static ApplicationManager applicationManager = new ApplicationManager();
    private static UserAlertController userAlertController;
    private ImageView selectedMenu;

    private Image profileIcon;
    private Image auctionsIcon;
    private Image favoritesIcon;
    private Image addAuctionIcon;

    private Image profileIconHovered;
    private Image auctionsIconHovered;
    private Image favoritesIconHovered;
    private Image addAuctionIconHovered;

    private static String lastCalledClass;
    private FXMLLoader fxmlLoader;
    private ProfileRepository profileRepository;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        setIcons();
        selectedMenu = imgviewAuctions;
        imgviewAuctions.setImage(auctionsIconHovered);

        try {
            paneContent.getChildren().clear();
            final Pane newContentPane = FXMLLoader.load(getClass().getResource("/core/javafx/login/login.fxml"));
            paneContent.getChildren().add(newContentPane);

            /* Setting up the UserAlert fxml + controller */
            fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/useralert/useralert.fxml"));
            final Pane newAlertPane = fxmlLoader.load();
            userAlertController = fxmlLoader.getController();
            paneAlert.getChildren().add(newAlertPane);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public MenuController() {
        profileRepository = new ProfileRepository(new ProfileMySqlContext());
    }

    private void setIcons() {
        profileIcon = new Image( "/utilities/images/menu/profile.png");
        auctionsIcon = new Image("/utilities/images/menu/auction.png");
        favoritesIcon = new Image("/utilities/images/menu/favorites.png");
        addAuctionIcon = new Image("/utilities/images/menu/addauction.png");

        profileIconHovered = new Image("/utilities/images/menu/profile_hovered.png");
        auctionsIconHovered = new Image("/utilities/images/menu/auction_hovered.png");
        favoritesIconHovered = new Image("/utilities/images/menu/favorites_hovered.png");
        addAuctionIconHovered = new Image("/utilities/images/menu/addauction_hovered.png");

        imgviewProfile.setImage(profileIcon);
        imgviewAuctions.setImage(auctionsIcon);
        imgviewFavorites.setImage(favoritesIcon);
        imgviewAddAuction.setImage(addAuctionIcon);
    }

    public void closeApplication() {
        Database.closeConnection();
        applicationManager.getRmiClientsManager().unsubscribeRemoteListeners();
        System.exit(0);
    }

    public void selectMenuItem(final MouseEvent mouseEvent) throws IOException {
        if (!applicationManager.isLoggedIn()) return;

        final ImageView source = (ImageView) mouseEvent.getSource();
        imgviewProfile.setImage(profileIcon);
        imgviewAuctions.setImage(auctionsIcon);
        imgviewFavorites.setImage(favoritesIcon);
        imgviewAddAuction.setImage(addAuctionIcon);
        highlightIconColor(mouseEvent);
        selectedMenu = source;

        paneContent.getChildren().clear();
        final Pane newLoadedPane;

        lastCalledClass = null; // This is for timer classes; to determine whether or not they should stop themselves because the auctions are not being looked at anymore

        if (source == imgviewProfile){
            fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/profile/profile.fxml"));
            newLoadedPane = fxmlLoader.load();
            ProfileController profileController = fxmlLoader.getController();

            try {
                final Profile profile = profileRepository.getProfileForId(applicationManager.getCurrentUser().getId(), ProfileLoadingType.FOR_PROFILE_PAGE);

                profileController.setMenuController(this);
                profileController.loadProfile(profile);
            } catch (SQLException | ClassNotFoundException exception) {
                MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
            }
        }
        else if(source == imgviewAuctions){
            newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/auctions/auctions.fxml"));
        }
        else if(source == imgviewAddAuction){
            newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/addAuction/addauction.fxml"));
        }
        else if(source == imgviewFavorites){
            fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/favorites/favorites.fxml"));
            newLoadedPane = fxmlLoader.load();
            FavoritesController favoritesController = fxmlLoader.getController();

            final int profileId = applicationManager.getCurrentUser().getId();

            favoritesController.loadFavorites(profileId);
        }
        else{
            newLoadedPane = new Pane();
        }

        paneContent.getChildren().add(newLoadedPane);

        /* Let's make sure that we're stopping all BidClients because when clicking at any MenuItem, it will result in the User not being able to
           view an Auction page anymore, meaning having a BidClient running would be redundant and a waste of resources. */
        applicationManager.getRmiClientsManager().unsubscribeRemoteListeners();
    }

    public void highlightIconColor(final MouseEvent mouseEvent) {
        final ImageView icon = (ImageView) mouseEvent.getSource();

        switch(icon.getId()) {
            case "imgviewProfile":
                imgviewProfile.setImage(profileIconHovered);
                break;
            case "imgviewAuctions":
                imgviewAuctions.setImage(auctionsIconHovered);
                break;
            case "imgviewFavorites":
                imgviewFavorites.setImage(favoritesIconHovered);
                break;
            case "imgviewAddAuction":
                imgviewAddAuction.setImage(addAuctionIconHovered);
                break;
        }
    }

    public void revertIconColor(final MouseEvent mouseEvent) {
        final ImageView icon = (ImageView) mouseEvent.getSource();

        if (Objects.equals(selectedMenu.getId(), icon.getId())) return;

        switch(icon.getId()) {
            case "imgviewProfile":
                imgviewProfile.setImage(profileIcon);
                break;
            case "imgviewAuctions":
                imgviewAuctions.setImage(auctionsIcon);
                break;
            case "imgviewFavorites":
                imgviewFavorites.setImage(favoritesIcon);
                break;
            case "imgviewAddAuction":
                imgviewAddAuction.setImage(addAuctionIcon);
                break;
        }
    }

    public void setLastCalledClass(final Class classname) {
        lastCalledClass = classname.getSimpleName();
    }

    public static String getLastCalledClass() {
        return lastCalledClass;
    }

    public static void showAlertMessage(final String message, final AlertType alertType, final int delay) {
        userAlertController.setMessage(message, alertType, delay);
    }
}

