package core.javaFX.addAuction;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import core.javaFX.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAuctionController extends MenuController {

    @FXML private ImageView imgviewPicture1;
    @FXML private ImageView imgviewPicture2;
    @FXML private ImageView imgviewPicture3;
    @FXML private JFXTextField txtTitle;
    @FXML private JFXTextArea txtDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImages();
    }

    public void addImage(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add an Image");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.gif", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);
        File selectedImage = fileChooser.showOpenDialog(txtTitle.getScene().getWindow());

        if (selectedImage != null) {
            ImageView source = (ImageView) mouseEvent.getSource();
            if (source == imgviewPicture1) {
                imgviewPicture1.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 275, 206, false, false));
            } else if (source == imgviewPicture2) {
                imgviewPicture2.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 275, 206, false, false));
            } else if (source == imgviewPicture3) {
                imgviewPicture3.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 275, 206, false, false));
            }
        }
    }

    public void createAuction() {

    }

    private void setImages() {
        final Image placeholderImage = new Image("file:" +  new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(),
                275, 206, false, false);
        imgviewPicture1.setImage(placeholderImage);
        imgviewPicture2.setImage(placeholderImage);
        imgviewPicture3.setImage(placeholderImage);
    }

    private boolean validate(String text) {
        return text.matches("[0-9]*");
    }
}
