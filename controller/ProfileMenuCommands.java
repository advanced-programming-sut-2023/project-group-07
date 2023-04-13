package controller;
import java.util.regex.*;

public enum ProfileMenuCommands {
    CHANGE_USERNAME("^\\s*profile\\s+change\\s+-u\\s+(?<username>\\S*|(\".*\"))\\s*$"),
    CHANGE_NICKNAME("^\\s*profile\\s+change\\s+-n\\s+(?<nickname>\\S*|(\".*\"))\\s*$"),
    CHANGE_PASSWORD("^\\s*profile\\s+change\\s+password\\s+(-n\\s+(?<new>\\S*|(\".*\"))\\s+-o\\s+(?<old>\\S*|(\".*\")))\\s*$"),
    CHANGE_EMAIL("^\\s*profile\\s+change\\s+-e\\s+(?<email>\\S*)\\s*$"),
    CHANGE_SLOGAN("^\\s*profile\\s+change\\s+slogan\\s+-s\\s+(?<slogan>\\S*|(\".*\"))\\s*$"),
    REMOVE_SLOGAN("^\\s*Profile\\s+remove\\s+slogan\\s*$"),
    SHOW_HIGHSCORE("^\\s*profile\\s+display\\s+highscore\\s*$"),
    SHOW_RANK("^\\s*profile\\s+display\\s+highscore\\s*$"),
    SHOW_SLOGAN("^\\s*profile\\s+display\\s+slogan\\s*$"),
    SHOW_INFO("^\\s*profile\\s+display\\s*$");
    private String regex;
    private ProfileMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , ProfileMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
    
}