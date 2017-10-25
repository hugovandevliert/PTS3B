package core.javaFX.addAuction;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAuctionController extends MenuController {

    @FXML private ImageView imgviewPicture1,imgviewPicture2, imgviewPicture3, imgviewPicture4;
    @FXML private JFXTextField txtTitle;
    @FXML private JFXTextArea txtDescription;
    @FXML private JFXTextField txtStartTime;
    @FXML private JFXTextField txtStartDate;
    @FXML private JFXTextField txtStartPrice;
    @FXML private JFXTextField txtEndTime;
    @FXML private JFXTextField txtEndDate;
    @FXML private JFXTextField txtIncrementation;
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
                imgviewPicture1.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 225, 156, false, false));
            } else if (source == imgviewPicture2) {
                images[1] = selectedImage;
                imgviewPicture2.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 225, 156, false, false));
            } else if (source == imgviewPicture3) {
                images[2] = selectedImage;
                imgviewPicture3.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 225, 156, false, false));
            } else if (source == imgviewPicture4) {
                images[3] = selectedImage;
                imgviewPicture3.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 225, 156, false, false));
            }
        }
    }

    public void createAuction() {
        try {
            final String title = txtTitle.getText();
            final String description = txtDescription.getText();
            final LocalDateTime openingDate = LocalDateTime.of(convertToDate(txtStartDate.getText()), convertToTime(txtStartTime.getText()));
            final LocalDateTime expirationDate = LocalDateTime.of(convertToDate(txtEndDate.getText()), convertToTime(txtEndTime.getText()));
            final double startbid = convertToDouble(txtStartPrice.getText());
            final double inrementation = convertToDouble(txtIncrementation.getText());

            //TODO: add incrementation to addAuction method
            //TODO: add minimum bid to GUI
            final boolean successful = applicationManager.currentUser.getProfile().addAuction(startbid, 1, expirationDate,
                    openingDate,false, title, description, getImages(this.images));

            if (successful) MenuController.showAlertMessage("Auction added successfully!", AlertType.MESSAGE, 3000);
            else MenuController.showAlertMessage("Your auction could not be added - Please try again.", AlertType.ERROR, 3000);
        } catch (IllegalArgumentException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        } catch (SQLException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void setImages() {
        final Image placeholderImage = new Image("file:" +  new File("src/utilities/images/auction/no_image_available.png").getAbsolutePath(),
                225, 156, false, true);

        imgviewPicture1.setImage(placeholderImage);
        imgviewPicture2.setImage(placeholderImage);
        imgviewPicture3.setImage(placeholderImage);
        imgviewPicture4.setImage(placeholderImage);
    }

    private ArrayList<File> getImages(final File... files) {
        ArrayList<File> images = new ArrayList<>();

        for (final File file : files){
            if (file != null) images.add(file);
        }
        return images;
    }

    private LocalTime convertToTime(final String time) {
        if (time.matches( "([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            return LocalTime.parse(time);
        } else {
            throw new IllegalArgumentException("Please enter a valid time");
        }
    }

    private LocalDate convertToDate(final String date) {
         if (date.matches("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|102]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))" +
                    "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|" +
                    "[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])" +
                    "|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")) {
             return LocalDate.parse(date);
        } else {
            throw new IllegalArgumentException("Please enter a valid date");
        }
    }

    private double convertToDouble(final String text) {
        if (text.matches("[0-9]*")) {
            return Double.parseDouble(text);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
