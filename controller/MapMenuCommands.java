package controller;
import java.util.regex.*;

public enum MapMenuCommands {
    MOVE_MAP("^\\s*map\\s+(up|down|right|left|\\-|\\d|\\s)+$"),
    MOVE_UP("up\\s+(?<up>\\-?\\d+)"),
    MOVE_DOWN("down\\s+(?<down>\\-?\\d+)"),
    MOVE_LEFT("left\\s+(?<left>\\-?\\d+)"),
    MOVE_RIGHT("right\\s+(?<right>\\-?\\d+)"),
    SHOW_DETAILS("^\\s*show\\s+details\\s+\\-x\\s+(?<x>\\-?\\d+)\\s+\\-y\\s+(?<y>\\-?\\d+)\\s*$"),
    MAP_GUIDE("^\\s*map\\s+guide\\s*$"),
    EXIT("^\\s*exit\\s*$");
    private String regex;
    private MapMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , MapMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
    
}