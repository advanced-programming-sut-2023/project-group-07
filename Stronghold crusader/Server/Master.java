package Server;

import model.Map;
import model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {
    private static ServerSocket serverSocket;
    private static final int port = 8080;

    public static void main(String[] args) {
        try {
            User.loadUsers();
            Map.loadMaps();
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
