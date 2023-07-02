package Server.view;

import controller.Controller;
import controller.MapMenuCommands;
import controller.MapMenuController;
import model.Game;
import model.Map;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class MapMenuServer {
    protected final MapMenuController controller = new MapMenuController();
    private int x;
    private int y;

    private AuthenticatedDataOutputStream dataOutputStream;
    private AuthenticatedDataInputStream dataInputStream;
    private Game game;
    private User currentUser;
    public MapMenuServer(AuthenticatedDataInputStream dataInputStream,
                         AuthenticatedDataOutputStream dataOutputStream,
                         Game game, User currentUser) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.game=game;
        this.currentUser = currentUser;
    }

    public void run(String initialInput) throws IOException {
        controller.refreshMap(game.getMap());
        showMap(initialInput);
        startOperation();
    }

    private void startOperation() throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            sendDataToWatchingUsers(input, true);
            String sendMessage = null;
            if (input.matches("\\s*exit\\s*")) {
               sendMessage = "Exit was successful!";
                return;
            } else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_MAP) != null) {
                String showMapStr = showMap(input);
                if (showMapStr != null)
                    sendMessage = showMapStr;
            } else if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_MAP) != null)
                moveMap(input);
            else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_DETAILS) != null)
                sendMessage = showDetails(input);
            else
                sendMessage = "Invalid command!";
            if (sendMessage != null) {
                dataOutputStream.writeUTF(sendMessage);
                sendDataToWatchingUsers(sendMessage, false);
            }
        }
    }

    public void run(Map map) throws IOException {
        controller.refreshMap(map);
        startOperation();
    }

    protected void moveMap(String input) throws IOException {
        int up = 0, down = 0, left = 0, right = 0;
        if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_UP) != null) {
            String upString = MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_UP).group("up");
            up = (upString == null) ? -1 : -Integer.parseInt(upString.trim());
        }
        if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_DOWN) != null) {
            String downString = MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_DOWN).group("down");
            down = (downString == null) ? 1 : Integer.parseInt(downString.trim());
        }
        if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_LEFT) != null) {
            String leftString = MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_LEFT).group("left");
            left = (leftString == null) ? -1 : -Integer.parseInt(leftString.trim());
        }
        if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_RIGHT) != null) {
            String rightString = MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_RIGHT).group("right");
            right = (rightString == null) ? 1 : Integer.parseInt(rightString.trim());
        }
        if (!controller.checkMoveCoordinates(this.x, this.y, up, down, left, right)) {
            dataOutputStream.writeUTF("Coordinates out of bounds or invalid!");
            sendDataToWatchingUsers("Coordinates out of bounds or invalid!", false);
        }
        else {
            this.x = controller.setMoveX(this.x, up, down);
            this.y = controller.setMoveY(this.y, left, right);
            printMap();
        }
    }

    protected void printMap() throws IOException {
        ArrayList<ArrayList<Colors>> mapColorList = controller.getMapColorList(this.x, this.y);
        ArrayList<ArrayList<String>> mapObjects;
        String result = "";
        if (Controller.currentGame == null)
            mapObjects = controller.getMapObjects(this.x, this.y, null);
        else
            mapObjects = controller.getMapObjects(this.x, this.y, Controller.currentGame.getCurrentGovernment());
        int numberOfRows = mapColorList.size();
        int numberOfColumns = mapColorList.get(0).size();
        int numberOfRowSplitters = numberOfColumns * 6 + 1;
        for (int i = 0; i <= 4 * numberOfRows; i++) {
            if (i % 4 == 0) {
                result += splitRow(numberOfRowSplitters, i != 0 && i != 4 * numberOfRows);
                continue;
            }
            for (int j = 0; j <= 6 * numberOfColumns; j++) {
                if (j % 6 == 0)
                    result += "|";
                else {
                    int row = (int) Math.floor(i / 4);
                    int column = (int) Math.floor(j / 6);
                    if (i % 4 == 2 && j % 6 == 3 && !mapObjects.get(row).get(column).equals(""))
                        result += mapObjects.get(row).get(column);
                    else
                        result += "#";
                }
            }
            result += "\n";
        }
        dataOutputStream.writeUTF(result);
        sendDataToWatchingUsers(result, false);
    }

    private String splitRow(int length, boolean flag) throws IOException {
        String result = "";
        if (flag) {
            length -= 2;
            result += "|";
            while (length-- > 0)
                result += "-";
            result += "|\n";
        } else {
            while (length-- > 0)
                result += "-";
            result += "\n";
        }
        return result;
    }

    protected String showDetails(String input) {
        Matcher rowMatcher = MapMenuCommands.getMatcher(input, MapMenuCommands.GET_ROW);
        Matcher columnMatcher = MapMenuCommands.getMatcher(input, MapMenuCommands.GET_COLUMN);
        if (rowMatcher == null)
            return "Enter the row number!";
        if (columnMatcher == null)
            return "Enter the column number!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        if (!controller.checkCordinates(row, column))
            return "Invalid coordinates!";
        return controller.getDetails(row, column);
    }

    protected String showMap(String input) throws IOException {
        Matcher rowMatcher = MapMenuCommands.getMatcher(input, MapMenuCommands.GET_ROW);
        Matcher columnMatcher = MapMenuCommands.getMatcher(input, MapMenuCommands.GET_COLUMN);
        if (rowMatcher == null || rowMatcher.group("row") == null)
            return "Enter the row number!";
        if (columnMatcher == null || columnMatcher.group("column") == null)
            return "Enter the column number!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        if (!controller.checkCordinates(row, column))
            return "Invalid coordinates!";
        this.x = row;
        this.y = column;
        printMap();
        return null;
    }

    private void sendDataToWatchingUsers(String data, boolean input) throws IOException {
        if (game == null)
            return;
        for (Connection connection : game.getWatchingUsers())
            if (connection.isAlive()) {
                if (input)
                    data += (" (" + currentUser.getUsername() + ")");
                connection.sendData(data);
            }

    }

}