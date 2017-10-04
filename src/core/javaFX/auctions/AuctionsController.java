package core.javaFX.auctions;


import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class AuctionsController extends MenuController {

    @FXML private JFXTextField txtSearchBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void searchAuction(ActionEvent actionEvent) {
    }
}
