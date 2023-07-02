package Server.view;

import Server.model.Lobby;
import Server.view.Connection;
import Server.view.GamesMenu;
import model.Map;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {
    private static ServerSocket serverSocket;
    private static final int port = 8080;

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for(int i = GamesMenu.getLobbies().size()-1; i>-1; i--){
                        Lobby lobby = GamesMenu.getLobbies().get(i);
                        if(lobby.getGame()==null && lobby.getUsers().size()==1){
                            if(lobby.getIdleTime()==0)
                                lobby.setIdleTime(System.currentTimeMillis());
                            else if(System.currentTimeMillis()-lobby.getIdleTime()>30*1000) {
                                GamesMenu.getLobbies().remove(lobby);
                            }
                        }
                        else lobby.setIdleTime(0);
                    }
                }
            }
        });
        thread.start();
        try {
            User.loadUsers();
            Map.loadMaps();
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                Connection connection = new Connection(socket, dataInputStream, dataOutputStream);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
