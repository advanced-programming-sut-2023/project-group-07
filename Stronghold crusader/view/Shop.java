package view;

import java.util.Scanner;
import controller.GameMenuController;
import controller.GameMenuCommands;
import java.util.regex.Matcher;

public class Shop {
    private final GameMenuController controller;

    public Shop(GameMenuController controller) {
        this.controller = controller;
    }

    public void run(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*back\\s*"))
                return;
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_PRICE_LIST) != null)
                System.out.println(controller.showPriceList());
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.BUY_COMMODITY) != null)
                System.out.println(buyCommodity(input));
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELL_COMMODITY) != null)
                System.out.println(sellCommodity(input));
            else
                System.out.println("Invalid command!");

        }
    }

    private String buyCommodity(String input) {
        Matcher itemMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.GET_ITEM);
        Matcher amountMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.GET_AMOUNT);
        if (itemMatcher == null)
            return "Enter the item you want to buy!";
        if (amountMatcher == null)
            return "Enter the amount you want to buy!";
        String item = itemMatcher.group("item").trim();
        int amount = Integer.parseInt(amountMatcher.group("amount"));
        switch (controller.buyCommodity(item, amount)) {
            case INVALID_ITEM:
                return "Invalid item!";
            case NOT_ENOUGH_GOLD:
                return "Not enough gold!";
            case SHOP_SUCCESSFUL:
                return "Buy commodity successful!";
            default:
                break;
        }
        return null;
    }

    private String sellCommodity(String input) {
        Matcher itemMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.GET_ITEM);
        Matcher amountMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.GET_AMOUNT);
        if (itemMatcher == null)
            return "Enter the item you want to sell!";
        if (amountMatcher == null)
            return "Enter the amount you want to sell!";
        String item = itemMatcher.group("item");
        int amount = Integer.parseInt(amountMatcher.group("amount"));
        switch (controller.sellCommodity(item, amount)) {
            case INVALID_ITEM:
                return "Invalid item!";
            case NOT_ENOUGH_RESOURCES:
                return "Not enough resources!";
            case SHOP_SUCCESSFUL:
                return "Sell commodity successful!";
            default:
                break;
        }
        return null;
    }
}