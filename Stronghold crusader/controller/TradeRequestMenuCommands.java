package controller;

import model.TradeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TradeRequestMenuCommands {
    TRADE_LIST("^\\s*trade\\s+list\\s*$"),
    TRADE_HISTORY("^\\s*trade\\s+history\\s*$"),
    ACCEPT_TRADE("^\\s*trade\\s+accept\\s+(-i\\s+\\d+\\s+-m\\s+([^\"\\s]+|\"[^\"]+\")" +
            "|-m\\s+([^\"\\s]+|\"[^\"]+\")\\s+-i\\s+\\d+)\\s*$"),
    ID("-i\\s+(?<id>\\d+)"),
    MESSAGE("-m\\s+(?<message>[^\"\\s]+|(\"[^\"]*\"))");
    private String regex;
    private TradeRequestMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , TradeRequestMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
