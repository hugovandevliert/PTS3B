package core.javaFX.auction;


import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import data.contexts.BidMySqlContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.repositories.AuctionRepository;
import logic.repositories.BidRepository;
import logic.timers.AuctionBidsLoadingTimer;
import logic.timers.AuctionCountdownTimer;
import models.Bid;
import utilities.database.Database;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class AuctionController extends MenuController {

    @FXML private Label lblAuctionTitle, lblAuctionSeller, lblTimer;
    @FXML private Text textAuctionDescription;
    @FXML private ImageView imgviewSelectedPicture, imgviewPicture1, imgviewPicture2, imgviewPicture3;
    @FXML private VBox vboxBids;
    @FXML private Pane panePlaceBid, paneEndAuction;
    @FXML private JFXTextField txtBid;

    private Timer auctionCountdown;
    private Timer bidsLoadingTimer;

    private AuctionRepository auctionRepository;
    private BidRepository bidRepository;

    private int auctionId, currenteUserId;
    private double auctionMinimumBid, auctionMinimumIncrementation;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public String getTimerString() {
        return lblTimer.getText();
    }

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

    public void setCurrenteUserId(final int currenteUserId) { this.currenteUserId = currenteUserId; }

    public void setBidTextfieldPromptText(final String value) {
        txtBid.setPromptText(value);
    }

    public void setAuctionMinimumBid(final double auctionMinimumBid) {
        this.auctionMinimumBid = auctionMinimumBid;
    }

    public void setAuctionMinimumIncrementation(final double auctionMinimumIncrementation) {
        this.auctionMinimumIncrementation = auctionMinimumIncrementation;
    }

    public void initializeRepositories() {
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
        bidRepository = new BidRepository(new BidMySqlContext());
    }

    public void initializeCountdownTimer(final LocalDateTime expirationDate) {
        final Date currentDate = new Date();
        final long countdownInMilliseconds = expirationDate.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - currentDate.getTime();

        if (countdownInMilliseconds > 0){
            auctionCountdown = new Timer();
            auctionCountdown.schedule(new AuctionCountdownTimer(this, this.auctionId), 0, 1000);
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

    public void placeNewBid() {
        try {
            if (!auctionRepository.auctionIsClosed(this.auctionId)){
                final String bidPriceString = txtBid.getText();
                txtBid.setText("");

                if (bidPriceString != null && bidPriceString.length() > 0 && !bidPriceString.isEmpty() && Database.isDouble(bidPriceString)){
                    final double bidAmount = Double.parseDouble(bidPriceString);
                    double minimumNeededAmount = 0;
                    final Bid mostRecentBid = bidRepository.getMostRecentBidForAuctionWithId(this.auctionId);

                    if (mostRecentBid != null){
                        minimumNeededAmount = mostRecentBid.getAmount() + this.auctionMinimumIncrementation;
                    }else{
                        minimumNeededAmount = this.auctionMinimumBid;
                    }

                    if (amountIsHighEnough(bidAmount, minimumNeededAmount)){
                        if (auctionRepository.addBid(bidAmount, currenteUserId, auctionId)){
                            System.out.println("Successfully placed bid!"); //TODO: show this with a User Alert
                        }else{
                            System.out.println("Placing the bid wasn't successfull!"); //TODO: show this with a User Alert
                        }
                    }else{
                        System.out.println("Your bid is not high enough, it should atleast be €" + minimumNeededAmount); //TODO: show htis with a User Alert
                    }
                }else{
                    System.out.println("Please fill in a valid bid!"); //TODO: show this with a User Alert
                }
            }else{
                System.out.println("This auction has been closed - you are not able to bid anymore"); //TODO: show this with a User Alert
            }
        } catch (SQLException e){
            e.printStackTrace(); //TODO: proper error handling
        }
    }

    private boolean amountIsHighEnough(final double bidAmount, final double minimumNeededAmount) {
        return bidAmount >= minimumNeededAmount;
    }
}
