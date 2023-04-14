package view;
import controller.ProfileMenuCommands;
import controller.ProfileMenuController;
import controller.Controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
public class ProfileMenu {
    private ProfileMenuController profileMenuController = new ProfileMenuController(Controller.currentUser);
    public void run(Scanner scanner) throws IOException,NoSuchAlgorithmException{
        while(true){
            String input = scanner.nextLine();
            if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_USERNAME)!=null)
                System.out.println(changeUsername(input));
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_NICKNAME)!=null)
                System.out.println(changeNickname(input));
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_PASSWORD)!=null)
                System.out.println(changePassword(input));
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_EMAIL)!=null)
                System.out.println(changeEmail(input));
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_SLOGAN)!=null)
                System.out.println(changeSlogan(input));
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.REMOVE_SLOGAN)!=null)
                System.out.println(profileMenuController.removeSlogan());
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_HIGHSCORE)!=null)
                System.out.println(profileMenuController.showHighScore());
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_RANK)!=null)
                System.out.println(profileMenuController.showRank());
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_SLOGAN)!=null)
                System.out.println(profileMenuController.showSlogan());
            else if(ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_INFO)!=null)
                System.out.println(profileMenuController.showInfo());
            else
                System.out.println("Invalid command!");
        }
    }
    private String changeUsername(String input) throws IOException,NoSuchAlgorithmException{
        String username = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_USERNAME).group("username");
        switch(profileMenuController.changeUsername(username)){
            case INVALID_USERNAME:
                return "Invalid username format!";
            case USERNAME_EXISTS:
                return "This username already exists!";
            case CHANGE_USERNAME_SUCCESSFULL:
                return "Your username changed successfully!";
            default:
                break;
        }
        return null;
    }
    private String changeNickname(String input) throws IOException,NoSuchAlgorithmException{
        String nickname = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_NICKNAME).group("nickname");
        profileMenuController.changeNickname(nickname);
        return "Your nickname changed successfully!";
    }
    private String changePassword(String input) throws IOException,NoSuchAlgorithmException{
        String newPassword = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_PASSWORD).group("new");
        String oldPassword = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_PASSWORD).group("old");
        switch(profileMenuController.changePassword(oldPassword, newPassword)){
            case INCORRECT_PASSWORD:
                return "Incorrect password!";
            case WEAK_PASSWORD_LENGTH:
                return "Your password should be at least 6 characters long!";
            case WEAK_PASSWORD_CHARACTERS:
                String upper = "1. At least one upper case letter.";
                String lower = "2. At least one lower case letter.";
                String digit = "3. At least one digit.";
                String nonWord = "4. At least one non-letter and non-digit character.";
                return "Your password must contain the following characters:\n"+upper+"\n"+lower+"\n"+digit+"\n"+nonWord; 
            default:
                break;
        }
        return "password changed successfully!";
    }
    private String changeSlogan(String input) throws IOException,NoSuchAlgorithmException{
        String slogan = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_SLOGAN).group("slogan");
        profileMenuController.changeSlogan(slogan);
        return "Your slogan changed successfully!";
    }
    private String changeEmail(String input) throws IOException,NoSuchAlgorithmException{
        String email = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_EMAIL).group("email");
        switch(profileMenuController.changeEmail(email)){
            case EMAIL_EXISTS:
                return "This email already exists!";
            case INVALID_EMAIL_FORMAT:
                return "Invalid email format!";
            case CHANGE_EMAIL_SUCCESSFULL:
                return "Your email changed successfully!";
            default:
                break;
        }
        return null;
    }
    
}