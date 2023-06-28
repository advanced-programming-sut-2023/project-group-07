package Client.view;

import Client.controller.Controller;
import Client.controller.TradeMenuController;
import Client.controller.TradeCommands;
import Client.model.TradeRequest;

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
            if (input.matches("\\s*exit\\s*")) {
                System.out.println("You have exited");
                return;
            } else if (TradeCommands.getMatcher(input, TradeCommands.TRADE_LIST) != null)
                showAvailableTrades();
            else if (TradeCommands.getMatcher(input, TradeCommands.TRADE_HISTORY) != null)
                showTradeHistory();
            else if (TradeCommands.getMatcher(input, TradeCommands.ACCEPT_TRADE) != null)
                System.out.println(acceptTrade(input));
            else if ((matcher = TradeCommands.getMatcher(input, TradeCommands.REJECT_TRADE)) != null)
                System.out.println(rejectTrade(matcher));
            else if (TradeCommands.getMatcher(input, TradeCommands.REQUEST_TRADE) != null && tradeRequestFormat(input))
                System.out.println(requestTrade(input));
            else
                System.out.println("Invalid command!");
        }
    }

    private String requestTrade(String input) {
        String typeName = TradeCommands.getMatcher(input, TradeCommands.RECURSE_TYPE).group("type"),
                colorString = TradeCommands.getMatcher(input, TradeCommands.COLOR).group("color"),
                message = Controller.trimmer(
                        TradeCommands.getMatcher(input, TradeCommands.MESSAGE).group("message"));
        int amount, price;
        try {
            amount = Integer.parseInt(TradeCommands.getMatcher(input, TradeCommands.AMOUNT).group("amount"));
            price = Integer.parseInt(TradeCommands.getMatcher(input, TradeCommands.PRICE).group("price"));
        } catch (NumberFormatException e) {
            return "input a valid number.";
        }
        switch (controller.requestTrade(typeName, colorString, message, amount, price)) {
            case NEGATIVE_PRICE:
                return "Price can't be negative.";
            case INVALID_COLOR:
                return "Color is invalid.";
            case INVALID_RESOURCE:
                return "Resource type is invalid";
            case NO_LORD_WITH_THIS_COLOR:
                return "No one has this color.";
            case REQUEST_YOURSELF:
                return "You can't request yourself.";
            case REQUEST_TRADE_SUCCESSFUL:
                return "You have successfully made a trade request.";
            default:
                break;
        }
        return null;
    }

    private boolean tradeRequestFormat(String input) {

        return (TradeCommands.getMatcher(input, TradeCommands.RECURSE_TYPE) != null) &&
                (TradeCommands.getMatcher(input, TradeCommands.AMOUNT) != null) &&
                (TradeCommands.getMatcher(input, TradeCommands.MESSAGE) != null) &&
                (TradeCommands.getMatcher(input, TradeCommands.PRICE) != null) &&
                (TradeCommands.getMatcher(input, TradeCommands.COLOR) != null);

    }

    private String rejectTrade(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        switch (controller.rejectTrade(id)) {
            case INVALID_ID:
                return "ID is invalid.";
            case REJECT_TRADE_SUCCESSFUL:
                return "You have successfully rejected a trade.";
            default:
                break;
        }
        return null;

    }

    private String acceptTrade(String input) {
        String idString = TradeCommands.getMatcher(input, TradeCommands.ID).group("id");
        int id = Integer.parseInt(idString);
        String message = Controller.trimmer(
                TradeCommands.getMatcher(input, TradeCommands.MESSAGE).group("message"));
        message = Controller.trimmer(message);
        switch (controller.acceptTrade(id, message)) {
            case ACCEPT_TRADE_SUCCESSFUL:
                return "You have successfully accepted a trade.";
            case POOR_RECEIVER:
                return "You don't have enough recourse for this trade.";
            case POOR_REQUESTER:
                return "Requester don't have enough gold to pay you right now.";
            case INVALID_ID:
                return "ID is invalid.";
            default:
                break;
        }
        return null;
    }

    private void showTradeHistory() {
        ArrayList<TradeRequest> tradeHistory = controller.getTradeHistory();
        if (tradeHistory.size() == 0)
            System.out.println("You don't have any trade history.");
        else {
            for (TradeRequest tradeRequest : tradeHistory) {
                System.out.println(tradeRequest);
            }
        }
    }

    private void showNewRequests() {
        ArrayList<TradeRequest> notSeenRequests = controller.getNotSeenRequests();
        if (notSeenRequests.size() == 0)
            System.out.println("You don't have any new trade request.");
        else {
            if (notSeenRequests.size() == 1)
                System.out.println("You have 1 new trade request :");
            else
                System.out.println("You have " + notSeenRequests.size() + " new trade requests :");
            for (TradeRequest tradeRequest : notSeenRequests) {
                System.out.println(tradeRequest);
            }
        }
        controller.setRequestsShown(notSeenRequests);
    }

    private void showAvailableTrades() {
        ArrayList<TradeRequest> availableTrades = controller.getAvailableTrades();
        if (availableTrades.size() == 0)
            System.out.println("No available trade request");
        else {
            for (TradeRequest tradeRequest : availableTrades) {
                System.out.println(tradeRequest);
            }
        }
    }

}
