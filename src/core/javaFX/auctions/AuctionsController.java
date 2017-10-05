package core.javaFX.auctions;


import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuctionsController extends MenuController {

    @FXML private JFXTextField txtSearchBar;
    @FXML private VBox vboxListedAuctions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void searchAuction(ActionEvent actionEvent) throws IOException {
        vboxListedAuctions.getChildren().clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/javaFX/auctions/listedAuction.fxml"));
        Pane pane = loader.load();
        ListedAuctionController controller = loader.getController();

        controller.setTitle(String.valueOf("Title of item"));
        controller.setDescription("Description of item");

        vboxListedAuctions.getChildren().add(pane);
    }
}
