package controller;
import java.util.regex.Pattern;

import model.User;
import view.LoginMenu;
import controller.Controller;
import model.RecoveryQuestion;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;
public class LoginMenuController {
    String answer;
    RecoveryQuestion recoveryQuestion;
   
    private String usernameExistenceCheck(String username) {
        if(Controller.getUserByUsername(username)==null)return null;
        int counter = 1;
        while(Controller.getUserByUsername(username + counter)!=null)counter++;
        return username + counter;
    }
    private String randomPasswordGenerator(){
        String password = "" , characters = "+=-_()\\*&^%$#@!`{}\"\'.,/:|~?><;";
        int length = Controller.randomNumber(7) + 3;
        int[] randomPositions = new int[4];
        for(int i = 0 ; i < 4 ; i++)randomPositions[i] = Controller.randomNumber(length);
        for (int i = 0; i < length; i++) {
            if(i == randomPositions[0])password += Controller.getRandomChar('a' , 26);
            if(i == randomPositions[1])password += Controller.getRandomChar('A' , 26);
            if(i == randomPositions[2])password += Controller.getRandomChar('0' , 10);
            if(i == randomPositions[3])password += characters.charAt(Controller.randomNumber(characters.length()));
            switch((Controller.randomNumber(100) * i + Controller.randomNumber(2)) % 4){
                case 0:
                    password += Controller.getRandomChar('a' , 26);
                    break;
                case 1:
                    password += Controller.getRandomChar('A' , 26);
                    break;
                case 2:
                    password += Controller.getRandomChar('0' , 10);
                    break;
                case 3:
                    password += characters.charAt(Controller.randomNumber(characters.length()));
                    break;
            }
        }
        return password;
    }
    private boolean emailExistenceCheck(String email){
        if(Controller.getUserByEmail(email) == null) return false;
        return true;
    }
    public boolean stayLoggedIn(String string) {
        if(Pattern.compile(string).matcher("--stay-logged-in").find()) return true;
        return false;
    }
    public Messages signUp(String username,String password,String passwordConfirm, String email, String slogan , String nickname)throws IOException,NoSuchAlgorithmException{
        if(password.length()==0 || nickname.length()==0 || username.length()==0 || email.length()==0) return Messages.EMPTY_FIELD;
        else if(!User.isUsernameValid(username)) return Messages.INVALID_USERNAME;
        else if(usernameExistenceCheck(username) != null){
            if(LoginMenu.doYouWantSuggestedUsername(usernameExistenceCheck(username))) username = usernameExistenceCheck(username);
            else return Messages.SIGNUP_FAILED;
        }
        else if(password.equals("random")){
            String randomPassword = randomPasswordGenerator();
            String newPassword = LoginMenu.confirmeRandomPassword(randomPassword);
            if(newPassword.equals(randomPassword)){
                password = randomPassword;
                passwordConfirm = randomPassword;
            }
            else return Messages.PASSWORD_NOT_CONFIRMED;
        }
        if(!User.isPasswordStrong(password).equals(Messages.STRONG_PASSWORD)) return User.isPasswordStrong(password);
        if(!password.equals(passwordConfirm)) return Messages.PASSWORD_NOT_CONFIRMED;
        if(emailExistenceCheck(email)) return Messages.EMAIL_EXISTS;
        if(!User.isEmailValid(email)) return Messages.INVALID_EMAIL_FORMAT;
        else{
            System.out.println("Pick your security question:" +
                                "\n1. " + RecoveryQuestion.getQuestion(RecoveryQuestion.FATHERNAME) + 
                                "\n2. " + RecoveryQuestion.getQuestion(RecoveryQuestion.MOTHERNAME) +
                                "\n3. " + RecoveryQuestion.getQuestion(RecoveryQuestion.PETNAME));
            Messages recoveryAnswer = recoveryQuestion();
            if(!recoveryAnswer.equals(Messages.ANSWER_ACCEPTED)) return recoveryAnswer;
        }
        if(!LoginMenu.checkCaptcha()) return Messages.EXIT_CAPTCHA;
        User user = new User(username, password, email, nickname, slogan, recoveryQuestion, answer);
        Controller.addUser(user);
        user.createFile(user);
        return Messages.SIGNUP_SUCCESSFUL;
    }
    private Messages recoveryQuestion(){
        String questionInput = LoginMenu.pickRecoveryQuestion();
        int questionNumber = Integer.parseInt(LoginMenuCommands.getMatcher(questionInput, LoginMenuCommands.QUESTION_NUMBER).group("questionNumber"));
        String answer = LoginMenuCommands.getMatcher(questionInput, LoginMenuCommands.ANSWER).group("answer");
        String answerConfirm = LoginMenuCommands.getMatcher(questionInput, LoginMenuCommands.ANSWER_CONFIRM).group("answerConfirm");
        if(questionNumber>3) return Messages.INVALID_QUESTION_NUMBER;
        else if(!answer.equals(answerConfirm)) return Messages.ANSWER_NOT_CONFIRMED;
        else{
            switch(questionNumber){
                case 1:
                    recoveryQuestion = RecoveryQuestion.FATHERNAME;
                    break;
                case 2:
                    recoveryQuestion = RecoveryQuestion.MOTHERNAME;
                    break;
                case 3:
                    recoveryQuestion = RecoveryQuestion.PETNAME;
                    break;
            }
            this.answer = answer;
            return Messages.ANSWER_ACCEPTED;
        }
    }
    public Messages login(String username, String password , boolean stayLoggedIn) throws IOException, NoSuchAlgorithmException{
        User user = Controller.getUserByUsername(username);
        if(user == null) return Messages.USERNAME_NOT_FOUND;
        else if(user != null && user.getLastAttempt()+1000*user.getAttempt()*5 > System.currentTimeMillis()){
            return Messages.WAIT_FOR_LOGIN;
        }
        else if(user != null && !User.getPasswordFromFile(username).equals(Controller.toSHA256(password))){
            user.setLastAttempt();
            user.addAttempt();
            return Messages.INCORRECT_PASSWORD;
        }
        if(!LoginMenu.checkCaptcha()) return Messages.EXIT_CAPTCHA;
        user.setLastAttempt();
        user.resetAttempt();
        Controller.currentUser = user;
        return Messages.LOGIN_SUCCESSFUL;
    }
    public Messages forgotPassword(String username,String password) throws IOException, NoSuchAlgorithmException{
        User user = Controller.getUserByUsername(username);
        if(user==null) return Messages.USERNAME_NOT_FOUND;
        String recoveryQuestion = RecoveryQuestion.getQuestion(user.getPasswordRecoveryQuestion());
        String answer = LoginMenu.getPasswordRecoveryAnswer(recoveryQuestion);
        if(!answer.equals(user.getPasswordRecoveryAnswer())) return Messages.INCORRECT_ANSWER;
        if(!User.isPasswordStrong(password).equals(Messages.STRONG_PASSWORD)) return User.isPasswordStrong(password);
        user.setNewPassword(password);
        user.createFile(user);
        return Messages.PASSWORD_CHANGED;
    }
}
