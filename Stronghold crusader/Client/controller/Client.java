package Client.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;

    public Client(String host,int port) throws IOException {
        Socket socket = new Socket(host,port);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }
}
