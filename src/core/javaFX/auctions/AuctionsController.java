package core.javaFX.auctions;


import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.repositories.AuctionRepository;
import models.Auction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionsController extends MenuController {

    @FXML private JFXTextField txtSearchBar;
    @FXML private VBox vboxListedAuctions;

    private AuctionRepository auctionRepository;
    private FXMLLoader fxmlLoader;
    private Pane listedAuctionPane;
    private ListedAuctionController listedAuctionController;

    public AuctionsController() { auctionRepository = new AuctionRepository(new AuctionMySqlContext()); }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void searchAuction() throws IOException {
        final String searchTerm = txtSearchBar.getText().trim();

        if (searchTerm != null && searchTerm.length() > 0 && !searchTerm.isEmpty()){
            vboxListedAuctions.getChildren().clear();

            try {
                final ArrayList<Auction> auctions = auctionRepository.getAuctionsForSearchTerm(searchTerm);

                if (auctions.size() > 0){
                    for (final Auction auction : auctions){
                        fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/auctions/listedAuction.fxml"));
                        listedAuctionPane = fxmlLoader.load();
                        listedAuctionController = fxmlLoader.getController();

                        listedAuctionController.setMenuController(this);
                        listedAuctionController.setListedAuction(auction);

                        vboxListedAuctions.getChildren().add(listedAuctionPane);
                    }
                }else{
                    final Label lblNoItemsForSearch = new Label();
                    lblNoItemsForSearch.setText("No items met your search criteria!");
                    lblNoItemsForSearch.setTextFill(Color.web("#747e8c"));
                    lblNoItemsForSearch.setFont(new Font("System", 17));

                    vboxListedAuctions.getChildren().add(lblNoItemsForSearch);
                }
            } catch (SQLException e) {
                e.printStackTrace(); //TODO: proper error handling
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //TODO: proper error handling
            }
        }else{
            //TODO: Proper message handling
            System.out.println("Please write an actual searchterm!");
        }
    }
}
