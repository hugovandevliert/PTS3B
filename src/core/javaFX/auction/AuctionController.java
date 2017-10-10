package core.javaFX.auction;


import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.timers.AuctionBidsLoadingTimer;
import logic.timers.AuctionCountdownTimer;
import models.Bid;

import java.net.URL;
import java.util.*;

public class AuctionController extends MenuController {

    @FXML private Label lblAuctionTitle, lblAuctionSeller, lblTimer;
    @FXML private Text textAuctionDescription;
    @FXML private ImageView imgviewSelectedPicture, imgviewPicture1, imgviewPicture2, imgviewPicture3;
    @FXML private VBox vboxBids;

    private Timer auctionCountdown;
    private Timer bidsLoadingTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void setTitle(final String title) { lblAuctionTitle.setText(title); }

    public void setDescription(final String description) { textAuctionDescription.setText(description); }

    public void setSeller(final String seller) { lblAuctionSeller.setText(seller); }

    public void setImages(final List<Image> images) {
        //TODO: set the selected image and fill the other imageviews with either placeholder images or the actual corresponding images
    }

    public void setBids(final List<Bid> bids, final double startBid) {
        vboxBids.getChildren().clear();

        if (bids != null && bids.size() > 0){
            for (final Bid bid : bids){
                final Label lblBid = new Label("€" + bid.getAmount() + " - " + bid.getProfile().getUsername() + " - " + bid.getDate());
                vboxBids.getChildren().add(lblBid);
            }
        }else{
            final Label lblNoBids1 = new Label("Be the first one to place a bid!");
            final Label lblNoBids2 = new Label("The price to start bidding at is " + startBid);
            vboxBids.getChildren().add(lblNoBids1);
            vboxBids.getChildren().add(lblNoBids2);
        }
    }

    public void initializeCountdownTimer(final Date expirationDate) {
        final Date currentDate = new Date();
        final long countdownInMilliseconds = expirationDate.getTime() - currentDate.getTime();

        if (countdownInMilliseconds > 0){
            auctionCountdown = new Timer();
            auctionCountdown.schedule(new AuctionCountdownTimer(expirationDate, this), 0, 1000);
        }else{
            setTimer("This auction has ended!");
        }
    }

    public void setTimer(final String timer) { lblTimer.setText(timer); }

    public void initializeBidsLoadingTimer(final List<Bid> bids, final int auctionId, final double startBid) {
        auctionCountdown = new Timer();
        auctionCountdown.schedule(new AuctionBidsLoadingTimer(this, bids, auctionId, startBid), 1000, 500);
    }
}
