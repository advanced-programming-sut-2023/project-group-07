package controller;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public enum CreateMapMenuCommands {
    SET_PIXEL_TEXTURE("^\\s*settexture\\s+(\\-x\\D|\\-y\\D|\\-t|[^\\-][a-z]|\\-?\\d+|\\s)+$"),
    GET_ROW("\\-x\\s+(?<row>\\-?\\d+)"),
    GET_COLOUMN("\\-y\\s+(?<column>\\-?\\d+)"),
    SET_REGION_TEXTURE("^\\s*settexture\\s+(\\-x1|\\-y1|\\-x2|\\-y2|\\-t|[^\\-][a-z]|\\-?\\d+|\\s)+$"),
    GET_FIRST_ROW("\\-x1\\s+(?<frow>\\-?\\d+)"),
    GET_FIRST_COLUMN("\\-y1\\s+(?<fcolumn>\\-?\\d+)"),
    GET_SECOND_ROW("\\-x2\\s+(?<srow>\\-?\\d+)"),
    GET_SECOND_COLUMN("\\-y2\\s+(?<scolumn>\\-?\\d+)"),
    SET_TEXTURE_TYPE("\\-t\\s+(?<type>[a-zA-Z\\s]+)"),
    CLEAR_PIXEL("^\\s*clear\\s+(\\-x|\\-y|\\-?\\d+|\\s)+$");
    private String regex;
    private CreateMapMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , CreateMapMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}