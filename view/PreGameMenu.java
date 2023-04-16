package view;

import controller.Controller;
import controller.PreGameMenuController;
import model.Game;
import model.Map;
import model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class PreGameMenu {
    private Scanner scanner;
    private PreGameMenuController controller;

    public PreGameMenu() {
        controller = new PreGameMenuController();
    }

    public void run(Scanner scanner) {
        this.scanner = scanner;
        ArrayList<User> players = new ArrayList<>();
        players.add(Controller.currentUser);
        int numberOfPlayers = getNumberOfPlayers();
        getPlayers(scanner, players, numberOfPlayers);
        Map map = getChosenMap(scanner);
        Game game = new Game(map,players);
        Controller.currentGame = game;
        // todo : go to next menu
    }

    private Map getChosenMap(Scanner scanner) {
        // todo: check number of players in each map
        ArrayList<String> mapsName = Map.getNamesOfMaps();
        System.out.println("choose a map number");
        int number = 1;
        for (String name : mapsName) {
            System.out.println(number + ". " + name);
            number++;
        }
        String input = scanner.next();
        int mapIndex;
        while (true) {
            if (input.matches("\\d+")) {
                mapIndex = Integer.parseInt(input);
                if (mapIndex > 0 && mapIndex <= mapsName.size()) break;
            }
            System.out.println("enter a number between 1 and " + mapsName.size());
        }
        return Map.getMapByName(mapsName.get(mapIndex));
    }


    private int getNumberOfPlayers() {
        int numberOfPlayers;
        System.out.println("how many players are in the game?");
        int maxPlayers = Controller.maxPlayers();
        while (true) {
            String input = scanner.next();
            if (input.matches("\\d+")) {
                int inputNumber = Integer.parseInt(input);
                if (inputNumber >= 2 && inputNumber <= maxPlayers) {
                    numberOfPlayers = inputNumber;
                    break;
                }
            }
            System.out.println("write a number between 2 to " + maxPlayers);
        }
        return numberOfPlayers;
    }

    private void getPlayers(Scanner scanner, ArrayList<User> players, int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers - 1; i++) { // todo : change messages
            System.out.println("write username of next player");
            String username = scanner.nextLine();
            User user = Controller.getUserByUsername(username);
            while (user == null) {
                System.out.println("there is no user with this username. enter another one");
                username = scanner.nextLine();
                user = Controller.getUserByUsername(username);
            }
            players.add(user);
        }
    }


}