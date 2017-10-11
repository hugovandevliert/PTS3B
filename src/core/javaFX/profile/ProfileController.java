package core.javaFX.profile;

import core.UserAlert;
import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import utilities.enums.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends MenuController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(paneAlert);
    }
}
