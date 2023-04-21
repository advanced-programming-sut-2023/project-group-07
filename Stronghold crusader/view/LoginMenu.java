package view;

import controller.LoginMenuCommands;
import controller.Messages;
import model.User;
import controller.LoginMenuController;
import controller.Controller;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.*;

public class LoginMenu {
    private static Scanner scanner;
    private final LoginMenuController controller = new LoginMenuController();
    private final MainMenu mainMenu = new MainMenu();
    public void run(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        LoginMenu.scanner = scanner;
        Controller.menuPrinter.print("LOGIN/REGISTER MENU", Colors.BLUE_BACKGROUND, 25);
        while (true) {
            String input = scanner.nextLine();
            if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.CREATE_USER) != null && createUserFormat(input))
                System.out.println(createUser(input));
            else if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.USER_LOGIN) != null) {
                String login = userLogin(input);
                System.out.println(login);
                if (login.equals("Login successful!"))
                    mainMenu.run(scanner);
            } 
            else if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.FORGOT_MY_PASSWORD) != null)
                System.out.println(forgotMyPassword(scanner));
            else
                System.out.println("Invalid command!");
        }
    }

    private String createUser(String input) throws IOException, NoSuchAlgorithmException {
        String username = extractUsername(input);
        String password = extractPassword(input);
        String passwordConfirm = extractPasswordConfirm(input);
        String nickname = extractNickname(input);
        String email = extractEmail(input);
        String slogan = extractSlogan(input);
        if (slogan == null) return "there's an empty field!";

        switch (controller.signUp(username, password, passwordConfirm, email, slogan, nickname)) {
            case EMPTY_FIELD:
                return "There's is an empty field!";
            case INVALID_USERNAME:
                return "Invalid username format!";
            case SIGNUP_FAILED:
                return "Signup failed!";
            case PASSWORD_NOT_CONFIRMED:
                return "Passwords do not match!";
            case WEAK_PASSWORD_LENGTH:
                return "Password length must be at least 6 characters long!";
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
            case EMAIL_EXISTS:
                return "There's already a user with this email address!";
            case INVALID_EMAIL_FORMAT:
                return "Incorrect email format!";
            case INVALID_QUESTION_NUMBER:
                return "Invalid question number!";
            case ANSWER_NOT_CONFIRMED:
                return "Answers do not match!";
            case SIGNUP_SUCCESSFUL:
                return "Signup successful!";
            case EXIT_CAPTCHA:
                return "Signup cancelled!";
            case INVALID_COMMAND:
                return "Invalid command!";
            default:
                break;
        }
        return null;
    }

    private String extractUsername(String input) {
        Matcher usernameMatcher = LoginMenuCommands.getMatcher(input, LoginMenuCommands.USERNAME);
        String username =
                Controller.trimmer(usernameMatcher.group("username"));
        return username;
    }

    private String extractPasswordConfirm(String input) {
        Matcher passwordMatcher = LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD);
        if (passwordMatcher.group("random") != null) return "random";
        else return Controller.trimmer(passwordMatcher.group("password"));
    }

    private String extractPassword(String input) {
        Matcher passwordMatcher = LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD);
        if (passwordMatcher.group("random") != null) return "random";
        else return Controller.trimmer(passwordMatcher.group("passwordConfirm"));

    }

    private String extractNickname(String input) {
        Matcher nicknameMatcher = LoginMenuCommands.getMatcher(input, LoginMenuCommands.NICKNAME);
        String nickname = Controller.trimmer(nicknameMatcher.group("nickname"));
        return nickname;
    }

    private String extractEmail(String input) {
        Matcher emailMatcher = LoginMenuCommands.getMatcher(input, LoginMenuCommands.EMAIL);
        String email = Controller.trimmer(emailMatcher.group("email"));
        return email;
    }

    private String extractSlogan(String input) {
        String slogan = "";
        if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.SLOGAN) != null) {
            Matcher sloganMarcher = LoginMenuCommands.getMatcher(input, LoginMenuCommands.SLOGAN);
            slogan = Controller.trimmer(sloganMarcher.group("slogan"));
            if (slogan.length() == 0) return null;
        }
        return slogan;
    }

    private String userLogin(String input) throws IOException, NoSuchAlgorithmException {
        String username = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.USERNAME).group("username"));
        String password = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD_LOGIN).group("password"));
        switch (controller.login(username, password, false)) {
            case USERNAME_NOT_FOUND:
                return "Username and password didn't match!";
            case WAIT_FOR_LOGIN:
                return "you have to wait for " +
                        User.getUserByUsername(username).getAttempt() * 5 + " more seconds to login!";
            case INCORRECT_PASSWORD:
                return "Incorrect password!";
            case EXIT_CAPTCHA:
                return "Login cancelled!";
            case LOGIN_SUCCESSFUL:
                return "Login successful!";
            default:
                break;
        }
        return null;
    }

    public static boolean doYouWantSuggestedUsername(String suggestedUsername) {
        System.out.println("this username already exists! here is a valid username: " + suggestedUsername
                + "\nenter yes to continue:");
        return LoginMenu.scanner.nextLine().equals("yes");
    }

    public static String confirmeRandomPassword(String randomPassword) {
        System.out.println("Your random password is: " + randomPassword);
        System.out.println("Please re-enter your password here:");
        return LoginMenu.scanner.nextLine();
    }

    public static String pickRecoveryQuestion() {
        String questionInput = "";
        while (LoginMenuCommands.getMatcher(questionInput, LoginMenuCommands.QUESTION_PICK) == null)
            questionInput = LoginMenu.scanner.nextLine();
        return questionInput;
    }

    private boolean createUserFormat(String input) {
        if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.USERNAME) == null) return false;
        if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD) == null) return false;
        if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.EMAIL) == null) return false;
        if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.NICKNAME) == null) return false;
        return true;
    }

    private String forgotMyPassword(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        System.out.println("please enter your username and new password:");
        String input = scanner.nextLine();
        String username, password;
        if (LoginMenuCommands.getMatcher(input, LoginMenuCommands.CHANGE_PASSWORD) != null) {
            username = LoginMenuCommands.getMatcher(input, LoginMenuCommands.USERNAME).group("username");
            password = LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD_LOGIN).group("password");
        } else return "Invalid format!";
        switch (controller.forgotPassword(username, password)) {
            case USERNAME_NOT_FOUND:
                return "Username not found!";
            case INCORRECT_ANSWER:
                return "Incorrect answer";
            case WEAK_PASSWORD_LENGTH:
                return "Password length must be at least 6 characters long!";
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
            case PASSWORD_NOT_CONFIRMED:
                return "Passwords do not match!";
            default:
                break;
        }
        return "Password changed successfully!";
    }

    public static String getPasswordRecoveryAnswer(String recoveryQuestion) {
        System.out.println(recoveryQuestion);
        return LoginMenu.scanner.nextLine();
    }

    public static boolean checkCaptcha() {
        CaptchaPrinter captchaPrinter = new CaptchaPrinter();
        while (true) {
            String captcha = Controller.generateCaptcha(captchaPrinter);
            System.out.println("enter the security code :");
            String input = LoginMenu.scanner.nextLine();
            if (input.equals("cancel")) return false;
            else if (input.equals(captcha)) return true;
        }
    }
}