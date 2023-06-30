package Server;

import Client.model.Game;
import Client.model.Map;
import Client.model.User;

import java.util.ArrayList;
import java.util.HashSet;

public class Lobby {
    private Game game;
    private final ArrayList<User> users = new ArrayList<>();
    private boolean isPublic = true;
    private Map map;
    private User admin;
    private int id;

    public Lobby(boolean isPublic, Map map, User admin) {
        this.isPublic = isPublic;
        this.map = map;
        this.admin = admin;
        if(GamesMenu.getLobbies().size()==0)
            this.id=0;
        else
            for(int i=GamesMenu.getLobbies().size();i<2*GamesMenu.getLobbies().size();i++){
                boolean flag=true;
                for(Lobby lobby:GamesMenu.getLobbies())
                    if(lobby.getId()==i)
                        flag=false;
                if(flag)
                    this.id=i;
                break;
            }
    }

    private void addUser(User user){
        if(!users.contains(user))
            users.add(user);
    }

    private void removeUser(User user){
        users.remove(user);
    }

    public int getId() {
        return id;
    }

    private void updateLobby(){
        if(users.size()==0)
            GamesMenu.getLobbies().remove(this);
        if(!users.contains(admin))
            admin=users.get(0);
    }
}
