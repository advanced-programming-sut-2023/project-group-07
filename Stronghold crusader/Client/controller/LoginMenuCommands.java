package Client.controller;

import java.util.regex.*;

public enum LoginMenuCommands {
    CREATE_USER(
            "^\\s*user\\s+create\\s+(\\-u\\s+([^\"\\s]+|(\"[^\"]*\"))|\\-p\\s+((random)|((\\S+)\\s+(\\S+)))|\\-n\\s+([^\"\\s]+|(\"[^\"]*\"))|\\-e\\s+\\S*|\\-s\\s+((random)|([^\"\\s]+|(\"[^\"]*\")))|\\s)+$"),
    QUESTION_PICK(
            "^\\s*question\\s+pick\\s+(\\-q\\s+\\d+|\\-a\\s+([^\"\\s]+|(\"[^\"]*\"))|\\-c\\s+([^\"\\s]+|(\"[^\"]*\"))|\\s)+$"),
    USER_LOGIN("^\\s*user\\s+login\\s+(\\-u\\s+([^\"\\s]+|(\"[^\"]*\"))|\\-p\\s+\\S+|--stay-logged-in|\\s)+$"),
    FORGOT_MY_PASSWORD("\\s*forgot\\s+my\\s+password\\s*"),
    USER_LOGOUT("\\s*user\\s+logout\\s*"),
    USERNAME("\\-u\\s+(?<username>[^\"\\s]+|(\"[^\"]*\"))"),
    PASSWORD_LOGIN("\\-p\\s+(?<password>\\S+)"),
    PASSWORD("\\-p\\s+((?<random>random)|((?<password>\\S+)\\s+(?<passwordConfirm>\\S+)))"),
    NICKNAME("\\-n\\s+(?<nickname>[^\"\\s]+|(\"[^\"]*\"))"),
    EMAIL("\\-e\\s+(?<email>\\S*)"),
    SLOGAN("\\-s\\s+((?<random>random)|(?<slogan>[^\"\\s]+|(\"[^\"]*\")))"),
    QUESTION_NUMBER("\\-q\\s+(?<questionNumber>\\d+)"),
    ANSWER("\\-a\\s+(?<answer>[^\"\\s]+|(\"[^\"]*\"))"),
    ANSWER_CONFIRM("\\-c\\s+(?<answerConfirm>[^\"\\s]+|(\"[^\"]*\"))"),
    CHANGE_PASSWORD("^\\-u\\s+(?<username>[^\"\\s]+|(\"[^\"]*\"))\\s+\\-p\\s+(?<password>\\S+)$"),
    STAY_LOGGED_IN("\\s*--stay-logged-in\\s*");

    private String regex;

    private LoginMenuCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, LoginMenuCommands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}