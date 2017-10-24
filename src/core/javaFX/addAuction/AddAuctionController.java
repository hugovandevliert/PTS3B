package core.javaFX.addAuction;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import utilities.enums.AlertType;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAuctionController extends MenuController {

    @FXML private ImageView imgviewPicture1,imgviewPicture2, imgviewPicture3;
    @FXML private JFXTextField txtTitle;
    @FXML private JFXTextArea txtDescription;
    @FXML private JFXDatePicker datepicker_Expiration, datepicker_Opening;

    private File[] images;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        images = new File[3];
        setImages();
    }

    public void addImage(final MouseEvent mouseEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add an Image");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.gif", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);
        final File selectedImage = fileChooser.showOpenDialog(txtTitle.getScene().getWindow());

        if (selectedImage != null) {
            ImageView source = (ImageView) mouseEvent.getSource();
            if (source == imgviewPicture1) {
                images[0] = selectedImage;
                imgviewPicture1.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 275, 206, false, false));
            } else if (source == imgviewPicture2) {
                images[1] = selectedImage;
                imgviewPicture2.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 275, 206, false, false));
            } else if (source == imgviewPicture3) {
                images[2] = selectedImage;
                imgviewPicture3.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 275, 206, false, false));
            }
        }
    }

    public void createAuction() {
        try {
            final boolean successful = applicationManager.currentUser.getProfile().addAuction(
                    1,
                    1,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusSeconds(1),
                    false,
                    txtTitle.getText(),
                    "Description",
                    getImages(this.images)
                    );

            if (successful) MenuController.showAlertMessage("Successfully added auction!", AlertType.MESSAGE, 3000);
            else MenuController.showAlertMessage("Your auction could not be added - Please try again!", AlertType.ERROR, 3000);
        } catch (IllegalArgumentException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        } catch (SQLException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void setImages() {
        final Image placeholderImage = new Image("file:" +  new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(),
                275, 206, false, true);

        imgviewPicture1.setImage(placeholderImage);
        imgviewPicture2.setImage(placeholderImage);
        imgviewPicture3.setImage(placeholderImage);
    }

    private ArrayList<File> getImages(final File... files) {
        ArrayList<File> images = new ArrayList<>();

        for (final File file : files){
            if (file != null) images.add(file);
        }
        return images;
    }

    private boolean validate(final String text) {
        return text.matches("[0-9]*");
    }
}
