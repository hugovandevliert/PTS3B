package logic.timers;

import core.javaFX.auction.AuctionController;
import data.contexts.BidMySqlContext;
import javafx.application.Platform;
import logic.repositories.BidRepository;
import models.Bid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

public class AuctionBidsLoadingTimer extends TimerTask {

    private AuctionController auctionController;
    private List<Bid> bids;
    private int auctionId;
    private double startBid;

    private BidRepository bidRepository;

    public AuctionBidsLoadingTimer(final AuctionController auctionController, final List<Bid> bids, final int auctionId, final double startBid) {
        this.auctionController = auctionController;
        this.bids = bids;
        this.auctionId = auctionId;
        this.startBid = startBid;

        bidRepository = new BidRepository(new BidMySqlContext());
    }

    @Override
    public void run() {
        try {
            if (!auctionHasEnded()){
                final ArrayList<Bid> newLoadedBids = bidRepository.getBids(this.auctionId);

                if (bidsHaveChanged(newLoadedBids)){
                    Collections.sort(newLoadedBids);
                    Platform.runLater(() -> auctionController.setBids(newLoadedBids, startBid));
                }
            }else{
                // There is no need to keep this TimerTask running as the auction has been ended
                // We will therefore cancel the TimerTask
                this.cancel();
            }
        } catch (SQLException e) {
            e.printStackTrace(); //TODO proper error handling
        }
    }

    private boolean auctionHasEnded() {
        return auctionController.getTimerString().toLowerCase().contains("end");
    }

    private boolean bidsHaveChanged(final ArrayList<Bid> newLoadedBids) {
        return this.bids.size() != newLoadedBids.size();
    }
}
