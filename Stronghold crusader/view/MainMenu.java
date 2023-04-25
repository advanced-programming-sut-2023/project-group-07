package view;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import controller.Controller;

public class MainMenu {
    private final ProfileMenu profileMenu = new ProfileMenu();
    private final PreGameMenu preGameMenu = new PreGameMenu();
    private final CreateMapMenu createMapMenu = new CreateMapMenu();

    public void run(Scanner scanner) throws IOException, NoSuchAlgorithmException {
        printMenuHeader();
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*4\\s*")) {
                FileWriter file = new FileWriter("Stronghold Crusader/DB/stayLoggedIn");
                file.close();
                System.out.println("Logged out");
                return;
            } else if (input.toLowerCase().matches("\\s*3\\s*")) {
                profileMenu.run(scanner);
                printMenuHeader();
            } else if (input.toLowerCase().matches("\\s*2\\s*")) {
                createMapMenu.run(scanner);
                printMenuHeader();
            } else if (input.toLowerCase().matches("\\s*1\\s*")) {
                preGameMenu.run(scanner);
                printMenuHeader();
            } else
                System.out.println("Invalid command!");
        }
    }

    private void printMenuHeader(){
        Controller.menuPrinter.print("MAIN MENU", Colors.PURPLE_BACKGROUND, 25, 1);
        Controller.menuPrinter.print("1.CRUSADER GAME", Colors.YELLOW_BACKGROUND, 25, 2);
        Controller.menuPrinter.print("2.CREATE MAP", Colors.GREEN_BACKGROUND, 25, 2);
        Controller.menuPrinter.print("3.PROFILE MENU", Colors.CYAN_BACKGROUND, 25, 2);
        Controller.menuPrinter.print("4.LOGOUT", Colors.MAGENTA_BACKGROUND, 25, 2);
        System.out.print(" ");
        for (int i = 0; i < 23; i++)
            System.out.print("-");
        System.out.println(" ");
    }
}