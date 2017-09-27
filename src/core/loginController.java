package core;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class loginController {

    public loginController() {
    }

    @FXML public Pane paneMenu;
    @FXML public Pane paneContent;
    @FXML public Label lblClose;

    @FXML public ImageView imgviewProfile;
    @FXML public ImageView imgviewAuctions;
    @FXML public ImageView imgviewFavorites;
    @FXML public ImageView imgviewAddAuction;

    private Image profileIcon;
    private Image auctionsIcon;
    private Image favoritesIcon;
    private Image addAuctionIcon;

    private Image profileIconHovered;
    private Image auctionsIconHovered;
    private Image favoritesIconHovered;
    private Image addAuctionIconHovered;

    private ImageView selectedMenu;

    @FXML
    void initialize() {
        setIcons();

        //After login start at auctions menu.
        selectedMenu = imgviewAuctions;
        imgviewAuctions.setImage(auctionsIconHovered);
    }

    public void closeApplication(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void selectMenuItem(MouseEvent mouseEvent) {
        imgviewProfile.setImage(profileIcon);
        imgviewAuctions.setImage(auctionsIcon);
        imgviewFavorites.setImage(favoritesIcon);
        imgviewAddAuction.setImage(addAuctionIcon);
        highlightIconColor(mouseEvent);
        ImageView icon = (ImageView) mouseEvent.getSource();
        selectedMenu = icon;
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

    private void setIcons() {
        profileIcon = new Image("utilities/images/menu/profile.png");
        auctionsIcon = new Image("utilities/images/menu/auction.png");
        favoritesIcon = new Image("utilities/images/menu/favorites.png");
        addAuctionIcon = new Image("utilities/images/menu/addauction.png");

        profileIconHovered = new Image("utilities/images/menu/profile_hovered.png");
        auctionsIconHovered = new Image("utilities/images/menu/auction_hovered.png");
        favoritesIconHovered = new Image("utilities/images/menu/favorites_hovered.png");
        addAuctionIconHovered = new Image("utilities/images/menu/addauction_hovered.png");

        imgviewProfile.setImage(profileIcon);
        imgviewAuctions.setImage(auctionsIcon);
        imgviewFavorites.setImage(favoritesIcon);
        imgviewAddAuction.setImage(addAuctionIcon);
    }
}
