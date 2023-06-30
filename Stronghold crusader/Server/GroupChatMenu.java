package Server;

import Client.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;

public class GroupChatMenu {
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private User currentUser;
    private GroupChatController groupChatController;

    public GroupChatMenu(DataOutputStream dataOutputStream, DataInputStream dataInputStream, User currentUser) {
        this.currentUser = currentUser;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        groupChatController = new GroupChatController();
    }

    public void GroupChat() {
        try {
            preSetGroupChat();
            Matcher matcher;
            while (true) {
                String input = dataInputStream.readUTF();
                if (GlobalChatCommands.getMatcher(input, GlobalChatCommands.SHOW_MESSAGES) != null){
                    dataOutputStream.writeUTF(showMessagesPrivateChat());
                    dataOutputStream.flush();
                }
                else if ((matcher = GlobalChatCommands.getMatcher(input, GlobalChatCommands.SEND_MESSAGE)) != null){
                    dataOutputStream.writeUTF(sendMessagePrivate(matcher.group("message")));
                    dataOutputStream.flush();
                }
                else if ((matcher = GlobalChatCommands.getMatcher(input, GlobalChatCommands.DELETE_MESSAGE)) != null) {
                    dataOutputStream.writeUTF(deleteMessage(matcher.group("id")));
                    dataOutputStream.flush();
                }
                else if ((matcher = GlobalChatCommands.getMatcher(input, GlobalChatCommands.EDIT_MESSAGE)) != null){
                    dataOutputStream.writeUTF(
                            editPrivateMessage(matcher.group("id"), matcher.group("newContent")));
                    dataOutputStream.flush();
                }
                else if (input.matches("\\s*exit\\s*")) {
                    dataOutputStream.writeUTF("enter main menu");
                    dataOutputStream.flush();
                    return;
                } else{
                    dataOutputStream.writeUTF("invalid input");
                    dataOutputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String deleteMessage(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            switch (groupChatController.deleteMessage(id, currentUser)) {
                case INVALID_ID:
                    return "id is invalid";
                case NOT_YOUR_MESSAGE:
                    return "this message is not yours";
                case SUCCESSFUL:
                    return "successful";
            }
        } catch (NumberFormatException e) {
            return e.getMessage();
        }
        return "";
    }

    private String editPrivateMessage(String idStr, String newContent) {
        try {
            int id = Integer.parseInt(idStr);
            switch (groupChatController.editMessage(id, currentUser, newContent)) {
                case INVALID_ID:
                    return "id is invalid";
                case NOT_YOUR_MESSAGE:
                    return "this message is not yours";
                case SUCCESSFUL:
                    return "successful";
            }
        } catch (NumberFormatException e) {
            return e.getMessage();
        }
        return "";
    }

    private String sendMessagePrivate(String message) {
        groupChatController.send(message, currentUser);
        return "message has been sent";
    }

    private String showMessagesPrivateChat() {
        ArrayList<ChatMessage> messages = groupChatController.showMessages(currentUser);
        StringBuilder str = new StringBuilder();
        for (ChatMessage message : messages) {
            str.append(message).append("\n");
        }
        return str.toString();
    }

    private void preSetGroupChat() throws IOException {
        dataOutputStream.writeUTF("write users you want to have chat with. Type \"done\" at the end ");
        dataOutputStream.flush();
        HashSet<User> users = new HashSet<>();
        users.add(currentUser);
        while (true) {
            System.out.println(14);
            dataOutputStream.writeUTF("enter next user");
            dataOutputStream.flush();
            String input = dataInputStream.readUTF();
            System.out.println(System.currentTimeMillis());
            if (input.matches("\\s*done\\s*")) break;
            User user = groupChatController.getUserByUsername(input);
            if (user == null) {
                dataOutputStream.writeUTF("enter a valid username");
            }
            else
                users.add(user);
        }
        System.out.println(users);
        groupChatController.setGroupChat(users);
    }
}
