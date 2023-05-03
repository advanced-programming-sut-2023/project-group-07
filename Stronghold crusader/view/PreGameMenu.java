package view;

import controller.Controller;
import controller.PreGameMenuController;
import model.Game;
import model.Map;
import model.User;
import model.Government;
import model.LordColor;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.min;

import java.io.IOException;

public class PreGameMenu { // todo: this class haven't been tested
    private Scanner scanner;
    private final PreGameMenuController controller;
    private final GameMenu gameMenu = new GameMenu();

    public PreGameMenu() {
        controller = new PreGameMenuController();
    }

    public void run(Scanner scanner) throws IOException{
        this.scanner = scanner;
        Integer numberOfPlayers = getNumberOfPlayers();
        if (numberOfPlayers == null)
            return;
        Map map = getChosenMap(numberOfPlayers);
        if (map == null)
            return; 
        ArrayList<Government> governments = getGovernments(numberOfPlayers, map);
        if (governments == null)
            return; 
        Integer earlyGameGolds = getGold();
        if (earlyGameGolds == null)
            return;

        Game game = new Game(map, governments, earlyGameGolds);
        Controller.currentGame = game;
        gameMenu.run(scanner);
    }

    private Integer getGold() {
        System.out.print("How much gold do you want to begin with? ");
        while (true) {
            System.out.println("Enter a positive whole number:");
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return null;

            if (input.matches("\\+?\\d+"))
                return Integer.parseInt(input);
        }
    }

    private Map getChosenMap(int numberOfPlayers) {
        ArrayList<Map> maps = Map.getMaps();
        System.out.println("Choose a map number:");
        int number = 1;
        for (Map map : maps) {
            System.out.println(number + ". " + map.getName() +
                    " maximum number of players : " + map.getNumberOfPlayers());
            number++;
        }
        int mapIndex;
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return null;

            if (input.matches("\\d+")) {
                mapIndex = Integer.parseInt(input);
                if (mapIndex > 0 && mapIndex <= maps.size()) {
                    mapIndex--;
                    if (maps.get(mapIndex).getNumberOfPlayers() < numberOfPlayers) {
                        System.out.println("Too few number of players!");
                        continue;
                    } else
                        return maps.get(mapIndex);
                }
            }
            System.out.println("Enter a number between 1 and " + maps.size() + "!");
        }
    }

    private Integer getNumberOfPlayers() {
        int numberOfPlayers;
        System.out.println("How many players are in the game?");
        int maxPlayers = Map.maxPlayerOfMaps();
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return null;
            if (input.matches("\\d+")) {
                int inputNumber = Integer.parseInt(input);
                if (inputNumber >= 2 && inputNumber <= maxPlayers) {
                    numberOfPlayers = inputNumber;
                    return numberOfPlayers;
                }
            }
            System.out.println("Enter a number between 2 and " + maxPlayers + "!");
        }
    }

    private ArrayList<Government> getGovernments(int numberOfPlayers, Map map) {
        ArrayList<Government> governments = new ArrayList<>();
        LordColor currentLordColor = LordColor.getLordColor(0);
        governments.add(new Government(LordColor.getLordColor(0), Controller.currentUser, 0, map.getKeepPosition(currentLordColor)[0],
                map.getKeepPosition(currentLordColor)[1]));
        for (int i = 1; i < numberOfPlayers; i++) {
            currentLordColor = LordColor.getLordColor(i);
            System.out.println("Enter username of next player (color " + currentLordColor + "):");
            String input = scanner.nextLine();
            if (input.matches("\\s*exit\\s*"))
                return null;
            User user = User.getUserByUsername(input);
            while (user == null) {
                System.out.println("There is no user with this username! Enter another one:");
                input = scanner.nextLine();
                if (input.matches("\\s*exit\\s*"))
                    return null;
                user = User.getUserByUsername(input);
            }
            governments.add(new Government(LordColor.getLordColor(i), user, 0, map.getKeepPosition(currentLordColor)[0],
                    map.getKeepPosition(currentLordColor)[1]));
        }
        return governments;
    }
}