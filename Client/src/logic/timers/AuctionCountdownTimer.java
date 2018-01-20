package logic.timers;

import core.javafx.auction.AuctionController;
import core.javafx.menu.MenuController;
import data.contexts.AuctionMySqlContext;
import javafx.application.Platform;
import logic.clients.TimeClient;
import logic.repositories.AuctionRepository;
import models.Auction;
import utilities.enums.AlertType;
import utilities.enums.AuctionLoadingType;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AuctionCountdownTimer extends TimerTask {

    private final AuctionController auctionController;
    private final AuctionRepository auctionRepository;
    private final int auctionId;
    private final TimeClient timeClient;

    public AuctionCountdownTimer(final AuctionController auctionController, final MenuController menuController, final int auctionId, final TimeClient timeClient) {
        this.auctionController = auctionController;
        this.auctionId = auctionId;
        this.timeClient = timeClient;

        menuController.setLastCalledClass(this.getClass());
        auctionRepository = new AuctionRepository(new AuctionMySqlContext());
    }

    @Override
    public void run() {
        try {
            if (userStoppedLookingAtThisAuction()) this.cancel();

            if (!auctionRepository.auctionIsClosed(this.auctionId)) {
                final Auction auction = auctionRepository.getAuctionForId(this.auctionId, AuctionLoadingType.FOR_COUNTDOWN_TIMER);

                if (auction != null && timeClient != null) {
                    final long currentDateInMillis = getMillisFromLocalDateTime(timeClient.getTime());
                    final long openingDateInMillis = getMillisFromLocalDateTime(auction.getOpeningDate());
                    final long expirationDateInMillis = getMillisFromLocalDateTime(auction.getExpirationDate());
                    long differenceInMs = -1;
                    String timerStringValue;

                    if (currentDateInMillis >= openingDateInMillis) {
                        differenceInMs = expirationDateInMillis - currentDateInMillis;
                    }

                    if (differenceInMs > 0) {
                        timerStringValue = getDurationFromMilliseconds(differenceInMs);
                    } else if (differenceInMs == -1) {
                        timerStringValue = "Still has to open";
                    } else {
                        timerStringValue = "This auction has ended";
                    }

                    final String finalTimerStringValue = timerStringValue;
                    setTimerValue(finalTimerStringValue);
                }else{
                    MenuController.showAlertMessage("AuctionCountdownTimer - Could not connect to TimerServer", AlertType.ERROR, 3000);
                }
            } else {
                // There is no need to keep this TimerTask running as the auction has been ended
                // We will therefore cancel the TimerTask
                setTimerValue("This auction has ended");
                this.cancel();
            }
        } catch (SQLException | IOException | ClassNotFoundException exception) {
            MenuController.showAlertMessage(exception.getMessage(), AlertType.ERROR, 3000);
        }
    }

    private void setTimerValue(final String value) {
        if (value != null){
            Platform.runLater(() -> auctionController.setTimer(value));
        }
    }

    private String getDurationFromMilliseconds(final long milliSeconds) {
        if (milliSeconds < 0) {
            throw new IllegalArgumentException("Milliseconds duration should be bigger than 0.");
        }

        final long days = TimeUnit.MILLISECONDS.toDays(milliSeconds);
        final long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds) % 24;
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) % 60;

        return String.format("%02d : %02d : %02d : %02d", days, hours, minutes, seconds);
    }

    private long getMillisFromLocalDateTime(final LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
    }

    private boolean userStoppedLookingAtThisAuction() {
        return MenuController.getLastCalledClass() == null;
    }
}
