package view;
import java.util.Scanner;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import controller.Controller;
public class MainMenu {
    private final ProfileMenu profileMenu = new ProfileMenu();
    private final PreGameMenu preGameMenu = new PreGameMenu();
    private final CreateMapMenu createMapMenu = new CreateMapMenu();
    public void run(Scanner scanner) throws IOException,NoSuchAlgorithmException{
        Controller.menuPrinter.print("MAIN MENU", Colors.MAGENTA_BACKGROUND, 20);
        while(true){
            String input = scanner.nextLine();
            if(input.matches("logout")){
                System.out.println("Logged out");
                return;
            }
            else if (input.toLowerCase().matches("\\s*show\\s+profile\\s*")){
                profileMenu.run(scanner);
            }
            else if(input.toLowerCase().matches("\\s*create\\s+map\\s*")){
                createMapMenu.run(scanner);
            }
            else if (input.toLowerCase().matches("\\s*start\\s+game\\s*")){
                preGameMenu.run(scanner);
            }
            else
                System.out.println("Invalid command!");
        }
    }    
}