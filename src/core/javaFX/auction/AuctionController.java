package core.javaFX.auction;


import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Bid;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionController extends MenuController {

    @FXML private Label lblAuctionTitle, lblAuctionSeller, lblTimer;
    @FXML private Text textAuctionDescription;
    @FXML private ImageView imgviewSelectedPicture, imgviewPicture1, imgviewPicture2, imgviewPicture3;
    @FXML private VBox vboxBids;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void setTitle(final String title) { lblAuctionTitle.setText(title); }

    public void setDescription(final String description) { textAuctionDescription.setText(description); }

    public void setSeller(final String seller) { lblAuctionSeller.setText(seller); }

    public void setImages(final ArrayList<Image> images) {
        //TODO: set the selected image and fill the other imageviews with either placeholder images or the actual corresponding images
    }

    public void setBids(final ArrayList<Bid> bids) {
        if (bids != null && bids.size() > 0){
            for (final Bid bid : bids){
                final Label lblBid = new Label(bid.getAmount() + " - " + bid.getProfile().getUsername() + " - " + bid.getDate());
                vboxBids.getChildren().add(lblBid);
            }
        }else{
            final Label lblNoBids = new Label("Be the first one to place a bid!");
            vboxBids.getChildren().add(lblNoBids);
        }
    }

    public void setTimer(final String timer) { lblTimer.setText(timer); }
}
