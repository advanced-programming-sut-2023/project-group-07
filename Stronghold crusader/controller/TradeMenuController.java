package controller;

import model.Government;
import model.Resources;
import model.TradeRequest;

import java.util.ArrayList;

public class TradeMenuController {

    public ArrayList<TradeRequest> getNotSeenRequests() {
        Government government =  Controller.currentGame.getCurrentGovernment();
        return TradeRequest.getNotSeenRequests(government);
    }

    public void setRequestsShown(ArrayList<TradeRequest> requests) {
        for(TradeRequest tradeRequest : requests){
            tradeRequest.setHaveBeenShownTrue();
        }
    }

    public ArrayList<TradeRequest> getAvailableTrades() {
        Government government =  Controller.currentGame.getCurrentGovernment();
        return TradeRequest.getAvailableRequests(government);
    }

    public ArrayList<TradeRequest> getTradeHistory() {
        Government government =  Controller.currentGame.getCurrentGovernment();
        return TradeRequest.getRelatedTradeHistory(government);
    }

    public Messages acceptTrade(int id, String message) {
        Government receiver =  Controller.currentGame.getCurrentGovernment();
        ArrayList<TradeRequest> availableTrades = TradeRequest.getAvailableRequests(receiver);
        TradeRequest targetRequest = null;
        for(TradeRequest request : availableTrades){
            if (request.getId() == id){
                targetRequest = request;
                break;
            }
        }
        if (targetRequest==null) return Messages.INVALID_ID;
        Government requester = targetRequest.requester();
        double price = targetRequest.price();
        if (requester.getGold() < price) return Messages.POOR_REQUESTER;
        int amount = targetRequest.amount();
        Resources resource = targetRequest.resource();
        if (receiver.getResourceAmount(resource) < amount) return Messages.POOR_RECEIVER;
        receiver.changeResources(resource, -amount);
        receiver.changeGold(price);
        requester.changeResources(resource, amount);
        requester.changeGold(-price);
        targetRequest.accept(receiver, message);
        return Messages.ACCEPT_TRADE_SUCCESSFUL;
    }
}