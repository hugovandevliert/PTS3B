package core.javaFX.register;

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

public class RegisterController extends MenuController {

    @FXML private JFXTextField txtName;
    @FXML private JFXTextField txtEmail;
    @FXML private JFXTextField txtEmailValidate;
    @FXML private JFXTextField txtUsername;
    @FXML private JFXPasswordField txtPassword;
    @FXML private JFXPasswordField txtPasswordValidate;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) { }

    public void register() throws InterruptedException, IOException {
        if (!txtPasswordValidate.getText().equals(txtPassword.getText())) {
            MenuController.showAlertMessage("The passwords do not match", AlertType.WARNING, 3000);

            txtPassword.setText("");
            txtPasswordValidate.setText("");
            return;
        }
        if (!txtEmailValidate.getText().equals(txtEmail.getText())) {
            MenuController.showAlertMessage("The emails do not match", AlertType.WARNING, 3000);
            return;
        }

        try {
            if (applicationManager.registerUser(txtUsername.getText(), txtPassword.getText(), txtEmail.getText(), txtName.getText())) {
                applicationManager.login(txtUsername.getText(), txtPassword.getText());
                if (applicationManager.isLoggedIn()) {
                    paneContent.getChildren().clear();
                    final Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javaFX/profile/profile.fxml"));
                    paneContent.getChildren().add(newLoadedPane);
                } else {
                    login();
                }
            } else {
                MenuController.showAlertMessage("Something went wrong, please try again!", AlertType.WARNING, 3000);
            }
        } catch (IllegalArgumentException exception){
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        } catch (SQLException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        } catch (ClassNotFoundException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }

    }

    public void login() throws IOException {
        paneContent.getChildren().clear();
        final Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/login/login.fxml"));
        paneContent.getChildren().add(newLoadedPane);
    }
}
