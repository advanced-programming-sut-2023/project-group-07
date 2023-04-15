package view;
import java.util.Scanner;


import controller.Controller;
import controller.GameMenuCommands;

import controller.GameMenuController;
import controller.Messages;
import model.Government;

public class GameMenu {
    private final GameMenuController controller = new GameMenuController();
    public void run(Scanner scanner) {
        while(true){
            String input = scanner.nextLine();
            if(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING)!=null){
                
            }
        }
    }
    private String getPopulation(){
        return null;
    }
    private String getPopularity(){
        //System.out.println("Your popularity: "+.getPopularity()+);
        return null;
    }
    private String setTaxRate(){
        return null;
    }
    private String showTaxRate(){
        Government government = Controller.currentGame.getCurrentGovernment();
        return ("Your tax rate: "+government.getTaxAmount()+"coins\n" +
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
        Messages returnMessage = controller.dropBuilding(row, column, type);
        switch(returnMessage){
            case INVALID_ROW_OR_COLUMN:
                return "Invalid row or column!";
            case DEPLOYMENT_SUCCESSFULL:
                return type+" deployed successfully!";
            default:
                break;
        }
        return null;
    }

}