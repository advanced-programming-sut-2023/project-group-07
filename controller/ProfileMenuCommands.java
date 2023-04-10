package controller;
import java.util.regex.*;

public enum ProfileMenuCommands {
    CHANGEUSERNAME("^\\s*profile\\s+change\\s+-u\\s+(?<username>\\S*|(\".*\"))\\s*$"),
    CHANGENICKNAME("^\\s*profile\\s+change\\s+-n\\s+(?<nickname>\\S*|(\".*\"))\\s*$"),
    CHANGEPASSWORD("^\\s*profile\\s+change\\s+password\\s+(-n\\s+(?<new>\\S*|(\".*\"))\\s+-o\\s+(?<old>\\S*|(\".*\")))\\s*$"),
    CHANGEEMAIL("^\\s*profile\\s+change\\s+-e\\s+(?<email>\\S*)\\s*$"),
    CHANGESLOGAN("^\\s*profile\\s+change\\s+slogan\\s+-s\\s+(?<slogan>\\S*|(\".*\"))\\s*$"),
    REMOVESLOGAN("^\\s*Profile\\s+remove\\s+slogan\\s*$"),
    SHOWHIGHSCORE("^\\s*profile\\s+display\\s+highscore\\s*$"),
    SHOWRANK("^\\s*profile\\s+display\\s+highscore\\s*$"),
    SHOWSLOGAN("^\\s*profile\\s+display\\s+slogan\\s*$"),
    SHOWINFO("^\\s*profile\\s+display\\s*$");
    private String regex;
    private ProfileMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , ProfileMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
    
}