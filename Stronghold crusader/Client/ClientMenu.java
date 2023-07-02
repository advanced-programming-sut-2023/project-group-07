package Client;

import Server.view.AuthenticatedDataInputStream;
import Server.view.AuthenticatedDataOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ClientMenu {
    private static final int port = 8080;
    private static final String host = "localhost";

    private static AuthenticatedDataInputStream authenticatedDataInputStream;

    private static AuthenticatedDataOutputStream authenticatedDataOutputStream;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(host, port);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            String connectionId = dataInputStream.readUTF();
            setAuthenticatingStream(connectionId,
                    dataInputStream,
                    dataOutputStream);

            final boolean[] isRunning = {true};
            Thread inputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning[0]) {
                        try {
                            String input = authenticatedDataInputStream.readUTF();
                            if (input.equals(toSHA256("connection is dead"))) {
                                isRunning[0] = false;
                                break;
                            }
                            System.out.println(input);
                        } catch (IOException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            inputThread.start();
            Thread outputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String input = null;
                    while (isRunning[0]) {
                        try {
                            if (input != null) authenticatedDataOutputStream.writeUTF(input);
                            input = scanner.nextLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            outputThread.start();
        } catch (IOException e) {
        }
    }


    private static void setAuthenticatingStream(String id,
                                                DataInputStream dataInputStream,
                                                DataOutputStream dataOutputStream) throws IOException {
        authenticatedDataInputStream =
                new AuthenticatedDataInputStream(dataInputStream, id);
        authenticatedDataOutputStream =
                new AuthenticatedDataOutputStream(dataOutputStream, id);
        dataOutputStream.writeUTF(id);
    }

    public static String toSHA256(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(string.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hex = String.format("%064x", new BigInteger(1, digest));
        return hex;
    }
}