package core.javaFX.login;

import core.javaFX.MenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;

public class LoginController extends MenuController {

    @FXML
    void initialize() {
        setIcons();

        //After login display auctions menu.
        selectedMenu = imgviewAuctions;
        imgviewAuctions.setImage(auctionsIconHovered);
    }

    public void login() throws IOException {
        File resourcesDirectory = new File("src/core/javaFX/login/login.fxml");


        Pane pane = FXMLLoader.load(getClass().getResource(String.valueOf(resourcesDirectory.toURI().toURL())));
    }
}
