package core.javaFX.register;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import core.UserAlert;
import core.javaFX.menu.MenuController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void register() throws InterruptedException, IOException {
        if (!txtPasswordValidate.getText().equals(txtPassword.getText())) {
            //TODO: Actual client side validation feedback
            System.out.println("The passwords don't match");
            txtPassword.setText("");
            txtPasswordValidate.setText("");
            return;
        }
        if (!txtEmailValidate.getText().equals(txtEmail.getText())) {
            //TODO: Actual client side validation feedback
            System.out.println("The emails don't match");
            return;
        }

        try {
            if (applicationManager.registerUser(txtUsername.getText(), txtPassword.getText(), txtEmail.getText(), txtName.getText())) {
                applicationManager.login(txtUsername.getText(), txtPassword.getText());
                if (applicationManager.isLoggedIn()) {
                    paneContent.getChildren().clear();
                    Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/profile/profile.fxml"));
                    paneContent.getChildren().add(newLoadedPane);
                } else {
                    login();
                }
            } else {
                //TODO: Actual client side validation feedback
                System.out.println("Something went wrong...");
//            UserAlert userAlert = new UserAlert();
//            userAlert.showMessage("Something went wrong, please try again", AlertType.Error, paneAlert, lblAlertMessage, this);
            }
        } catch (IllegalArgumentException e){
            //TODO: Actual client side validation feedback
            System.out.println(e.getMessage());

//            UserAlert userAlert = new UserAlert();
//            userAlert.showMessage(ex.getMessage(), AlertType.Error, paneAlert, lblAlertMessage, this);
        } catch (SQLException ex) {
            //TODO: Actual client side validation feedback
            System.out.println(ex.getMessage());

//            UserAlert userAlert = new UserAlert();
//            userAlert.showMessage(ex.getMessage(), AlertType.Error, paneAlert, lblAlertMessage, this);
        }
    }

    public void login() throws IOException {
        paneContent.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/core/javafx/login/login.fxml"));
        paneContent.getChildren().add(newLoadedPane);
    }

//    public void ClearAlert() {
//        Platform.runLater(this::run);
//    }
//
//    private void run() {
//        paneAlert.setStyle("fx-background-color: rgba(61, 72, 87, 1); -fx-background-radius: 2");
//        lblAlertMessage.setText("");
//    }
}
