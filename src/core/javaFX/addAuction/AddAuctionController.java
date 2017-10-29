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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAuctionController extends MenuController {

    @FXML private ImageView imgviewPicture1,imgviewPicture2, imgviewPicture3, imgviewPicture4;
    @FXML private JFXTextField txtTitle;
    @FXML private JFXTextArea txtDescription;
    @FXML private JFXTextField txtMinimumPrice;
    @FXML private JFXTextField txtStartTime;
    @FXML private JFXTextField txtStartDate;
    @FXML private JFXTextField txtStartBid;
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
            final double minimumPrice = convertToDouble(txtMinimumPrice.getText());
            final LocalDateTime openingDate = LocalDateTime.of(convertToDate(txtStartDate.getText()), convertToTime(txtStartTime.getText()));
            final LocalDateTime expirationDate = LocalDateTime.of(convertToDate(txtEndDate.getText()), convertToTime(txtEndTime.getText()));
            final double startBid = convertToDouble(txtStartBid.getText());
            final double incrementation = convertToDouble(txtIncrementation.getText());

            final boolean successful = applicationManager.getCurrentUser().getProfile().addAuction(startBid, incrementation, minimumPrice,
                    expirationDate, openingDate, false, title, description, getImages(this.images));

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
         if (date.matches("(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))" +
                 "|((29|30)[\\/](0[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])" +
                 "(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)")) {
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
             return LocalDate.parse(date, formatter);
        } else {
            throw new IllegalArgumentException("Please enter a valid date");
        }
    }

    private double convertToDouble(final String text) {
        text.replaceAll(",", ".");
        if (text.matches("[0-9]*")) {
            return Double.parseDouble(text);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
