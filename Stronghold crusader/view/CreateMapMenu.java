package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.CreateMapMenuCommands;
import controller.CreateMapMenuController;
import controller.MapMenuCommands;
import controller.Messages;
import model.LordColor;
import java.util.regex.Matcher;
import controller.Controller;
import java.util.HashMap;

public class CreateMapMenu extends MapMenu {
    private final CreateMapMenuController controller = new CreateMapMenuController(super.controller);
    private Scanner scanner;

    public void run(Scanner scanner) throws IOException{
        Controller.menuPrinter.print("CREATE/CHANGE MAPS", Colors.RED_BACKGROUND, 25, 1);
        this.scanner = scanner;
        while (true) {
            if (setMap().equals(Messages.EXIT_CREATE_MAP_MENU))
                return;
            while (true) {
                String input = scanner.nextLine();
                if (input.matches("\\s*exit\\s*")) {
                    controller.saveMap();
                    System.out.println("Changes saved!");
                    return;
                } else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_MAP) != null) {
                    String showMapStr = super.showMap(input);
                    if (showMapStr != null)
                        System.out.println(showMapStr);
                } else if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_MAP) != null)
                    super.moveMap(input);
                else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_DETAILS) != null)
                    System.out.println(super.showDetails(input));
                else if (MapMenuCommands.getMatcher(input, MapMenuCommands.MAP_GUIDE) != null)
                    super.mapGuide();
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.REMOVE_MAP) != null) {
                    if (removeMap().equals(Messages.REMOVE_MAP_SUCCESSFUL)) {
                        if (setMap().equals(Messages.EXIT_CREATE_MAP_MENU))
                            return;
                    }
                } else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_PIXEL_TEXTURE) != null)
                    System.out.println(setPixelTexture(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_REGION_TEXTURE) != null)
                    System.out.println(setRegionTexture(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.CLEAR_PIXEL) != null)
                    System.out.println(clearPixel(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.CLEAR_REGION) != null)
                    System.out.println(clearRegion(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.DROP_TREE) != null)
                    System.out.println(dropTree(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.DROP_ROCK) != null)
                    System.out.println(dropRock(input));
                else
                    System.out.println("Invalid command!");

            }

        }
    }

    private Messages removeMap() {
        System.out.println("Are you sure you want to remove this map?");
        String input = scanner.nextLine();
        if (input.toLowerCase().matches("\\s*yes\\s*")) {
            controller.removeMap();
            return Messages.REMOVE_MAP_SUCCESSFUL;
        }
        return Messages.REMOVE_MAP_CANCLED;
    }

    private Messages setMap() {
        ArrayList<String> maps = controller.getMaps();
        System.out.println("Available maps:");
        for (int i = 0; i < maps.size(); i++)
            System.out.println((i + 1) + ". " + maps.get(i));
        System.out.println("Enter the number of the map you want to edit or enter \"new map\" to create a new one:");
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return Messages.EXIT_CREATE_MAP_MENU;
            else if (input.matches("\\s*new\\s+map\\s*"))
                return setNewMap();
            else if (!input.matches("\\-?\\d+"))
                System.out.println("Enter a whole number!");
            else if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > maps.size())
                System.out.println("Enter a number between 1 and " + maps.size());
            else {
                controller.setExistingMap(Integer.parseInt(input) - 1);
                return Messages.SET_MAP_SUCCESSFUL;
            }

        }
    }

    private Messages setNewMap() {
        int mapSize = getNewMapSize();
        if (mapSize < 0)
            return Messages.EXIT_CREATE_MAP_MENU;
        int numberOfPlayers = getNumberOfPlayers();
        if (numberOfPlayers < 0)
            return Messages.EXIT_CREATE_MAP_MENU;
        HashMap<LordColor,int[]> lordsPositions = getKeepsPositions(numberOfPlayers, mapSize);
        if (lordsPositions == null)
            return Messages.EXIT_CREATE_MAP_MENU;
        String mapName = getNewMapName();
        if (mapName == null)
            return Messages.EXIT_CREATE_MAP_MENU;
        controller.setNewMap(mapSize, mapName, numberOfPlayers, lordsPositions);
        return Messages.SET_MAP_SUCCESSFUL;
    }

    private int getNumberOfPlayers() {
        System.out.println("Enter the number of players:");
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return -1;
            else if (!input.matches("\\-?\\d+"))
                System.out.println("Enter a whole number!");
            else if (Integer.parseInt(input) < 2 || Integer.parseInt(input) > 8)
                System.out.println("Enter a number between 2 and 8!");
            else
                return Integer.parseInt(input);
        }
    }

    private HashMap<LordColor,int[]> getKeepsPositions(int numberOfPlayers, int mapSize) {
        int counter = 1;
        HashMap<LordColor, int[]> positions = new HashMap<LordColor, int[]>();
        while (counter <= numberOfPlayers) {
            System.out.println("Enter the cordinates of lord number " + counter + " (color "
                    + LordColor.getLordColor(counter - 1) + ") keep:");
            String input = scanner.nextLine();
            Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
            Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
            if (input.matches("\\s*exit\\s*"))
                return null;
            if (rowMatcher == null)
                System.out.println("Enter the row number!");
            else if (columnMatcher == null)
                System.out.println("Enter the column number!");
            else {
                int row = Integer.parseInt(rowMatcher.group("row")) - 1;
                int column = Integer.parseInt(columnMatcher.group("column")) - 1;
                if (row < 0 || row >= mapSize-7 || column < 0 || column >= mapSize-12)
                    System.out.println("Invalid cordinates!");
               if (!controller.canDropKeep(positions, row, column))
                    System.out.println("There is already a lord castle in this position!");
                else {
                    int[] keepPosition = new int[] { row, column };
                    positions.put(LordColor.getLordColor(counter - 1), keepPosition);
                    counter++;
                }
            }
        }
        return positions;
    }

    private int getNewMapSize() {
        System.out.println("Enter the size of the new map:");
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return -1;
            else if (!input.matches("-?\\d+"))
                System.out.println("Enter a whole number!");
            else if (Integer.parseInt(input) < 200 || Integer.parseInt(input) > 400)
                System.out.println("Enter a number between 200 and 400!");
            else
                return Integer.parseInt(input);
        }
    }

    private String getNewMapName() {
        System.out.println("Enter a name for the new map:");
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return null;
            else if (!input.matches("\\s*\\w+\\s*"))
                System.out.println("Map name can only contain letters , digits and underscore!");
            else
                return input;
        }
    }

    private String setPixelTexture(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        Matcher textureMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TEXTURE_TYPE);
        String checkCordinates = checkCordinatesFormat(rowMatcher, columnMatcher);
        String checkTexture = checkTypeFormat(textureMatcher);
        if (checkCordinates != null)
            return checkCordinates;
        if (checkTexture != null)
            return checkTexture;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String textureName = textureMatcher.group("type");
        switch (controller.setPixelTexture(row - 1, column - 1, textureName)) {
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case INVALID_TEXTURE:
                return "Invalid type/texture!";
            case BUILDING_EXIST:
                return "There are some buildings in this pixel!";
            case SET_TEXTURE_SUCCESSFUL:
                return "Set type/texture successfull!";
            default:
                break;
        }
        return null;
    }

    private String setRegionTexture(String input) {
        Matcher x1Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_FIRST_ROW);
        Matcher y1Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_FIRST_COLUMN);
        Matcher x2Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_SECOND_ROW);
        Matcher y2Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_SECOND_COLUMN);
        Matcher textureMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TEXTURE_TYPE);
        String checkRegionCordinates = checkRegionCordinatesFormat(x1Matcher, y1Matcher, x2Matcher, y2Matcher);
        String checkTexture = checkTypeFormat(textureMatcher);
        if (checkRegionCordinates != null)
            return checkRegionCordinates;
        if (checkTexture != null)
            return checkTexture;
        int x1 = Integer.parseInt(x1Matcher.group("frow"));
        int y1 = Integer.parseInt(y1Matcher.group("fcolumn"));
        int x2 = Integer.parseInt(x2Matcher.group("srow"));
        int y2 = Integer.parseInt(y2Matcher.group("scolumn"));
        String textureName = textureMatcher.group("type");
        switch (controller.setRegionTexture(x1 - 1, y1 - 1, x2 - 1, y2 - 1, textureName)) {
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case INVALID_TEXTURE:
                return "Invalid type/texture!";
            case INVALID_SET_POND:
                return "Invalid command for this type/texture!";
            case BUILDING_EXIST:
                return "There are some buildings in this area!";
            case SET_TEXTURE_SUCCESSFUL:
                return "Set type/texture successfull!";
            default:
                break;
        }
        return null;
    }

    private String checkRegionCordinatesFormat(Matcher x1Matcher, Matcher y1Matcher, Matcher x2Matcher,
            Matcher y2Matcher) {
        if (x1Matcher == null)
            return "Enter first row number!";
        if (y1Matcher == null)
            return "Enter first column number!";
        if (x2Matcher == null)
            return "Enter second row number!";
        if (y2Matcher == null)
            return "Enter second column number!";
        return null;
    }

    private String checkTypeFormat(Matcher typeMatcher) {
        if (typeMatcher == null)
            return "Enter the type/texture you want to set!";
        return null;
    }

    private String clearPixel(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        String checkCordinatesFormat = checkCordinatesFormat(rowMatcher, columnMatcher);
        if (checkCordinatesFormat != null)
            return checkCordinatesFormat;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        switch (controller.clearPixel(row - 1, column - 1)) {
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case CLEAR_SUCCESSFUL:
                return "Clear pixel successfull!";
            default:
                break;
        }
        return null;
    }

    private String clearRegion(String input) {
        Matcher x1Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_FIRST_ROW);
        Matcher y1Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_FIRST_COLUMN);
        Matcher x2Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_SECOND_ROW);
        Matcher y2Matcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_SECOND_COLUMN);
        String checkRegionCordinates = checkRegionCordinatesFormat(x1Matcher, y1Matcher, x2Matcher, y2Matcher);
        if (checkRegionCordinates != null)
            return checkRegionCordinates;
        int x1 = Integer.parseInt(x1Matcher.group("frow"));
        int y1 = Integer.parseInt(y1Matcher.group("fcolumn"));
        int x2 = Integer.parseInt(x2Matcher.group("srow"));
        int y2 = Integer.parseInt(y2Matcher.group("scolumn"));
        switch (controller.clearRegion(x1 - 1, y1 - 1, x2 - 1, y2 - 1)) {
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case CLEAR_SUCCESSFUL:
                return "Clear region successfull!";
            default:
                break;
        }
        return null;
    }

    private String dropTree(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        Matcher typeMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TEXTURE_TYPE);
        String checkCordinates = checkCordinatesFormat(rowMatcher, columnMatcher);
        String checkType = checkTypeFormat(typeMatcher);
        if (checkCordinates != null)
            return checkCordinates;
        if (checkType != null)
            return checkType;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String type = typeMatcher.group("type").trim();
        switch (controller.dropTree(row - 1, column - 1, type)) {
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case INVALID_TEXTURE:
                return "Invalid tree type!";
            case CANT_PLACE_THIS:
                return "Can't drop a tree in this pixel!";
            case DROP_TREE_SUCCESSFUL:
                return "Drop tree successful!";
            default:
                break;
        }

        return null;
    }

    private String dropRock(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        Matcher directionMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_DIRECTION);
        String checkCordinates = checkCordinatesFormat(rowMatcher, columnMatcher);
        if (checkCordinates != null)
            return checkCordinates;
        if (directionMatcher == null)
            return "Enter the direction!";
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String direction = directionMatcher.group("direction").trim();
        switch (controller.dropRock(row - 1, column - 1, direction)) {
            case INVALID_CORDINATES:
                return "Invalid cordinates!";
            case INVALID_DIRECTION:
                return "Invalid direction!";
            case CANT_PLACE_THIS:
                return "Can't drop a rock in this pixel!";
            case DROP_ROCK_SUCCESSFUL:
                return "Drop rock successful!";
            default:
                break;
        }
        return null;
    }

    private String checkCordinatesFormat(Matcher rowMatcher, Matcher columnMatcher) {
        if (rowMatcher == null)
            return "Enter the row number!";
        if (columnMatcher == null)
            return "Enter the column number!";
        return null;
    }

}