package view;

import controller.MapMenuController;
import controller.MapMenuCommands;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Matcher;
import model.Texture;

public class MapMenu {
    protected final MapMenuController controller = new MapMenuController();
    private int x;
    private int y;

    public MapMenu(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void run(Scanner scanner) {
        printMap(this.x, this.y);
        while (true) {
            String input = scanner.nextLine();
            if (MapMenuCommands.getMatcher(input, MapMenuCommands.EXIT) != null)
                return;
            else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_MAP) != null){
                String showMapStr = showMap(input);
                if(showMapStr != null) System.out.println(showMapStr);
            }
            else if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_MAP) != null)
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
            up = (upString == null) ? 1 : -Integer.parseInt(upString);
        }
        if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_DOWN) != null) {
            String downString = MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_DOWN).group("down");
            down = (downString == null) ? 1 : Integer.parseInt(downString);
        }
        if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_LEFT) != null) {
            String leftString = MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_LEFT).group("left");
            left = (leftString == null) ? 1 : -Integer.parseInt(leftString);
        }
        if (MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_RIGHT) != null) {
            String rightString = MapMenuCommands.getMatcher(input, MapMenuCommands.MOVE_RIGHT).group("right");
            right = (rightString == null) ? 1 : Integer.parseInt(rightString);
        }
        if (!controller.checkMoveCordinates(this.x, this.y, up, down, left, right))
            System.out.println("Cordinates out of bounds or invalid!");
        else {
            this.x += up + down;
            this.y += left + right;
            printMap(this.x, this.y);
        }
    }

    protected void printMap(int x, int y) {
        ArrayList<ArrayList<Colors>> mapColorList = controller.getMapColorList(x, y);
        int numberOfRows = mapColorList.size();
        int numberOfColumns = mapColorList.get(0).size();
        int numberOfRowSplitters = numberOfColumns * 7 + 1;
        for (int i = 0; i <= 4 * numberOfRows; i++) {
            if (i % 4 == 0) {
                splitRow(numberOfRowSplitters, i != 0 && i != 4 * numberOfRows);
                continue;
            }
            for (int j = 0; j <= 7 * numberOfColumns; j++) {
                if (j % 7 == 0) System.out.print("|");
                else {
                    int row = (int) Math.floor(i / 4);
                    int column = (int) Math.floor(j / 7);
                    System.out.print(mapColorList.get(row).get(column));
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
            while (length-- > 0) System.out.print("-");
            System.out.println("|");
        } else {
            while (length-- > 0) System.out.print("-");
            System.out.println();
        }
    }
    protected String showDetails(String input) {
        Matcher rowMatcher = 
            MapMenuCommands.getMatcher(input, MapMenuCommands.GET_ROW);
        Matcher columnMatcher = 
            MapMenuCommands.getMatcher(input, MapMenuCommands.GET_COLUMN);
        if(rowMatcher == null || rowMatcher.group("row") == null)
            return "Enter the row number!";
        if(columnMatcher == null || columnMatcher.group("column") == null)
            return "Enter the column number!";
        if(!rowMatcher.group("row").matches("\\-?\\d+")
            || !columnMatcher.group("column").matches("\\-?\\d+"))
            return "Enter whole number for cordinates!";
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        if(!controller.checkCordinates(row, column))
            return "Invalid cordinates!";
        return controller.getDetails(row, column);
    }
    protected void mapGuide() {
        for(Texture texture : Texture.values())
            printGuide(texture);
    }
    private void printGuide(Texture texture) {
        System.out.print(texture.getColor());
        System.out.print(" ");
        System.out.print(Colors.RESET);
        System.out.println(" : " + texture);
    }
    protected String showMap(String input){
        Matcher rowMatcher =
            MapMenuCommands.getMatcher(input, MapMenuCommands.GET_ROW);
        Matcher columnMatcher =
            MapMenuCommands.getMatcher(input, MapMenuCommands.GET_COLUMN);
        if(rowMatcher == null || rowMatcher.group("row") == null)
            return "Enter the row number!";
        if(columnMatcher == null || columnMatcher.group("column") == null)
            return "Enter the column number!";
        if(!rowMatcher.group("row").matches("\\-?\\d+")
            || !columnMatcher.group("column").matches("\\-?\\d+"))
            return "Enter whole number for cordinates!";
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        if(!controller.checkCordinates(row, column))
            return "Invalid cordinates!";
        this.x = row - 1;
        this.y = column - 1;
        printMap(this.x, this.y);
        return null;
    }

}