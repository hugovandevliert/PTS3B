package core.javaFX.feedback;

import core.javaFX.menu.MenuController;
import core.javaFX.profile.ProfileController;
import data.contexts.FeedbackMySqlContext;
import data.contexts.ProfileMySqlContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.repositories.FeedbackRepository;
import logic.repositories.ProfileRepository;
import modelslibrary.Auction;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelslibrary.Profile;
import utilities.enums.AlertType;
import utilities.enums.ProfileLoadingType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeedbackController {

    @FXML private Pane paneContent;
    @FXML private Label lblUsername;
    @FXML private ComboBox cboxAuction;
    @FXML private JFXTextArea txtDescription;
    @FXML private ImageView imgviewPositiveIcon;
    @FXML private ImageView imgviewNegativeIcon;
    @FXML private Rectangle rectSelectedIcon;

    private Profile profile;
    private int authorId;
    private MenuController menuController;

    public void addFeedback() {
        final Object selectedItem = cboxAuction.getValue();

        if (selectedItem != null){
            final Auction auction = (Auction) selectedItem;

            if (auction != null){
                if (rectSelectedIcon == null) {
                    MenuController.showAlertMessage("Please select whether this is a positive or negative feedback!", AlertType.WARNING, 3000);
                    return;
                }
                else if (txtDescription.getText().isEmpty()){
                    MenuController.showAlertMessage("Please write some actual feedback!", AlertType.WARNING, 3000);
                }else{
                    final FeedbackRepository feedbackRepository = new FeedbackRepository(new FeedbackMySqlContext());
                    final boolean isPositive = isPositiveFeedback(rectSelectedIcon);
                    final String message = txtDescription.getText().trim();

                    if (feedbackRepository.addFeedback(isPositive, message, this.profile.getProfileId(), this.authorId, auction)){
                        try {
                            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/profile/profile.fxml"));
                            final Pane newLoadedPane = fxmlLoader.load();
                            final ProfileController profileController = fxmlLoader.getController();
                            final ProfileRepository profileRepository = new ProfileRepository(new ProfileMySqlContext());
                            profile = profileRepository.getProfileForId(profile.getProfileId(), ProfileLoadingType.FOR_PROFILE_PAGE);

                            profileController.setMenuController(this.menuController);
                            profileController.loadProfile(profile);

                            this.menuController.paneContent.getChildren().removeAll();
                            this.menuController.paneContent.getChildren().add(newLoadedPane);

                            MenuController.showAlertMessage("Your feedback has successfully been added!", AlertType.MESSAGE, 3000);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
                        } catch (ClassNotFoundException exception) {
                            exception.printStackTrace();
                            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
                        }
                    }else{
                        MenuController.showAlertMessage("Something went wrong with adding the feedback. Please try again!", AlertType.ERROR, 3000);
                    }
                }
            }else{
                MenuController.showAlertMessage("Something went wrong with extracting the auction. Please try again!", AlertType.ERROR, 3000);
            }
        }else{
            MenuController.showAlertMessage("Please select an auction you'd like to leave this user a feedback for!", AlertType.WARNING, 3000);
        }
    }

    public void setFeedbackIcons() {
        final Image positiveFeedbackIcon = new Image( "/utilities/images/feedback/positive_feedback_icon.png");
        final Image negativeFeedbackIcon = new Image( "/utilities/images/feedback/negative_feedback_icon.png");

        imgviewPositiveIcon.setImage(positiveFeedbackIcon);
        imgviewNegativeIcon.setImage(negativeFeedbackIcon);
    }

    public void setMenuController(final MenuController menuController) { this.menuController = menuController; }

    public void setUserName(final String username) {
        lblUsername.setText("Add feedback on: " + username);
    }

    public void setProfile(final Profile profile) { this.profile = profile; }

    public void setAuthor(final int authorId) { this.authorId = authorId; }

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
