package view;

import controller.TradeMenuController;
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
        ArrayList<TradeRequest> notSeenRequests = controller.getNotSeenRequests(); // todo : this may change when adding 1-all
        if (notSeenRequests.size() == 0) System.out.println("You don't have any new trade request");
        else {
            if (notSeenRequests.size() == 1) System.out.println("You have 1 new trade request :");
            else System.out.println("You have " + notSeenRequests.size() + " new trade requests :");
            for (TradeRequest tradeRequest : notSeenRequests) {
                System.out.println("ID." + tradeRequest.getId() + " : " + tradeRequest.requester().color() + " wants " +
                        tradeRequest.amount() + " "+tradeRequest.resource() + " for "+tradeRequest.price()+" golds");
            }
        }
        controller.setRequestsShown(notSeenRequests); // todo : this may change when adding 1-all
        while (true) {
            String input = scanner.nextLine();

            // todo: add commands
        }
    }
}
