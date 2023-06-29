package Client.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientMenu {
    private static final int port = 8080;
    private static final String host = "localhost";

    public static void main(String[] args){
        try {
            Socket socket = new Socket(host, port);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println(dataInputStream.readUTF());
                dataOutputStream.writeUTF(scanner.nextLine());
            }
        } catch (IOException e){
        }
    }
}
