package controller;

import model.User;

import java.util.ArrayList;

public class ScoreBoardController {
    public ArrayList<User> sortedUsers(){
        return User.getUsers();
    }
}
