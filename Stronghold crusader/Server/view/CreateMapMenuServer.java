package Server.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import controller.CreateMapMenuCommands;
import controller.CreateMapMenuController;
import controller.MapMenuCommands;
import controller.Messages;
import model.LordColor;
import java.util.regex.Matcher;
import controller.Controller;

import java.util.HashMap;

public class CreateMapMenuServer extends MapMenuServer {
    private final CreateMapMenuController controller = new CreateMapMenuController(super.controller);
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String currentUserUsername;

    public CreateMapMenuServer(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        super(dataInputStream, dataOutputStream,null);
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    public void run(String currentUserUsername) throws IOException {
        this.currentUserUsername = currentUserUsername;
        while (true) {
            if (setMap().equals(Messages.EXIT_CREATE_MAP_MENU))
                return;
            dataOutputStream.writeUTF("Now modify your map.");
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.matches("\\s*exit\\s*")) {
                    dataOutputStream.writeUTF(controller.saveMap(currentUserUsername));
                    return;
                } else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_MAP) != null) {
                    String showMapStr = super.showMap(input);
                    if (showMapStr != null)
                        dataOutputStream.writeUTF(showMapStr);
                } else if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_MAP) != null)
                    super.moveMap(input);
                else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_DETAILS) != null)
                    dataOutputStream.writeUTF(super.showDetails(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.REMOVE_MAP) != null) {
                    if (removeMap().equals(Messages.REMOVE_MAP_SUCCESSFUL)) {
                        if (setMap().equals(Messages.EXIT_CREATE_MAP_MENU))
                            return;
                    }
                } else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_PIXEL_TEXTURE) != null)
                    dataOutputStream.writeUTF(setPixelTexture(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_REGION_TEXTURE) != null)
                    dataOutputStream.writeUTF(setRegionTexture(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.CLEAR_PIXEL) != null)
                    dataOutputStream.writeUTF(clearPixel(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.CLEAR_REGION) != null)
                    dataOutputStream.writeUTF(clearRegion(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.DROP_TREE) != null)
                    dataOutputStream.writeUTF(dropTree(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.DROP_ROCK) != null)
                    dataOutputStream.writeUTF(dropRock(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.DROP_UNIT) != null)
                    dataOutputStream.writeUTF(dropUnit(input));
                else if (CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.DROP_BUILDING) != null)
                    dataOutputStream.writeUTF(dropBuilding(input));
                else
                    dataOutputStream.writeUTF("Invalid command!");

            }

        }
    }

    private Messages removeMap() throws IOException {
        dataOutputStream.writeUTF("Are you sure you want to remove this map?");
        String input = dataInputStream.readUTF();
        if (input.toLowerCase().matches("\\s*yes\\s*")) {
            controller.removeMap(currentUserUsername);
            return Messages.REMOVE_MAP_SUCCESSFUL;
        }
        return Messages.REMOVE_MAP_CANCLED;
    }

    private Messages setMap() throws IOException {
        ArrayList<String> maps = controller.getMaps(currentUserUsername);
        dataOutputStream.writeUTF("Available maps:");
        for (int i = 0; i < maps.size(); i++)
            dataOutputStream.writeUTF((i + 1) + ". " + maps.get(i));
        dataOutputStream.writeUTF("Enter the number of the map you want to edit or enter \"new map\" to create a new one:");
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return Messages.EXIT_CREATE_MAP_MENU;
            else if (input.matches("\\s*new\\s+map\\s*"))
                return setNewMap();
            else if (!input.matches("\\-?\\d+"))
                dataOutputStream.writeUTF("Enter a whole number!");
            else if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > maps.size())
                dataOutputStream.writeUTF("Enter a number between 1 and " + maps.size());
            else {
                controller.setExistingMap(Integer.parseInt(input) - 1, currentUserUsername);
                return Messages.SET_MAP_SUCCESSFUL;
            }

        }
    }

    private Messages setNewMap() throws IOException {
        int mapSize = getNewMapSize();
        if (mapSize < 0)
            return Messages.EXIT_CREATE_MAP_MENU;
        int numberOfPlayers = getNumberOfPlayers();
        if (numberOfPlayers < 0)
            return Messages.EXIT_CREATE_MAP_MENU;
        HashMap<LordColor, int[]> lordsPositions = getKeepsPositions(numberOfPlayers, mapSize);
        if (lordsPositions == null)
            return Messages.EXIT_CREATE_MAP_MENU;
        String mapName = getNewMapName();
        if (mapName == null)
            return Messages.EXIT_CREATE_MAP_MENU;
        controller.setNewMap(currentUserUsername, mapSize, mapName, numberOfPlayers, lordsPositions);
        for (LordColor lordColor : lordsPositions.keySet()) {
            int row = lordsPositions.get(lordColor)[0];
            int column = lordsPositions.get(lordColor)[1];
            controller.dropBuilding(row, column + 7, "stock pile", lordColor.toString());
            controller.dropUnit(row + 3, column + 3, 1, "lord", lordColor.toString(), true);
        }
        return Messages.SET_MAP_SUCCESSFUL;
    }

    private int getNumberOfPlayers() throws IOException {
        dataOutputStream.writeUTF("Enter the number of players:");
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return -1;
            else if (!input.matches("\\-?\\d+"))
                dataOutputStream.writeUTF("Enter a whole number!");
            else if (Integer.parseInt(input) < 2 || Integer.parseInt(input) > 8)
                dataOutputStream.writeUTF("Enter a number between 2 and 8!");
            else
                return Integer.parseInt(input);
        }
    }

    private HashMap<LordColor, int[]> getKeepsPositions(int numberOfPlayers, int mapSize) throws IOException {
        int counter = 1;
        HashMap<LordColor, int[]> positions = new HashMap<LordColor, int[]>();
        while (counter <= numberOfPlayers) {
            dataOutputStream.writeUTF("Enter the Coordinates of lord number " + counter + " (color "
                    + LordColor.getLordColor(counter - 1) + ") keep:");
            String input = dataInputStream.readUTF();
            Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
            Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
            if (input.matches("\\s*exit\\s*"))
                return null;
            if (rowMatcher == null)
                dataOutputStream.writeUTF("Enter the row number!");
            else if (columnMatcher == null)
                dataOutputStream.writeUTF("Enter the column number!");
            else {
                int row = Integer.parseInt(rowMatcher.group("row")) - 1;
                int column = Integer.parseInt(columnMatcher.group("column")) - 1;
                if (row < 0 || row >= mapSize - 7 || column < 0 || column >= mapSize - 12)
                    dataOutputStream.writeUTF("Invalid Coordinates!");
                if (!controller.canDropKeep(positions, row, column))
                    dataOutputStream.writeUTF("There is already a lord castle in this position!");
                else {
                    int[] keepPosition = new int[] { row, column };
                    positions.put(LordColor.getLordColor(counter - 1), keepPosition);
                    counter++;
                }
            }
        }
        return positions;
    }

    private int getNewMapSize() throws IOException {
        dataOutputStream.writeUTF("Enter the size of the new map:");
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return -1;
            else if (!input.matches("-?\\d+"))
                dataOutputStream.writeUTF("Enter a whole number!");
            else if (Integer.parseInt(input) < 200 || Integer.parseInt(input) > 400)
                dataOutputStream.writeUTF("Enter a number between 200 and 400!");
            else
                return Integer.parseInt(input);
        }
    }

    private String getNewMapName() throws IOException {
        dataOutputStream.writeUTF("Enter a name for the new map:");
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return null;
            else if (!input.matches("\\s*\\w+\\s*"))
                dataOutputStream.writeUTF("Map name can only contain letters , digits and underscore!");
            else {
                boolean nameAlreadyExists = false;
                ArrayList<String> userMaps = controller.getMaps(currentUserUsername);
                for (String mapName : userMaps)
                    if (mapName.equals(input)) {
                        dataOutputStream.writeUTF("You already have a map with this name!");
                        nameAlreadyExists = true;
                        break;
                    }
                if (!nameAlreadyExists)
                    return input;
            }
        }
    }

    private String setPixelTexture(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        Matcher textureMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TYPE);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        String checkTexture = checkTypeFormat(textureMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        if (checkTexture != null)
            return checkTexture;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String textureName = textureMatcher.group("type");
        switch (controller.setPixelTexture(row - 1, column - 1, textureName)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case INVALID_TEXTURE:
                return "Invalid type/texture!";
            case BUILDING_EXIST:
                return "There are some buildings in this pixel!";
            case SET_TEXTURE_SUCCESSFUL:
                return "Set type/texture successful!";
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
        Matcher textureMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TYPE);
        String checkRegionCoordinates = Controller.checkRegionCoordinatesFormat(x1Matcher, y1Matcher, x2Matcher,
                y2Matcher);
        String checkTexture = checkTypeFormat(textureMatcher);
        if (checkRegionCoordinates != null)
            return checkRegionCoordinates;
        if (checkTexture != null)
            return checkTexture;
        int x1 = Integer.parseInt(x1Matcher.group("frow"));
        int y1 = Integer.parseInt(y1Matcher.group("fcolumn"));
        int x2 = Integer.parseInt(x2Matcher.group("srow"));
        int y2 = Integer.parseInt(y2Matcher.group("scolumn"));
        String textureName = textureMatcher.group("type");
        switch (controller.setRegionTexture(x1 - 1, y1 - 1, x2 - 1, y2 - 1, textureName)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
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

    private String checkTypeFormat(Matcher typeMatcher) {
        if (typeMatcher == null)
            return "Enter the type/texture you want to set!";
        return null;
    }

    private String clearPixel(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        String checkCoordinatesFormat = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinatesFormat != null)
            return checkCoordinatesFormat;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        switch (controller.clearPixel(row - 1, column - 1)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
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
        String checkRegionCoordinates = Controller.checkRegionCoordinatesFormat(x1Matcher, y1Matcher, x2Matcher,
                y2Matcher);
        if (checkRegionCoordinates != null)
            return checkRegionCoordinates;
        int x1 = Integer.parseInt(x1Matcher.group("frow"));
        int y1 = Integer.parseInt(y1Matcher.group("fcolumn"));
        int x2 = Integer.parseInt(x2Matcher.group("srow"));
        int y2 = Integer.parseInt(y2Matcher.group("scolumn"));
        switch (controller.clearRegion(x1 - 1, y1 - 1, x2 - 1, y2 - 1)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
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
        Matcher typeMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TYPE);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        String checkType = checkTypeFormat(typeMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        if (checkType != null)
            return checkType;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String type = typeMatcher.group("type").trim();
        switch (controller.dropTree(row - 1, column - 1, type)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
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
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        if (directionMatcher == null)
            return "Enter the direction!";
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String direction = directionMatcher.group("direction").trim();
        switch (controller.dropRock(row - 1, column - 1, direction)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
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

    private String dropUnit(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        Matcher typeMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TYPE);
        Matcher colorMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLOR);
        Matcher countMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COUNT);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        String checkType = checkTypeFormat(typeMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        if (checkType != null)
            return checkType;
        if (colorMatcher == null)
            return "Enter the lord color!";
        if (countMatcher == null)
            return "Enter the count of units!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        int count = Integer.parseInt(countMatcher.group("count"));
        String type = typeMatcher.group("type").trim();
        String lordColor = colorMatcher.group("color");
        switch (controller.dropUnit(row, column, count, type, lordColor, false)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case INVALID_UNIT_NAME:
                return "Invalid unit name!";
            case INVALID_COLOR:
                return "Invalid lord color!";
            case CANT_PLACE_THIS:
                return "Can't drop a unit in this pixel";
            case DROP_UNIT_SUCCESSFUL:
                return "Drop unit successful!";
            default:
                break;
        }

        return null;
    }

    private String dropBuilding(String input) {
        Matcher rowMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_ROW);
        Matcher columnMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLUMN);
        Matcher typeMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_TYPE);
        Matcher colorMatcher = CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.GET_COLOR);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        String checkType = checkTypeFormat(typeMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        if (checkType != null)
            return checkType;
        if (colorMatcher == null)
            return "Enter the lord color!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        String type = typeMatcher.group("type").trim();
        String lordColor = colorMatcher.group("color");
        switch (controller.dropBuilding(row, column, type, lordColor)) {
            case INVALID_ROW_OR_COLUMN:
                return "Invalid row or column!";
            case INVALID_BUILDING_NAME:
                return "Invalid building name!";
            case INVALID_COLOR:
                return "Invalid lord color!";
            case THERES_ALREADY_BUILDING:
                return "There is already a building here!";
            case THERES_ALREADY_UNIT:
                return "You can't drop buildings on units!";
            case CANT_PLACE_THIS:
                return "You can't place this here!";
            case MUST_BE_ADJACENT_TO_BUILDINGS_OF_THE_SAME_TYPE:
                return "Must be adjacent to buildings of the same type!";
            case DEPLOYMENT_SUCCESSFUL:
                return type + " deployed successfully!";
            default:
                break;
        }
        return null;
    }

}