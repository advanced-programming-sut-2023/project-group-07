import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import controller.Controller;
import controller.LoginMenuController;
import model.Map;
import model.RecoveryQuestion;
import model.User;
import view.LoginMenu;
import view.Colors;
import view.CreateMapMenu;
import UnitTest.LoginMenuTest;
public class Main {
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
        System.out.print(Colors.YELLOW_BOLD);
        System.out.print("Loading maps and users...");
        User.loadUsers();
        Map.loadMaps();
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        System.out.print(Colors.RESET);
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run(new Scanner(System.in));
    
    }
    
}
