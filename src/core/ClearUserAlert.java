package core;

import core.javaFX.menu.MenuController;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.TimerTask;

public class ClearUserAlert extends TimerTask {

    private MenuController menuController;

    public ClearUserAlert(MenuController menuController) {
        this.menuController = menuController;
    }

    @Override
    public void run() {
        menuController.ClearAlert();
    }
}
