package core.javaFX.feedback;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import modelslibrary.Auction;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class FeedbackController {

    @FXML private Pane paneContent;
    @FXML private Label lblUsername;
    @FXML private ComboBox cboxAuction;
    @FXML private JFXTextArea txtDescription;
    @FXML private ImageView imgviewPositiveIcon;
    @FXML private ImageView imgviewNegativeIcon;
    @FXML private Rectangle rectSelectedIcon;

    public void addFeedback() {
        if (rectSelectedIcon == null) {
            //TODO: add alert
            return;
        }else if (isPositiveFeedback(rectSelectedIcon)) {
            //positive review
        }else{
            //negative review
        }
    }

    public void setFeedbackIcons() {
        final Image positiveFeedbackIcon = new Image( "/utilities/images/feedback/positive_feedback_icon.png");
        final Image negativeFeedbackIcon = new Image( "/utilities/images/feedback/negative_feedback_icon.png");

        imgviewPositiveIcon.setImage(positiveFeedbackIcon);
        imgviewNegativeIcon.setImage(negativeFeedbackIcon);
    }

    public void setUserName(final String username) {
        lblUsername.setText("Add feedback on: " + username);
    }

    public void addAuctionNames(final ArrayList<Auction> auctions) {
        for (final Auction auction : auctions){
            cboxAuction.getItems().add(auction);
        }
    }

    public void selectIcon(MouseEvent event) {
        if (paneContent.getChildren().contains(event.getSource())) {
            paneContent.getChildren().remove(rectSelectedIcon);
        }
        if (event.getSource().equals(imgviewPositiveIcon)) {
            rectSelectedIcon = new Rectangle(341, 310, 100, 100);
        }else{
            rectSelectedIcon = new Rectangle(553, 310, 100, 100);
        }

        rectSelectedIcon.setStroke(Color.valueOf("a6b5c9"));
        rectSelectedIcon.setFill(null);
        rectSelectedIcon.setStrokeWidth(3);
        rectSelectedIcon.setArcWidth(5);
        rectSelectedIcon.setArcHeight(5);
        paneContent.getChildren().add(rectSelectedIcon);
    }

    private boolean isPositiveFeedback(final Rectangle rectSelectedIcon) {
        return rectSelectedIcon.getX() == 341;
    }
}
