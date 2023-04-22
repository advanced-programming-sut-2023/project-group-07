package controller;

import model.Government;
import model.TradeRequest;

import java.util.ArrayList;

import static controller.Controller.currentGame;

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
}
