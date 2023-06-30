package Server;

import controller.GameMenuController;
import model.Game;
import model.Map;
import model.User;

import java.util.ArrayList;
import java.util.HashSet;

public class Lobby {
    private Game game = null;
    private GameMenuController gameMenuController = null;
    private final ArrayList<User> users = new ArrayList<>();
    private boolean isPublic = true;
    private Map map;
    private User admin;

    public Lobby(boolean isPublic, Map map, User admin) {
        this.isPublic = isPublic;
        this.map = map;
        this.admin = admin;
    }

    private void addUser(User user){
        if(!users.contains(user))
            users.add(user);
    }

    private void removeUser(User user){
        users.remove(user);
    }


    private void updateLobby(){
        if(users.size()==0)
            GamesMenu.getLobbies().remove(this);
        if(!users.contains(admin))
            admin=users.get(0);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGameMenuController(GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
    }

    public GameMenuController getGameMenuController() {
        return gameMenuController;
    }
}
