package core.javaFX.menu;

import core.ApplicationManager;
import core.UserAlert;
import core.javaFX.auctions.ListedAuctionController;
import core.javaFX.profile.ProfileController;
import data.contexts.ProfileMySqlContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import logic.repositories.ProfileRepository;
import models.Profile;
import utilities.database.Database;
import utilities.enums.AlertType;
import utilities.enums.ProfileLoadingType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML public Pane paneMenu;
    @FXML public Pane paneContent;
    @FXML public Pane paneBackground;
    @FXML public Label lblClose;

    @FXML public ImageView imgviewProfile;
    @FXML public ImageView imgviewAuctions;
    @FXML public ImageView imgviewFavorites;
    @FXML public ImageView imgviewAddAuction;
    @FXML protected Pane paneAlert;
    @FXML public Label lblAlertMessage;

    protected static ApplicationManager applicationManager = new ApplicationManager();
    protected ImageView selectedMenu;

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
    private ProfileController profileController;

    private ProfileRepository profileRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        selectedMenu = imgviewAuctions;
        imgviewAuctions.setImage(auctionsIconHovered);

        try {
            paneContent.getChildren().clear();
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/login/login.fxml"));
            paneContent.getChildren().add(newLoadedPane);

            ShowMessage("Please login", AlertType.Message);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
        System.exit(0);
    }

    public void selectMenuItem(MouseEvent mouseEvent) throws IOException {
        if (!applicationManager.isLoggedIn()) return;

        ImageView source = (ImageView) mouseEvent.getSource();
        imgviewProfile.setImage(profileIcon);
        imgviewAuctions.setImage(auctionsIcon);
        imgviewFavorites.setImage(favoritesIcon);
        imgviewAddAuction.setImage(addAuctionIcon);
        highlightIconColor(mouseEvent);
        selectedMenu = source;

        paneContent.getChildren().clear();
        Pane newLoadedPane;

        lastCalledClass = null; // This is for timer classes; to determine whether or not they should stop themselves because the auctions are not being looked at anymore

        if (source == imgviewProfile){
            fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/profile/profile.fxml"));
            newLoadedPane = fxmlLoader.load();
            profileController = fxmlLoader.getController();

            try {
                final Profile profile = profileRepository.getProfileForId(applicationManager.getCurrentUser().getId(), ProfileLoadingType.FOR_PROFILE_PAGE);

                profileController.setProfileVariable(profile);
                profileController.setProfilePicture(profile.getPhoto());
                profileController.setName(profile.getUsername());
                profileController.setUserSince(profile.getCreationDate());
                profileController.setFeedbackCounts(profile.getFeedbacks());
                profileController.setAuctions(profile.getAuctions());
                profileController.setFeedbacks(profile.getFeedbacks());

            } catch (SQLException e) { e.printStackTrace(); } //TODO: proper error handling
              catch (ClassNotFoundException e) { e.printStackTrace(); }
        }
        else if(source == imgviewAuctions){
            newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/auctions/auctions.fxml"));
        }
        else if(source == imgviewAddAuction){
            newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/addAuction/addauction.fxml"));
        }
        else if(source == imgviewFavorites){
            newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/favorites/favorites.fxml"));
        }
        else{
            newLoadedPane = new Pane();
        }

        paneContent.getChildren().add(newLoadedPane);
    }

    public void highlightIconColor(MouseEvent mouseEvent) {
        ImageView icon = (ImageView) mouseEvent.getSource();
        if (Objects.equals(selectedMenu.getId(), icon.getId())) return;
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

    public void revertIconColor(MouseEvent mouseEvent) {
        ImageView icon = (ImageView) mouseEvent.getSource();
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

    protected void ShowMessage(String message, AlertType type) throws InterruptedException {
        UserAlert userAlert = new UserAlert();
        userAlert.showMessage(message, type, paneAlert, lblAlertMessage, this);
    }

    public void ClearAlert(){
        Platform.runLater(() -> paneAlert.getChildren().clear());
    }

    public void setLastCalledClass(final Class classname) {
        this.lastCalledClass = classname.getSimpleName();
    }

    public static String getLastCalledClass() {
        return lastCalledClass;
    }
}

