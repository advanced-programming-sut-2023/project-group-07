package view;
import java.util.Scanner;


import controller.GameMenuCommands;

import controller.GameMenuController;
public class GameMenu {
    GameMenuController gameMenuController = new GameMenuController();
    public void run(Scanner scanner) {
        while(true){
            String input = scanner.nextLine();
            if(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING)!=null){
                
            }
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
        System.out.println("Your tax rate: "+government.getTaxAmount()+"coins\n" +
                "Popularity effect: "+government.getTaxPopularity());
    }
    private void endOfTurn(){
        
    }
    private void showMap(){

    }
    private String dropBuilding(String input){
        int row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("x"));
        int column = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("y"));
        String type = GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("type");
        //String returnMessage = gameMenuController.dropBuilding(row, column, type);
    }

}