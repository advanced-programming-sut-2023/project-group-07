package Server;

import Client.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection extends Thread {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private User currentUser;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        currentUser = new LoginMenuServer(dataOutputStream, dataInputStream).login();
        enterMainChatMenu();
    }

    private void enterMainChatMenu() {
        try {
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("enter global chat"))
                    new GlobalChatMenu(dataOutputStream, dataInputStream, currentUser).globalChat();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





//    private void
}
