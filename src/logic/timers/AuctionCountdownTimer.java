package logic.timers;

import core.javaFX.auction.AuctionController;
import core.javaFX.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.application.Platform;
import logic.repositories.AuctionRepository;
import utilities.enums.AuctionLoadingType;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AuctionCountdownTimer extends TimerTask {

    private AuctionController auctionController;
    private MenuController menuController;

    private LocalDateTime expirationDate;
    private int auctionId;

    private AuctionRepository auctionRepository;

    public AuctionCountdownTimer(final AuctionController auctionController, final MenuController menuController, final int auctionId) {
        this.auctionController = auctionController;
        this.menuController = menuController;
        this.auctionId = auctionId;

        menuController.setLastCalledClass(this.getClass());
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
    }

    @Override
    public void run() {
        try {
            if(userStoppedLookingAtThisAuction()) this.cancel();
            if (!auctionRepository.auctionIsClosed(this.auctionId)){
                final Date currentDate = new Date();
                expirationDate = auctionRepository.getAuctionForId(this.auctionId, AuctionLoadingType.FOR_COUNTDOWN_TIMER).getExpirationDate();

                final long differenceInMs = expirationDate.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - currentDate.getTime();
                String timerStringValue;

                if (differenceInMs > 0){
                    timerStringValue = getDurationFromMilliseconds(differenceInMs);
                } else{
                    timerStringValue = "This auction has ended!";
                }

                //TODO: ook zorgen dat deze thread netjes gestopt wordt als deze pagina verwijdert wordt!
                final String finalTimerStringValue = timerStringValue;
                setTimerValue(finalTimerStringValue);
            } else{
                // There is no need to keep this TimerTask running as the auction has been ended
                // We will therefore cancel the TimerTask
                setTimerValue("This auction has ended!");
                this.cancel();
            }
        } catch (SQLException exception) {
            exception.printStackTrace(); //TODO: proper error handling
        } catch (IOException exception) {
            exception.printStackTrace(); //TODO: proper error handling
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace(); //TODO: proper error handling
        }
    }

    private String getDurationFromMilliseconds(final long milliSeconds) {
        if (milliSeconds < 0) {
            throw new IllegalArgumentException("milliSeconds duration should be bigger then 0");
        }

        final long days = TimeUnit.MILLISECONDS.toDays(milliSeconds);
        final long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds) % 24;
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) % 60;

        return String.format("%d D %d H %d M %d S", days, hours, minutes, seconds);
    }

    private boolean userStoppedLookingAtThisAuction() {
        return MenuController.getLastCalledClass() == null;
    }

    private void setTimerValue(final String value) {
        Platform.runLater(() -> auctionController.setTimer(value));
    }
}
