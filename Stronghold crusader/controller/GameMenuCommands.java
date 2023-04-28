package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    SHOW_POPULARITY_FACTORS("\\s*show\\s+popularity\\s+factors\\s*"),
    SHOW_POPULARITY("\\s*show\\s+popularity\\s*"),
    SHOW_FOOD_LIST("\\s*show\\s+food\\s+list\\s*"),
    FOOD_RATE("\\s*food\\s+rate\\s+-r\\s+(?<rate>\\d+)\\s*"),
    FOOD_RATE_SHOW("\\s*food\\s+rate\\s+show\\s*"),
    TAX_RATE("\\s*tax\\s+rate\\s+-r\\s+(?<rate>\\d+)\\s*"),
    TAX_RATE_SHOW("\\s*tax\\s+rate\\s+show\\s*"),
    FEAR_RATE("\\s*fear\\s+rate\\s+-r\\s+(?<rate>\\d+)\\s*"),
    DROP_BUILDING("\\s*dropbuilding(.+)"),
    ROW("\\-x\\s+(?<row>\\d+)"),
    COLUMN("\\-y\\s+(?<column>\\d+)"),
    FIRST_ROW("\\-x1\\s+(?<frow>\\-?\\d+)"),
    FIRST_COLUMN("\\-y1\\s+(?<fcolumn>\\-?\\d+)"),
    SECOND_ROW("\\-x2\\s+(?<srow>\\-?\\d+)"),
    SECOND_COLUMN("\\-y2\\s+(?<scolumn>\\-?\\d+)"),
    TYPE("\\-t\\s+(?<type>[a-z\\s]+)"),
    STANCE("\\-s\\s+(?<stance>\\w+)"),
    SELECT_PIXEL_UNIT("^\\s*select\\s+unit\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\s)+$"),
    SELECT_REGION_UNIT("^\\s*select\\s+unit\\s+(\\-x1\\s+\\-?\\d+|\\-y1\\s+\\-?\\d+|\\-x2\\s+\\-?\\d+|\\-y2\\s+\\-?\\d+|\\s)+$"),
    SELECT_BUILDING("\\s*select\\s+building\\s+((-x\\s+(?<row1>\\d+)\\s+-y\\s+(?<column1>\\d+)\\s*)|(-y\\s+(?<column2>\\d+)\\s+-x\\s+(?<row2>\\d+)\\s*))"),
    MOVE_UNIT("^\\s*move\\s+unit\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\s)+$"),
    PATROL_UNIT("^\\s*select\\s+unit\\s+(\\-x1\\s+\\-?\\d+|\\-y1\\s+\\-?\\d+|\\-x2\\s+\\-?\\d+|\\-y2\\s+\\-?\\d+|\\s)+$"),
    STOP_UNIT("\\s*stop\\s+unit\\s*"),
    SET_STANCE("^\\s*set\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\-s\\s+[a-z\\s]+|\\s)+$"),
    ATTACK_ENEMY("\\s*attack\\s+-e\\s+(?<row>\\d+)\\s+(?<column>\\d+)\\s*"),
    AIR_ATTACK("^\\s*attack\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\s)+$"),
    CREATE_UNIT("^\\s*createunit\\s+(\\-t\\s+[a-z\\s]+|\\-c\\s+\\d+|\\s)+$"),
    COUNT("\\-c\\s+(?<count>\\d+)"),
    EXIT("\\s*exit\\s*"),
    SHOW_UNITS("\\s*show\\s+units\\s*"),
    REPAIR("\\s*repair\\s*"),
    CLOSE_GATE("\\s*close\\s+gate\\s*"),
    OPEN_GATE("\\s*open\\s+gate\\s*"),
    CHANGE_ARMS("\\s*change\\s+arms\\s*"),
    CHANGE_WORKING_STATE("\\s*change\\s+working\\s+state\\s*"),
    SHOW_PRICE_LIST("^\\s*show\\s+price\\s+list\\s*$"),
    BUY_COMMODITY("^\\s*buy\\s+(\\-i|\\-a|\\-?\\d+|[^\\-][a-z]|\\s)+$"),
    SELL_COMMODITY("^\\s*sell\\s+(\\-i|\\-a|\\-?\\d+|[^\\-][a-z]|\\s)+$"),
    GET_ITEM("\\-i\\s+(?<item>[a-z\\s]+)"),
    GET_AMOUNT("\\-a\\s+(?<amount>\\d+)"),
    ENTER_TRADE_MENU("^\\s*enter\\s*trade\\s*menu$");
    
    private String regex;
    private GameMenuCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, GameMenuCommands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
