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
    REJECT_TRADE("^\\s*trade\\s+reject\\s+-i\\s+(?<id>\\d+)\\s*$");
    private String regex;
    private TradeCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , TradeCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
