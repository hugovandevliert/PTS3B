package core.javaFX.login;

import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends MenuController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void login() throws IOException {
        paneContent.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/profile/profile.fxml"));
        paneContent.getChildren().add(newLoadedPane);
    }
}
