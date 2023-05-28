package controller;

import java.util.regex.Pattern;
import com.google.gson.*;
import javafx.scene.image.Image;
import model.User;
import view.LoginMenu;
import model.RecoveryQuestion;

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.ArrayList;

public class LoginMenuController {
    private User.Information information;
    private int currentCaptcha;

    public String usernameExistenceCheck(String username) {
        if (User.getUserByUsername(username) == null)
            return null;
        int counter = 1;
        while (User.getUserByUsername(username + counter) != null)
            counter++;
        return username + counter;
    }

    public String randomPasswordGenerator() {
        ArrayList<Character> passwordHolder = new ArrayList<Character>();
        String password = "", characters = "+=-_()\\*&^%$#@!`{}\"\'.,/:|~?><;";
        int length = Controller.randomNumber(7) + 3;
        int[] randomPositions = new int[4];
        for (int i = 0; i < 4; i++)
            randomPositions[i] = Controller.randomNumber(length);
        for (int i = 0; i < length; i++) {
            if (i == randomPositions[0])
                passwordHolder.add(Controller.getRandomChar('a', 26));
            if (i == randomPositions[1])
                passwordHolder.add(Controller.getRandomChar('A', 26));
            if (i == randomPositions[2])
                passwordHolder.add(Controller.getRandomChar('0', 10));
            if (i == randomPositions[3])
                passwordHolder.add(characters.charAt(Controller.randomNumber(characters.length())));
            switch (Controller.randomNumber(4)) {
                case 0:
                    passwordHolder.add(Controller.getRandomChar('a', 26));
                    break;
                case 1:
                    passwordHolder.add(Controller.getRandomChar('A', 26));
                    break;
                case 2:
                    passwordHolder.add(Controller.getRandomChar('0', 10));
                    break;
                case 3:
                    passwordHolder.add(characters.charAt(Controller.randomNumber(characters.length())));
                    break;
            }
        }
        Collections.shuffle(passwordHolder);
        for (char c : passwordHolder)
            password += c;
        return password;
    }

    private boolean emailExistenceCheck(String email) {
        if (User.getUserByEmail(email) == null)
            return false;
        return true;
    }

    public boolean stayLoggedIn(String string) {
        if (Pattern.compile(string).matcher("--stay-logged-in").find())
            return true;
        return false;
    }

    public Messages getInformation(String username, String password, String passwordConfirm, String email, String nickname,
                                   String slogan) throws NoSuchAlgorithmException {
        if (password.isBlank() || nickname.isBlank() || username.isBlank() || email.isBlank() || (slogan != null && slogan.isBlank()))
            return Messages.EMPTY_FIELD;
        else if (!User.isUsernameValid(username))
            return Messages.INVALID_USERNAME;
        else if (usernameExistenceCheck(username) != null)
            return Messages.USERNAME_EXISTS;
        if (!User.isPasswordStrong(password).equals(Messages.STRONG_PASSWORD))
            return User.isPasswordStrong(password);
        if (!password.equals(passwordConfirm))
            return Messages.PASSWORD_NOT_CONFIRMED;
        if (emailExistenceCheck(email))
            return Messages.EMAIL_EXISTS;
        if (!User.isEmailValid(email))
            return Messages.INVALID_EMAIL_FORMAT;
        password = Controller.toSHA256(password);
        information = new User.Information(username, password, email, nickname, slogan);
        return Messages.SUCCESS;
    }

    public Messages signUp(RecoveryQuestion recoveryQuestion, String recoveryAnswer, String recoveryAnswerConfirm) throws IOException, NoSuchAlgorithmException {
        User user = new User(information, recoveryQuestion, recoveryAnswer);
        User.addUser(user);
        return Messages.SUCCESS;
    }

    public Messages login(String username, String password, boolean stayLoggedIn)
            throws IOException, NoSuchAlgorithmException {
        User user = User.getUserByUsername(username);
        if (user == null)
            return Messages.USERNAME_NOT_FOUND;
        else if (user != null && user.getLastAttempt() + 1000 * user.getAttempt() * 5 > System.currentTimeMillis()) {
            return Messages.WAIT_FOR_LOGIN;
        } else if (user != null && !user.checkPassword(password)) {
            user.setLastAttempt();
            user.addAttempt();
            return Messages.INCORRECT_PASSWORD;
        }
        if (!LoginMenu.checkCaptcha())
            return Messages.EXIT_CAPTCHA;
        user.setLastAttempt();
        user.resetAttempt();
        Controller.currentUser = user;
        if (stayLoggedIn) {
            FileWriter file = new FileWriter("Stronghold crusader/DB/stayLoggedIn");
            Gson gson = new Gson();
            String userString = gson.toJson(user);
            file.write(userString);
            file.close();
        }
        return Messages.LOGIN_SUCCESSFUL;
    }

    public Messages forgotPassword(String username, String password) throws IOException, NoSuchAlgorithmException {
        User user = User.getUserByUsername(username);
        if (user == null)
            return Messages.USERNAME_NOT_FOUND;
        String recoveryQuestion = RecoveryQuestion.getQuestion(user.getPasswordRecoveryQuestion());
        String answer = LoginMenu.getPasswordRecoveryAnswer(recoveryQuestion);
        if (!answer.equals(user.getPasswordRecoveryAnswer()))
            return Messages.INCORRECT_ANSWER;
        if (!User.isPasswordStrong(password).equals(Messages.STRONG_PASSWORD))
            return User.isPasswordStrong(password);
        user.setNewPassword(password);
        User.updateUsers();
        return Messages.PASSWORD_CHANGED;
    }

    public Image generateCaptcha() {
        int captcha = Controller.getRandomCaptcha();
        Image image = new Image(LoginMenuController.class.getResource("/Images/Captcha/" + String.valueOf(captcha) + ".png").toString());
        currentCaptcha = captcha;
        return image;
    }

    public int getCurrentCaptcha() {
        return currentCaptcha;
    }
}
