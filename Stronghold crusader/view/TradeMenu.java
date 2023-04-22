package view;

import controller.TradeMenuController;
import controller.TradeRequestMenuCommands;
import model.TradeRequest;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Scanner;

public class TradeMenu {
    TradeMenuController controller;

    public TradeMenu() {
        controller = new TradeMenuController();
    }

    public void run(Scanner scanner) {
        showNewRequests();
        while (true) {
            String input = scanner.nextLine();
            if (TradeRequestMenuCommands.getMatcher(input, TradeRequestMenuCommands.TRADE_LIST) != null)
                showAvailableTrades();
            else if (TradeRequestMenuCommands.getMatcher(input, TradeRequestMenuCommands.TRADE_HISTORY) != null)
                showTradeHistory();
            // todo: add commands
        }
    }

    private void showTradeHistory() { // todo : this may change when adding 1-all
        // todo : check this method
        ArrayList<TradeRequest> tradeHistory = controller.getTradeHistory();
        if (tradeHistory.size() == 0) System.out.println("You don't have any trade history.");
        else {
            for (TradeRequest tradeRequest : tradeHistory) {
                System.out.println(tradeRequest);
            }
        }
    }

    private void showNewRequests() { // todo : this may change when adding 1-all
        ArrayList<TradeRequest> notSeenRequests = controller.getNotSeenRequests();
        if (notSeenRequests.size() == 0) System.out.println("You don't have any new trade request.");
        else {
            if (notSeenRequests.size() == 1) System.out.println("You have 1 new trade request :");
            else System.out.println("You have " + notSeenRequests.size() + " new trade requests :");
            for (TradeRequest tradeRequest : notSeenRequests) {
                System.out.println(tradeRequest);
            }
        }
        controller.setRequestsShown(notSeenRequests);
    }

    private void showAvailableTrades() {  // todo : this may change when adding 1-all
         ArrayList<TradeRequest> availableTrades = controller.getAvailableTrades();
        if (availableTrades.size() == 0) System.out.println("No available trade request");
        else {
            for (TradeRequest tradeRequest : availableTrades) {
                System.out.println(tradeRequest);
            }
        }
    }

}
