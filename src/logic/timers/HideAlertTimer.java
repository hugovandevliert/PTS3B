package logic.timers;

import core.javaFX.useralert.UserAlertController;
import javafx.application.Platform;

import java.util.TimerTask;

public class HideAlertTimer extends TimerTask {

    private final UserAlertController userAlertController;
    private final String alertMessage;

    public HideAlertTimer(final UserAlertController userAlertController, final String alertMessage) {
        this.userAlertController = userAlertController;
        this.alertMessage = alertMessage;
    }

    public void run() {
        if (alertMessageHasNotChanged()) Platform.runLater(userAlertController::hideAlert);
        else cancel();
    }

    private boolean alertMessageHasNotChanged() {
        return userAlertController.getCurrentAlertMessage().equals(this.alertMessage) || userAlertController.getCurrentAlertMessage().equals("");
    }
}
