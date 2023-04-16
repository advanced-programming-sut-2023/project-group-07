package view;

import controller.Controller;
import controller.PreGameMenuController;
import model.Game;
import model.Map;
import model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class PreGameMenu {
    //todo : make first letters of each sentence capital
    //todo : input each player gold at the beginning of the game
    private Scanner scanner;
    private final PreGameMenuController controller;
    private final GameMenu gameMenu = new GameMenu();
    public PreGameMenu() {
        controller = new PreGameMenuController();
    }

    public void run(Scanner scanner) {
        this.scanner = scanner;
        ArrayList<User> players = new ArrayList<>();
        players.add(Controller.currentUser);
        int numberOfPlayers = getNumberOfPlayers();
        //todo : delete scanner input
        getPlayers(scanner, players, numberOfPlayers);
        Map map = getChosenMap(scanner, numberOfPlayers);
        Game game = new Game(map, players);
        Controller.currentGame = game;
        gameMenu.run(scanner);
    }

    private Map getChosenMap(Scanner scanner, int numberOfPlayers) {
        // todo: check number of players in each map
        ArrayList<Map> maps = Map.getMaps();
        System.out.println("choose a map number");
        int number = 1;
        for (Map map : maps) {
            System.out.println(number + ". " + map.getName() +
                    " maximum number of players : " + map.getNumberOfPlayers());
            number++;
        }
        String input = scanner.next();
        int mapIndex;
        while (true) {
            if (input.matches("\\d+")) {
                mapIndex = Integer.parseInt(input);
                if (mapIndex > 0 && mapIndex <= maps.size()) {
                    mapIndex--;
                    if (maps.get(mapIndex).getNumberOfPlayers() < numberOfPlayers){
                        System.out.println("you can't choose this map because of its number of players is too few");
                        continue;
                    }
                    else break;
                }
            }
            System.out.println("enter a number between 1 and " + maps.size());
        }
        return maps.get(mapIndex);
    }


    private int getNumberOfPlayers() {
        //todo : handling exit
        //todo :  fix return 
        int numberOfPlayers;
        System.out.println("how many players are in the game?");
        int maxPlayers = Controller.maxPlayers();
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\d+")) {
                int inputNumber = Integer.parseInt(input);
                if (inputNumber >= 2 && inputNumber <= maxPlayers) {
                    numberOfPlayers = inputNumber;
                    break;
                }
            }
            System.out.println("write a whole number between 2 to " + maxPlayers);
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