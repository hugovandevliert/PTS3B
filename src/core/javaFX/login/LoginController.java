package core.javaFX.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import core.ApplicationManager;
import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController extends MenuController {

    @FXML private JFXTextField txtUsername;
    @FXML private JFXPasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void login() throws IOException {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            System.out.println("VOER FF ALLES IN JONGE");
            return;
        }

        applicationManager.login(txtUsername.getText(), txtPassword.getText());

        if (applicationManager.isLoggedIn()) {
            paneContent.getChildren().clear();
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/profile/profile.fxml"));
            paneContent.getChildren().add(newLoadedPane);
        }
        else {
            System.out.println("CAN NOT LOG IN");
        }
    }
}
