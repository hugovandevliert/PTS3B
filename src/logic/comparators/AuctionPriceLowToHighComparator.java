package logic.comparators;

import modelslibrary.Auction;

import java.util.Comparator;

public class AuctionPriceLowToHighComparator implements Comparator<Auction> {

    @Override
    public int compare(Auction o1, Auction o2) {
        if (o1.getStartBid() > o2.getStartBid()) return 1;
        else if (o1.getStartBid() < o2.getStartBid()) return -1;
        return 0;
    }
}
