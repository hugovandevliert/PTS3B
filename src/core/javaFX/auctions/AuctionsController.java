package core.javaFX.auctions;


import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Auction;
import models.Bid;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionsController extends MenuController {

    @FXML private JFXTextField txtSearchBar;
    @FXML private VBox vboxListedAuctions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void searchAuction(ActionEvent actionEvent) throws IOException {
        vboxListedAuctions.getChildren().clear();

        ArrayList<Bid> bids = new ArrayList<>();
        bids.add(new Bid(279));
        Auction auction = new Auction(1, "Samsung Galaxy S7", "This is its description", bids);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/javaFX/auctions/listedAuction.fxml"));
        Pane pane = loader.load();
        ListedAuctionController controller = loader.getController();

        controller.setTitle(auction.getTitle());
        controller.setDescription(auction.getDescription());
        controller.setCurrentOffer(auction.getBids().get((auction.getBids().size() - 1)).getAmount());

        vboxListedAuctions.getChildren().add(pane);
    }
}
