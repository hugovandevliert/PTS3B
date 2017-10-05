package core.javaFX.auctions;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ListedAuctionController {

    @FXML private Label lblAuctionTitle, lblCurrentOffer;
    @FXML private Text textAuctionDescription;

    public void setTitle(String title) {
        lblAuctionTitle.setText(title);
    }

    public void setDescription(String description) { textAuctionDescription.setText(description); }

    public void setCurrentOffer(double offer) { lblCurrentOffer.setText(String.valueOf(offer)); }
}
