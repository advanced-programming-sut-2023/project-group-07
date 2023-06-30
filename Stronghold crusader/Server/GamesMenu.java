package Server;

import controller.Controller;
import controller.GameMenuController;
import model.*;

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

    public void gameMenuHandler() throws IOException, InterruptedException {
        dataOutputStream.writeUTF("you have entered lobby");
        while (true) {
            if(dataInputStream.available()!=0){
                String input = dataInputStream.readUTF();
                if (input.equals("create")) {
                    createGame();
                } else if(input.equals("join")) {
                    joinGame();
                } else if(input.equals("enter main menu"))
                    return;
                else
                    dataOutputStream.writeUTF("invalid input");
            }
        }
    }

    public void createGame() throws IOException, InterruptedException {
        Integer numberOfPlayers = getNumberOfPlayers();
        if (numberOfPlayers == null)
            return;
        Map map = getChosenMap(numberOfPlayers);
        if (map == null)
            return;
        Integer earlyGameGolds = getGold();
        if (earlyGameGolds == null)
            return;
        dataOutputStream.writeUTF("Game created.");
        Lobby lobby = new Lobby(true,map,currentUser);
        getLobbies().add(lobby);
        lobby.getUsers().add(currentUser);
        while(true) {
            System.out.println(lobby.getUsers().size()+"   "+numberOfPlayers);
            if(lobby.getUsers().size()==numberOfPlayers){
                System.out.println("LOR");
                lobby.setGame(new Game(map,createGovernments(numberOfPlayers,lobby,map),earlyGameGolds));
                lobby.setGameMenuController(new GameMenuController());
                new GameMenuServer(dataOutputStream,dataInputStream,currentUser, lobby.getGameMenuController(), lobby.getGame()).gameHandler();
            }
        }
    }

    public void joinGame() throws IOException {
        while (true) {
            if (dataInputStream.available() != 0) {
                String input = dataInputStream.readUTF();
                int id = Integer.parseInt(input);
                if(id>=getLobbies().size())
                    dataOutputStream.writeUTF("invalid id!");
                else{
                    Lobby lobby = getLobbies().get(id);
                    lobby.getUsers().add(currentUser);
                    while (true) {
                        if(lobby.getGame()!=null && lobby.getGameMenuController()!=null) {
                            System.out.println("KIR");
                            new GameMenuServer(dataOutputStream,dataInputStream,currentUser, lobby.getGameMenuController(), lobby.getGame()).gameHandler();
                        }
                    }
                }
            }
        }
    }

    private Integer getGold() throws IOException {
        dataOutputStream.writeUTF("How much gold do you want to begin with? ");
        while (true) {
            dataOutputStream.writeUTF("Enter a positive whole number:");
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return null;

            if (input.matches("\\+?\\d+"))
                return Integer.parseInt(input);
        }
    }

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

    private Integer getNumberOfPlayers() throws IOException {
        int numberOfPlayers;
        dataOutputStream.writeUTF("How many players are in the game?");
        int maxPlayers = Map.maxPlayerOfMaps();
        while (true) {
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return null;
            if (input.matches("\\d+")) {
                int inputNumber = Integer.parseInt(input);
                if (inputNumber >= 2 && inputNumber <= maxPlayers) {
                    numberOfPlayers = inputNumber;
                    return numberOfPlayers;
                }
            }
            dataOutputStream.writeUTF("Enter a number between 2 and " + maxPlayers + "!");
        }
    }

    private ArrayList<Government> getGovernments(int numberOfPlayers, Map map) throws IOException {
        ArrayList<Government> governments = new ArrayList<>();
        LordColor currentLordColor = LordColor.getLordColor(0);
        governments.add(new Government(LordColor.getLordColor(0), Controller.currentUser, 0,
                map.getKeepPosition(currentLordColor)[0],
                map.getKeepPosition(currentLordColor)[1]));
        for (int i = 1; i < numberOfPlayers; i++) {
            currentLordColor = LordColor.getLordColor(i);
            dataOutputStream.writeUTF("Enter username of next player (color " + currentLordColor + "):");
            String input = dataInputStream.readUTF();
            if (input.matches("\\s*exit\\s*"))
                return null;
            User user = User.getUserByUsername(input);
            while (user == null || repeatedUsername(user, governments)) {
                if (user == null)
                    dataOutputStream.writeUTF("There is no user with this username! Enter another one:");

                else
                    dataOutputStream.writeUTF("This user is already chosen!");
                input = dataInputStream.readUTF();
                if (input.matches("\\s*exit\\s*"))
                    return null;
                user = User.getUserByUsername(input);
            }
            governments.add(new Government(LordColor.getLordColor(i), user, 0, map.getKeepPosition(currentLordColor)[0],
                    map.getKeepPosition(currentLordColor)[1]));
        }
        return governments;
    }

    private ArrayList<Government> createGovernments(int numberOfPlayers,Lobby lobby,Map map) {
        ArrayList<Government> governments = new ArrayList<>();
        LordColor currentLordColor = LordColor.getLordColor(0);
        governments.add(new Government(LordColor.getLordColor(0), currentUser, 0,
                map.getKeepPosition(currentLordColor)[0],
                map.getKeepPosition(currentLordColor)[1]));
        for (int i = 1; i < numberOfPlayers; i++) {
            currentLordColor = LordColor.getLordColor(i);
            String input = lobby.getUsers().get(i).getUsername();
            User user = User.getUserByUsername(input);
            governments.add(new Government(LordColor.getLordColor(i), user, 0, map.getKeepPosition(currentLordColor)[0],
                    map.getKeepPosition(currentLordColor)[1]));
        }
        return governments;
    }

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
