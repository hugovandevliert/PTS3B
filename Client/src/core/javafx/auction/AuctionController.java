package core.javafx.auction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import core.javafx.menu.MenuController;
import core.javafx.profile.ProfileController;
import data.contexts.AuctionMySqlContext;
import data.contexts.BidMySqlContext;
import data.contexts.ProfileMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.algorithms.MusicPlayer;
import logic.clients.BidClient;
import logic.repositories.AuctionRepository;
import logic.repositories.BidRepository;
import logic.repositories.ProfileRepository;
import logic.timers.AuctionCountdownTimer;
import models.Auction;
import models.Bid;
import models.Profile;
import utilities.Constants;
import utilities.database.Database;
import utilities.enums.AlertType;
import utilities.enums.BidLoadingType;
import utilities.enums.ProfileLoadingType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

public class AuctionController extends MenuController {

    @FXML private Label lblDays;
    @FXML private Label lblHours;
    @FXML private Label lblMinutes;
    @FXML private Label lblSeconds;
    @FXML private Label lblAuctionTitle;
    @FXML private Label lblAuctionSeller;
    @FXML private Label lblTimer;
    @FXML private Text textAuctionDescription;
    @FXML private ImageView imgviewSelectedPicture;
    @FXML private ImageView imgviewPicture1;
    @FXML private ImageView imgviewPicture2;
    @FXML private ImageView imgviewPicture3;
    @FXML private VBox vboxBids;
    @FXML private Pane panePlaceBid;
    @FXML private Pane paneEndAuction;
    @FXML private Pane paneAuctionContent;
    @FXML private JFXTextField txtBid;
    @FXML private JFXButton btnEndAuction, btnAddToFavorites;

    private AuctionRepository auctionRepository;
    private BidRepository bidRepository;
    private ProfileRepository profileRepository;
    private MenuController menuController;

    private int auctionId, currentUserId, creatorId;
    private Auction auction;
    private double auctionMinimumBid, auctionMinimumIncrementation;
    private boolean hasShownAuctionEndingMessage;

    private ArrayList<Bid> bids;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        /* Adding the AddFavorites Button with a star icon */
        final Image starIcon = new Image("/utilities/images/button/button_star_image.png");

        btnAddToFavorites = new JFXButton("Add to favorites", new ImageView(starIcon));
        btnAddToFavorites.setPrefSize(346, 48);
        btnAddToFavorites.setLayoutX(626);
        btnAddToFavorites.setLayoutY(580);
        btnAddToFavorites.setFont(Font.font("Segoe UI Light"));
        btnAddToFavorites.setTextFill(Color.web("#824923"));
        btnAddToFavorites.setStyle("-fx-font-size: 14; -fx-background-color: #f2aa78");
        btnAddToFavorites.setOnAction(event -> addToFavoriteAuctions());
        btnAddToFavorites.setCursor(Cursor.HAND);

        paneAuctionContent.getChildren().add(btnAddToFavorites);

        bids = new ArrayList<>();
    }

    public void setAuction(final Auction auction, final MenuController menuController) throws SQLException, IOException, ClassNotFoundException {
        this.auction = auction;

        initializeRepositories();
        setTitle(auction.getTitle());
        setDescription(auction.getDescription());
        setSeller(auction.getCreator().getUsername());
        setImages(auction.getImages());
        setBids(auction.getBids(), auction.getStartBid());
        setAuctionId(auction.getId());
        setCreatorId(auction.getCreator().getProfileId());
        setCurrentUserId(applicationManager.getCurrentUser().getId());
        final Bid mostRecentBid = bidRepository.getMostRecentBidForAuctionWithId(auction.getId(), BidLoadingType.FOR_MOST_RECENT_BID);
        double minimalBid;
        if (mostRecentBid != null) {
            minimalBid = mostRecentBid.getAmount() + auction.getIncrementation();
        } else {
            minimalBid = auction.getStartBid();
        }
        setBidTextfieldPromptText("Your bid: (at least " + convertToEuro(minimalBid) + ")");
        setAuctionMinimumBid(auction.getStartBid());
        setAuctionMinimumIncrementation(auction.getIncrementation());
        setMenuController(menuController);
        initializeCountdownTimer();
        handleEndAuctionPaneRemoving();
        handleAddToFavoritesButtonRemoving(auction.getCreator().getProfileId());

        if (currentUserIsCreatorOfThisAuction(auction)) {
            disablePlaceBidPane();
        } else {
            disableEndAuctionPane();
        }

        /* Let's make sure we will add a BidClient to our RMIClientsManager for the auction that we're currently trying to view.
           This will handle all the incoming bids through Server Push RMI Mechanics.
         */
        try {
            final BidClient bidClient = new BidClient(applicationManager.getRmiClientsManager().getBidsRegistry(), this.auctionId, applicationManager.getCurrentUser().getId(), applicationManager.getRmiClientsManager(), this);
            applicationManager.getRmiClientsManager().addBidClient(bidClient);
        } catch (IOException | NotBoundException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void setTitle(final String title) {
        lblAuctionTitle.setText(title);
    }

    private void setDescription(final String description) {
        textAuctionDescription.setText(description);
    }

    private void setSeller(final String seller) {
        lblAuctionSeller.setText(seller);
    }

    private void setImages(final List<Image> images) {
        if (images != null) {
            final Image placeholderImage = new Image("file:" + new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(), 429, 277, false, false);

            if (!images.isEmpty()) {
                imgviewSelectedPicture.setImage(images.get(0));
            } else {
                imgviewSelectedPicture.setImage(placeholderImage);
            }

            if (images.size() >= 2) {
                imgviewPicture1.setImage(images.get(1));
            } else {
                imgviewPicture1.setImage(placeholderImage);
            }

            if (images.size() >= 3) {
                imgviewPicture2.setImage(images.get(2));
            } else {
                imgviewPicture2.setImage(placeholderImage);
            }

            if (images.size() >= 4) {
                imgviewPicture3.setImage(images.get(3));
            } else {
                imgviewPicture3.setImage(placeholderImage);
            }
        }
    }

    public void setBids(final List<Bid> bids, final double startBid) {
        vboxBids.getChildren().clear();

        if (bids != null && !bids.isEmpty()) {
            for (final Bid bid : bids) {
                addBidToList(bid);
            }

            addBidsToInterface();
        } else {
            final Label lblNoBids1 = new Label("Be the first one to place a bid!");
            final Label lblNoBids2 = new Label("The price to start bidding at is " + convertToEuro(startBid));
            final String fontSegoe = "Segoe UI Semilight";
            final String fontWeb = "#A6B5C9";
            final String style = "-fx-font-size: 16";
            lblNoBids1.setFont(Font.font(fontSegoe));
            lblNoBids1.setTextFill(Color.web(fontWeb));
            lblNoBids1.setStyle(style);
            lblNoBids2.setFont(Font.font(fontSegoe));
            lblNoBids2.setTextFill(Color.web(fontWeb));
            lblNoBids2.setStyle(style);
            vboxBids.getChildren().add(lblNoBids1);
            vboxBids.getChildren().add(lblNoBids2);
        }
    }

    public void addBidToList(final Bid bid) {
        this.bids.add(bid);
    }

    public void addBidsToInterface() {
        vboxBids.getChildren().clear();

        Collections.sort(this.bids);

        for (final Bid bid : bids) {
            displayBid(bid);
        }
    }

    public void setTimer(final String timer) {
        lblTimer.setText(timer);

        if (timer.equals("This auction has ended")) {
            /* The auction ended - we should remove the addBid pane for clarity */
            paneAuctionContent.getChildren().remove(panePlaceBid);
            lblDays.setVisible(false);
            lblHours.setVisible(false);
            lblMinutes.setVisible(false);
            lblSeconds.setVisible(false);

            if (!hasShownAuctionEndingMessage){
                hasShownAuctionEndingMessage = true;
                /* Let's also check whether this current user has won the auction or not. We'll have to make the current user aware of this. */
                try {
                    final Bid lastBid = bidRepository.getMostRecentBidForAuctionWithId(this.auctionId, BidLoadingType.FOR_AUCTION_WINNER_LAST_BID);

                    /* The current user is the author of the last bid, meaning the current user won this auction. */
                    if (lastBid.getProfile().getProfileId() == this.currentUserId){
                        final Profile auctionOwnerProfile = profileRepository.getProfileForId(lastBid.getProfile().getProfileId(), ProfileLoadingType.FOR_AUCTION_WON_OWNER_DISPLAYING);
                        String email = "[Error] - Could not find owner's email!";

                        if (auctionOwnerProfile != null) email = auctionOwnerProfile.getEmail();

                        MenuController.showAlertMessage("Congratulations, you have won this auction! You can contact the owner on this email: " + email, AlertType.MESSAGE);
                        final MusicPlayer musicPlayer = new MusicPlayer(Constants.SOUND_AUCTION_WON_MP3);
                        musicPlayer.playSound();
                    }else{
                        MenuController.showAlertMessage("Unfortunately, somebody else has won this auction. Better luck next time!", AlertType.WARNING);
                        final MusicPlayer musicPlayer = new MusicPlayer(Constants.SOUND_AUCTION_LOST_MP3);
                        musicPlayer.playSound();
                    }
                } catch (SQLException | IOException | ClassNotFoundException exception) {
                    MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 5000);
                }
            }
        }
    }

    private void displayBid(final Bid bid) {
        final String time = String.format("%02d:%02d:%02d", bid.getDate().getHour(), bid.getDate().getMinute(), bid.getDate().getSecond());
        final String date = bid.getDate().getDayOfMonth() + " " + bid.getDate().getMonth().toString().toLowerCase() + ", " + bid.getDate().getYear();
        final Label lblBid = new Label(convertToEuro(bid.getAmount()) + " - " + bid.getProfile().getUsername() + " - (" + time + " - " + date + ")");
        lblBid.setFont(Font.font("Segoe UI Semilight"));
        lblBid.setTextFill(Color.web("#A6B5C9"));
        lblBid.setStyle("-fx-font-size: 16");
        vboxBids.getChildren().add(lblBid);
    }

    private void setAuctionId(final int auctionId) {
        this.auctionId = auctionId;
    }

    private void setCreatorId(final int creatorId) {
        this.creatorId = creatorId;
    }

    private void setCurrentUserId(final int currentUserId) {
        this.currentUserId = currentUserId;
    }

    private void setBidTextfieldPromptText(final String value) {
        txtBid.setPromptText(value);
    }

    public void setBidTextfieldPromptText(final Bid bid) {
        txtBid.setPromptText("Your bid: (at least " + convertToEuro(bid.getAmount() + auction.getIncrementation()) + ")");
    }

    private void setAuctionMinimumBid(final double auctionMinimumBid) {
        this.auctionMinimumBid = auctionMinimumBid;
    }

    private void setAuctionMinimumIncrementation(final double auctionMinimumIncrementation) {
        this.auctionMinimumIncrementation = auctionMinimumIncrementation;
    }

    private void setMenuController(final MenuController menuController) {
        this.menuController = menuController;
    }

    private void initializeRepositories() {
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
        bidRepository = new BidRepository(new BidMySqlContext());
        profileRepository = new ProfileRepository(new ProfileMySqlContext());
    }

    private void initializeCountdownTimer() {
        Timer auctionCountdown = new Timer();
        auctionCountdown.schedule(new AuctionCountdownTimer(this, this.menuController, this.auctionId, applicationManager.getRmiClientsManager().getTimeClient()), 0, 1000);
    }

    private void handleEndAuctionPaneRemoving() {
        // If this auction has been closed, we should never get the option again to close it once more
        try {
            if (auctionRepository.auctionIsClosed(this.auctionId)) {
                paneEndAuction.getChildren().remove(btnEndAuction);
            }
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void handleAddToFavoritesButtonRemoving(final int auctionCreatorProfileId) {
        try {
            // If we have already marked the auction as our favorite, there is no need to display the user an option to mark it once more
            // Neither do we want the creator of an auction to mark his/her own auction as favorite
            if (auctionRepository.auctionIsFavoriteForUser(this.auctionId, this.currentUserId) || auctionCreatorProfileId == this.currentUserId) {
                paneAuctionContent.getChildren().remove(btnAddToFavorites);
            }
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void disablePlaceBidPane() {
        paneAuctionContent.getChildren().remove(panePlaceBid);
    }

    private void disableEndAuctionPane() {
        paneAuctionContent.getChildren().remove(paneEndAuction);
    }

    public void manuallyEndAuction() {
        if (auctionRepository.manuallyEndAuction(this.auctionId)) {
            MenuController.showAlertMessage("Auction successfully ended!", AlertType.MESSAGE, 3000);

            // We just ended the auction - we should therefore remove the option to keep ending it!
            paneEndAuction.getChildren().remove(btnEndAuction);
        } else {
            MenuController.showAlertMessage("Auction has not successfully been ended!", AlertType.ERROR, 3000);
        }
    }

    public void placeNewBid() {
        try {
            if (auctionRepository.auctionIsClosed(this.auctionId)) {
                MenuController.showAlertMessage("This auction has been closed - you are not able to bid anymore.", AlertType.WARNING, 3000);
                return;
            }
            final String bidPriceString = txtBid.getText().replaceAll(",", ".");
            txtBid.setText("");

            if (bidPriceString == null || bidPriceString.length() == 0 || !Database.isDouble(bidPriceString)) {
                MenuController.showAlertMessage("Please fill in a valid bid!", AlertType.WARNING, 3000);
                return;
            }
            final double bidAmount = Double.parseDouble(bidPriceString);
            double minimumNeededAmount = 0;
            final Bid mostRecentBid = bidRepository.getMostRecentBidForAuctionWithId(this.auctionId, BidLoadingType.FOR_MOST_RECENT_BID);

            if (mostRecentBid != null) {
                minimumNeededAmount = mostRecentBid.getAmount() + this.auctionMinimumIncrementation;
            } else {
                minimumNeededAmount = this.auctionMinimumBid;
            }
            if (!amountIsHighEnough(bidAmount, minimumNeededAmount)) {
                MenuController.showAlertMessage("Your bid is not high enough, it should at least be " + convertToEuro(minimumNeededAmount), AlertType.WARNING, 3000);
                return;
            }

            final LocalDateTime currentTime = applicationManager.getRmiClientsManager().getTimeClient().getTime();

            if (currentTime == null){
                MenuController.showAlertMessage("Could not grab the time from the server", AlertType.ERROR, 5000);
            }

            if (currentTime != null && currentTime.isAfter(auction.getExpirationDate())){
                MenuController.showAlertMessage("The expiration date has been exceeded. You are no longer able to place a bet on this auction!", AlertType.ERROR, 5000);
            }

            if (auctionRepository.addBid(bidAmount, currentUserId, auctionId, currentTime)) {
                final Bid bid = new Bid(applicationManager.getCurrentUser().getProfile(), bidAmount, LocalDateTime.now(), auctionId);
                applicationManager.getRmiClientsManager().getBidClient().sendBid(bid);
                MenuController.showAlertMessage("Successfully placed bid!", AlertType.MESSAGE, 3000);
            } else {
                MenuController.showAlertMessage("Placing the bid wasn't successful!", AlertType.ERROR, 3000);
            }
        } catch (SQLException | ClassNotFoundException | IOException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    public void changeSelectedImage(final MouseEvent event) {
        final ImageView clickedImageView = (ImageView) event.getSource();
        final Image previousSelectedPicture = imgviewSelectedPicture.getImage();

        imgviewSelectedPicture.setImage(clickedImageView.getImage());
        clickedImageView.setImage(previousSelectedPicture);
    }

    public void goToCreatorProfile() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/profile/profile.fxml"));
            final Pane newLoadedPane = fxmlLoader.load();
            ProfileController profileController = fxmlLoader.getController();
            profileRepository = new ProfileRepository(new ProfileMySqlContext());

            final Profile profile = profileRepository.getProfileForId(this.creatorId, ProfileLoadingType.FOR_PROFILE_PAGE);

            profileController.setMenuController(this.menuController);
            profileController.loadProfile(profile);

            this.menuController.paneContent.getChildren().removeAll();
            this.menuController.paneContent.getChildren().add(newLoadedPane);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            MenuController.showAlertMessage(e.getMessage(), AlertType.ERROR, 3000);
        }
    }

    public void addToFavoriteAuctions() {
        final Profile profile = MenuController.applicationManager.getCurrentUser().getProfile();

        if (profileRepository.addFavoriteAuction(profile, this.auctionId)) {
            // We successfully added this auction to our favorites, we can now delete the button because we already added to our favorites
            MenuController.showAlertMessage("Successfully added auction to favorites!", AlertType.MESSAGE, 3000);

            paneAuctionContent.getChildren().remove(btnAddToFavorites);
        } else {
            MenuController.showAlertMessage("Could not add the auction to favorites!", AlertType.ERROR, 3000);
        }
    }

    private boolean amountIsHighEnough(final double bidAmount, final double minimumNeededAmount) {
        return bidAmount >= minimumNeededAmount;
    }

    private boolean currentUserIsCreatorOfThisAuction(final Auction auction) {
        return applicationManager.getCurrentUser().getId() == auction.getCreator().getProfileId();
    }

    public static String convertToEuro(final double amount) {
        final Locale dutch = new Locale("nl", "NL");
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(dutch);
        return decimalFormat.format(amount);
    }
}
