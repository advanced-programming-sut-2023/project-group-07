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
        connections.add(this);
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
                if(dataInputStream.available()!=0) {
                    System.out.println("KIr");
                    String input = dataInputStream.readUTF();
                    if (input.matches("\\s*friendRequest (\\w+)\\s*")) {
                        dataOutputStream.writeUTF(sendFriendRequest(input));
                        User.updateUsers();
                    } else if (input.matches("\\s*acceptFriend (\\w+)\\s*")) {
                        dataOutputStream.writeUTF(acceptFriendRequest(input));
                        User.updateUsers();
                    } else if (input.matches("\\s*friendReject (\\w+)\\s*")) {
                        dataOutputStream.writeUTF(rejectFriendRequest(input));
                        User.updateUsers();
                    } else if (input.matches("\\s*showDetails (\\w+)\\s*")) {
                        dataOutputStream.writeUTF(showFriendDetails(input));
                        User.updateUsers();
                    }
                    else if (input.equals("enter global chat"))
                        new GlobalChatMenu(dataOutputStream, dataInputStream, currentUser).globalChat();
                    else if (input.equals("enter private chat"))
                        new PrivateChatMenu(dataOutputStream, dataInputStream, currentUser).privateChat();
                    else if (input.equals("enter group chat"))
                        new GroupChatMenu(dataOutputStream, dataInputStream, currentUser).GroupChat();
                    else if (input.matches("\\s*logout\\s*")) return;
                    else dataOutputStream.writeUTF("invalid input");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String showFriendDetails(String command) {
        String[] input = command.split(" ");
        if(input.length!=2 || !input[0].equals("showDetails"))
            return "invalid command!";
        else {
            String targetUser = input[1];
            Connection connection = getConnectionByUsername(targetUser);
            if (connection == null) {
                return "invalid target!";
            }
            return "username: " + connection.currentUser.getUsername()+"\nrank: "+connection.currentUser.getRank()+
                    "\nnickname: "+connection.currentUser.getNickname()+"\nslogan: "+connection.currentUser.getSlogan();
        }
    }

    public synchronized String sendFriendRequest(String friendRequest) throws IOException {
        String[] request = friendRequest.split(" ");
        System.out.println(request.length+" "+request[0]+" "+request[1]);
        if(request.length!=2 || !request[0].equals("friendRequest"))
            return "invalid command!";
        else{
            String targetUser = request[1];
            Connection connection = getConnectionByUsername(targetUser);
            if(connection==null || targetUser.equals(currentUser.getUsername())){
                return "invalid target!";
            }
            if(currentUser.getFriends().contains(targetUser))
                return "this user is already your friend!";
            if(connection.currentUser.getPendingFriendRequests().contains(currentUser.getUsername()))
                return "you have already sent a request to this user!";
            connection.dataOutputStream.writeUTF("acceptFriend? "+currentUser.getUsername());
            connection.currentUser.addFriendRequest(currentUser.getUsername());
        }
        return "successful!";
    }

    public synchronized String acceptFriendRequest(String acceptRequest) throws IOException {
        String[] accept = acceptRequest.split(" ");
        if(accept.length!=2 || !accept[0].equals("acceptFriend"))
            return "invalid command!";
        else{
            String targetUser = accept[1];
            Connection connection = getConnectionByUsername(targetUser);
            if(connection==null || targetUser.equals(currentUser.getUsername())){
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
            if(connection==null || targetUser.equals(currentUser.getUsername())){
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
