package controller;

import model.Government;
import model.LordColor;
import model.Resources;
import model.TradeRequest;

import java.util.ArrayList;
import java.util.Locale;

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

    public Messages rejectTrade(int id) {
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
        targetRequest.reject();
        return Messages.REJECT_TRADE_SUCCESSFUL;
    }

    public Messages requestTrade(String typeName, String colorString, String message, int amount, double price) {
        if (price < 0) return Messages.NEGATIVE_PRICE;
        Resources resource = Resources.getResource(typeName);
        if (resource == null) return Messages.INVALID_RESOURCE;
        LordColor color = LordColor.getColorByName(colorString);
        if (color == null) return Messages.INVALID_COLOR;
        Government receiver = Controller.currentGame.getGovernmentByColor(color);
        if (receiver == null) return Messages.NO_LORD_WITH_THIS_COLOR;
        Government requester = Controller.currentGame.getCurrentGovernment();
        if (receiver.equals(requester)) return Messages.REQUEST_YOURSELF;
        TradeRequest.makeATradeRequest(price,resource,amount,requester,receiver,message);
        return Messages.REQUEST_TRADE_SUCCESSFUL;

    }
}
