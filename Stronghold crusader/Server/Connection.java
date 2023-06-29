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
        currentUser = login();
        enterMainChatMenu();
    }

    private void enterMainChatMenu() {
        try {
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("enter global chat")) globalChat();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void globalChat() {
        try {
            dataOutputStream.writeUTF("you have entered global chat");
            Matcher matcher;
            while (true) {
                String input = dataInputStream.readUTF();
                if (GlobalChatCommands.getMatcher(input, GlobalChatCommands.SHOW_MESSAGES) != null)
                    dataOutputStream.writeUTF(showMessagesGlobalChat());
                else if ((matcher = GlobalChatCommands.getMatcher(input,GlobalChatCommands.SEND_MESSAGE)) != null)
                    dataOutputStream.writeUTF(sendMessageGlobal(matcher.group("message")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String sendMessageGlobal(String message) {
        GlobalChatController.send(message, currentUser);
        return "message has been sent";
    }

    private String showMessagesGlobalChat() {
        ArrayList<ChatMessage> messages = GlobalChatController.getMessages();
        StringBuilder str = new StringBuilder();
        for(ChatMessage message : messages){
            str.append(message).append("\n");
        }
        return str.toString();
    }

    private User login() {
        try {
            dataOutputStream.writeUTF("do you want to sign up or login?");
            String input = dataInputStream.readUTF();
            while (true) {
                if (input.matches(".*1.*") || input.matches(".*sign up.*"))
                    return signUp();
                else if ((input.matches(".*2.*") || input.matches(".*login.*")))
                    return loginWihExistingUser();
                dataOutputStream.writeUTF("sign up/login? 1/2?");
                input = dataInputStream.readUTF();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User loginWihExistingUser() {
        String regex = "login -u (?<username>\\S+) -p (?<password>\\S+)";
        try {
            dataOutputStream.writeUTF(regex);
            while (true) {
                String input = dataInputStream.readUTF();
                Matcher matcher = Pattern.compile(regex).matcher(input);
                if (!matcher.find()) {
                    dataOutputStream.writeUTF("invalid input");
                    continue;
                }
                User user = LoginController.login(matcher.group("username"), matcher.group("password"));
                if (user == null) dataOutputStream.writeUTF("invalid username or password");
                else return user;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User signUp() {
        // TODO: 6/29/2023 complete
        return null;
    }
}
