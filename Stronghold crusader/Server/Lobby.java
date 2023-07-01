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
    private final int id;
    private int numberOfPlayers;
    private int earlyGameGolds;
    private long idleTime=0;
    private GroupChat groupChat = null;


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

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Map getMap() {
        return map;
    }

    public int getEarlyGameGolds() {
        return earlyGameGolds;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getId() {
        return id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIdleTime(long idleTime) {
        this.idleTime = idleTime;
    }

    public long getIdleTime() {
        return idleTime;
    }

    public GroupChat groupChat() {
        if (groupChat == null) groupChat = new GroupChat();
        return groupChat;
    }
}
