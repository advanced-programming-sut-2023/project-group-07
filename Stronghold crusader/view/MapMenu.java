package view;

import controller.MapMenuController;
import controller.MapMenuCommands;
import controller.Controller;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Matcher;
import model.Texture;

public class MapMenu {
    protected final MapMenuController controller = new MapMenuController();
    private int x;
    private int y;
    public void run(Scanner scanner, String initialInput) {
        controller.refreshMap(Controller.currentGame.getMap());
        showMap(initialInput);
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*")){
                System.out.println("Exit was successful!");
                return;
            }
            else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_MAP) != null) {
                String showMapStr = showMap(input);
                if (showMapStr != null)
                    System.out.println(showMapStr);
            } else if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_MAP) != null)
                moveMap(input);
            else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_DETAILS) != null)
                System.out.println(showDetails(input));
            else if (MapMenuCommands.getMatcher(input, MapMenuCommands.MAP_GUIDE) != null)
                mapGuide();
            else
                System.out.println("Invalid command!");
        }
    }

    protected void moveMap(String input) {
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
        if (!controller.checkMoveCoordinates(this.x, this.y, up, down, left, right))
            System.out.println("Cordinates out of bounds or invalid!");
        else {
            this.x = controller.setMoveX(this.x, up, down);
            this.y = controller.setMoveY(this.y, left, right);
            printMap();
        }
    }

    protected void printMap() {
        ArrayList<ArrayList<Colors>> mapColorList = controller.getMapColorList(this.x, this.y);
        ArrayList<ArrayList<String>> mapObjects;
        if(Controller.currentGame == null)
            mapObjects = controller.getMapObjects(this.x, this.y, null);
        else 
            mapObjects = controller.getMapObjects(this.x, this.y, Controller.currentGame.getCurrentGovernment());
        int numberOfRows = mapColorList.size();
        int numberOfColumns = mapColorList.get(0).size();
        int numberOfRowSplitters = numberOfColumns * 6 + 1;
        for (int i = 0; i <= 4 * numberOfRows; i++) {
            if (i % 4 == 0) {
                splitRow(numberOfRowSplitters, i != 0 && i != 4 * numberOfRows);
                continue;
            }
            for (int j = 0; j <= 6 * numberOfColumns; j++) {
                if (j % 6 == 0)
                    System.out.print("|");
                else {
                    int row = (int) Math.floor(i / 4);
                    int column = (int) Math.floor(j / 6);
                    System.out.print(mapColorList.get(row).get(column));
                    if (i % 4 == 2 && j % 6 == 3 && !mapObjects.get(row).get(column).equals("")) {
                        System.out.print(Colors.BLACK_BOLD);
                        System.out.print(mapObjects.get(row).get(column));
                    } else
                        System.out.print("#");
                    System.out.print(Colors.RESET);
                }
            }
            System.out.println();
        }
    }

    private void splitRow(int length, boolean flag) {
        if (flag) {
            length -= 2;
            System.out.print("|");
            while (length-- > 0)
                System.out.print("-");
            System.out.println("|");
        } else {
            while (length-- > 0)
                System.out.print("-");
            System.out.println();
        }
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
            return "Invalid cordinates!";
        return controller.getDetails(row, column);
    }

    protected void mapGuide() {
        for (Texture texture : Texture.values())
            printGuide(texture);
    }

    private void printGuide(Texture texture) {
        System.out.print(texture.getColor());
        System.out.print(" ");
        System.out.print(Colors.RESET);
        System.out.println(" : " + texture);
    }

    protected String showMap(String input) {
        Matcher rowMatcher = MapMenuCommands.getMatcher(input, MapMenuCommands.GET_ROW);
        Matcher columnMatcher = MapMenuCommands.getMatcher(input, MapMenuCommands.GET_COLUMN);
        if (rowMatcher == null || rowMatcher.group("row") == null)
            return "Enter the row number!";
        if (columnMatcher == null || columnMatcher.group("column") == null)
            return "Enter the column number!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        if (!controller.checkCordinates(row, column))
            return "Invalid cordinates!";
        this.x = row;
        this.y = column;
        printMap();
        return null;
    }

}