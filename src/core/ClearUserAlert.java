package core;

import core.javaFX.menu.MenuController;
import java.util.TimerTask;

public class ClearUserAlert extends TimerTask {

    final private MenuController menuController;

    public ClearUserAlert(final MenuController menuController) {
        this.menuController = menuController;
    }

    @Override
    public void run() {
        menuController.ClearAlert();
    }
}
