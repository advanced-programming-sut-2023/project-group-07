package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import model.User;
import model.RecoveryQuestion;

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.ArrayList;

public class LoginMenuController {
    private User.Information information;
    private int currentCaptcha;
    private int timerCount = -1;

    public String usernameExistenceCheck(String username) {
        if (User.getUserByUsername(username) == null)
            return null;
        int counter = 1;
        while (User.getUserByUsername(username + counter) != null)
            counter++;
        return username + counter;
    }

    public int getTimerCount() {
        return timerCount;
    }

    public void setTimerCount(int newValue) {
        timerCount = newValue;
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
            return Messages.FAIL;
        else if (!User.isUsernameValid(username))
            return Messages.FAIL;
        else if (usernameExistenceCheck(username) != null)
            return Messages.FAIL;
        if (!User.isPasswordStrong(password).equals(Messages.STRONG_PASSWORD))
            return User.isPasswordStrong(password);
        if (!password.equals(passwordConfirm))
            return Messages.FAIL;
        if (emailExistenceCheck(email))
            return Messages.FAIL;
        if (!User.isEmailValid(email))
            return Messages.FAIL;
        password = Controller.toSHA256(password);
        information = new User.Information(username, password, email, nickname, slogan);
        return Messages.SUCCESS;
    }

    public Messages signUp(String recoveryQuestion, String recoveryAnswer, String recoveryAnswerConfirm, String captcha) throws IOException, NoSuchAlgorithmException {
        if (!recoveryAnswer.equals(recoveryAnswerConfirm))
            return Messages.FAIL;
        if (!captcha.equals(String.valueOf(currentCaptcha)))
            return Messages.FAIL;
        User user = new User(information, RecoveryQuestion.getRecoveryQuestion(recoveryQuestion), recoveryAnswer);
        User.addUser(user);
        return Messages.SUCCESS;
    }

    public Messages login(String username, String password, boolean stayLoggedIn, String captcha)
            throws IOException, NoSuchAlgorithmException {
        User user = User.getUserByUsername(username);
        if (user == null)
            return Messages.FAIL;
        else if (user != null && timerCount >= 0) {
            return Messages.FAIL;
        } else if (user != null && !user.checkPassword(password)) {
            user.setLastAttempt();
            user.addAttempt();
            timerCount = (int) Math.floor(user.getAttempt() * 5 + (user.getLastAttempt() - System.currentTimeMillis())/1000);
            return Messages.WAIT_FOR_LOGIN;
        }
        if (!captcha.equals(String.valueOf(currentCaptcha)))
            return Messages.FAIL;
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
        return Messages.SUCCESS;
    }

    public boolean doeHaveTimer(String username) {
        User user = User.getUserByUsername(username);
        int time;
        if (user != null && (time = (int) Math.floor(user.getAttempt() * 5 + (user.getLastAttempt() - System.currentTimeMillis())/1000)) >= 0){
            timerCount = time;
            return true;
        }
        return false;

    }

    public Messages forgotPassword(String username, String password) throws IOException, NoSuchAlgorithmException {
        User user = User.getUserByUsername(username);
        if (user == null)
            return Messages.USERNAME_NOT_FOUND;
        String recoveryQuestion = user.getPasswordRecoveryQuestion().toString();
        //String answer = LoginMenu.getPasswordRecoveryAnswer(recoveryQuestion);
        //if (!answer.equals(user.getPasswordRecoveryAnswer()))
            //return Messages.INCORRECT_ANSWER;
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
