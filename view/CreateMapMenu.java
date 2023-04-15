package view;
import java.util.ArrayList;
import java.util.Scanner;

import controller.CreateMapMenuCommands;
import controller.CreateMapMenuController;
import controller.MapMenuCommands;
import controller.Messages;
import java.util.regex.Matcher;
public class CreateMapMenu extends MapMenu{
    private final CreateMapMenuController controller = new CreateMapMenuController(super.controller);
    private Scanner scanner;
    public CreateMapMenu(){
        super(0,0);
    }
    public void run(Scanner scanner){
        this.scanner = scanner;
        while(true){
            if(setMap().equals(Messages.EXIT_CREATE_MAP_MENU)){
                System.out.println("Back to main menu!");
                return;
            }
            while (true) {
                String input = scanner.nextLine();
                if(input.matches("\\s*exit\\s*")){
                   saveChanges();
                   System.out.println("Back to main menu!");
                   return;
                }
                else if(input.matches("\\s*save\\s*"))
                    saveChanges();
                else if(CreateMapMenuCommands.getMatcher(input,CreateMapMenuCommands.SET_PIXEL_TEXTURE)!=null)
                    System.out.println(setPixelTexture(input));
                else
                    System.out.println("Invalid command!");

            }
            

        }
    }
    private Messages setMap(){
        ArrayList<String> maps = controller.getMaps();
        System.out.println("Your maps:");
        for(int i = 0; i<maps.size() ; i++)
            System.out.println((i+1) + ". " + maps.get(i));
        System.out.println("Enter the number of the map you want to edit or enter \"new map\" to create a new map:");
        while(true){
            String input = scanner.nextLine();
            if(input.matches("\\s*exit\\s*"))
                return Messages.EXIT_CREATE_MAP_MENU;
            else if(input.matches("\\s*new\\s+map\\s*"))
                return setNewMap();
            else if(!input.matches("\\-?\\d+"))
                System.out.println("Enter a whole number!");
            else if(Integer.parseInt(input) < 1 || Integer.parseInt(input) > maps.size())
                System.out.println("Enter a number between 1 and " + maps.size());
            else{
                controller.setExistingMap(Integer.parseInt(input)-1);
                return Messages.SET_MAP_SUCCESSFULL;
            }

        }
    }
    private Messages setNewMap(){
        int mapSize = getNewMapSize();
        if(mapSize < 0)return Messages.EXIT_CREATE_MAP_MENU;
        String mapName = getNewMapName();
        if(mapName == null)return Messages.EXIT_CREATE_MAP_MENU;
        controller.setNewMap(mapSize, mapName);
        return Messages.SET_MAP_SUCCESSFULL;
    }
    private int getNewMapSize(){
        System.out.println("Choose the size of the new map:");
        while (true) {
            String input = scanner.nextLine();
            if(input.matches("\\s*exit\\s*"))
                return -1;
            else if(!input.matches("-?\\d+"))
                System.out.println("Enter a whole number!");
            else if(Integer.parseInt(input) < 200)
                System.out.println("Enter a number greater than or equal 200!");
            else if(Integer.parseInt(input) > 400)
                System.out.println("Enter a number less than or equal 400!"); 
            else
                return Integer.parseInt(input);
        }
    }
    private String getNewMapName(){
        System.out.println("Choose a name for the new map:");
        while (true) {
            String input = scanner.nextLine();
            if(input.matches("\\s*exit\\s*"))
                return null;
            else if(!input.matches("\\s*\\w+\\s*"))
                System.out.println("Map name can only contain letters , digits and underscore!");
            else
                return input;
        }
    }
    private void saveChanges(){
        System.out.println("Do you want to save your changes?");
        if(scanner.nextLine().toLowerCase().matches("\\s*yes\\s*")){
            controller.saveMap();
            System.out.println("Changes saved");
        }
    }
    private String setPixelTexture(String input){
        Matcher rowMatcher = 
            CreateMapMenuCommands.getMatcher(input , CreateMapMenuCommands.SET_PIXEL_TEXTURE_ROW);
        Matcher columnMatcher = 
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_PIXEL_TEXTURE_COLOUMN);
        //if(rowMatcher == null || columnMatcher == null)
        return null;

    }
    
    
}