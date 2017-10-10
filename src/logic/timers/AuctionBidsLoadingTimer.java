package logic.timers;

import core.javaFX.auction.AuctionController;
import data.contexts.BidMySqlContext;
import javafx.application.Platform;
import logic.repositories.BidRepository;
import models.Bid;

import java.sql.SQLException;
import java.util.ArrayList;
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
            final ArrayList<Bid> newLoadedBids = bidRepository.getBids(this.auctionId);

            if (bidsHaveChanged(newLoadedBids)){
                Platform.runLater(() -> auctionController.setBids(newLoadedBids, startBid));
            }
        } catch (SQLException e) {
            e.printStackTrace(); //TODO proper error handling
        }
    }

    private boolean bidsHaveChanged(final ArrayList<Bid> newLoadedBids) {
        return this.bids.size() != newLoadedBids.size();
    }
}
