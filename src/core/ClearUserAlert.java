package core;

import core.javaFX.menu.MenuController;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.TimerTask;

public class ClearUserAlert extends TimerTask {

    private Label label;
    private Pane pane;
    private MenuController menuController;

    public ClearUserAlert(Label label, Pane pane, MenuController menuController) {
        this.label = label;
        this.pane = pane;
        this.menuController = menuController;
    }

    @Override
    public void run() {
        menuController.ClearAlert();
    }
}
