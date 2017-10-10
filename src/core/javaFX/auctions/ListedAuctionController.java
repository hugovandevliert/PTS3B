package core.javaFX.auctions;

import core.javaFX.auction.AuctionController;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import logic.repositories.AuctionRepository;
import models.Auction;
import models.Profile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListedAuctionController {

    @FXML private Label lblAuctionTitle, lblCurrentOffer;
    @FXML private Text textAuctionDescription;
    @FXML private Label lblListedAuctionId;
    @FXML private ImageView imgviewImage;

    private MenuController menuController;
    private AuctionController auctionController;

    private FXMLLoader fxmlLoader;
    private Pane auctionPane;

    private AuctionRepository auctionRepository;

    public ListedAuctionController() { auctionRepository = new AuctionRepository(new AuctionMySqlContext()); }

    public void setMenuController(MenuController menuController) { this.menuController = menuController; }

    public void setTitle(final String title) {
        lblAuctionTitle.setText(title);
    }

    public void setDescription(final String description) { textAuctionDescription.setText(description); }

    public void setCurrentOffer(final double offer) { lblCurrentOffer.setText(String.valueOf(offer)); }

    public void setId(final int id) { lblListedAuctionId.setText(String.valueOf(id)); }

    public void setImage(final Image image) { imgviewImage.setImage(image); }

    public void hideAuctionIdLabel() { lblListedAuctionId.setVisible(false); }

    public void loadAuctionPage() {
        try {
            menuController.paneContent.getChildren().clear();

            fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/auction/auction.fxml"));
            auctionPane = fxmlLoader.load();
            auctionController = fxmlLoader.getController();

            final Auction auction = auctionRepository.getAuctionForId(getAuctionId());

            if (auction != null){
                auctionController.setTitle(auction.getTitle());
                auctionController.setDescription(auction.getDescription());
                auctionController.setSeller(auction.getCreator().getUsername());
                auctionController.setImages(auction.getImages());
                auctionController.setBids(auction.getBids(), auction.getStartBid());
                auctionController.initializeCountdownTimer();

                menuController.paneContent.getChildren().add(auctionPane);
            }else{
                System.out.println("Something went wrong - Couldn't load auction page"); //TODO: proper error handling
            }
        } catch (IOException e){
            e.printStackTrace(); //TODO: proper error handling
        } catch (SQLException e) {
            e.printStackTrace(); //TODO: proper error handling
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); //TODO: proper error handling
        }
    }

    private int getAuctionId() { return Integer.parseInt(lblListedAuctionId.getText()); }
}
