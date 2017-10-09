package logic.timers;

import core.javaFX.auction.AuctionController;
import javafx.application.Platform;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AuctionCountdownTimer extends TimerTask {

    private AuctionController auctionController;
    private long differenceInMs = 14000000;

    public AuctionCountdownTimer(AuctionController auctionController) {
        this.auctionController = auctionController;
    }

    @Override
    public void run() {
        differenceInMs -= 1000;
        //TODO: zorgen dat dit echte tijdsverschil wordt --> differenceinMS moet niet zo opgeslagen worden; was alleen als test!
        //TODO: ook zorgen dat deze thread netjes gestopt wordt als deze pagina verwijdert wordt!

        Platform.runLater(() -> auctionController.setTimer(getDurationFromMilliseconds(differenceInMs)));
    }

    public String getDurationFromMilliseconds(final long milliSeconds) {
        if (milliSeconds < 0) {
            throw new IllegalArgumentException("milliSeconds duration should be bigger then 0");
        }

        final long days = TimeUnit.MILLISECONDS.toDays(milliSeconds);
        final long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds) % 24;
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) % 60;

        return String.format("%d D %d H %d M %d S", days, hours, minutes, seconds);
    }
}
