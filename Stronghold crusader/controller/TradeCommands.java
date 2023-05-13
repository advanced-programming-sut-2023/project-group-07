package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TradeCommands {
    TRADE_LIST("^\\s*trade\\s+list\\s*$"),
    TRADE_HISTORY("^\\s*trade\\s+history\\s*$"),
    ACCEPT_TRADE("^\\s*trade\\s+accept\\s+(-i\\s+\\d+\\s+-m\\s+([^\"\\s]+|\"[^\"]+\")" +
            "|-m\\s+([^\"\\s]+|\"[^\"]+\")\\s+-i\\s+\\d+)\\s*$"),
    ID("-i\\s+(?<id>\\d+)"),
    MESSAGE("-m\\s+(?<message>[^\"\\s]+|(\"[^\"]*\"))"),
    REJECT_TRADE("^\\s*trade\\s+reject\\s+-i\\s+(?<id>\\d+)\\s*$"),
    REQUEST_TRADE("^\\s*trade\\s+(" +
            "\\s*-t\\s+([^\"\\s]+|\"[^\"]+\")|" +
            "\\s*-a\\s+\\d+|" +
            "\\s*-p\\s+\\d+|" +
            "\\s*-m\\s+([^\"\\s]+|\"[^\"]+\")|" +
            "\\s*-c\\s+([^\"\\s]+|\"[^\"]+\")" +
            "){5}\\s*$"),
    RECURSE_TYPE("-t\\s+(?<type>([^\"\\s]+|\"[^\"]+\"))"),
    AMOUNT("-a\\s+(?<amount>\\d+)"),
    PRICE("-p\\s+(?<price>\\d+)"),
    COLOR("-c\\s+(?<color>([^\"\\s]+|\"[^\"]+\"))");

    private String regex;

    private TradeCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, TradeCommands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }

    public static int countNumberOfAppearance(String input, TradeCommands command) {
        int i = 0;
        Matcher matcher = getMatcher(input, command);
        while (matcher.find())
            i++;
        return i;
    }
}
