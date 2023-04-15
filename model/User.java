package model;
import controller.Controller;
import controller.Messages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.*;
public class User {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private RecoveryQuestion passwordRecoveryQuestion;
    private String slogan;
    private String passwordRecoveryAnswer;
    private long lastAttempt;
    private int numberOfAttempts;
    private int highScore;
    private int rank;
    public User(String username, String password, String email, String nickname, String slogan,
                RecoveryQuestion passwordRecoveryQuestion, String passwordRecoveryAnswer) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.slogan = slogan;
        this.passwordRecoveryQuestion = passwordRecoveryQuestion;
        this.passwordRecoveryAnswer = passwordRecoveryAnswer;
        this.lastAttempt = 0;
        this.numberOfAttempts = 0;
        this.highScore = 0;
        this.rank = 0; //TODO : CHECK THIS SHIT :)
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail(){
        return email;
    }
    public String getNickname() {
        return nickname;
    }
    public String getSlogan() {
        return slogan;
    }
    public void setLastAttempt() {
        lastAttempt = System.currentTimeMillis();
    }
    public long getLastAttempt() {
        return lastAttempt;
    }
    public void addAttempt(){
        numberOfAttempts++;
    }
    public void resetAttempt(){
        numberOfAttempts = 0;
    }
    public int getAttempt(){
        return numberOfAttempts;
    }
    public RecoveryQuestion getPasswordRecoveryQuestion() {
        return passwordRecoveryQuestion;
    }
    public String getPasswordRecoveryAnswer() {
        return passwordRecoveryAnswer;
    }
    public void setNewPassword(String newPassword) {
        this.password = newPassword;
    }
    public void setNewUsername(String newUsername) {
        this.username = newUsername;
    }
    public void setNewNickname(String newNickname) {
        this.nickname = newNickname;
    }
    public void setNewEmail(String newEmail) {
        this.email = newEmail;
    }
    public void setNewSlogan(String newSlogan) {
        this.slogan = newSlogan;
    }
    public void removeSlogan(){
        this.slogan = "";
    }
    public static boolean isUsernameValid(String username) {
        return username.matches("[\\w_]+");
    }
    public static Messages isPasswordStrong(String password) {
        boolean containsSmallLetter = Pattern.compile("[a-z]").matcher(password).find();
        boolean containsCapitalLetter = Pattern.compile("[A-Z]").matcher(password).find();
        boolean containsDigit = Pattern.compile("\\d").matcher(password).find();
        boolean containsNonWord = Pattern.compile("[\\W_]").matcher(password).find();
        if(password.length() < 6) return Messages.WEAK_PASSWORD_LENGTH;
        if(!containsSmallLetter || !containsCapitalLetter || !containsDigit || !containsNonWord) 
            return Messages.WEAK_PASSWORD_CHARACTERS;
        return Messages.STRONG_PASSWORD;
    }
    public static boolean isEmailValid(String email) {
        return email.matches("[\\w_\\.]+@[\\w_\\.]+\\.[\\w_\\.]+");
    }
    public static String getPasswordFromFile(String username) throws IOException{
        File file = new File(username+".txt");
        Scanner line = new Scanner(file);
        line.nextLine();
        String password = line.nextLine();
        line.close();
        return password;
    }
    public int getHighScore() {
        return highScore;
    }
    public int getRank() {
        return rank;
    }
    public void createFile(User user) throws IOException ,NoSuchAlgorithmException {
        FileWriter file = new FileWriter(user.getUsername()+".txt");
        file.write(user.getUsername()+"\n"+
                    Controller.toSHA256(user.getPassword())+"\n"+
                    user.getEmail()+"\n"+
                    user.getNickname()+"\n"+
                    RecoveryQuestion.getQuestion(user.getPasswordRecoveryQuestion())+"\n"+
                    user.getPasswordRecoveryAnswer()+"\n"+
                    user.getSlogan()+"\n"+
                    user.getHighScore()+"\n"+
                    user.getRank()+"\n");
        file.close();
    }
    
}