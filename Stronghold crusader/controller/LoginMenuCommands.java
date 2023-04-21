package controller;
import java.util.regex.*;
public enum LoginMenuCommands {
    CREATE_USER("^\\s*user\\s+create\\s+(?<content>.+)$"),
    QUESTION_PICK("^\\s*question\\s+pick\\s+(?<content>.+)$"),
    USER_LOGIN("^\\s*user\\s+login\\s+(?<content>.+)$"),
    FORGOT_MY_PASSWORD("\\s*forgot\\s+my\\s+password\\s*"),
    USER_LOGOUT("\\s*user\\s+logout\\s*"),
    USERNAME("-u\\s+(?<username>\\S*|(\".*\"))"),
    PASSWORD_LOGIN("-p\\s+(?<password>\\S*|(\".*\"))"),
    PASSWORD("-p\\s+((?<random>random)|((?<password>\\S*|(\".*\"))\\s+(?<passwordConfirm>\\S+|(\".*\"))))"),
    NICKNAME("-n\\s+(?<nickname>\\S*|(\".*\"))"),
    EMAIL("-e\\s+(?<email>\\S*)"),
    SLOGAN("-s\\s+((?<random>random)|(?<slogan>\\S*|(\".*\")))"),
    QUESTION_NUMBER("-q\\s+(?<questionNumber>\\d+)"),
    ANSWER("-a\\s+(?<answer>\\S+|(\".+\"))"),
    ANSWER_CONFIRM("-c\\s+(?<answerConfirm>\\S+|(\".+\"))"),
    CHANGE_PASSWORD("^-u\\s+(?<username>\\S*|(\".*\"))\\s+-p\\s+(?<password>\\S*|(\".*\"))$");
    
    private String regex;
    private LoginMenuCommands(String regex){
        this.regex = regex;
    }
    public static Matcher getMatcher(String input , LoginMenuCommands command){
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}