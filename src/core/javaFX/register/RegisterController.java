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
import java.util.ResourceBundle;

public class RegisterController extends MenuController {

    @FXML private JFXTextField txtName;
    @FXML private JFXTextField txtEmail;
    @FXML private JFXTextField txtEmailValidate;

    @FXML private JFXTextField txtUsername;
    @FXML private JFXPasswordField txtPasswordValidate;
    @FXML private JFXPasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void register() throws InterruptedException {

//        UserAlert userAlert = new UserAlert();
//        userAlert.showMessage("Logged in!", AlertType.Message, paneAlert, lblAlertMessage, this);
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
