package core.javaFX.auction;


import com.jfoenix.controls.JFXTextField;
import core.javaFX.auctions.ListedAuctionController;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.repositories.AuctionRepository;
import models.Auction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionController extends MenuController {

    @Override
    public void initialize(URL location, ResourceBundle resources) { }
}
