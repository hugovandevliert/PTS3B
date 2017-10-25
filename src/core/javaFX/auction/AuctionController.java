package core.javaFX.auction;

import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import core.javaFX.profile.ProfileController;
import data.contexts.AuctionMySqlContext;
import data.contexts.BidMySqlContext;
import data.contexts.ProfileMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.repositories.AuctionRepository;
import logic.repositories.BidRepository;
import logic.repositories.ProfileRepository;
import logic.timers.AuctionBidsLoadingTimer;
import logic.timers.AuctionCountdownTimer;
import models.Bid;
import models.Profile;
import utilities.database.Database;
import utilities.enums.AlertType;
import utilities.enums.ProfileLoadingType;
import java.io.File;
import java.io.IOException;
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
    private ProfileRepository profileRepository;

    private MenuController menuController;
    private ProfileController profileController;

    private int auctionId, currenteUserId, creatorId;
    private double auctionMinimumBid, auctionMinimumIncrementation;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) { }

    public String getTimerString() {
        return lblTimer.getText();
    }

    public void setTitle(final String title) { lblAuctionTitle.setText(title); }

    public void setDescription(final String description) { textAuctionDescription.setText(description); }

    public void setSeller(final String seller) { lblAuctionSeller.setText(seller); }

    public void setImages(final List<Image> images) {
        if (images != null){
            final Image placeholderImage = new Image("file:" +  new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(), 429, 277, false, false);

            if (images.size() >= 1){
                imgviewSelectedPicture.setImage(images.get(0));
            }else{
                imgviewSelectedPicture.setImage(placeholderImage);
            }

            if (images.size() >= 2){
                imgviewPicture1.setImage(images.get(1));
            }else{
                imgviewPicture1.setImage(placeholderImage);
            }

            if (images.size() >= 3){
                imgviewPicture2.setImage(images.get(2));
            }else{
                imgviewPicture2.setImage(placeholderImage);
            }

            if (images.size() >= 4){
                imgviewPicture3.setImage(images.get(3));
            }else{
                imgviewPicture3.setImage(placeholderImage);
            }
        }
    }

    public void setBids(final List<Bid> bids, final double startBid) {
        vboxBids.getChildren().clear();

        if (bids != null && bids.size() > 0){
            for (final Bid bid : bids){
                final Label lblBid = new Label("€" + bid.getAmount() + " - " + bid.getProfile().getUsername() + " - " + bid.getDate().toLocalDate() + " " + bid.getDate().toLocalTime());
                lblBid.setFont(Font.font("Segoe UI Light"));
                lblBid.setStyle("-fx-text-fill: #A6B5C9");
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

    public void setCreatorId(final int creatorId) { this.creatorId = creatorId; }

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

    public void setMenuController(final MenuController menuController) { this.menuController = menuController; }

    public void initializeRepositories() {
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
        bidRepository = new BidRepository(new BidMySqlContext());
        profileRepository = new ProfileRepository(new ProfileMySqlContext());
    }

    public void initializeCountdownTimer(final LocalDateTime expirationDate) {
        final Date currentDate = new Date();
        final long countdownInMilliseconds = expirationDate.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - currentDate.getTime();

        if (countdownInMilliseconds > 0){
            auctionCountdown = new Timer();
            auctionCountdown.schedule(new AuctionCountdownTimer(this, this.menuController, this.auctionId), 0, 1000);
        }else{
            setTimer("This auction has ended!");
        }
    }

    public void initializeBidsLoadingTimer(final List<Bid> bids, final int auctionId, final double startBid) {
        bidsLoadingTimer = new Timer();
        bidsLoadingTimer.schedule(new AuctionBidsLoadingTimer(this, this.menuController, bids, auctionId, startBid), 1000, 500);
    }

    public void disablePlaceBidPane() {
        paneContent.getChildren().remove(panePlaceBid);
    }

    public void disableEndAuctionPane() {
        paneContent.getChildren().remove(paneEndAuction);
    }

    public void manuallyEndAuction() {
        if (auctionRepository.manuallyEndAuction(this.auctionId)){
            MenuController.showAlertMessage("Auction successfully ended!", AlertType.MESSAGE, 3000);
        }else{
            MenuController.showAlertMessage("Auction has not successfully been ended!", AlertType.ERROR, 3000);
        }
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
                            MenuController.showAlertMessage("Successfully placed bid!", AlertType.MESSAGE, 3000);
                        }else{
                            MenuController.showAlertMessage("Placing the bid wasn't successfull!", AlertType.ERROR, 3000);
                        }
                    }else{
                        MenuController.showAlertMessage("Your bid is not high enough, it should atleast be €" + minimumNeededAmount, AlertType.WARNING, 3000);
                    }
                }else{
                    MenuController.showAlertMessage("Please fill in a valid bid!", AlertType.WARNING, 3000);
                }
            }else{
                MenuController.showAlertMessage("This auction has been closed - you are not able to bid anymore", AlertType.WARNING, 3000);
            }
        } catch (SQLException exception){
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        } catch (IOException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        } catch (ClassNotFoundException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    public void changeSelectedImage(final MouseEvent event) {
        final ImageView clickedImageView = (ImageView)event.getSource();
        final Image previousSelectedPicture = imgviewSelectedPicture.getImage();

        imgviewSelectedPicture.setImage(clickedImageView.getImage());
        clickedImageView.setImage(previousSelectedPicture);
    }

    public void goToCreatorProfile() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/profile/profile.fxml"));
            final Pane newLoadedPane = fxmlLoader.load();
            profileController = fxmlLoader.getController();
            profileRepository = new ProfileRepository(new ProfileMySqlContext());

            final Profile profile = profileRepository.getProfileForId(this.creatorId, ProfileLoadingType.FOR_PROFILE_PAGE);

            profileController.setMenuController(this.menuController);
            profileController.loadProfile(profile);

            this.menuController.paneContent.getChildren().removeAll();
            this.menuController.paneContent.getChildren().add(newLoadedPane);
        } catch (SQLException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        } catch (ClassNotFoundException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        } catch (IOException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private boolean amountIsHighEnough(final double bidAmount, final double minimumNeededAmount) {
        return bidAmount >= minimumNeededAmount;
    }
}
