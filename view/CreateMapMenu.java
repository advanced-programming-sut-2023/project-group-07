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
                else if(CreateMapMenuCommands.getMatcher(input,CreateMapMenuCommands.SET_REGION_TEXTURE)!=null)
                    System.out.println(setRegionTexture(input));
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
            CreateMapMenuCommands.getMatcher(input , CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = 
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLOUMN);
        Matcher textureMatcher = 
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_TEXTURE_TYPE);
        String checkFormat = checkSetPixelTextureFormat(rowMatcher, columnMatcher, textureMatcher);
        if(checkFormat!=null) return checkFormat;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String textureName = textureMatcher.group("type");
        switch(controller.setPixelTexture(row, column, textureName)){
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case INVALID_TEXTURE:
                return "Invalid type/texture!";
            case SET_TEXTURE_SUCCESSFULL:
                return "Set type/texture successfull!";
            default:
                break;
        }
        return null;
    }
    private String checkSetPixelTextureFormat(Matcher rowMatcher , Matcher columnMatcher , Matcher textureMatcher){
        if(rowMatcher == null || rowMatcher.group("row") == null)
            return "Enter the row number!";
        if(columnMatcher == null || columnMatcher.group("column") == null)
            return "Enter the column number!";
        if(!rowMatcher.group("row").matches("\\-?\\d+") 
            || !columnMatcher.group("column").matches("\\-?\\d+"))
            return "Enter whole number for cordinates!";
        if(textureMatcher == null || textureMatcher.group("type") == null)
            return "Enter the type/texture you want to set!";
        return null;
    }

    private String setRegionTexture(String input){
        Matcher x1Matcher = 
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_FIRST_ROW);
        Matcher y1Matcher = 
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_FIRST_COLUMN);
        Matcher x2Matcher = 
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_SECOND_ROW);
        Matcher y2Matcher =
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_SECOND_COLUMN);
        Matcher textureMatcher =
            CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_TEXTURE_TYPE);
        String checkFormat = checkSetRegionTextureFormat(x1Matcher, y1Matcher, x2Matcher, y2Matcher, textureMatcher);
        if(checkFormat != null) return checkFormat;
        int x1 = Integer.parseInt(x1Matcher.group("frow"));
        int y1 = Integer.parseInt(y1Matcher.group("fcolumn"));
        int x2 = Integer.parseInt(x2Matcher.group("srow"));
        int y2 = Integer.parseInt(y2Matcher.group("scolumn"));
        String textureName = textureMatcher.group("type");
        switch(controller.setRegionTexture(x1, y1, x2, y2, textureName)){
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case INVALID_TEXTURE:
                return "Invalid type/texture!";
            case SET_TEXTURE_SUCCESSFULL:
                return "Set type/texture successfull!";
            default:
                break;
        }
        return null;
    }
    private String checkSetRegionTextureFormat(Matcher x1Matcher , Matcher y1Matcher , Matcher x2Matcher , Matcher y2Matcher , Matcher textureMatcher){
        if(x1Matcher == null || x1Matcher.group("frow") == null)
            return "Enter first row number!";
        if(y1Matcher == null || y1Matcher.group("fcolumn") == null)
            return "Enter first column number!";
        if(x2Matcher == null || x2Matcher.group("srow") == null)
            return "Enter second row number!";
        if(y2Matcher == null || y2Matcher.group("scolumn") == null)
            return "Enter second column number!";
        if(!x1Matcher.group("frow").matches("\\-?\\d+") 
            || !y1Matcher.group("fcolumn").matches("\\-?\\d+")
            || !x2Matcher.group("srow").matches("\\-?\\d+")
            || !y2Matcher.group("scolumn").matches("\\-?\\d+"))
            return "Enter whole number for cordinates!";
        if(textureMatcher == null || textureMatcher.group("type") == null)
            return "Enter the type/texture you want to set!";
        return null;
    }
    
    
}