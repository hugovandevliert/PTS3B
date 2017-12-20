package core.javafx.useralert;

import core.javafx.menu.MenuController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import logic.timers.HideAlertTimer;
import utilities.enums.AlertType;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class UserAlertController extends MenuController {

    @FXML private Label lblMessage;
    @FXML private Pane paneBackgroundColor;

    private Timer timer;
    private HideAlertTimer hideAlertTimer;
    private boolean isClickable;
    private Timeline timelineAlertDown;
    private Timeline timelineAlertUp;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        //Should not run the Super method again because fields will be NULL
    }

    public void setMessage(final String message, final AlertType alertType) {
        this.isClickable = true;
        initializeAlertMessage(message, alertType, -1);
        paneBackgroundColor.setCursor(Cursor.HAND);
        lblMessage.setCursor(Cursor.HAND);
    }

    public void setMessage(final String message, final AlertType alertType, final int delay) {
        initializeAlertMessage(message, alertType, delay);
    }

    private void initializeAlertMessage(final String message, final AlertType alertType, final int delay) {
        switch (alertType){
            case MESSAGE:
                paneBackgroundColor.setStyle("-fx-background-color: rgba(93, 116, 78, 1); -fx-background-radius: 2;");
                break;
            case ERROR:
                paneBackgroundColor.setStyle("-fx-background-color: rgba(132, 89, 89, 1); -fx-background-radius: 2;");
                break;
            case WARNING:
                paneBackgroundColor.setStyle("-fx-background-color: #F4A257; -fx-background-radius: 2;");
                break;
            default:
                paneBackgroundColor.setStyle("-fx-background-color: #F4A257; -fx-background-radius: 2;");
                break;
        }

        lblMessage.setText(message);

        if (!this.isClickable){
            timer = new Timer();
            hideAlertTimer = new HideAlertTimer(this, message);
            timer.schedule(hideAlertTimer, delay);
        }
    }

    public void hideAlert() {
        timer.cancel();
        hideAlertTimer.cancel();

        paneBackgroundColor.setStyle("-fx-background-color: #3d4857; -fx-background-radius: 2;");
        lblMessage.setText("");
    }

    public void clickedOnAlert() {
        if (this.isClickable) hideAlert();
    }

    public String getCurrentAlertMessage() {
        return lblMessage.getText();
    }

    public void setAlertAnimation(Pane paneAlert){
        // Initial position setting for Pane
        Rectangle2D boxBounds = new Rectangle2D(0, 0, 1000, 50);
        Rectangle clipRect = new Rectangle();
        clipRect.setHeight(0);
        clipRect.setWidth(boxBounds.getWidth());
        clipRect.translateYProperty().set(boxBounds.getHeight());
        paneAlert.setClip(clipRect);
        paneAlert.translateYProperty().set(-boxBounds.getHeight());

        // Animation for bouncing effect
        final Timeline timelineMenuBounce = new Timeline();
        timelineMenuBounce.setCycleCount(2);
        timelineMenuBounce.setAutoReverse(true);
        final KeyValue kvb1 = new KeyValue(clipRect.heightProperty(), boxBounds.getHeight() - 15);
        final KeyValue kvb2 = new KeyValue(clipRect.translateYProperty(), 15);
        final KeyValue kvb3 = new KeyValue(paneAlert.translateYProperty(), -15);
        final KeyFrame kfb = new KeyFrame(Duration.millis(100), kvb1, kvb2, kvb3);
        timelineMenuBounce.getKeyFrames().add(kfb);

        // Event handler to call bouncing effect after scrolling is finished
        EventHandler<ActionEvent> onFinished = t -> timelineMenuBounce.play();

        timelineAlertDown = new Timeline();
        timelineAlertUp = new Timeline();

        // Animation for scroll down
        timelineAlertDown.setCycleCount(1);
        timelineAlertDown.setAutoReverse(true);
        final KeyValue kvDwn1 = new KeyValue(clipRect.heightProperty(), boxBounds.getHeight());
        final KeyValue kvDwn2 = new KeyValue(clipRect.translateYProperty(), 0);
        final KeyValue kvDwn3 = new KeyValue(paneAlert.translateYProperty(), 0);
        final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), onFinished, kvDwn1, kvDwn2, kvDwn3);
        timelineAlertDown.getKeyFrames().add(kfDwn);

        // Event handler to remove pane from canvas after it's finished going up
        EventHandler<ActionEvent> onFinishedUp = t ->
                ((AnchorPane) paneAlert.getParent()).getChildren().remove(paneAlert);

        // Animation for scroll up
        timelineAlertUp.setCycleCount(1);
        timelineAlertUp.setAutoReverse(true);
        final KeyValue kvUp1 = new KeyValue(clipRect.heightProperty(), 0);
        final KeyValue kvUp2 = new KeyValue(clipRect.translateYProperty(), boxBounds.getHeight());
        final KeyValue kvUp3 = new KeyValue(paneAlert.translateYProperty(), - boxBounds.getHeight());
        final KeyFrame kfUp = new KeyFrame(Duration.millis(200), onFinishedUp, kvUp1, kvUp2, kvUp3);
        timelineAlertUp.getKeyFrames().add(kfUp);
    }
}
