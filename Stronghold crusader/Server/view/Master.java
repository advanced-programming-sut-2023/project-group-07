package Server.view;

import Server.model.Lobby;
import Server.view.Connection;
import Server.view.GamesMenu;
import model.Government;
import model.Map;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Master {
    private static ServerSocket serverSocket;
    private static final int port = 8080;

    private static AuthenticatedDataInputStream authenticatedDataInputStream;

    private static AuthenticatedDataOutputStream authenticatedDataOutputStream;

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ArrayList<Connection> toBeRemoved = new ArrayList<>();
                        for(Connection connection:Connection.connections()){
                            ArrayList<User> disconnectedUsers = new ArrayList<>();
                            for(User user:Connection.getDisconnectionTime().keySet()){
                                if(System.currentTimeMillis()-Connection.getDisconnectionTime().get(user)>10*1000)
                                    for(Lobby lobby:GamesMenu.getLobbies()){
                                        if(lobby.getUsers().contains(user)){
                                            if(lobby.getGame()==null){
                                                lobby.removeUser(user);
                                                disconnectedUsers.add(user);
                                            }
                                            else {
                                                lobby.removeUser(user);
                                                boolean flag = false;
                                                for(Government government:lobby.getGame().getGovernments())
                                                    if(government.getUser().equals(user)){
                                                        government.getPeople().get(0).changeHP(-10000);
                                                        flag =true;
                                                    }
                                                if(flag)
                                                    for(int i=0;i<lobby.getGame().getGovernments().size();i++) {
                                                        try {
                                                            lobby.getGame().nextTurn();
                                                        } catch (IOException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }

                                            }
                                        }
                                    }

                            }
                            for(User user:disconnectedUsers)
                                Connection.getDisconnectionTime().remove(user);
                            try {
                                connection.getDataOutputStream().writeUTF("~");
                            } catch (IOException e) {
                                if(!Connection.getDisconnectionTime().containsKey(connection.currentUser())){
                                    Connection.addToLostConnection(connection);
                                    toBeRemoved.add(connection);
                                    Connection.getDisconnectionTime().put(connection.currentUser(),System.currentTimeMillis());
                                }
                            }
                        }
                        Connection.connections().removeAll(toBeRemoved);
                    }
                },5000,5000);
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
//        Thread onlineCheck = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    for(Connection connection:Connection.connections()){
//                        try {
//                            connection.getDataOutputStream().writeUTF("~");
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//        });
//        onlineCheck.start();
        try {
            User.loadUsers();
            Map.loadMaps();
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                setAuthenticatingStream(dataInputStream, dataOutputStream);

                Connection connection = new Connection(socket,
                        authenticatedDataInputStream, authenticatedDataOutputStream);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setAuthenticatingStream(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        String id = generateConnectionId();
        authenticatedDataInputStream =
                new AuthenticatedDataInputStream(dataInputStream, id);
        authenticatedDataOutputStream =
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
        int length = random().nextInt(10) + 7;
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < length; i++)
            id.append(random().nextInt(10));
        return id.toString();
    }
}
