package core;


import core.javaFX.menu.MenuController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utilities.enums.AlertType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class UserAlert {

    private String message;
    private AlertType alertType;

    public Pane ShowMessage(String message, AlertType alertType, Pane pane, Label label, MenuController menuController) throws InterruptedException {
        switch (alertType){
            case Message:
                pane.setStyle("-fx-background-color: rgba(93, 116, 78, 1); -fx-background-radius: 2;");
                break;
            case Error:
                pane.setStyle("-fx-background-color: rgba(132, 89, 89, 1); -fx-background-radius: 2;");
                break;
            case Warning:
                pane.setStyle("-fx-background-color: rgba(121, 128, 86, 1); -fx-background-radius: 2;");
                break;
        }
        label.setText(message);
        label.setTextFill(Color.web("#FFFFFF"));
        label.setFont(new Font("Segoe UI Light", 20));

        TimerTask clearUserAlert = new ClearUserAlert(label, pane, menuController);

        Timer timer = new Timer();
        timer.schedule(clearUserAlert, 5000);



        return pane;
    }
}
