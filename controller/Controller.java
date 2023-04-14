package controller;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import model.*;
import view.*;
public class Controller {
    private static ArrayList<User> users = new ArrayList<User>();
    public static User currentUser;
    public static Game currentGame;
    public static int randomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
    public static char getRandomChar(char c , int max){
        return (char)(c + randomNumber(max));
    }
    public static boolean isLoggedIn(){
        return false;
    }
    public static User getUserByUsername(String username){
        for(User user : users){
            if(user.getUsername().equals(username))return user;
        }
        return null;
    }
    public static User getUserByEmail(String email){
        email = email.toLowerCase();
        for(User user : users){
            if(user.getEmail().toLowerCase().equals(email))return user;
        }
        return null;
    }
    public static String trimmer(String string){
        if(string.charAt(0)=='\"')
            return string.substring(1,string.length()-1);
        return string;
    }
    public static String generateCaptcha(CaptchaPrinter captchaPrinter){
        String captcha = "" , captchaPrint = "";
        int length = randomNumber(5) + 4;
        for(int i = 0 ; i < length ; i++){
            char toBeAdded = getRandomChar('A', 26);
            captcha += toBeAdded;
            captchaPrint += toBeAdded;
            captchaPrint += ' ';
        }
        captchaPrinter.print(captchaPrint);
        return captcha;
    }
    
    public static void addUser(User user) {
        users.add(user);
    }
    public static String toSHA256(String string) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(string.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hex = String.format("%064x",new BigInteger(1,digest));
        return hex;
    }
}
