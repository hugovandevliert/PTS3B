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
import modelslibrary.Auction;
import utilities.enums.AlertType;
import utilities.enums.AuctionLoadingType;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ListedAuctionController extends MenuController {

    @FXML private Label lblAuctionTitle, lblCurrentOffer;
    @FXML private Text textAuctionDescription;
    @FXML private ImageView imgviewImage;

    private MenuController menuController;
    private AuctionRepository auctionRepository;
    private int auctionId;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) { }

    public ListedAuctionController() { auctionRepository = new AuctionRepository(new AuctionMySqlContext()); }

    public void setMenuController(final MenuController menuController) { this.menuController = menuController; }

    public void setListedAuction(final Auction auction) {
        setTitle(auction.getTitle());
        setDescription(auction.getDescription());
        setCurrentOffer(auction.getStartBid());
        setAuctionId(auction.getId());

        Image image = new Image("file:" + new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(), 200, 150, false, false);

        if (!auction.getImages().isEmpty()) {
            final Image img = auction.getImages().get(0);

            if (img != null) image = img;
        }

        setImage(image);
    }

    private void setTitle(final String title) {
        lblAuctionTitle.setText(title);
    }

    private void setDescription(final String description) { textAuctionDescription.setText(description); }

    private void setCurrentOffer(final double offer) { lblCurrentOffer.setText(AuctionController.convertToEuro(offer)); }

    private void setImage(final Image image) { imgviewImage.setImage(image); }

    private void setAuctionId(final int auctionId) {
        this.auctionId = auctionId;
    }

    public void loadAuctionPage() {
        try {
            menuController.paneContent.getChildren().clear();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/auction/auction.fxml"));
            Pane auctionPane = fxmlLoader.load();
            AuctionController auctionController = fxmlLoader.getController();

            final Auction auction = auctionRepository.getAuctionForId(this.auctionId, AuctionLoadingType.FOR_AUCTION_PAGE);

            if (auction != null){
                auctionController.setAuction(auction, this.menuController);

                menuController.paneContent.getChildren().add(auctionPane);
            }else{
                MenuController.showAlertMessage("Something went wrong - Couldn't load auction page", AlertType.ERROR, 3000);
            }
        } catch (IOException | SQLException | ClassNotFoundException exception){
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }
}
