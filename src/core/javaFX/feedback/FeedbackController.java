package core.javaFX.feedback;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import modelslibrary.Auction;

import java.util.ArrayList;

public class FeedbackController {

    @FXML private Label lblUsername;
    @FXML private ComboBox cboxAuction;

    public void addFeedback() { }

    public void setFeedbackIcons() {
        final Image positiveFeedbackIcon = new Image( "/utilities/images/feedback/positive_feedback_icon.png");
        final Image negativeFeedbackIcon = new Image( "/utilities/images/feedback/negative_feedback_icon.png");

        //imgviewPositiveIcon.setImage(positiveFeedbackIcon);
        //imgviewNegativeIcon.setImage(negativeFeedbackIcon);
    }

    public void setUserName(final String username) {
        lblUsername.setText("Add feedback on: " + username);
    }

    public void addAuctionNames(final ArrayList<Auction> auctions) {
        for (final Auction auction : auctions){
            cboxAuction.getItems().add(auction);
        }
    }
}
