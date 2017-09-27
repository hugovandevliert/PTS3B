package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Open login
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}