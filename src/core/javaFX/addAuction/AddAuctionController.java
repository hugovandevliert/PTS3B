package core.javaFX.addAuction;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import core.javaFX.auction.AuctionController;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import logic.repositories.AuctionRepository;
import modelslibrary.Auction;
import modelslibrary.Profile;
import utilities.enums.AlertType;
import utilities.enums.AuctionLoadingType;
import java.io.File;
import java.io.IOException;
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
    private AuctionRepository auctionRepository;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        images = new File[4];
        setImages();

        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
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
                imgviewPicture4.setImage(new Image("file:" +  selectedImage.getAbsolutePath(), 225, 156, false, false));
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

            final boolean successful = this.addAuction(startBid, incrementation, minimumPrice,
                    expirationDate, openingDate, false, title, description, getImages(this.images));

            if (successful){
                MenuController.showAlertMessage("Auction added successfully!", AlertType.MESSAGE, 3000);

                final Auction auction = auctionRepository.getAuctionForId(auctionRepository.getLastInsertedAuctionId(), AuctionLoadingType.FOR_AUCTION_PAGE);

                if (auction != null){
                    paneContent.getChildren().clear();

                    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/core/javaFX/auction/auction.fxml"));
                    final Pane auctionPane = fxmlLoader.load();
                    final AuctionController auctionController = fxmlLoader.getController();

                    auctionController.setAuction(auction, this);
                    paneContent.getChildren().add(auctionPane);
                }else{
                    MenuController.showAlertMessage("Something went wrong - Couldn't load auction page", AlertType.ERROR, 5000);
                }
            }else{
                MenuController.showAlertMessage("Your auction could not be added - Please try again.", AlertType.ERROR, 5000);
            }
        } catch (IllegalArgumentException | IOException | SQLException | ClassNotFoundException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
            exception.printStackTrace();
        }
    }

    /**
     * Method for creating a new auction.
     * @param startBid:       Minimum value of the first bid. Must be >0.
     * @param incrementation: Minimum difference a bid must have from the previous one. Must be >0.
     * @param minimum:        Minimum bid the auction must have reached before the seller actually sells the item. Must be >0.
     * @param expirationDate: Date/time when the auction is planned to close. Should be later then the current date.
     * @param openingDate:    Date/time when the auction is planned to open. Should be earlier then the the expirationdate, and can't be earlier then today.
     * @param isPremium:      Indicates if a user paid to boost his auction.
     * @param title:          Title of the auction. Can't contain more then 64 or less than 5 characters.
     * @param description:    Description of the auction. Can't contain more than 1000 or less than 20 characters.
     * @param fileImages:     The file's which represent the images added to this auction.
     */
    private boolean addAuction(final double startBid, final double incrementation, final double minimum, final LocalDateTime expirationDate, final LocalDateTime openingDate,
                              final boolean isPremium, final String title, final String description, final ArrayList<File> fileImages) throws SQLException {
        if (startBid <= 0){
            throw new IllegalArgumentException("Start bid should be higher than 0.");
        }
        else if (incrementation <= 0){
            throw new IllegalArgumentException("incrementation bid should be higher than 0.");
        }
        else if (minimum <= 0){
            throw new IllegalArgumentException("Minimum bid should be higher than 0.");
        }
        else if (expirationDate == null){
            throw new IllegalArgumentException("Expiration date can not be empty.");
        }
        else if (expirationDate.compareTo(LocalDateTime.now()) < 0){
            throw new IllegalArgumentException("Expiration date should be in the future");
        }
        else if (openingDate == null){
            throw new IllegalArgumentException("Opening date can not be empty");
        }
        else if (openingDate.compareTo(LocalDateTime.now()) < 0){
            throw new IllegalArgumentException("Opening date can not be before today");
        }
        else if (expirationDate.compareTo(openingDate) < 1){
            throw new IllegalArgumentException("Opening date must be before the expiration date");
        }
        else if (title.length() > 64){
            throw new IllegalArgumentException("Title can not be longer than 64 characters.");
        }
        else if (title.length() < 5){
            throw new IllegalArgumentException("Title can not be less than 5 characters.");
        }
        else if (description.length() > 1000){
            throw new IllegalArgumentException("Description can not be longer than 1000 characters.");
        }
        else if (description.length() < 20){
            throw new IllegalArgumentException("Description can not be less than 20 characters.");
        }

        final Profile currentUser = applicationManager.getCurrentUser().getProfile();
        final Auction auction = new Auction(title, description, startBid, minimum, openingDate, expirationDate, isPremium, currentUser, fileImages, incrementation);

        if (auctionRepository.addAuction(auction)) {
            applicationManager.getCurrentUser().getProfile().addAuction(auction);
            return true;
        }
        return false;
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
        ArrayList<File> imagesFromFile = new ArrayList<>();

        for (final File file : files){
            if (file != null) imagesFromFile.add(file);
        }
        return imagesFromFile;
    }

    private LocalTime convertToTime(final String time) {
        if (time.matches( "([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            return LocalTime.parse(time);
        } else {
            throw new IllegalArgumentException("Please enter a valid time.");
        }
    }

    private LocalDate convertToDate(final String date) {
         if (date.matches("(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))" +
                 "|((29|30)[\\/](0[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])" +
                 "(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)")) {
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
             return LocalDate.parse(date, formatter);
        } else {
            throw new IllegalArgumentException("Please enter a valid date.");
        }
    }

    private double convertToDouble(final String text) {
        text.replaceAll(",", ".");
        if (!text.isEmpty() && text.matches("[0-9]*")) {
            return Double.parseDouble(text);
        } else {
            throw new IllegalArgumentException("Please enter a valid amount.");
        }
    }
}
