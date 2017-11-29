package core.javaFX.profile;

import core.javaFX.menu.MenuController;
import data.contexts.ProfileMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import logic.repositories.ProfileRepository;
import modelslibrary.Profile;
import utilities.enums.AlertType;
import utilities.enums.ProfileLoadingType;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ListedFeedbackController extends MenuController {

    @FXML private Label lblAuthorName, lblDateValue;
    @FXML private Text textAuctionDescription;

    private int authorId;
    private MenuController menuController;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) { }

    void setMenuController(final MenuController menuController) { this.menuController = menuController; }

    void setAuthorId(final int authorId) {
        this.authorId = authorId;
    }

    void setAuthor(final String author) {
        lblAuthorName.setText(author);
    }

    void setDate(final LocalDateTime date) {
        lblDateValue.setText(date.toLocalDate().toString());
    }

    void setDescription(final String description) {
        textAuctionDescription.setText(description);
    }

    public void goToAuthorProfile() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/core/javafx/profile/profile.fxml"));
            final Pane newLoadedPane = fxmlLoader.load();
            final ProfileController profileController = fxmlLoader.getController();
            final ProfileRepository profileRepository = new ProfileRepository(new ProfileMySqlContext());
            final Profile profile = profileRepository.getProfileForId(this.authorId, ProfileLoadingType.FOR_PROFILE_PAGE);

            profileController.setMenuController(this.menuController);
            profileController.loadProfile(profile);

            this.menuController.paneContent.getChildren().removeAll();
            this.menuController.paneContent.getChildren().add(newLoadedPane);
        } catch (SQLException | ClassNotFoundException | IOException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }
}
