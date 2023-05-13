package view;

import controller.ProfileMenuCommands;
import controller.ProfileMenuController;
import controller.Controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ProfileMenu {
    private final ProfileMenuController controller = new ProfileMenuController();

    public void run(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        Controller.menuPrinter.print("PROFILE MENU", Colors.RED_BACKGROUND, 25, 1);
        controller.refreshProfile();
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return;
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_USERNAME) != null)
                System.out.println(changeUsername(input));
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_NICKNAME) != null)
                System.out.println(changeNickname(input));
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_PASSWORD) != null)
                System.out.println(changePassword(input));
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_EMAIL) != null)
                System.out.println(changeEmail(input));
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_SLOGAN) != null)
                System.out.println(changeSlogan(input));
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.REMOVE_SLOGAN) != null)
                System.out.println(removeSlogan());
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_HIGHSCORE) != null)
                System.out.println(showHighScore());
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_RANK) != null)
                System.out.println(showRank());
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_SLOGAN) != null)
                System.out.println(showSlogan());
            else if (ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.SHOW_INFO) != null)
                System.out.println(showInfo());
            else
                System.out.println("Invalid command!");
        }
    }

    public String showInfo() {
        String[] info = controller.getInfo();
        return ("Username : " + info[0] +
                "\nNickname : " + info[1] +
                "\nEmail : " + info[2] +
                "\nSlogan : " + info[3] +
                "\nHighest score : " + info[4] +
                "\nRank : " + info[5]);
    }

    private String showSlogan() {
        String slogan = controller.getSlogan();
        if (slogan.equals(""))
            return "Slogan is empty!";
        return "Your slogan is : " + slogan;
    }

    private String showRank() {
        return "Your rank among all of the players is : " + controller.getRank();

    }

    private String showHighScore() {
        return "Your highest score is : " + controller.getHighScore();
    }

    private String removeSlogan() throws IOException, NoSuchAlgorithmException {
        controller.removeSlogan();
        return "Slogan removed successfully!";
    }

    private String changeUsername(String input) throws IOException, NoSuchAlgorithmException {
        String username = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_USERNAME).group("username");
        switch (controller.changeUsername(username)) {
            case INVALID_USERNAME:
                return "Invalid username format!";
            case USERNAME_EXISTS:
                return "This username already exists!";
            case CHANGE_USERNAME_SUCCESSFUL:
                return "Username changed successfully!";
            default:
                break;
        }
        return null;
    }

    private String changeNickname(String input) throws IOException, NoSuchAlgorithmException {
        String nickname = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_NICKNAME).group("nickname");
        controller.changeNickname(nickname);
        return "Your nickname changed successfully!";
    }

    private String changePassword(String input) throws IOException, NoSuchAlgorithmException {
        String newPassword = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_PASSWORD).group("new"),
                oldPassword = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_PASSWORD).group("old");
        switch (controller.changePassword(oldPassword, newPassword)) {
            case INCORRECT_PASSWORD:
                return "Incorrect password!";
            case WEAK_PASSWORD_LENGTH:
                return "Your password should be at least 6 characters long!";
            case WEAK_PASSWORD_CHARACTERS:
                String upper = "1. At least one upper case letter.";
                String lower = "2. At least one lower case letter.";
                String digit = "3. At least one digit.";
                String nonWord = "4. At least one non-letter and non-digit character.";
                return "Your password must contain the following characters:\n"
                        + upper + "\n"
                        + lower + "\n"
                        + digit + "\n"
                        + nonWord;
            default:
                break;
        }
        return "Password changed successfully!";
    }

    private String changeSlogan(String input) throws IOException, NoSuchAlgorithmException {
        String slogan = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_SLOGAN).group("slogan");
        controller.changeSlogan(slogan);
        return "Slogan changed successfully!";
    }

    private String changeEmail(String input) throws IOException, NoSuchAlgorithmException {
        String email = ProfileMenuCommands.getMatcher(input, ProfileMenuCommands.CHANGE_EMAIL).group("email");
        switch (controller.changeEmail(email)) {
            case EMAIL_EXISTS:
                return "This email already exists!";
            case INVALID_EMAIL_FORMAT:
                return "Invalid email format!";
            case CHANGE_EMAIL_SUCCESSFUL:
                return "Email changed successfully!";
            default:
                break;
        }
        return null;
    }

}