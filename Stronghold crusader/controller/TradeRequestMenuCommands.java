package controller;

import model.TradeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TradeRequestMenuCommands {
    TRADE_LIST("^\\s*trade\\s+list\\s*$"),
    TRADE_HISTORY("^\\s*trade\\s+history\\s*$");
    private String regex;
    private TradeRequestMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , TradeRequestMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
