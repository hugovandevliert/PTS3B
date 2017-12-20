package core.javaFX.useralert;

import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
}
