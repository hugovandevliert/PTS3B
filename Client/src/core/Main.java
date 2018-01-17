package core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset, yOffset;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        //Open login
        final Parent root = FXMLLoader.load(getClass().getResource("javafx/menu/menu.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        final Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        enableGUIMoving(root, primaryStage);

        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    private void enableGUIMoving(final Parent parent, final Stage stage) {
        // Change the offset for both X and Y
        // whenever the user clicks on our GUI we need to register that coordinate and save it
        parent.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Change the actual offset of the scene with the changed variables
        parent.setOnMouseDragged(event -> {
            if (yOffset < 50) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }
}