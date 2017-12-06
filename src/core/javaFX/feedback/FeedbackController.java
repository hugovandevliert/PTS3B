package core.javaFX.feedback;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class FeedbackController {

    @FXML private Label lblUsername;
    @FXML private ComboBox cboxAuction;

    public void addFeedback() {
    }

    public void setUserName(String username) {
        lblUsername.setText("Add feedback on: " + username);
    }

    public void addAuctionNames(String[] names) {
        cboxAuction.getItems().addAll(names);
    }
}
