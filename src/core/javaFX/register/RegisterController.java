package core.javaFX.register;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import utilities.enums.AlertType;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController extends MenuController {

    @FXML private JFXCheckBox checkPass;
    @FXML private JFXTextField txtName;
    @FXML private JFXTextField txtEmail;
    @FXML private JFXTextField txtEmailValidate;
    @FXML private JFXTextField txtUsername;
    @FXML private JFXPasswordField txtPassword;
    @FXML private JFXTextField txtPasswordVisible;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        //Should not run the Super method again because fields will be NULL
    }

    public void register() throws InterruptedException, IOException {
        if (!txtEmailValidate.getText().equals(txtEmail.getText())) {
            MenuController.showAlertMessage("The emails do not match", AlertType.WARNING, 3000);
            return;
        }

        try {
            if (applicationManager.registerUser(txtUsername.getText(), txtPassword.getText(), txtEmail.getText(), txtName.getText())) {
                applicationManager.login(txtUsername.getText(), txtPassword.getText());
                if (applicationManager.isLoggedIn()) {
                    paneContent.getChildren().clear();
                    final Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/auctions/auctions.fxml"));
                    paneContent.getChildren().add(newLoadedPane);
                } else {
                    login();
                }
            } else {
                MenuController.showAlertMessage("Something went wrong, please try again!", AlertType.ERROR, 3000);
            }
        } catch (SQLException exception) {
            MenuController.showAlertMessage("Could not connect to our server. Error: " + exception.getMessage(), AlertType.ERROR, 5000);
        } catch (Exception exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.WARNING, 5000);
        }
    }

    public void login() throws IOException {
        paneContent.getChildren().clear();
        final Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/login/login.fxml"));
        paneContent.getChildren().add(newLoadedPane);
    }

    public void checkChanged() {
        if(checkPass.isSelected()){
            txtPassword.setVisible(false);
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.requestFocus();
            txtPasswordVisible.deselect();
            txtPasswordVisible.positionCaret(txtPasswordVisible.getLength());
        }else{
            txtPassword.setVisible(true);
            txtPasswordVisible.setVisible(false);
            txtPassword.setText(txtPasswordVisible.getText());
            txtPassword.requestFocus();
            txtPassword.deselect();
            txtPassword.positionCaret(txtPassword.getLength());
        }
    }
}
