package core.javaFX.auction;


import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.repositories.AuctionRepository;
import logic.timers.AuctionBidsLoadingTimer;
import logic.timers.AuctionCountdownTimer;
import models.Bid;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class AuctionController extends MenuController {

    @FXML private Label lblAuctionTitle, lblAuctionSeller, lblTimer;
    @FXML private Text textAuctionDescription;
    @FXML private ImageView imgviewSelectedPicture, imgviewPicture1, imgviewPicture2, imgviewPicture3;
    @FXML private VBox vboxBids;
    @FXML private Pane panePlaceBid, paneEndAuction;

    private Timer auctionCountdown;
    private Timer bidsLoadingTimer;

    private AuctionRepository auctionRepository;

    private int auctionId;

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
                final Label lblBid = new Label("€" + bid.getAmount() + " - " + bid.getProfile().getUsername() + " - " + bid.getDate().toLocalDate() + " " + bid.getDate().toLocalTime());
                vboxBids.getChildren().add(lblBid);
            }
        }else{
            final Label lblNoBids1 = new Label("Be the first one to place a bid!");
            final Label lblNoBids2 = new Label("The price to start bidding at is " + startBid);
            vboxBids.getChildren().add(lblNoBids1);
            vboxBids.getChildren().add(lblNoBids2);
        }
    }

    public void setTimer(final String timer) { lblTimer.setText(timer); }

    public void setAuctionId(final int auctionId) {
        this.auctionId = auctionId;
    }

    public void initializeAuctionRepository() {
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
    }

    public void initializeCountdownTimer(final LocalDateTime expirationDate) {
        final Date currentDate = new Date();
        final long countdownInMilliseconds = expirationDate.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - currentDate.getTime();

        if (countdownInMilliseconds > 0){
            auctionCountdown = new Timer();
            auctionCountdown.schedule(new AuctionCountdownTimer(expirationDate, this), 0, 1000);
        }else{
            setTimer("This auction has ended!");
        }
    }

    public void initializeBidsLoadingTimer(final List<Bid> bids, final int auctionId, final double startBid) {
        bidsLoadingTimer = new Timer();
        bidsLoadingTimer.schedule(new AuctionBidsLoadingTimer(this, bids, auctionId, startBid), 1000, 500);
    }

    public void disablePlaceBidPane() {
        paneContent.getChildren().remove(panePlaceBid);
    }

    public void disableEndAuctionPane() {
        paneContent.getChildren().remove(paneEndAuction);
    }

    public void manuallyEndAuction() {
        if (auctionRepository.manuallyEndAuction(this.auctionId)){
            System.out.println("Auction successfully ended!");
        }else{
            System.out.println("Auction has not successfully been ended!");
        }
        //TODO: Use UserAlert here instead of System prints
    }
}
