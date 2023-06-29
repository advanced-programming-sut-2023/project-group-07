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
                else dataOutputStream.writeUTF("invalid input");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void handleClient() throws IOException {
        if(dataInputStream.available()!=0){
            String input = dataInputStream.readUTF();
            if(input.matches("\\s*acceptFriend (\\w+)\\s*")){
                dataOutputStream.writeUTF(sendFriendRequest(input));
            }
            else if(input.matches("\\s*friendRequest (\\w+)\\s*")){
                dataOutputStream.writeUTF(acceptFriendRequest(input));
            }
            else if(input.matches("\\s*friendReject (\\w+)\\s*")) {
                dataOutputStream.writeUTF(rejectFriendRequest(input));
            }
        }
    }

    public synchronized String sendFriendRequest(String friendRequest) throws IOException {
        String[] request = friendRequest.split(" ");
        if(request.length!=2 || !request[0].equals("friendRequest"))
            return "invalid command!";
        else{
            String targetUser = request[1];
            Connection connection = getConnectionByUsername(targetUser);
            if(connection==null){
                return "invalid target!";
            }
            if(currentUser.getFriends().contains(targetUser))
                return "this user is already your friend!";
            if(connection.currentUser.getPendingFriendRequests().contains(targetUser))
                return "you have already sent a request to this user!";
            connection.dataOutputStream.writeUTF("acceptFriend? "+currentUser.getUsername());
            connection.currentUser.addFriendRequest(currentUser.getUsername());
        }
        return "successful!";
    }

    public synchronized String acceptFriendRequest(String acceptRequest) throws IOException {
        String[] accept = acceptRequest.split(" ");
        if(accept.length!=2 || !accept[0].equals("friendAccept"))
            return "invalid command!";
        else{
            String targetUser = accept[1];
            Connection connection = getConnectionByUsername(targetUser);
            if(connection==null){
                return "invalid target!";
            }
            if(!currentUser.getPendingFriendRequests().contains(targetUser))
                return "no request from this user!";
            connection.dataOutputStream.writeUTF(currentUser.getUsername()+" accepted your friendship!");
            connection.currentUser.acceptFriend(currentUser.getUsername());
            currentUser.acceptFriend(targetUser);
        }
        return "successful!";
    }

    public synchronized String rejectFriendRequest(String rejectRequest) throws IOException {
        String[] reject = rejectRequest.split(" ");
        if(reject.length!=2 || !reject[0].equals("friendReject"))
            return "invalid command!";
        else{
            String targetUser = reject[1];
            Connection connection = getConnectionByUsername(targetUser);
            if(connection==null){
                return "invalid target!";
            }
            if(!currentUser.getPendingFriendRequests().contains(targetUser))
                return "no request from this user!";
            connection.dataOutputStream.writeUTF(currentUser.getUsername()+" rejected your friendship!");
            currentUser.getPendingFriendRequests().remove(targetUser);
        }
        return "successful!";
    }

    private Connection getConnectionByUsername(String username) {
        for(Connection connection: connections)
            if(connection.currentUser.getUsername().equals(username))
                return connection;
        return null;
    }}
