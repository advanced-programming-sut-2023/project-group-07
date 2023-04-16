package view;
import java.util.Scanner;
import java.util.regex.Matcher;

import controller.Controller;
import controller.CreateMapMenuCommands;
import controller.GameMenuCommands;

import controller.GameMenuController;
import controller.Messages;
import model.Government;

public class GameMenu {
    private final GameMenuController gameMenuController = new GameMenuController();
    public void run(Scanner scanner) {
        while(true){
            String input = scanner.nextLine();
            if(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING)!=null){
                
            }
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING)!=null){
                System.out.println(selectBuilding(input, scanner));
            }
            else{
                System.out.println("Invalid command!");
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
    private String dropBuilding(String input,Scanner scanner){
        int row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("x"));
        int column = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("y"));
        String type = GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING).group("type");
        Messages returnMessage = gameMenuController.dropBuilding(row, column, type);
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
    private String selectBuilding(String input,Scanner scanner){
        int row = Integer.parseInt(GameMenuCommands.getMatcher(input,GameMenuCommands.SELECT_BUILDING).group("row"));
        int column = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING).group("column"));
        Messages returnMessage = gameMenuController.selectBuilding(row, column);
        switch(returnMessage){
            case NO_BUILDING_HERE:
                return "No building here!";
            case ENEMY_BUILDING:
                return "Enemy building!";
            case ENTERED_TOWER:
                return "";
            case ENTERED_GATEHOUSE:
                return "";
            case ENTERED_MILITARY_CAMP:
                System.out.println("you have entered "+gameMenuController.getCurrentMilitaryCamp()+"!");
                while(true){
                    String command = scanner.nextLine();
                    if(GameMenuCommands.getMatcher(command, GameMenuCommands.CREATE_UNIT)!=null){
                        returnMessage = gameMenuController.createUnit(command);
                        switch(returnMessage) {
                            case INVALID_UNIT_NAME:
                                System.out.println("Invalid unit name!");
                            case INVALID_NUMBER:
                                System.out.println("Invalid number!");
                            case NOT_ENOUGH_GOLD:
                                System.out.println("Not enough gold!");
                            case NOT_ENOUGH_RESOURCES:
                                System.out.println("Not enough resources!");
                            case CANT_CREATE_THIS_UNIT_HERE:
                                System.out.println("Can't create this unit here!");
                            case UNIT_CREATED_SUCCESSFULLY:
                                System.out.println("Unit created successfully!");
                            default:
                                break;
                        }
                    }
                    else if(GameMenuCommands.getMatcher(command, GameMenuCommands.SHOW_UNITS)!=null){
                        System.out.print(gameMenuController.getUnitsInfo(gameMenuController.getCurrentMilitaryCamp()));
                        return "";
                    }
                    else if(GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT)!=null) break;
                    else{
                        System.out.println("Invalid command!");
                        break;
                    }
                }
            default:
                break;
        }
        return "";
    }
 

}