package Server;

import Client.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Connection extends Thread {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private User currentUser = null;
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
        for (int i = connections.size() - 1; i >= 0; i--){
            if (!connections.get(i).isAlive()) connections.remove(i);
        }
    }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        connections.add(this);
    }

    @Override
    public void run() {
        while (true) {
            currentUser = new LoginMenuServer(dataOutputStream, dataInputStream).login();
            if (currentUser == null) return;
            enterMainMenu();
        }
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
                    else if (input.matches("\\s*logout\\s*")) return;
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
