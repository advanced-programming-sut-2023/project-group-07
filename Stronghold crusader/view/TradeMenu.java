package view;

import controller.Controller;
import controller.TradeMenuController;
import controller.TradeCommands;
import model.TradeRequest;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class TradeMenu {
    TradeMenuController controller;

    public TradeMenu() {
        controller = new TradeMenuController();
    }

    public void run(Scanner scanner) {
        showNewRequests();
        Matcher matcher;
        while (true) {
            String input = scanner.nextLine();
            if(input.matches("\\s*exit\\s*"))
                return;
            else if (TradeCommands.getMatcher(input, TradeCommands.TRADE_LIST) != null)
                showAvailableTrades();
            else if (TradeCommands.getMatcher(input, TradeCommands.TRADE_HISTORY) != null)
                showTradeHistory();
            else if (TradeCommands.getMatcher(input, TradeCommands.ACCEPT_TRADE) != null)
                System.out.println(acceptTrade(input));
            else if ((matcher = TradeCommands.getMatcher(input, TradeCommands.REJECT_TRADE)) != null)
                System.out.println(rejectTrade(matcher));
            else
                System.out.println("Invalid command!");

            // todo: add commands
        }
    }

    private String rejectTrade(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        switch (controller.rejectTrade(id)){
            case INVALID_ID :
                return "ID is invalid.";
            case REJECT_TRADE_SUCCESSFUL:
                return "You have successfully reject a trade.";
        }
        return null;

    }

    private String acceptTrade(String input) {
        String idString = TradeCommands.getMatcher(input, TradeCommands.ID).group("id");
        int id= Integer.parseInt(idString);
        String message = TradeCommands.getMatcher(input, TradeCommands.MESSAGE).group("message");
        message = Controller.trimmer(message);
        switch (controller.acceptTrade(id,message)) {
            case ACCEPT_TRADE_SUCCESSFUL :
                return "You have successfully accept a trade.";
            case POOR_RECEIVER:
                return "You don't have enough recourse for this trade.";
            case POOR_REQUESTER:
                return "Requester don't have enough gold to pay you right now.";
            case INVALID_ID:
                return "ID is invalid.";
        }
        return null;
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
