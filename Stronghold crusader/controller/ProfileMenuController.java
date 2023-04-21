package controller;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import model.*;
public class ProfileMenuController {
    private User currentUser;
    public ProfileMenuController(User currentUser) {
        this.currentUser = currentUser;
    }
    public Messages changeUsername(String username) throws IOException,NoSuchAlgorithmException{
        if(!User.isUsernameValid(username))return Messages.INVALID_USERNAME;
        else if(User.getUserByUsername(username)!=null)return Messages.USERNAME_EXISTS;
        this.currentUser.setNewUsername(username);
        User.updateUsers();
        return Messages.CHANGE_USERNAME_SUCCESSFUL;
    }
    public Messages changePassword(String oldPassword,String newPassword) throws IOException,NoSuchAlgorithmException{
        if(!currentUser.checkPassword(oldPassword))
            return Messages.INCORRECT_PASSWORD;
        else if(!User.isPasswordStrong(newPassword).equals(Messages.STRONG_PASSWORD)) return User.isPasswordStrong(newPassword);
        this.currentUser.setNewPassword(newPassword);
        User.updateUsers();
        return Messages.CHANGE_PASSWORD_SUCCESSFUL;
    }
    public void changeNickname(String nickName) throws IOException,NoSuchAlgorithmException{
        this.currentUser.setNewNickname(nickName);
        User.updateUsers();
    }
    public Messages changeEmail(String email) throws IOException,NoSuchAlgorithmException{
        if(User.getUserByEmail(email)!=null)return Messages.EMAIL_EXISTS;
        else if(!User.isEmailValid(email))return Messages.INVALID_EMAIL_FORMAT;
        this.currentUser.setNewEmail(email);
        User.updateUsers();
        return Messages.CHANGE_EMAIL_SUCCESSFUL;
    }
    public void changeSlogan(String slogan) throws IOException,NoSuchAlgorithmException{
        this.currentUser.setNewSlogan(slogan);
        User.updateUsers();
    }
    public String removeSlogan() throws IOException,NoSuchAlgorithmException{
        this.currentUser.removeSlogan();
        User.updateUsers();
        return "Slogan removed successfully!";
    }
    public String showHighScore(){
        return "Your highest score is : " + this.currentUser.getHighScore();
    }
    public String showRank(){
        return "Your rank among all of the players is : " + this.currentUser.getRank();
    }
    public String showSlogan(){
        if(this.currentUser.getSlogan().equals(""))return "Slogan is empty!";
        return "Your slogan is : " + this.currentUser.getSlogan();
    }
    public String showInfo(){
        return ("Username : " + this.currentUser.getUsername() +
                "\nNickname : " + this.currentUser.getNickname() +
                "\nEmail : " + this.currentUser.getEmail() +
                "\nSlogan : " + this.currentUser.getSlogan() +
                "\nHighest score : " + this.currentUser.getHighScore() +
                "\nRank : " + this.currentUser.getRank());
    }
    
}