package core.javaFX.favorites;

import core.javaFX.auctions.ListedAuctionController;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.repositories.AuctionRepository;
import modelslibrary.Auction;
import utilities.enums.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FavoritesController extends MenuController {

    @FXML private VBox vboxListedAuctions;

    private AuctionRepository auctionRepository;

    public FavoritesController() {
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) { }

    public void loadFavorites(final int profileId) {
        try {
            final ArrayList<Auction> auctions = auctionRepository.getFavoriteAuctionsForProfile(profileId);

            if (!auctions.isEmpty()){
                for (final Auction auction : auctions){
                    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/auctions/listedAuction.fxml"));
                    final Pane listedAuctionPane = fxmlLoader.load();
                    final ListedAuctionController listedAuctionController = fxmlLoader.getController();

                    listedAuctionController.setMenuController(this);
                    listedAuctionController.setListedAuction(auction);

                    vboxListedAuctions.getChildren().add(listedAuctionPane);
                }
            }else{
                final Label lblNoItemsForSearch = new Label();
                lblNoItemsForSearch.setText("You currently have no favorite auctions!");
                lblNoItemsForSearch.setTextFill(Color.web("#747e8c"));
                lblNoItemsForSearch.setFont(new Font("System", 17));

                vboxListedAuctions.getChildren().add(lblNoItemsForSearch);
            }
        } catch (SQLException | IOException | ClassNotFoundException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }
}
