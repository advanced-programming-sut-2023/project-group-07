package view;
import java.util.Scanner;
public class PreGameMenu {
    private Scanner scanner;
    public void run(Scanner scanner) {
        this.scanner = scanner;
        int mapSize = getMapSize();
        System.out.println("Now you have to build the map:");
        while(true){

        }

    }
    private int getMapSize(){
        while (true) {
            System.out.println("Choose the size of the map:");
            String input = scanner.nextLine();
            if(!input.matches("-?\\d+"))
                System.out.println("Enter a whole number!");
            else if(Integer.parseInt(input) < 200)
                System.out.println("Enter a number greater than or equal 200!");
            else if(Integer.parseInt(input) > 400)
                System.out.println("Enter a number less than or equal 400!"); 
            else
                return Integer.parseInt(input);
        }
    }
    
}