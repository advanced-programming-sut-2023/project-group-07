package view;
import java.util.Scanner;
<<<<<<< HEAD

import controller.GameMenuCommands;
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
import controller.GameMenuController;
public class GameMenu {
    GameMenuController gameMenuController = new GameMenuController();
    public void run(Scanner scanner) {
        while(true){
<<<<<<< HEAD
            String input = scanner.nextLine();
            if(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING)!=null){
                
            }
            
=======

>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
        }
    }
    private String getPopulation(){

    }
    private String getPopularity(){
        //System.out.println("Your popularity: "+.getPopularity()+);
    }
    private String setTaxRate(){

    }
    private String showTaxRate(){
        System.out.println("Your tax rate: "+government.getTaxAmount()+"coins\nPopularity effect: "+government.getTaxPopularity());
    }
    private void endOfTurn(){
        
    }
<<<<<<< HEAD
    private void showMap(){

    }
    private String dropBuilding(String input){
        int row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("x"));
        int column = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("y"));
        String type = GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("type");
        //String returnMessage = gameMenuController.dropBuilding(row, column, type);
    }
=======
    private void showMap()
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
}