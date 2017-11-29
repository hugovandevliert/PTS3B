package core.javaFX.auctions;

import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.List;
import java.util.ResourceBundle;

public class AuctionsController extends MenuController {

    @FXML private JFXTextField txtSearchBar;
    @FXML private VBox vboxListedAuctions;
    @FXML private ImageView imgviewSortPrice;

    private AuctionRepository auctionRepository;
    private FXMLLoader fxmlLoader;
    private Pane listedAuctionPane;
    private ListedAuctionController listedAuctionController;

    public AuctionsController() { auctionRepository = new AuctionRepository(new AuctionMySqlContext()); }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        imgviewSortPrice.setImage(new Image("/utilities/images/auctions/sort_price.png"));
    }

    public void searchAuction() throws IOException {
        final String searchTerm = txtSearchBar.getText().trim();

        if (searchTerm != null && searchTerm.length() > 0 && !searchTerm.isEmpty()){
            try {
                applicationManager.setLoadedAuctions(auctionRepository.getAuctionsForSearchTerm(searchTerm));
                applicationManager.sortAuctionsByPrice();
                displayAuctionsInInterface();
            } catch (SQLException exception) {
                MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
            } catch (ClassNotFoundException exception) {
                MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
            }
        }else{
            MenuController.showAlertMessage("Please write an actual searchterm!", AlertType.WARNING, 3000);
        }
    }

    public void sortAuctionsByPrice() {
        applicationManager.sortAuctionsByPrice();
        displayAuctionsInInterface();
    }

    private void displayAuctionsInInterface() {
        try {
            vboxListedAuctions.getChildren().clear();
            final List<Auction> auctions = applicationManager.getLoadedAuctions();

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
        } catch (IOException exception) {
            exception.printStackTrace();
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }
}
