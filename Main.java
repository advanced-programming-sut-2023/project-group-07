import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import controller.Controller;
import controller.LoginMenuController;
import model.RecoveryQuestion;
import view.LoginMenu;
import view.ProfileMenu;
import view.*; // TODO : remove this
import model.*; // TODO : remove this
public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
        if(Controller.isLoggedIn()){
            
        }
        else{
            LoginMenu loginMenu = new LoginMenu();
            loginMenu.run(new Scanner(System.in));
        }
    }
    
}
