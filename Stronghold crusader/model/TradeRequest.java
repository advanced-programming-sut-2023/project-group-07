package model;

import view.TradeMenu;

import java.util.ArrayList;

public class TradeRequest {
    private static final ArrayList<TradeRequest> allTrades;
    private static int lastId;
    private final double price;
    private final Resources resource;
    private final int amount;
    private final Government requester;
    private final Government receiver;
    private boolean haveBeenShown;
    private int id = 0;

    static {
        allTrades = new ArrayList<>();
        lastId = 0;
    }

    private TradeRequest(double price, Resources resource, int amount, Government requester, Government receiver) {
        this.price = price;
        this.resource = resource;
        this.amount = amount;
        this.requester = requester;
        this.receiver = receiver;
        haveBeenShown = false;
        this.setId();
    }

    public static TradeRequest makeATradeRequest(double price, Resources resource, int amount, Government requester, Government receiver) {
        TradeRequest tradeRequest = new TradeRequest(price, resource, amount, requester, receiver);
        allTrades.add(tradeRequest);
        return tradeRequest;
    }

    public static ArrayList<TradeRequest> getNotSeenRequests(Government receiver) {
        ArrayList<TradeRequest> notSeenRequests = new ArrayList<>();
        for (TradeRequest tradeRequest : allTrades) {
            if (!tradeRequest.haveBeenShown && tradeRequest.receiver.equals(receiver))
                notSeenRequests.add(tradeRequest);
        }
        return notSeenRequests;
    }

    public double price() {
        return price;
    }

    public Resources resource() {
        return resource;
    }

    public int amount() {
        return amount;
    }

    public Government requester() {
        return requester;
    }

    public Government receiver() {
        return receiver;
    }

    public boolean haveBeenShown() {
        return haveBeenShown;
    }

    public void setHaveBeenShownTrue() {
        this.haveBeenShown = true;
    }

    private void setId() {
        if (this.id == 0) {
            this.id = lastId + 1;
            lastId++;
        }
    }

    public int getId() {
        return this.id;
    }


}
