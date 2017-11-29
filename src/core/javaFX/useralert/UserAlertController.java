package core.javaFX.useralert;

import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
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

    @Override
    public void initialize(final URL location, final ResourceBundle resources) { }

    public void setMessage(final String message, final AlertType alertType, final int delay) {
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

        final Timer timer = new Timer();
        final HideAlertTimer hideAlertTimer = new HideAlertTimer(this, message);
        timer.schedule(hideAlertTimer, delay);
    }

    public void hideAlert() {
        paneBackgroundColor.setStyle("-fx-background-color: #3d4857; -fx-background-radius: 2;");
        lblMessage.setText("");
    }

    public String getCurrentAlertMessage() {
        return lblMessage.getText();
    }
}
