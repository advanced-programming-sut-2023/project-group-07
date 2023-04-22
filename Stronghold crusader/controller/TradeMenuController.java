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
}
