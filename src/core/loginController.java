package core;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class loginController {

    public Pane paneMenu;
    public Pane paneContent;
    public Label lblClose;

    public void closeApplication(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
