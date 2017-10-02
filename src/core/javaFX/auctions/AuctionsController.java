package core.javaFX.auctions;

import core.javaFX.MenuController;
import javafx.fxml.FXML;

public class AuctionsController extends MenuController {

    @FXML
    void initialize() {
        setIcons();

        selectedMenu = imgviewAuctions;
        imgviewAuctions.setImage(auctionsIconHovered);
    }
}
