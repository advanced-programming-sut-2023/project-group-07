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
<<<<<<< HEAD
    FEAR_RATE("\\s*fear\\s+rate\\s+-r\\s+(?<rate>\\d+)\\s*"),
<<<<<<< Updated upstream:controller/GameMenuCommands.java
    DROP_BUILDING("\\s*dropbuilding\\s+-x\\s+(?<x>\\d+)\\s+-y\\s+(?<y>\\d+)\\s+-type\\s+(?<type>\\w+)\\s*");
=======
    FEAR_RATE("\\s*fear\\s+rate\\s+-r\\s+(?<rate>\\d+)\\s*");
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
=======
    DROP_BUILDING("\\s*dropbuilding(.+)"),
    ROW("-x\\s+(?<row>\\d+)\\s*"),
    COLUMN("-y\\s+(?<column>\\d+)\\s*"),
    TYPE("-type\\s+(?<type>\\w+)\\s*"),
    SELECT_BUILDING("\\s*select\\s+building\\s+((-x\\s+(?<row1>\\d+)\\s+-y\\s+(?<column1>\\d+)\\s*)|(-y\\s+(?<column2>\\d+)\\s+-x\\s+(?<row2>\\d+)\\s*))"),
    CREATE_UNIT("\\s*createunit\\s+((-t\\s+(?<type1>\\w+)\\s+-c\\s+(?<count1>\\w+)\\s*)|(-c\\s+(?<count2>\\w+)-t\\s+(?<type2>\\w+)\\s*))"),
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
    GET_AMOUNT("\\-a\\s+(?<amount>\\d+)");
    
>>>>>>> Stashed changes:Stronghold crusader/controller/GameMenuCommands.java
    private String regex;
    private GameMenuCommands(String regex) {
        this.regex=regex;
    }
    public static Matcher getMatcher(String input , GameMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
