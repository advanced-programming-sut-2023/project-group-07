package Server.view;

import controller.Controller;
import controller.TradeCommands;
import controller.TradeMenuController;
import model.TradeRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class TradeMenuServer {
    TradeMenuController controller;

    public TradeMenuServer() {
        controller = new TradeMenuController();
    }
    AuthenticatedDataOutputStream dataOutputStream;
    AuthenticatedDataInputStream dataInputStream;

    public void run(AuthenticatedDataInputStream dataInputStream,
                    AuthenticatedDataOutputStream dataOutputStream) throws IOException {
        this.dataInputStream=dataInputStream;
        this.dataOutputStream=dataOutputStream;
        showNewRequests();
        Matcher matcher;
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*")) {
                dataOutputStream.writeUTF("You have exited");
                return;
            } else if (TradeCommands.getMatcher(input, TradeCommands.TRADE_LIST) != null)
                showAvailableTrades();
            else if (TradeCommands.getMatcher(input, TradeCommands.TRADE_HISTORY) != null)
                showTradeHistory();
            else if (TradeCommands.getMatcher(input, TradeCommands.ACCEPT_TRADE) != null)
                dataOutputStream.writeUTF(acceptTrade(input));
            else if ((matcher = TradeCommands.getMatcher(input, TradeCommands.REJECT_TRADE)) != null)
                dataOutputStream.writeUTF(rejectTrade(matcher));
            else if (TradeCommands.getMatcher(input, TradeCommands.REQUEST_TRADE) != null && tradeRequestFormat(input))
                dataOutputStream.writeUTF(requestTrade(input));
            else
                dataOutputStream.writeUTF("Invalid command!");
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

    private void showTradeHistory() throws IOException {
        ArrayList<TradeRequest> tradeHistory = controller.getTradeHistory();
        if (tradeHistory.size() == 0)
            dataOutputStream.writeUTF("You don't have any trade history.");
        else {
            for (TradeRequest tradeRequest : tradeHistory) {
                dataOutputStream.writeUTF(tradeRequest.toString());
            }
        }
    }

    private void showNewRequests() throws IOException {
        ArrayList<TradeRequest> notSeenRequests = controller.getNotSeenRequests();
        if (notSeenRequests.size() == 0)
            dataOutputStream.writeUTF("You don't have any new trade request.");
        else {
            if (notSeenRequests.size() == 1)
                dataOutputStream.writeUTF("You have 1 new trade request :");
            else
                dataOutputStream.writeUTF("You have " + notSeenRequests.size() + " new trade requests :");
            for (TradeRequest tradeRequest : notSeenRequests) {
                dataOutputStream.writeUTF(tradeRequest.toString());
            }
        }
        controller.setRequestsShown(notSeenRequests);
    }

    private void showAvailableTrades() throws IOException {
        ArrayList<TradeRequest> availableTrades = controller.getAvailableTrades();
        if (availableTrades.size() == 0)
            dataOutputStream.writeUTF("No available trade request");
        else {
            for (TradeRequest tradeRequest : availableTrades) {
                dataOutputStream.writeUTF(tradeRequest.toString());
            }
        }
    }

}
