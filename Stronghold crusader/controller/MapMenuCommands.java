package controller;

import java.util.regex.*;

public enum MapMenuCommands {

    MOVE_MAP("^\\s*map\\s+(up(\\s+\\-?\\d+)?|down(\\s+\\-?\\d+)?|right(\\s+\\-?\\d+)?|left(\\s+\\-?\\d+)?|\\s)+$"),
    MOVE_UP("up(?<up>\\s+\\-?\\d+)?"),
    MOVE_DOWN("down(?<down>\\s+\\-?\\d+)?"),
    MOVE_LEFT("left(?<left>\\s+\\-?\\d+)?"),
    MOVE_RIGHT("right(?<right>\\s+\\-?\\d+)?"),
    SHOW_DETAILS("^\\s*show\\s+details\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\s)+$"),
    GET_ROW("\\-x\\s+(?<row>\\-?\\d+)"),
    GET_COLUMN("\\-y\\s+(?<column>\\-?\\d+)"),
    MAP_GUIDE("^\\s*map\\s+guide\\s*$"),
    SHOW_MAP("^\\s*show\\s+map\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\s)+$");

    private String regex;

    private MapMenuCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, MapMenuCommands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }

}