package Server;

import controller.GameMenuController;
import model.Game;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Connection extends Thread {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private User currentUser = null;
    private boolean isRunning = true;
    private static final ArrayList<Connection> connections = new ArrayList<>();
    public static ArrayList<Connection> connections() {
        pureConnections();
        return connections;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
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

    public Connection(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
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
            while (true) {
                if(dataInputStream.available()!=0) {
                    String input = dataInputStream.readUTF();
                    if(input.equals("enter games menu")){
                        new GamesMenu(dataOutputStream,dataInputStream,currentUser).gameMenuHandler();
                    }
                    if(input.equals("enter friendship menu"))
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



}
