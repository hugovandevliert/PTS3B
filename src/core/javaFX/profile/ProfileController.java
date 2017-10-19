package core.javaFX.profile;

import core.javaFX.auctions.ListedAuctionController;
import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Auction;
import models.Feedback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController extends MenuController {

    @FXML private ImageView imgviewProfilePicture, imgviewPositiveIcon, imgviewNegativeIcon;
    @FXML private Label lblName, lblUserSince, lblPositiveFeedbacksCount, lblNegativeFeedbacksCount;
    @FXML private VBox vboxListedAuctions, vboxListedFeedbacks;

    private FXMLLoader fxmlLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) { setIcons(); }

    private void setIcons() {
        final Image positiveFeedbackIcon = new Image( "/utilities/images/feedback/positive_feedback_icon.png");
        final Image negativeFeedbackIcon = new Image( "/utilities/images/feedback/negative_feedback_icon.png");

        imgviewPositiveIcon.setImage(positiveFeedbackIcon);
        imgviewNegativeIcon.setImage(negativeFeedbackIcon);
    }

    public void setProfilePicture(final Image image) {
        if (image != null){
            imgviewProfilePicture.setImage(image);
        }else{
            final Image placeholderImage = new Image("file:" +  new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(), 275, 196, false, false);
            imgviewProfilePicture.setImage(placeholderImage);
        }
    }

    public void setName(final String name) {
        this.lblName.setText(name);
    }

    public void setUserSince(final LocalDateTime registerDate) {
        lblUserSince.setText("User since " + registerDate.toLocalDate().toString());
    }

    public void setFeedbackCounts(final List<Feedback> feedbacks) {
        lblPositiveFeedbacksCount.setText(String.valueOf(getPositiveFeedbackCount(feedbacks)));
        lblNegativeFeedbacksCount.setText(String.valueOf(getNegativeFeedbackCount(feedbacks)));
    }

    public void setAuctions(final List<Auction> auctions) throws IOException {
        if (auctions.size() > 0) {
            for (final Auction auction : auctions) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/auctions/listedAuction.fxml"));
                Pane listedAuctionPane = fxmlLoader.load();
                final ListedAuctionController listedAuctionController = fxmlLoader.getController();

                listedAuctionController.setMenuController(this);
                listedAuctionController.setTitle(auction.getTitle());
                listedAuctionController.setDescription(auction.getDescription());
                listedAuctionController.setCurrentOffer(auction.getStartBid());
                listedAuctionController.setAuctionId(auction.getId());

                Image image = new Image("file:" + new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(), 200, 150, false, false);

                if (auction.getImages().size() > 0) {
                    final Image img = auction.getImages().get(0);

                    if (img != null) image = img;
                }
                listedAuctionController.setImage(image);

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

    public void setFeedbacks(final List<Feedback> feedbacks) throws IOException {
        if (feedbacks.size() > 0) {
            for (final Feedback feedback : feedbacks) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/profile/listedFeedback.fxml"));
                Pane listedFeedbackPane = fxmlLoader.load();
                final ListedFeedbackController listedFeedbackController = fxmlLoader.getController();

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
}
