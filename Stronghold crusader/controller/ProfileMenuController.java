package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import model.*;

public class ProfileMenuController {
    private User currentUser = Controller.currentUser;

    public void refreshProfile() {
        this.currentUser = Controller.currentUser;
    }

    public Messages changeUsername(String username) throws IOException, NoSuchAlgorithmException {
        if (username.equals(currentUser.getUsername())) return Messages.NEW_USERNAME_IS_CURRENT_USERNAME;
        else if (!User.isUsernameValid(username))
            return Messages.INVALID_USERNAME;
        else if (User.getUserByUsername(username) != null)
            return Messages.USERNAME_EXISTS;
        this.currentUser.setNewUsername(Controller.trimmer(username));
        User.updateUsers();
        return Messages.CHANGE_USERNAME_SUCCESSFUL;
    }

    public Messages changePassword(String oldPassword, String newPassword)
            throws IOException, NoSuchAlgorithmException {
        if (!currentUser.checkPassword(oldPassword))
            return Messages.INCORRECT_PASSWORD;
        else if (!User.isPasswordStrong(newPassword).equals(Messages.STRONG_PASSWORD))
            return User.isPasswordStrong(newPassword);
        this.currentUser.setNewPassword(newPassword);
        User.updateUsers();
        return Messages.CHANGE_PASSWORD_SUCCESSFUL;
    }
    public Messages changePassword(String oldPassword, String newPassword ,String confirmation)
            throws IOException, NoSuchAlgorithmException {
        if (!newPassword.equals(confirmation)) return Messages.PASSWORD_NOT_CONFIRMED;
        else if (!currentUser.checkPassword(oldPassword))
            return Messages.INCORRECT_PASSWORD;
        else if (!User.isPasswordStrong(newPassword).equals(Messages.STRONG_PASSWORD))
            return User.isPasswordStrong(newPassword);
        this.currentUser.setNewPassword(newPassword);
        User.updateUsers();
        return Messages.CHANGE_PASSWORD_SUCCESSFUL;
    }

    public Messages changeNickname(String nickName) throws IOException, NoSuchAlgorithmException {
        if (nickName.isEmpty()) return Messages.EMPTY_FIELD;
        this.currentUser.setNewNickname(Controller.trimmer(nickName));
        User.updateUsers();
        return Messages.CHANGE_NICKNAME_SUCCESSFUL;
    }

    public Messages changeEmail(String email) throws IOException, NoSuchAlgorithmException {
        if (email.equals(currentUser.getEmail())) return Messages.NEW_EMAIL_IS_CURRENT_EMAIL;
        else if (User.getUserByEmail(email) != null)
            return Messages.EMAIL_EXISTS;
        else if (!User.isEmailValid(email))
            return Messages.INVALID_EMAIL_FORMAT;
        this.currentUser.setNewEmail(email);
        User.updateUsers();
        return Messages.CHANGE_EMAIL_SUCCESSFUL;
    }

    public void changeSlogan(String slogan) throws IOException, NoSuchAlgorithmException {
        this.currentUser.setNewSlogan(Controller.trimmer(slogan));
        User.updateUsers();
    }

    public void removeSlogan() throws IOException, NoSuchAlgorithmException {
        this.currentUser.removeSlogan();
        User.updateUsers();
    }

    public void setAvatarName(String name){
        currentUser.setAvatarName(name);
        User.updateUsers();
    }
    public int getHighScore() {
        return this.currentUser.getHighScore();
    }

    public int getRank() {
        return this.currentUser.getRank();
    }

    public String getSlogan() {
        return this.currentUser.getSlogan();
    }

    public String getUsername() {
        return this.currentUser.getUsername();
    }

    public String getEmail() {
        return this.currentUser.getEmail();
    }

    public String getNickname() {
        return this.currentUser.getNickname();
    }

    public String[] getInfo() {
        String[] info = new String[]{
                this.currentUser.getUsername(),
                this.currentUser.getNickname(),
                this.currentUser.getEmail(),
                this.currentUser.getSlogan(),
                ((Integer) this.currentUser.getHighScore()).toString(),
                ((Integer) this.currentUser.getRank()).toString()
        };
        return info;
    }

    public String getAvatarName() {
        return currentUser.avatarName();
    }

    public boolean doesUsernameExist(String username) {
        return !currentUser.getUsername().equals(username) && (User.getUserByUsername(username) != null) ;
    }

    public boolean doesEmailExist(String email) {
        return !currentUser.getEmail().equals(email) && (User.getUserByEmail(email) != null) ;

    }

}