package view;
import controller.LoginMenuCommands;
import controller.Messages;
import controller.LoginMenuController;
import controller.Controller;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.*;
public class LoginMenu {
    private LoginMenuController loginMenuController = new LoginMenuController();
    public void run(Scanner scanner) throws IOException, NoSuchAlgorithmException{
        while(true){
            String input = scanner.nextLine();
            if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.CREATE_USER)!=null && createUserFormat(input))
                System.out.println(createUser(input, scanner));
            else if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.USER_LOGIN)!=null){
                System.out.println(userLogin(input, scanner));
                if(userLogin(input, scanner).equals("Login successful!")){
                    ProfileMenu profileMenu = new ProfileMenu();
                    profileMenu.run(scanner);
                }
            }
            else if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.FORGOT_MY_PASSWORD)!=null)
                System.out.println(forgotMyPassword(scanner));
            else
                System.out.println("Invalid command!");
        }
    }

    private String createUser(String input,Scanner scanner) throws IOException, NoSuchAlgorithmException{
        String username = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.USERNAME).group("username"));
        String password = "" , passwordConfirm = "";
        if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD).group("random")!=null) password = "random";
        else{
            password = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD).group("password"));
            passwordConfirm = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD).group("passwordConfirm"));
        }
        String nickname = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.NICKNAME).group("nickname"));
        String email = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.EMAIL).group("email"));
        String slogan="";
        if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.SLOGAN)!=null){
            slogan = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.SLOGAN).group("slogan"));
            if(slogan.length()==0) return "there's an empty field!";
        }
        switch (loginMenuController.signUp(username,password,passwordConfirm,email,slogan,nickname,scanner)) {
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
                return "Your password must contain the following characters:\n"+upper+"\n"+lower+"\n"+digit+"\n"+nonWord;
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
            default:
                break;
        }
        return null;
    }
    private String userLogin(String input,Scanner scanner) throws IOException ,NoSuchAlgorithmException{
        String username = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.USERNAME).group("username"));
        String password = Controller.trimmer(LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD_LOGIN).group("password"));
        switch (loginMenuController.login(username, password, scanner, false)) {
            case USERNAME_NOT_FOUND:
                return "Username and password didn't match!";
            case WAIT_FOR_LOGIN:
                return "you have to wait for "+Controller.getUserByUsername(username).getAttempt()*5+" more seconds to login!";
            case INCORRECT_PASSWORD:
                return "Incorrect password!";
            case EXIT_CAPTCHA:
                return "login cancelled!";
            case LOGIN_SUCCESSFUL:
                return "Login successful!";
            default:
                break;
        }
        return null;
    }
    private boolean createUserFormat(String input){
        if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.USERNAME)==null) return false;
        if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.PASSWORD)==null) return false;
        if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.EMAIL)==null) return false;
        if(LoginMenuCommands.getMatcher(input, LoginMenuCommands.NICKNAME)==null) return false;
        return true;
    }
    private String forgotMyPassword(Scanner scanner) throws IOException, NoSuchAlgorithmException{
        switch(loginMenuController.forgotPassword(scanner)) {
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
                return "Your password must contain the following characters:\n"+upper+"\n"+lower+"\n"+digit+"\n"+nonWord;
            case PASSWORD_NOT_CONFIRMED:
                return "Passwords do not match!";
            default:
            break;
        }
        return "Password changed successfully!";
    }
}