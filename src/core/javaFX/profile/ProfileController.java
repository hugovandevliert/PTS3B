package core.javaFX.profile;

import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Feedback;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController extends MenuController {

    @FXML private ImageView imgviewProfilePicture, imgviewPositiveIcon, imgviewNegativeIcon;
    @FXML private Label lblName, lblUserSince, lblPositiveFeedbacksCount, lblNegativeFeedbacksCount;

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
