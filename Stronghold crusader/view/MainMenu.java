package view;
import java.util.Scanner;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MainMenu {
    private final ProfileMenu profileMenu = new ProfileMenu();
    private final PreGameMenu preGameMenu = new PreGameMenu();
    private final CreateMapMenu createMapMenu = new CreateMapMenu();
    public void run(Scanner scanner) throws IOException,NoSuchAlgorithmException{
        while(true){
            String input = scanner.nextLine();
            if(input.matches("logout")){
                System.out.println("Logged out");
                return;
            }
            else if (input.toLowerCase().matches("\\s*show\\s+profile\\s*")){
                System.out.println("Entered profile menu!");
                profileMenu.run(scanner);
            }
            else if(input.toLowerCase().matches("\\s*create\\s+map\\s*")){
                System.out.println("Entered create map menu!");
                createMapMenu.run(scanner);
            }
            else if (input.toLowerCase().matches("\\s*start\\s+game\\s*")){
                System.out.println("Entered start game menu!");
                preGameMenu.run(scanner);
            }
            else
                System.out.println("Invalid command!");
        }
    }    
}