package Server;

import Client.controller.Client;
import Client.controller.Controller;
import Client.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection extends Thread {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private User currentUser;
    private static final ArrayList<Connection> connections = new ArrayList<>();
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (true){
            currentUser = new LoginMenuServer(dataOutputStream, dataInputStream).login();
            enterMainChatMenu();
        }
    }

    private void enterMainChatMenu() {
        try {
            dataOutputStream.writeUTF("entered main menu");
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("enter global chat"))
                    new GlobalChatMenu(dataOutputStream, dataInputStream, currentUser).globalChat();
                else if (input.equals("enter private chat"))
                    new GlobalChatMenu(dataOutputStream, dataInputStream, currentUser).globalChat();// TODO: 6/29/2023 pv
                else if (input.matches("\\s*logout\\s*")) return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String getFriendRequest(String username) throws IOException {
        String[] request = username.split(" ");
        if(request.length!=2 || !request[0].equals("friendRequest"))
            return "invalid command!";
        else{
            String targetUser = request[1];
            Connection connection = getConnectionByUsername(targetUser);
            if(connection==null){
                return "invalid target!";
            }
            connection.dataOutputStream.writeUTF("acceptFriend? "+currentUser.getUsername());
        }
        return "successful!";
    }

    private Connection getConnectionByUsername(String username) {
        for(Connection connection: connections)
            if(connection.currentUser.getUsername().equals(username))
                return connection;
        return null;
    }}
