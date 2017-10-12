package core.javaFX.addAuction;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAuctionController extends MenuController {

    @FXML private JFXTextArea txtDescription;
    @FXML private JFXTextField txtTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void createAuction() {

    }

    private boolean validate(String text)
    {
        return text.matches("[0-9]*");
    }
}
