package core.javaFX.auction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import core.javaFX.profile.ProfileController;
import data.contexts.AuctionMySqlContext;
import data.contexts.BidMySqlContext;
import data.contexts.ProfileMySqlContext;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.repositories.AuctionRepository;
import logic.repositories.BidRepository;
import logic.repositories.ProfileRepository;
import logic.timers.AuctionBidsLoadingTimer;
import logic.timers.AuctionCountdownTimer;
import models.Auction;
import models.Bid;
import models.Profile;
import utilities.database.Database;
import utilities.enums.AlertType;
import utilities.enums.ProfileLoadingType;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

public class AuctionController extends MenuController {

    @FXML private Label lblAuctionTitle, lblAuctionSeller, lblTimer;
    @FXML private Text textAuctionDescription;
    @FXML private ImageView imgviewSelectedPicture, imgviewPicture1, imgviewPicture2, imgviewPicture3;
    @FXML private VBox vboxBids;
    @FXML private Pane panePlaceBid, paneEndAuction, paneContent;
    @FXML private JFXTextField txtBid;
    @FXML private JFXButton btnEndAuction, btnAddToFavorites;

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
    public void initialize(final URL location, final ResourceBundle resources) {
        /* Adding the AddFavorites Button with a star icon */
        final Image starIcon = new Image( "/utilities/images/button/button_star_image.png");

        btnAddToFavorites = new JFXButton("Add to favorites", new ImageView(starIcon));
        btnAddToFavorites.setPrefSize(346, 48);
        btnAddToFavorites.setLayoutX(626);
        btnAddToFavorites.setLayoutY(580);
        btnAddToFavorites.setFont(Font.font("Segoe UI Light"));
        btnAddToFavorites.setTextFill(Color.web("#824923"));
        btnAddToFavorites.setStyle("-fx-font-size: 14; -fx-background-color: #f2aa78");
        btnAddToFavorites.setOnAction(event -> addToFavoriteAuctions());

        paneContent.getChildren().add(btnAddToFavorites);
    }

    public void setAuction(final Auction auction, final MenuController menuController) {
        setTitle(auction.getTitle());
        setDescription(auction.getDescription());
        setSeller(auction.getCreator().getUsername());
        setImages(auction.getImages());
        setBids(auction.getBids(), auction.getStartBid());
        setAuctionId(auction.getId());
        setCreatorId(auction.getCreator().getProfileId());
        setCurrenteUserId(applicationManager.getCurrentUser().getId());
        setBidTextfieldPromptText("Your bid: (at least + " + convertToEuro(auction.getIncrementation()) + ")");
        setAuctionMinimumBid(auction.getMinimum());
        setAuctionMinimumIncrementation(auction.getIncrementation());
        setMenuController(menuController);
        initializeCountdownTimer();
        initializeBidsLoadingTimer(auction.getBids(), this.auctionId, auction.getStartBid());
        initializeRepositories();
        handleEndAuctionPaneRemoving();
        handleAddtoFavoritesButtonRemoving(auction.getCreator().getProfileId());

        if (currentUserIsCreatorOfThisAuction(auction)){
            disablePlaceBidPane();
        }else{
            disableEndAuctionPane();
        }
    }

    private void setTitle(final String title) { lblAuctionTitle.setText(title); }

    private void setDescription(final String description) { textAuctionDescription.setText(description); }

    private void setSeller(final String seller) { lblAuctionSeller.setText(seller); }

    private void setImages(final List<Image> images) {
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
                final Label lblBid = new Label(convertToEuro(bid.getAmount()) + " - " + bid.getProfile().getUsername() + " - " + bid.getDate().toLocalDate() + " " + bid.getDate().toLocalTime());
                lblBid.setFont(Font.font("Segoe UI Semilight"));
                lblBid.setTextFill(Color.web("#A6B5C9"));
                lblBid.setStyle("-fx-font-size: 16");
                vboxBids.getChildren().add(lblBid);
            }
        }else{
            final Label lblNoBids1 = new Label("Be the first one to place a bid!");
            final Label lblNoBids2 = new Label("The price to start bidding at is " + convertToEuro(startBid));
            lblNoBids1.setFont(Font.font("Segoe UI Semilight"));
            lblNoBids1.setTextFill(Color.web("#A6B5C9"));
            lblNoBids1.setStyle("-fx-font-size: 16");
            lblNoBids2.setFont(Font.font("Segoe UI Semilight"));
            lblNoBids2.setTextFill(Color.web("#A6B5C9"));
            lblNoBids2.setStyle("-fx-font-size: 16");
            vboxBids.getChildren().add(lblNoBids1);
            vboxBids.getChildren().add(lblNoBids2);
        }
    }

    public void setTimer(final String timer) {
        lblTimer.setText(timer);

        if (timer.equals("This auction has ended!")){ // The auction ended - we should remove the addBid pane for clearity
            paneContent.getChildren().remove(panePlaceBid);
        }
    }

    private void setAuctionId(final int auctionId) {
        this.auctionId = auctionId;
    }

    private void setCreatorId(final int creatorId) { this.creatorId = creatorId; }

    private void setCurrenteUserId(final int currenteUserId) { this.currenteUserId = currenteUserId; }

    private void setBidTextfieldPromptText(final String value) {
        txtBid.setPromptText(value);
    }

    private void setAuctionMinimumBid(final double auctionMinimumBid) {
        this.auctionMinimumBid = auctionMinimumBid;
    }

    private void setAuctionMinimumIncrementation(final double auctionMinimumIncrementation) {
        this.auctionMinimumIncrementation = auctionMinimumIncrementation;
    }

    private void setMenuController(final MenuController menuController) { this.menuController = menuController; }

    private void initializeRepositories() {
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
        bidRepository = new BidRepository(new BidMySqlContext());
        profileRepository = new ProfileRepository(new ProfileMySqlContext());
    }

    private void initializeCountdownTimer() {
        auctionCountdown = new Timer();
        auctionCountdown.schedule(new AuctionCountdownTimer(this, this.menuController, this.auctionId), 0, 1000);
    }

    private void initializeBidsLoadingTimer(final List<Bid> bids, final int auctionId, final double startBid) {
        bidsLoadingTimer = new Timer();
        bidsLoadingTimer.schedule(new AuctionBidsLoadingTimer(this, this.menuController, bids, auctionId, startBid), 1000, 500);
    }

    private void handleEndAuctionPaneRemoving() {
        // If this auction has been closed, we should never get the option again to close it once more
        try {
            if (auctionRepository.auctionIsClosed(this.auctionId)) {
                paneEndAuction.getChildren().remove(btnEndAuction);
            }
        } catch (SQLException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void handleAddtoFavoritesButtonRemoving(final int auctionCreatorProfileId) {
        try {
            // If we have already marked the auction as our favorite, there is no need to display the user an option to mark it once more
            // Neither do we want the creator of an auction to mark his/her own auction as favorite
            if (auctionRepository.auctionIsFavoriteForUser(this.auctionId, this.currenteUserId) || auctionCreatorProfileId == this.currenteUserId){
                paneContent.getChildren().remove(btnAddToFavorites);
            }
        } catch (SQLException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void disablePlaceBidPane() {
        paneContent.getChildren().remove(panePlaceBid);
    }

    private void disableEndAuctionPane() {
        paneContent.getChildren().remove(paneEndAuction);
    }

    public void manuallyEndAuction() {
        if (auctionRepository.manuallyEndAuction(this.auctionId)){
            MenuController.showAlertMessage("Auction successfully ended!", AlertType.MESSAGE, 3000);

            // We just ended the auction - we should therefore remove the option to keep ending it!
            paneEndAuction.getChildren().remove(btnEndAuction);
        }else{
            MenuController.showAlertMessage("Auction has not successfully been ended!", AlertType.ERROR, 3000);
        }
    }

    public void placeNewBid() {
        try {
            if (!auctionRepository.auctionIsClosed(this.auctionId)){
                final String bidPriceString = txtBid.getText().replaceAll(",", ".");
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
                            MenuController.showAlertMessage("Placing the bid wasn't successful!", AlertType.ERROR, 3000);
                        }
                    }else{
                        MenuController.showAlertMessage("Your bid is not high enough, it should at least be " + convertToEuro(minimumNeededAmount), AlertType.WARNING, 3000);
                    }
                }else{
                    MenuController.showAlertMessage("Please fill in a valid bid!", AlertType.WARNING, 3000);
                }
            }else{
                MenuController.showAlertMessage("This auction has been closed - you are not able to bid anymore.", AlertType.WARNING, 3000);
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

    public void addToFavoriteAuctions() {
        final Profile profile = MenuController.applicationManager.getCurrentUser().getProfile();

        if (profile.addFavoriteAuction(this.auctionId)) {
            // We successfully added this auction to our favorites, we can now delete the button because we already added to our favorites
            MenuController.showAlertMessage("Successfully added auction to favorites!", AlertType.MESSAGE, 3000);

            paneContent.getChildren().remove(btnAddToFavorites);
        } else {
            MenuController.showAlertMessage("Could not add the auction to favorites!", AlertType.ERROR, 3000);
        }
    }

    public String getTimerString() {
        return lblTimer.getText();
    }

    private boolean amountIsHighEnough(final double bidAmount, final double minimumNeededAmount) {
        return bidAmount >= minimumNeededAmount;
    }

    private boolean currentUserIsCreatorOfThisAuction(final Auction auction) {
        return applicationManager.getCurrentUser().getId() == auction.getCreator().getProfileId();
    }

    private long getMillisFromLocalDateTime(final LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
    }

    public static String convertToEuro(final double amount) {
        final Locale dutch = new Locale("nl", "NL");
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(dutch);
        return decimalFormat.format(amount);
    }
}
