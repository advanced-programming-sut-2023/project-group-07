import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import controller.Controller;
import controller.LoginMenuController;
import model.RecoveryQuestion;
import model.User;
import view.LoginMenu;
import view.Colors;
import view.CreateMapMenu;
public class Main {
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
        // Game game = new Game(100);
        // Controller.currentGame = game;
        // MapMenu mn = new MapMenu(30 , 40);
        // mn.run(new Scanner(System.in));
        // CreateMapMenu cm = new CreateMapMenu();
        // cm.run(new Scanner(System.in));
        User.loadUsers();
        
        // GameMenu gameMenu = new GameMenu();
        // gameMenu.run(new Scanner(System.in));
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run(new Scanner(System.in));
    
    }
    
}
