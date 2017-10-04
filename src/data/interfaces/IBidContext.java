package data.interfaces;

import models.Bid;

import java.util.ArrayList;

public interface IBidContext {

    ArrayList<Bid> getBids(int auctionId);
}
