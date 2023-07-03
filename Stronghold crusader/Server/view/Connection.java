package Server.view;

import Server.model.Lobby;
import model.Game;
import model.GamePlayBack;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Connection extends Thread {
    private final Socket socket;
    private final AuthenticatedDataInputStream dataInputStream;
    private final AuthenticatedDataOutputStream dataOutputStream;
    private User currentUser = null;
    private boolean isRunning = true;
    private static final ArrayList<Connection> connections = new ArrayList<>();
    private static final ArrayList<Connection> lostConnections = new ArrayList<>();
    private static final HashMap<User,Long> disconnectionTime = new HashMap<>();
    public int counter;
    public boolean isPlayingBack;
    public static ArrayList<Connection> connections() {
        pureConnections();
        return connections;
    }

    public static HashMap<User, Long> getDisconnectionTime() {
        return disconnectionTime;
    }

    public static void addToLostConnection(Connection connection){
        if(!lostConnections.contains(connection))
            lostConnections.add(connection);
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public AuthenticatedDataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    private static void pureConnections() {
        synchronized (connections) {
            for (int i = connections.size() - 1; i >= 0; i--) {
                if (!connections.get(i).isAlive() || !connections.get(i).isRunning) {
                    connections.get(i).closeConnection();
                    connections.remove(i);
                }
            }
        }
    }

    private void closeConnection() {
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection(Socket socket,
                      AuthenticatedDataInputStream dataInputStream,
                      AuthenticatedDataOutputStream dataOutputStream) throws IOException {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        synchronized (connections) {
            connections.add(this);
        }
    }

    @Override
    public void run() {
        while (true) {
            currentUser = new LoginMenuServer(dataOutputStream, dataInputStream).login();
            if (currentUser == null) {
                this.isRunning = false;
                try {
                    dataOutputStream.writeUTF(toSHA256("connection is dead"));
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                pureConnections();
                return;
            }
            enterMainMenu();
        }
    }
    public static String toSHA256(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(string.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hex = String.format("%064x", new BigInteger(1, digest));
        return hex;
    }


    public User currentUser() {
        return currentUser;
    }

    private void enterMainMenu() {
        try {
            dataOutputStream.writeUTF("entered main menu");
            if(disconnectionTime.containsKey(currentUser)){
                disconnectionTime.remove(currentUser);
                for(Lobby lobby:GamesMenu.getLobbies())
                    if(lobby.getUsers().contains(currentUser))
                        if(lobby.getGame()==null)
                            new GamesMenu(dataOutputStream,dataInputStream,currentUser).lobbyHandler(lobby);
                        else{
                            new GameMenuServer(dataOutputStream,dataInputStream,currentUser,lobby.getGameMenuController(),lobby.getGame()).gameHandler();
                        }
            }
            while (true) {
                if(dataInputStream.available()!=0) {
                    String input = dataInputStream.readUTF();
                    if(input.equals("enter games menu")){
                        new GamesMenu(dataOutputStream,dataInputStream,currentUser).gameMenuHandler();
                    }
                    else if(input.equals("enter friendship menu"))
                        new FriendshipMenu(dataOutputStream,dataInputStream,currentUser).friendShipMenuHandler();
                    else if(input.equals("enter lobby"))
                        new GamesMenu(dataOutputStream,dataInputStream,currentUser);
                    else if (input.equals("enter global chat"))
                        new GlobalChatMenu(dataOutputStream, dataInputStream, currentUser).globalChat();
                    else if (input.equals("enter private chat"))
                        new PrivateChatMenu(dataOutputStream, dataInputStream, currentUser).privateChat();
                    else if (input.equals("enter group chat"))
                        new GroupChatMenu(dataOutputStream, dataInputStream, currentUser).GroupChat();
                    else if (input.equals("show scoreboard")){
                        ScoreBoardMenu scoreBoardMenu = new ScoreBoardMenu(dataOutputStream, dataInputStream);
                        scoreBoardMenu.start();
                        scoreBoardMenu.join();
                    }
                    else if (input.equals("enter create map menu"))
                        new CreateMapMenuServer(dataInputStream, dataOutputStream).run(currentUser.getUsername());
                    else if (input.equals("enter share maps menu"))
                        new ShareMapsMenu(dataInputStream, dataOutputStream, currentUser).run();
                    else if (input.equals("enter live stream"))
                        enterLiveStream();
                    else if (input.equals("enter play back"))
                        enterPlayBack();
                    else if (input.matches("\\s*logout\\s*")) {
                        currentUser = null;
                        return;
                    }
                    else dataOutputStream.writeUTF("invalid input");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void enterPlayBack() throws IOException {
        while (true) {
            dataOutputStream.writeUTF("These are the available play backs! choose one to watch:");
            int counter = 1;
            for (GamePlayBack gamePlayBack : Game.getGamePlayBacks()) {
                dataOutputStream.writeUTF(counter + ". " + gamePlayBack.getId());
                counter++;
            }
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("exit"))
                    return;
                else if (input.equals("back"))
                    break;
                else if (!input.matches("\\d+") || Integer.parseInt(input) < 1 || Integer.parseInt(input) > Game.getGamePlayBacks().size())
                    dataOutputStream.writeUTF("enter a whole number between 1 and " + Game.getGamePlayBacks().size());
                else
                    playBack(Game.getGamePlayBacks().get(Integer.parseInt(input) - 1));
                    
            }
        }
    }

    private void playBack(GamePlayBack gamePlayBack) throws IOException {
        ArrayList<String> content = gamePlayBack.getContent();
        counter = 0;
        isPlayingBack = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (counter*2 >= content.size() || !isPlayingBack) {
                    this.cancel();
                    return;
                }
                try {
                    dataOutputStream.writeUTF(content.get(2*counter) + "\n" + content.get(2*counter + 1));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                counter = counter + 1;
            }
        }, 0, 2000);

        while (true) {
            if (dataInputStream.available() != 0) {
                if (dataInputStream.readUTF().equals("back")) {
                    isPlayingBack = false;
                    return;
                }
            }
        }
    }

    private void enterLiveStream() throws IOException {
        ArrayList<Lobby> publicLobbies = new ArrayList<>();
        for (Lobby lobby : GamesMenu.getLobbies())
            if (lobby.isPublic())
                publicLobbies.add(lobby);
        dataOutputStream.writeUTF("These are the streaming games (public lobbies)! choose one to watch:");
        for (int i = 0 ; i < publicLobbies.size() ; i++)
            dataOutputStream.writeUTF((i + 1) + ". " + publicLobbies.get(i).getId() + " number of players: " + publicLobbies.get(i).getUsers().size());
        while (true) {
            String input = dataInputStream.readUTF();
            if(input.equals("exit")) {
                for (Lobby lobby : publicLobbies)
                    if (lobby.getGame().getWatchingUsers().contains(this))
                        lobby.getGame().getWatchingUsers().remove(this);
                dataOutputStream.writeUTF("exited successfully!");
                return;
            }
            if (!input.matches("\\d+") || Integer.parseInt(input) < 1 || Integer.parseInt(input) > publicLobbies.size())
                dataOutputStream.writeUTF("enter a whole number between 1 and " + publicLobbies.size());
            else {
                Lobby lobby = publicLobbies.get(Integer.parseInt(input) - 1);
                lobby.getGame().getWatchingUsers().add(this);
                dataOutputStream.writeUTF("you joined the live stream of lobby " + lobby.getId());
            }
        }


    }

    public static Connection getConnectionByUsername(String username) {
        for (Connection connection : connections())
            if (connection.getCurrentUser().getUsername().equals(username))
                return connection;
        return null;
    }

    public void sendData(String data) throws IOException {
        dataOutputStream.writeUTF(data);
    }
}
