package core.javaFX.profile;

import core.javaFX.auctions.ListedAuctionController;
import core.javaFX.feedback.FeedbackController;
import core.javaFX.feedback.ListedFeedbackController;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import data.contexts.UserMySqlContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import logic.repositories.AuctionRepository;
import logic.repositories.UserRepository;
import modelslibrary.Auction;
import modelslibrary.Feedback;
import modelslibrary.Profile;
import modelslibrary.User;
import utilities.enums.AlertType;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController extends MenuController {

    @FXML private ImageView imgviewProfilePicture, imgviewPositiveIcon, imgviewNegativeIcon;
    @FXML private Label lblName, lblUserSince, lblPositiveFeedbacksCount, lblNegativeFeedbacksCount;
    @FXML private VBox vboxListedAuctions, vboxListedFeedbacks;

    private FXMLLoader fxmlLoader;
    private Profile profile;
    private MenuController menuController;
    private UserRepository userRepository;
    private AuctionRepository auctionRepository;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.userRepository = new UserRepository(new UserMySqlContext());
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());

        setFeedbackIcons();
    }

    private void setFeedbackIcons() {
        final Image positiveFeedbackIcon = new Image( "/utilities/images/feedback/positive_feedback_icon.png");
        final Image negativeFeedbackIcon = new Image( "/utilities/images/feedback/negative_feedback_icon.png");

        imgviewPositiveIcon.setImage(positiveFeedbackIcon);
        imgviewNegativeIcon.setImage(negativeFeedbackIcon);
    }

    public void setMenuController(final MenuController menuController) { this.menuController = menuController; }

    public void loadProfile(final Profile profile) throws IOException {
        setProfileVariable(profile);
        setProfilePicture(profile.getPhoto());
        setName(profile.getUsername());
        setUserSince(profile.getCreationDate());
        setFeedbackCounts(profile.getFeedbacks());
        setAuctions(profile.getAuctions());
        setFeedbacks(profile.getFeedbacks());
    }

    private void setProfileVariable(final Profile profile) { this.profile = profile; }

    private void setProfilePicture(final Image image) {
        if (image != null){
            imgviewProfilePicture.setImage(image);
        }else{
            final Image placeholderImage = new Image("file:" +  new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(),
                    275, 206, true, false);
            imgviewProfilePicture.setImage(placeholderImage);
        }
    }

    private void setName(final String name) {
        this.lblName.setText(name);
    }

    private void setUserSince(final LocalDateTime registerDate) {
        lblUserSince.setText("User since " + registerDate.toLocalDate().toString());
    }

    private void setFeedbackCounts(final List<Feedback> feedbacks) {
        lblPositiveFeedbacksCount.setText(String.valueOf(getPositiveFeedbackCount(feedbacks)));
        lblNegativeFeedbacksCount.setText(String.valueOf(getNegativeFeedbackCount(feedbacks)));
    }

    private void setAuctions(final List<Auction> auctions) throws IOException {
        if (!auctions.isEmpty()) {
            for (final Auction auction : auctions) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/auctions/listedAuction.fxml"));
                final Pane listedAuctionPane = fxmlLoader.load();
                final ListedAuctionController listedAuctionController = fxmlLoader.getController();

                listedAuctionController.setMenuController(this);
                listedAuctionController.setListedAuction(auction);

                vboxListedAuctions.getChildren().add(listedAuctionPane);
            }
        }else{
            final Label lblNoAuctions = new Label();
            lblNoAuctions.setText("This user has no active auctions!");
            lblNoAuctions.setTextFill(Color.web("#747e8c"));
            lblNoAuctions.setFont(new Font("System", 17));

            vboxListedAuctions.getChildren().add(lblNoAuctions);
        }
    }

    private void setFeedbacks(final List<Feedback> feedbacks) throws IOException {
        if (!feedbacks.isEmpty()) {
            for (final Feedback feedback : feedbacks) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/feedback/listedFeedback.fxml"));
                final Pane listedFeedbackPane = fxmlLoader.load();
                final ListedFeedbackController listedFeedbackController = fxmlLoader.getController();

                listedFeedbackController.setMenuController(this.menuController);
                listedFeedbackController.setAuthorId(feedback.getAuthor().getProfileId());
                listedFeedbackController.setAuthor(feedback.getAuthor().getUsername());
                listedFeedbackController.setDate(feedback.getDate());
                listedFeedbackController.setDescription(feedback.getMessage());

                vboxListedFeedbacks.getChildren().add(listedFeedbackPane);
            }
        }else{
            final Label lblNoFeedbacks = new Label();
            lblNoFeedbacks.setText("This user has received no feedbacks yet!");
            lblNoFeedbacks.setTextFill(Color.web("#747e8c"));
            lblNoFeedbacks.setFont(new Font("System", 17));

            vboxListedFeedbacks.getChildren().add(lblNoFeedbacks);
        }
    }

    public void changeProfilePicture() {
        if (profileIsFromLoggedInUser()){
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose your image");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.gif", "*.png", "*.jpeg");
            fileChooser.getExtensionFilters().add(filter);
            final File selectedImage = fileChooser.showOpenDialog(null);

            if (selectedImage != null) {
                final User currentUser = MenuController.applicationManager.getCurrentUser();
                final boolean successful = userRepository.setPhoto(currentUser, selectedImage);

                if (successful){
                    MenuController.showAlertMessage("Your profile picture has successfully been changed!", AlertType.MESSAGE, 3000);

                    final Image image = new Image("file:" +  selectedImage.getAbsolutePath(), 275, 196, true, false);
                    setProfilePicture(image);
                }else{
                    MenuController.showAlertMessage("Uploading the selected image failed - please try again!", AlertType.ERROR, 3000);
                }
            }else{
                MenuController.showAlertMessage("You did not select an image!", AlertType.WARNING, 3000);
            }
        }else{
            MenuController.showAlertMessage("You can't change this picture - It is not your profile!", AlertType.WARNING, 3000);
        }
    }

    public void logout() {
        MenuController.applicationManager.logout();

        this.menuController.paneContent.getChildren().clear();
        try {
            final Pane newContentPane = FXMLLoader.load(getClass().getResource("/core/javafx/login/login.fxml"));
            this.menuController.paneContent.getChildren().add(newContentPane);

            MenuController.showAlertMessage("Successfully logged out!", AlertType.MESSAGE, 3000);
        } catch (IOException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private boolean profileIsFromLoggedInUser() {
        return MenuController.applicationManager.getCurrentUser().getProfile().getProfileId() == this.profile.getProfileId();
    }

    private int getNegativeFeedbackCount(final List<Feedback> feedbacks) {
        int counter = 0;

        for (final Feedback feedback : feedbacks){
            if (!feedback.isPositive()) counter++;
        }
        return counter;
    }

    private int getPositiveFeedbackCount(final List<Feedback> feedbacks) {
        int counter = 0;

        for (final Feedback feedback : feedbacks){
            if (feedback.isPositive()) counter++;
        }
        return counter;
    }

    public void addFeedback() throws IOException {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/feedback/feedback.fxml"));
            final Pane feedbackPane = fxmlLoader.load();
            final FeedbackController feedbackController = fxmlLoader.getController();

            feedbackController.setFeedbackIcons();
            feedbackController.setMenuController(this.menuController);
            feedbackController.setProfile(profile);
            feedbackController.setAuthor(applicationManager.getCurrentUser().getId());
            feedbackController.setUserName(profile.getUsername());
            feedbackController.addAuctionNames(auctionRepository.getWonAuctionsWithoutFeedbackForProfile(profile.getProfileId(), applicationManager.getCurrentUser().getId()));
            paneContent.getChildren().clear();
            paneContent.getChildren().add(feedbackPane);
        } catch (SQLException exception) {
            exception.printStackTrace();
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }
}
