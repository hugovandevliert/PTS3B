package core.javaFX.auctions;


import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.repositories.AuctionRepository;
import models.Auction;
import models.Bid;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionsController extends MenuController {

    @FXML private JFXTextField txtSearchBar;
    @FXML private VBox vboxListedAuctions;

    private AuctionRepository auctionRepository;

    public AuctionsController() {
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void searchAuction() throws IOException {
        //if (txtSearchBar.getText() != null){
            //String searchTerm = txtSearchBar.getText();
            vboxListedAuctions.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/javaFX/auctions/listedAuction.fxml"));
            Pane pane = loader.load();
            ListedAuctionController controller = loader.getController();

            if (controller != null){
                try {
                    for (final Auction auction : auctionRepository.getAuctionsForSearchTerm("Speed")){
                        controller.setTitle(auction.getTitle());
                        controller.setDescription(auction.getDescription());
                        controller.setCurrentOffer(auction.getStartBid());
                    }
                } catch (SQLException e) {
                    e.printStackTrace(); //TODO: proper error handling
                }
            }

            vboxListedAuctions.getChildren().add(pane);
        /*}else{
            //TODO: Proper message handling
            System.out.println("Please write an actual searchterm!");
        }*/
    }
}
