package Server.controller;

import Server.view.Connection;
import model.User;

import java.util.ArrayList;

public class ScoreBoardController {
    public static ArrayList<ArrayList<String>> getUsersInformation(){
        ArrayList<ArrayList<String>> usersInformation = new ArrayList<>();
        for (User user: User.getUsers()){ // getUsers is sorted
            ArrayList<String> aUserInformation = new ArrayList<>();
            aUserInformation.add(String.valueOf(user.getRank()));
            aUserInformation.add(user.getUsername());
            aUserInformation.add(String.valueOf(user.getHighScore()));
            if (isUserOnline(user)) aUserInformation.add("online");
            else aUserInformation.add(user.lastEntrance());
            usersInformation.add(aUserInformation);
        }
        return usersInformation;
    }
    public static boolean isUserOnline(User user){
        ArrayList<Connection> connections = Connection.connections();
        for (Connection connection : connections){
            if (connection.currentUser()!= null &&
                    connection.currentUser().getUsername().equals(user.getUsername()))
                return true;
        }
        return false;
    }
}

