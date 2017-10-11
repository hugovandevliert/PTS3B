package logic.timers;

import core.javaFX.auction.AuctionController;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AuctionCountdownTimer extends TimerTask {

    private AuctionController auctionController;
    private LocalDateTime expirationDate;

    public AuctionCountdownTimer(LocalDateTime expirationDate, AuctionController auctionController) {
        this.expirationDate = expirationDate;
        this.auctionController = auctionController;
    }

    @Override
    public void run() {
        Date currentDate = new Date();
        final long differenceInMs = expirationDate.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - currentDate.getTime();
        String timerStringValue = "";

        if (differenceInMs > 0){
            timerStringValue = getDurationFromMilliseconds(differenceInMs);
        }else{
            timerStringValue = "This auction has ended!";
        }

        //TODO: ook zorgen dat deze thread netjes gestopt wordt als deze pagina verwijdert wordt!
        final String finalTimerStringValue = timerStringValue;
        Platform.runLater(() -> auctionController.setTimer(finalTimerStringValue));
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
