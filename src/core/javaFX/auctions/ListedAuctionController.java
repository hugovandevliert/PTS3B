package core.javaFX.auctions;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ListedAuctionController {

    @FXML private Label lblAuctionTitle;
    @FXML private Text textAuctionDescription;

    public void setDescription(String description) {
        textAuctionDescription.setText(description);
    }

    public void setTitle(String title) {
        lblAuctionTitle.setText(title);
    }
}
