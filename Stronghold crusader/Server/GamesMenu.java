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
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.currentUser = currentUser;
    }

    public void gameMenuHandler() throws IOException, InterruptedException {
        dataOutputStream.writeUTF("you have entered lobby");
        while (true) {
            if (dataInputStream.available() != 0) {
                String input = dataInputStream.readUTF();
                if (input.equals("create")) {
                    createGame();
                } else if (input.equals("join")) {
                    joinGame();
                } else if (input.equals("show games")) {
                    showGames();
                } else if (input.equals("enter main menu"))
                    return;
                else
                    dataOutputStream.writeUTF("invalid input");
            }
        }
    }

    private void showGames() throws IOException {
        for (Lobby lobby : getLobbies()) {
            if (lobby.isPublic()) {
                String output = "id: " + lobby.getId() + "  ,  " + "capacity: " + lobby.getUsers().size() + "/" + lobby.getNumberOfPlayers() + "\nUsers:";
                for (User user : lobby.getUsers()) {
                    output += "\nusername: " + user.getUsername() + "  ,  " + "nickname: " + user.getNickname();
                }
                dataOutputStream.writeUTF(output);
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
        Lobby lobby = new Lobby(true, map, currentUser, numberOfPlayers, earlyGameGolds);
        getLobbies().add(lobby);
        lobby.getUsers().add(currentUser);
        lobbyHandler(lobby);
    }

    public void joinGame() throws IOException {
        dataOutputStream.writeUTF("enter lobby id");
        while (true) {
            if (dataInputStream.available() != 0) {
                String input = dataInputStream.readUTF();
                int id = 0;
                try {
                    id = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    dataOutputStream.writeUTF("enter a number");
                    showGames();
                    continue;
                }
                Lobby lobby = null;
                for (Lobby lobby2 : getLobbies())
                    if (lobby2.getId() == id) {
                        lobby = lobby2;
                        break;
                    }
                if (lobby == null){
                    dataOutputStream.writeUTF("invalid id");
                    showGames();
                }
                else {
                    if (lobby.getUsers().size() == lobby.getNumberOfPlayers())
                        dataOutputStream.writeUTF("This lobby is full!");
                    else {
                        lobby.getUsers().add(currentUser);
                        lobbyHandler(lobby);
                    }
                }
            }
        }
    }

    public void lobbyHandler(Lobby lobby) throws IOException {
        Map map = lobby.getMap();
        int numberOfPlayers = lobby.getNumberOfPlayers();
        int earlyGameGolds = lobby.getEarlyGameGolds();
        while (true) {
            if (!getLobbies().contains(lobby)) {
                dataOutputStream.writeUTF("lobby was deleted due to being idle for too long!");
                return;
            } else if (lobby.getGame() == null && lobby.getUsers().size() == numberOfPlayers && currentUser.equals(lobby.getAdmin())) {
                Game game = new Game(map, createGovernments(numberOfPlayers, lobby, map), earlyGameGolds);
                lobby.setGame(game);
                lobby.setGameMenuController(new GameMenuController(game));
                new GameMenuServer(dataOutputStream, dataInputStream, currentUser, lobby.getGameMenuController(), lobby.getGame()).gameHandler();
            } else if (lobby.getGame() != null && lobby.getGameMenuController() != null && !currentUser.equals(lobby.getAdmin())) {
                new GameMenuServer(dataOutputStream, dataInputStream, currentUser, lobby.getGameMenuController(), lobby.getGame()).gameHandler();
            } else if (dataInputStream.available() != 0) {
                String input = dataInputStream.readUTF();
                if (input.equals("exit")) {
                    dataOutputStream.writeUTF("You left the lobby");
                    lobby.removeUser(currentUser);
                    lobby.updateAdminAndLobby();
                    return;
                } else if (currentUser.equals(lobby.getAdmin()) && input.equals("start game")) {
                    if (lobby.getUsers().size() < 2)
                        dataOutputStream.writeUTF("too few players!");
                    else {
                        Game game = new Game(map, createGovernments(lobby.getUsers().size(), lobby, map), earlyGameGolds);
                        lobby.setGame(game);
                        lobby.setGameMenuController(new GameMenuController(game));
                        new GameMenuServer(dataOutputStream, dataInputStream, currentUser, lobby.getGameMenuController(), lobby.getGame()).gameHandler();
                    }
                } else if (currentUser.equals(lobby.getAdmin()) && input.equals("private")) {
                    lobby.setPublic(false);
                    dataOutputStream.writeUTF("This lobby is now private");
                } else if (currentUser.equals(lobby.getAdmin()) && input.equals("public")) {
                    lobby.setPublic(true);
                    dataOutputStream.writeUTF("This lobby is now public");
                } else if (input.equals("enter chat")) {
                    dataOutputStream.writeUTF("you have entered chat");
                    enterChat(lobby);
                } else
                    dataOutputStream.writeUTF("Invalid command");
            }
        }
    }

    private GroupChatMenu groupChatMenu = null;

    private void enterChat(Lobby lobby) {
        if (groupChatMenu == null) {
            groupChatMenu = new GroupChatMenu(dataOutputStream, dataInputStream, currentUser);
        }
        groupChatMenu.lobbyChat(lobby.groupChat());
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
                    if (maps.get(mapIndex).getNumberOfPlayers() < numberOfPlayers) {
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

//    private ArrayList<Government> getGovernments(int numberOfPlayers, Map map) throws IOException {
//        ArrayList<Government> governments = new ArrayList<>();
//        LordColor currentLordColor = LordColor.getLordColor(0);
//        governments.add(new Government(LordColor.getLordColor(0), Controller.currentUser, 0,
//                map.getKeepPosition(currentLordColor)[0],
//                map.getKeepPosition(currentLordColor)[1]));
//        for (int i = 1; i < numberOfPlayers; i++) {
//            currentLordColor = LordColor.getLordColor(i);
//            dataOutputStream.writeUTF("Enter username of next player (color " + currentLordColor + "):");
//            String input = dataInputStream.readUTF();
//            if (input.matches("\\s*exit\\s*"))
//                return null;
//            User user = User.getUserByUsername(input);
//            while (user == null || repeatedUsername(user, governments)) {
//                if (user == null)
//                    dataOutputStream.writeUTF("There is no user with this username! Enter another one:");
//
//                else
//                    dataOutputStream.writeUTF("This user is already chosen!");
//                input = dataInputStream.readUTF();
//                if (input.matches("\\s*exit\\s*"))
//                    return null;
//                user = User.getUserByUsername(input);
//            }
//            governments.add(new Government(LordColor.getLordColor(i), user, 0, map.getKeepPosition(currentLordColor)[0],
//                    map.getKeepPosition(currentLordColor)[1]));
//        }
//        return governments;
//    }

    private ArrayList<Government> createGovernments(int numberOfPlayers, Lobby lobby, Map map) {
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

//    private boolean repeatedUsername(User user, ArrayList<Government> governments) {
//        for (Government government : governments)
//            if (government.getUser().equals(user))
//                return true;
//        return false;
//    }

    public static ArrayList<Lobby> getLobbies() {
        return Lobbies;
    }
}
