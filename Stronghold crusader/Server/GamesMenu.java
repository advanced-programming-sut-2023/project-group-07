package Server;

import Client.controller.Controller;
import Client.model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GamesMenu {
    private static final ArrayList<Lobby> Lobbies = new ArrayList<>();
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    User currentUser;
    public GamesMenu(DataOutputStream dataOutputStream, DataInputStream dataInputStream, User currentUser) {
        this.dataOutputStream=dataOutputStream;
        this.dataInputStream=dataInputStream;
        this.currentUser=currentUser;
    }

//    public void gameMenuHandler() throws IOException {
//        dataOutputStream.writeUTF("you have entered lobby");
//        while (true) {
//            if(dataInputStream.available()!=0){
//                String input = dataInputStream.readUTF();
//                if (input.equals("create")) {
//                    createGame();
//                } else if(input.equals("enter main menu"))
//                    return;
//                else
//                    dataOutputStream.writeUTF("invalid input");
//            }
//        }
//    }

//    public void createGame() throws IOException {
//        Integer numberOfPlayers = getNumberOfPlayers();
//        if (numberOfPlayers == null)
//            return;
//        Map map = getChosenMap(numberOfPlayers);
//        if (map == null)
//            return;
//        ArrayList<Government> governments = getGovernments(numberOfPlayers, map);
//        if (governments == null)
//            return;
//        Integer earlyGameGolds = getGold();
//        if (earlyGameGolds == null)
//            return;
//
//        Game game = new Game(map, governments, earlyGameGolds);
//        Controller.currentGame = game;
//        System.out.println("You have entered the game.");
//        gameMenu.run(scanner);
//    }
//
//    private Integer getGold() {
//        System.out.print("How much gold do you want to begin with? ");
//        while (true) {
//            System.out.println("Enter a positive whole number:");
//            String input = scanner.nextLine();
//            if (input.matches("\\s*exit\\s*"))
//                return null;
//
//            if (input.matches("\\+?\\d+"))
//                return Integer.parseInt(input);
//        }
//    }

    private Map getChosenMap(int numberOfPlayers) throws IOException {
        ArrayList<Map> maps = Map.getMaps();
        dataOutputStream.writeUTF("Choose a map number:");
        int number = 1;
        for (Map map : maps) {
            dataOutputStream.writeUTF(number + ". " + map.getName() +
                    " maximum number of players : " + map.getNumberOfPlayers());
            number++;
        }
        int mapIndex;
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return null;

            if (input.matches("\\d+")) {
                mapIndex = Integer.parseInt(input);
                if (mapIndex > 0 && mapIndex <= maps.size()) {
                    mapIndex--;
                    if (maps.get(mapIndex).getNumberOfPlayers() != numberOfPlayers) {
                        dataOutputStream.writeUTF("Too few number of players!");
                        continue;
                    } else
                        return maps.get(mapIndex);
                }
            }
            dataOutputStream.writeUTF("Enter a number between 1 and " + maps.size() + "!");
        }
    }

//    private Integer getNumberOfPlayers() throws IOException {
//        int numberOfPlayers;
//        dataOutputStream.writeUTF("How many players are in the game?");
//        int maxPlayers = Map.maxPlayerOfMaps();
//        while (true) {
//            String input = dataInputStream.readUTF();
//            if (input.matches("\\s*exit\\s*"))
//                return null;
//            if (input.matches("\\d+")) {
//                int inputNumber = Integer.parseInt(input);
//                if (inputNumber >= 2 && inputNumber <= maxPlayers) {
//                    numberOfPlayers = inputNumber;
//                    return numberOfPlayers;
//                }
//            }
//            System.out.println("Enter a number between 2 and " + maxPlayers + "!");
//        }
//    }
//
//    private ArrayList<Government> getGovernments(int numberOfPlayers, Map map) {
//        ArrayList<Government> governments = new ArrayList<>();
//        LordColor currentLordColor = LordColor.getLordColor(0);
//        governments.add(new Government(LordColor.getLordColor(0), Controller.currentUser, 0,
//                map.getKeepPosition(currentLordColor)[0],
//                map.getKeepPosition(currentLordColor)[1]));
//        for (int i = 1; i < numberOfPlayers; i++) {
//            currentLordColor = LordColor.getLordColor(i);
//            System.out.println("Enter username of next player (color " + currentLordColor + "):");
//            String input = scanner.nextLine();
//            if (input.matches("\\s*exit\\s*"))
//                return null;
//            User user = User.getUserByUsername(input);
//            while (user == null || repeatedUsername(user, governments)) {
//                if (user == null)
//                    System.out.println("There is no user with this username! Enter another one:");
//
//                else
//                    System.out.println("This user is already chosen!");
//                input = scanner.nextLine();
//                if (input.matches("\\s*exit\\s*"))
//                    return null;
//                user = User.getUserByUsername(input);
//            }
//            governments.add(new Government(LordColor.getLordColor(i), user, 0, map.getKeepPosition(currentLordColor)[0],
//                    map.getKeepPosition(currentLordColor)[1]));
//        }
//        return governments;
//    }

    private boolean repeatedUsername(User user, ArrayList<Government> governments) {
        for (Government government : governments)
            if (government.getUser().equals(user))
                return true;
        return false;
    }

    public static ArrayList<Lobby> getLobbies() {
        return Lobbies;
    }
}
