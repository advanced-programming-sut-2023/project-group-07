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
import java.util.Random;

public class Master {
    private static ServerSocket serverSocket;
    private static final int port = 8080;

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = GamesMenu.getLobbies().size() - 1; i > -1; i--) {
                        Lobby lobby = GamesMenu.getLobbies().get(i);
                        if (lobby.getGame() == null && lobby.getUsers().size() == 1) {
                            if (lobby.getIdleTime() == 0)
                                lobby.setIdleTime(System.currentTimeMillis());
                            else if (System.currentTimeMillis() - lobby.getIdleTime() > 30 * 1000) {
                                GamesMenu.getLobbies().remove(lobby);
                            }
                        } else lobby.setIdleTime(0);
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
                setAuthenticatingStream(dataInputStream, dataOutputStream);

                Connection connection = new Connection(socket, dataInputStream, dataOutputStream);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setAuthenticatingStream(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        String id = generateConnectionId();
        AuthenticatedDataInputStream authenticatedDataInputStream =
                new AuthenticatedDataInputStream(dataInputStream, id);
        AuthenticatedDataOutputStream authenticatedDataOutputStream =
                new AuthenticatedDataOutputStream(dataOutputStream, id);
        dataOutputStream.writeUTF(id);
    }



    static Random random = null;
    public static Random random() {
        if (random == null)
            random = new Random();
        return random;
    }

    private static String generateConnectionId() {
        int length = random().nextInt(10) + 7 ;
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < length; i++)
            id.append(random().nextInt(10));
        return id.toString();
    }
}
