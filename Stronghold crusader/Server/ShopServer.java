package Server;

import controller.GameMenuCommands;
import controller.GameMenuController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ShopServer {
    private final GameMenuController controller;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public ShopServer(DataOutputStream dataOutputStream, DataInputStream dataInputStream, GameMenuController controller) {
        this.dataInputStream= dataInputStream;
        this.dataOutputStream   =dataOutputStream;
        this.controller = controller;
    }

    public void run() throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return;
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_PRICE_LIST) != null)
                dataOutputStream.writeUTF(controller.showPriceList());
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.BUY_COMMODITY) != null)
                dataOutputStream.writeUTF(buyCommodity(input));
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELL_COMMODITY) != null)
                dataOutputStream.writeUTF(sellCommodity(input));
            else
                dataOutputStream.writeUTF("Invalid command!");

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
        String item = itemMatcher.group("item").trim();
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