import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import model.Map;
import model.User;
import Server.Colors;

public class Main {
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
        System.out.print(Colors.YELLOW_BOLD);
        System.out.print("Loading maps and users...");
        User.loadUsers();
        Map.loadMaps();
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        System.out.print(Colors.RESET);
//        LoginMenu loginMenu = new LoginMenu();
//        loginMenu.run(new Scanner(System.in));
    
    }
    
}
