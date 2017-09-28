package core.javaFX.login;

import core.javaFX.MenuController;
import javafx.fxml.FXML;

public class LoginController extends MenuController {

    @FXML
    void initialize() {
        setIcons();

        //After login display auctions menu.
        selectedMenu = imgviewAuctions;
        imgviewAuctions.setImage(auctionsIconHovered);
    }
}
