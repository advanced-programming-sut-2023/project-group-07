package model;

import java.util.ArrayList;

public class TradeRequest {
    private static final ArrayList<TradeRequest> allTrades;
    private static int lastId;
    private final double price;
    private final Resources resource;
    private final int amount;
    private final Government requester;
    private Government receiver;
    private boolean hasBeenShown;
    private String requesterMessage;
    private String receiverMessage;
    private boolean isAvailable;
    private boolean accepted;
    private boolean rejected;
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
        hasBeenShown = false;
        isAvailable = true;
        accepted = false;
        rejected = false;
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
            if (tradeRequest.isAvailable && tradeRequest.receiver.equals(receiver))
                notSeenRequests.add(tradeRequest);
        }
        return notSeenRequests;
    }

    public static ArrayList<TradeRequest> getAvailableRequests(Government receiver) {
        ArrayList<TradeRequest> availableTrades = new ArrayList<>();
        for (TradeRequest tradeRequest : allTrades) {
            if (!tradeRequest.hasBeenShown && tradeRequest.receiver.equals(receiver))
                availableTrades.add(tradeRequest);
        }
        return availableTrades;
    }

    public static ArrayList<TradeRequest> getRelatedTradeHistory(Government government) {
        ArrayList<TradeRequest> tradeHistory = new ArrayList<>();
        for (TradeRequest tradeRequest : allTrades) {
            if (!tradeRequest.isAvailable &&
                    (tradeRequest.receiver.equals(government) || tradeRequest.requester.equals(government)))
                tradeHistory.add(tradeRequest);
        }
        return tradeHistory;
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
        return hasBeenShown;
    }

    public void setHaveBeenShownTrue() {
        this.hasBeenShown = true;
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

    @Override
    public String toString() { // todo : this may change when adding 1-all
        String str = "ID." + this.getId() + " : " + this.requester().color() + " wants " +
                this.amount() + " " + this.resource() + " for " + this.price() + " golds";
        if (!this.isAvailable) {
            if (this.accepted) str += " :was accepted";
            else if (this.rejected) str += " :was rejected";
        }
        return str;
    }

    public void accept(Government receiver, String receiverMessage) {
        this.receiverMessage = receiverMessage;
        this.receiver = receiver;
        this.isAvailable = false;
        this.accepted = true;

    }
}
