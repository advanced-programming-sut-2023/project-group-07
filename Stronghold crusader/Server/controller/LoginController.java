package Server.controller;

import Server.controller.OutputMessage;
import controller.Controller;
import controller.Messages;
import model.RecoveryQuestion;
import model.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginController {
    public static User login(String username, String password) {
        User user = User.getUserByUsername(username);
        try {
            if (user == null || !user.checkPassword(password)) return null;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static OutputMessage singUp(String username, String nickname, String password, String email) {
        if (User.getUserByUsername(username) != null) return OutputMessage.USERNAME_EXISTS;
        if (User.getUserByEmail(email) != null) return OutputMessage.EMAIL_EXISTS;
        if (!User.isPasswordStrong(password).equals(Messages.STRONG_PASSWORD))
            return OutputMessage.WEEK_PASSWORD;
        if (!User.isUsernameValid(username)) return OutputMessage.INVALID_USERNAME;
        try {
            password = Controller.toSHA256(password);
            User user = new User(new User.Information(username, password, email, nickname, ""),
                    RecoveryQuestion.FATHERNAME,
                    "answer");

            User.addUser(user);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return OutputMessage.SUCCESSFUL;
    }

    public static User getUser(String username) {
        return User.getUserByUsername(username);
    }
}
