package core.javaFX.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import utilities.enums.AlertType;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends MenuController {

    @FXML private JFXTextField txtUsername;
    @FXML private JFXPasswordField txtPassword;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) { }

    public void login() throws IOException {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            MenuController.showAlertMessage("Please fill in both a username and a password!", AlertType.WARNING, 3000);
            return;
        }

        try {
            if (!applicationManager.login(txtUsername.getText(), txtPassword.getText())) {
                MenuController.showAlertMessage("Username or password incorrect. Please try again.", AlertType.WARNING, 3000);
                return;
            }
        } catch (SQLException exception) {
            MenuController.showAlertMessage("Could not connect to our server. Error: " + exception.getMessage(), AlertType.ERROR, 3000);
            return;
        } catch (Exception exception) {
            MenuController.showAlertMessage("Something went wrong. Error: " + exception.getMessage(), AlertType.ERROR, 3000);
            return;
        }

        paneContent.getChildren().clear();
        final Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/auctions/auctions.fxml"));
        paneContent.getChildren().add(newLoadedPane);

        MenuController.showAlertMessage("Log in successful!", AlertType.MESSAGE, 3000);
    }

    public void register() throws IOException {
        paneContent.getChildren().clear();
        final Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/register/register.fxml"));
        paneContent.getChildren().add(newLoadedPane);
    }
}
