package controller;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public enum PreGameMenuCommands {
    SET_PIXEL_TEXTURE("sard"),
    SET_REGION_TEXTURE("sag");

    private String regex;
    private PreGameMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , PreGameMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
