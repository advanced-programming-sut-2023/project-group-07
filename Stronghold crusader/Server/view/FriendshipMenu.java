package Server.view;

import Server.view.Connection;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FriendshipMenu {
    private AuthenticatedDataOutputStream dataOutputStream;
    private AuthenticatedDataInputStream dataInputStream;
    private User currentUser;
    public FriendshipMenu(AuthenticatedDataOutputStream dataOutputStream,
                          AuthenticatedDataInputStream dataInputStream,
                          User currentUser){
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.currentUser = currentUser;
    }
    public void friendShipMenuHandler() throws IOException {
        dataOutputStream.writeUTF("you have entered friendship menu");
        while (true) {
            if(dataInputStream.available()!=0){
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
                } else if(input.equals("enter main menu"))
                    return;
                else
                    dataOutputStream.writeUTF("invalid input");
            }
        }
    }
    public synchronized String showFriendDetails(String command) {
        String[] input = command.split(" ");
        if(input.length!=2 || !input[0].equals("showDetails"))
            return "invalid command!";
        else {
            String targetUser = input[1];
            Connection connection = Connection.getConnectionByUsername(targetUser);
            if (connection == null) {
                return "invalid target!";
            }
            return "username: " + connection.getCurrentUser().getUsername()+"\nrank: "+connection.getCurrentUser().getRank()+
                    "\nnickname: "+connection.getCurrentUser().getNickname()+"\nslogan: "+connection.getCurrentUser().getSlogan();
        }
    }

    public synchronized String sendFriendRequest(String friendRequest) throws IOException {
        String[] request = friendRequest.split(" ");
        System.out.println(request.length+" "+request[0]+" "+request[1]);
        if(request.length!=2 || !request[0].equals("friendRequest"))
            return "invalid command!";
        else {
            String targetUser = request[1];
            Connection connection = Connection.getConnectionByUsername(targetUser);
            if(connection==null || targetUser.equals(currentUser.getUsername())){
                return "invalid target!";
            }
            if (currentUser.getFriends().contains(targetUser))
                return "this user is already your friend!";
            if(connection.getCurrentUser().getPendingFriendRequests().contains(currentUser.getUsername()))
                return "you have already sent a request to this user!";
            connection.getDataOutputStream().writeUTF("acceptFriend? " + currentUser.getUsername());
            connection.getCurrentUser().addFriendRequest(currentUser.getUsername());
        }
        return "successful!";
    }

    public synchronized String acceptFriendRequest(String acceptRequest) throws IOException {
        String[] accept = acceptRequest.split(" ");
        if(accept.length!=2 || !accept[0].equals("acceptFriend"))
            return "invalid command!";
        else {
            String targetUser = accept[1];
            Connection connection = Connection.getConnectionByUsername(targetUser);
            if(connection==null || targetUser.equals(currentUser.getUsername())){
                return "invalid target!";
            }
            if (!currentUser.getPendingFriendRequests().contains(targetUser))
                return "no request from this user!";
            connection.getDataOutputStream().writeUTF(currentUser.getUsername() + " accepted your friendship!");
            connection.getCurrentUser().acceptFriend(currentUser.getUsername());
            currentUser.acceptFriend(targetUser);
        }
        return "successful!";
    }

    public synchronized String rejectFriendRequest(String rejectRequest) throws IOException {
        String[] reject = rejectRequest.split(" ");
        if (reject.length != 2 || !reject[0].equals("friendReject"))
            return "invalid command!";
        else {
            String targetUser = reject[1];
            Connection connection = Connection.getConnectionByUsername(targetUser);
            if(connection==null || targetUser.equals(currentUser.getUsername())){
                return "invalid target!";
            }
            if (!currentUser.getPendingFriendRequests().contains(targetUser))
                return "no request from this user!";
            connection.getDataOutputStream().writeUTF(currentUser.getUsername() + " rejected your friendship!");
            currentUser.getPendingFriendRequests().remove(targetUser);
        }
        return "successful!";
    }


}
