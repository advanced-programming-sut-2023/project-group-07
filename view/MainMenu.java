package view;
import java.util.Scanner;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MainMenu {
    public void run(Scanner scanner) throws IOException,NoSuchAlgorithmException{
        String input = scanner.nextLine();
        if(input.matches("logout")){
            System.out.println("Logged out");
            return;
        }
        else if (input.toLowerCase().matches("enter\\s+profile\\s+menu"))
            new ProfileMenu().run(scanner);
        else if (input.toLowerCase().matches("enter\\s+game\\s+menu"))
            new GameMenu().run(scanner);
        else
            System.out.println("Invalid command!");
    }    
}