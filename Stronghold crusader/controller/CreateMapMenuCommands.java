package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CreateMapMenuCommands {
    SET_PIXEL_TEXTURE("^\\s*settexture\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\-t\\s+[a-z\\s]+|\\s)+$"),
    GET_ROW("\\-x\\s+(?<row>\\-?\\d+)"),
    GET_COLUMN("\\-y\\s+(?<column>\\-?\\d+)"),
    SET_REGION_TEXTURE(
            "^\\s*settexture\\s+(\\-x1\\s+\\-?\\d+|\\-y1\\s+\\-?\\d+|\\-x2\\s+\\-?\\d+|\\-y2\\s+\\-?\\d+|\\-t\\s+[a-z\\s]+|\\s)+$"),
    GET_FIRST_ROW("\\-x1\\s+(?<frow>\\-?\\d+)"),
    GET_FIRST_COLUMN("\\-y1\\s+(?<fcolumn>\\-?\\d+)"),
    GET_SECOND_ROW("\\-x2\\s+(?<srow>\\-?\\d+)"),
    GET_SECOND_COLUMN("\\-y2\\s+(?<scolumn>\\-?\\d+)"),
    GET_TYPE("\\-t\\s+(?<type>[a-z\\s]+)"),
    CLEAR_PIXEL("^\\s*clear\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\s)+$"),
    CLEAR_REGION("^\\s*clear\\s+(\\-x1\\s+\\-?\\d+|\\-y1\\s+\\-?\\d+|\\-x2\\s+\\-?\\d+|\\-y2\\s+\\-?\\d+|\\s)+$"),
    DROP_TREE("^\\s*droptree\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\-t\\s+[a-z\\s]+|\\s)+$"),
    DROP_ROCK("^\\s*droprock\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\-d\\s+[a-z]+|\\s)+$"),
    GET_DIRECTION("\\-d\\s+(?<direction>\\S+)"),
    REMOVE_MAP("^\\s*remove\\s+map\\s*$"),
    DROP_UNIT(
            "^\\s*dropunit\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\-t\\s+[a-z\\s]+|\\-c\\s+\\d+|\\-color\\s+[a-z]+|\\s)+$"),
    DROP_BUILDING(
            "^\\s*dropbuilding\\s+(\\-x\\s+\\-?\\d+|\\-y\\s+\\-?\\d+|\\-t\\s+[a-z\\s]+|\\-color\\s+[a-z]+|\\s)+$"),
    GET_COLOR("\\-color\\s+(?<color>[a-z]+)"),
    GET_COUNT("\\-c\\s+(?<count>\\d+)");

    private String regex;

    private CreateMapMenuCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, CreateMapMenuCommands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
