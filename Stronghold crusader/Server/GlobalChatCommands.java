package Server;

import Client.controller.LoginMenuCommands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GlobalChatCommands {
    SHOW_MESSAGES("\\s*show\\s*messages\\s*"),
    SEND_MESSAGE("\\s*send\\s+-m (?<message>)\\s*")
    ;

    private String regex;

    private GlobalChatCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, GlobalChatCommands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return (matcher.find()) ? matcher : null;
    }
}
