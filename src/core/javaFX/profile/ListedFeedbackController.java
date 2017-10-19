package core.javaFX.profile;

import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ListedFeedbackController extends MenuController {

    @FXML private Label lblAuthorName, lblDateValue;
    @FXML private Text textAuctionDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void setAuthor(final String author) {
        lblAuthorName.setText(author);
    }

    public void setDate(final LocalDateTime date) {
        lblDateValue.setText(date.toLocalDate().toString());
    }

    public void setDescription(final String description) {
        textAuctionDescription.setText(description);
    }
}
