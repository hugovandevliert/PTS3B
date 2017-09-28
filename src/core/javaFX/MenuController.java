package core.javaFX;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Objects;

public abstract class MenuController {

    @FXML public Pane paneMenu;
    @FXML public Pane paneContent;
    @FXML public Label lblClose;

    @FXML public ImageView imgviewProfile;
    @FXML public ImageView imgviewAuctions;
    @FXML public ImageView imgviewFavorites;
    @FXML public ImageView imgviewAddAuction;

    protected ImageView selectedMenu;

    protected Image profileIcon;
    protected Image auctionsIcon;
    protected Image favoritesIcon;
    protected Image addAuctionIcon;

    protected Image profileIconHovered;
    protected Image auctionsIconHovered;
    protected Image favoritesIconHovered;
    protected Image addAuctionIconHovered;

    protected void setIcons() {
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
}
