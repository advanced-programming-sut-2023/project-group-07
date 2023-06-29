package Server;

import Client.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;

public class PrivateChatMenu {
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    User currentUser;
    public PrivateChatMenu(DataOutputStream dataOutputStream, DataInputStream dataInputStream, User currentUser) {
        this.currentUser = currentUser;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }
    public void privateChat(){
        try {
            preSetPrivateChat();
            Matcher matcher;
            while (true) {
                String input = dataInputStream.readUTF();
                if (GlobalChatCommands.getMatcher(input, GlobalChatCommands.SHOW_MESSAGES) != null)
                    dataOutputStream.writeUTF(showMessagesPrivateChat());
                else if ((matcher = GlobalChatCommands.getMatcher(input,GlobalChatCommands.SEND_MESSAGE)) != null)
                    dataOutputStream.writeUTF(sendMessagePrivate(matcher.group("message")));
                else if ((matcher = GlobalChatCommands.getMatcher(input,GlobalChatCommands.DELETE_MESSAGE)) != null)
                    dataOutputStream.writeUTF(deleteMessage(matcher.group("id")));
                else if ((matcher = GlobalChatCommands.getMatcher(input,GlobalChatCommands.EDIT_MESSAGE)) != null)
                    dataOutputStream.writeUTF(
                            editPrivateMessage(matcher.group("id"), matcher.group("newContent")));
                else if (input.matches("\\s*exit\\s*")) {
                    dataOutputStream.writeUTF("enter main menu");
                    return;
                }else
                    dataOutputStream.writeUTF("invalid input");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private String deleteMessage(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            switch(PrivateChatController.deleteMessage(id, currentUser)){
                case INVALID_ID :
                    return "id is invalid";
                case NOT_YOUR_MESSAGE:
                    return "this message is not yours";
                case SUCCESSFUL:
                    return "successful";
            }
        }catch (NumberFormatException e){
            return e.getMessage();
        }
        return "";
    }
    private String editPrivateMessage(String idStr, String newContent) {
        try {
            int id = Integer.parseInt(idStr);
            switch(PrivateChatController.editMessage(id, currentUser, newContent)){
                case INVALID_ID :
                    return "id is invalid";
                case NOT_YOUR_MESSAGE:
                    return "this message is not yours";
                case SUCCESSFUL:
                    return "successful";
            }
        }catch (NumberFormatException e){
            return e.getMessage();
        }
        return "";
    }

    private String sendMessagePrivate(String message) {
        PrivateChatController.send(message, currentUser);
        return "message has been sent";
    }

    private String showMessagesPrivateChat() {
        ArrayList<ChatMessage> messages = PrivateChatController.getMessages();
        StringBuilder str = new StringBuilder();
        for(ChatMessage message : messages){
            str.append(message).append("\n");
        }
        return str.toString();
    }

    private void preSetPrivateChat() throws IOException {
        User targetUser = null;
        while (true){
            dataOutputStream.writeUTF("enter target user's username");
            dataOutputStream.flush();
            targetUser = PrivateChatController.getUserByUsername(dataInputStream.readUTF());
            if (targetUser!=null) break;
            else dataOutputStream.writeUTF("enter a valid username");

        }
        dataOutputStream.writeUTF("user : " + targetUser.getUsername() +" is selected");
        dataOutputStream.flush();
        HashSet<User> usersSet = new HashSet<>();
        usersSet.add(currentUser);
        usersSet.add(targetUser);
        PrivateChatController.setPrivateChat(usersSet);
    }
}
