package core;


import core.javaFX.menu.MenuController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import utilities.enums.AlertType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class UserAlert {

//    private String message;
//    private AlertType alertType;

    public Pane showMessage(String message, AlertType alertType, Pane pane, Label label, MenuController menuController) throws InterruptedException {
        Pane tempPane = new Pane();
        tempPane.setPrefWidth(870);
        tempPane.setPrefHeight(45);

        Label tempLabel = new Label();
        tempLabel.setLayoutX(19.0);
        tempLabel.setLayoutY(-1.0);
        tempLabel.prefHeight(45.0);
        tempLabel.prefWidth(870);
        tempLabel.setTextAlignment(TextAlignment.LEFT);
        tempLabel.setContentDisplay(ContentDisplay.CENTER);
        tempLabel.setAlignment(Pos.CENTER_LEFT);
        System.out.println("message = [" + message + "], alertType = [" + alertType + "], pane = [" + pane + "], label = [" + label + "], menuController = [" + menuController + "]");
        switch (alertType){
            case Message:
                tempPane.setStyle("-fx-background-color: rgba(93, 116, 78, 1); -fx-background-radius: 2;");
                break;
            case Error:
                tempPane.setStyle("-fx-background-color: rgba(132, 89, 89, 1); -fx-background-radius: 2;");
                break;
            case Warning:
                tempPane.setStyle("-fx-background-color: rgba(121, 128, 86, 1); -fx-background-radius: 2;");
                break;
        }
        tempLabel.setText(message);
        tempLabel.setTextFill(Color.web("#FFFFFF"));
        tempLabel.setFont(new Font("Segoe UI Light", 20));

        TimerTask clearUserAlert = new ClearUserAlert(menuController);

        Timer timer = new Timer();
        timer.schedule(clearUserAlert, 5000);

        tempPane.getChildren().add(tempLabel);
        pane.getChildren().add(tempPane);

        return tempPane;
    }
}
