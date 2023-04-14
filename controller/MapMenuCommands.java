package controller;
import java.util.regex.*;

public enum MapMenuCommands {
<<<<<<< HEAD
    MOVE_MAP("^\\s*map\\s+(up|down|right|left|\\-\\d|\\d|\\s)+$"),
=======
    MOVE_MAP("^\\s*map\\s+(up|down|right|left|\\-|\\d|\\s)+$"),
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
    MOVE_UP("up\\s+(?<up>\\-?\\d+)"),
    MOVE_DOWN("down\\s+(?<down>\\-?\\d+)"),
    MOVE_LEFT("left\\s+(?<left>\\-?\\d+)"),
    MOVE_RIGHT("right\\s+(?<right>\\-?\\d+)"),
<<<<<<< HEAD
    SHOW_DETAILS("^\\s*show\\s+details\\s+(\\-x|\\-y|\\-\\d|\\d|\\s)+$"),
    SHOW_DETAILS_ROW("\\-x\\s+(?<row>\\-?\\d+)"),
    SHOW_DETAILS_COLUMN("\\-y\\s+(?<column>\\-?\\d+)"),
=======
    SHOW_DETAILS("^\\s*show\\s+details\\s+\\-x\\s+(?<x>\\-?\\d+)\\s+\\-y\\s+(?<y>\\-?\\d+)\\s*$"),
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
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