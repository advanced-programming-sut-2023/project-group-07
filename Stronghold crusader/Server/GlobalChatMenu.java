package Server;

import Client.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class GlobalChatMenu {
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private User currentUser;
    private GlobalChatController globalChatController;


    public GlobalChatMenu(DataOutputStream dataOutputStream, DataInputStream dataInputStream, User currentUser) {
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.currentUser = currentUser;
        globalChatController = new GlobalChatController();
    }

    public void globalChat() {
        try {
            dataOutputStream.writeUTF("you have entered global chat");
            Matcher matcher;
            while (true) {
                String input = dataInputStream.readUTF();
                if (GlobalChatCommands.getMatcher(input, GlobalChatCommands.SHOW_MESSAGES) != null)
                    dataOutputStream.writeUTF(showMessagesGlobalChat());
                else if ((matcher = GlobalChatCommands.getMatcher(input,GlobalChatCommands.SEND_MESSAGE)) != null)
                    dataOutputStream.writeUTF(sendMessageGlobal(matcher.group("message")));
                else if ((matcher = GlobalChatCommands.getMatcher(input,GlobalChatCommands.DELETE_MESSAGE)) != null)
                    dataOutputStream.writeUTF(deleteGlobalMessage(matcher.group("id")));
                else if ((matcher = GlobalChatCommands.getMatcher(input,GlobalChatCommands.EDIT_MESSAGE)) != null)
                    dataOutputStream.writeUTF(
                            editGlobalMessage(matcher.group("id"), matcher.group("newContent")));
                else if (input.matches("\\s*exit\\s*")) {
                    dataOutputStream.writeUTF("enter main menu");
                    return;
                }else
                    dataOutputStream.writeUTF("invalid input");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String deleteGlobalMessage(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            switch(globalChatController.deleteMessage(id, currentUser)){
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
    private String editGlobalMessage(String idStr, String newContent) {
        try {
            int id = Integer.parseInt(idStr);
            switch(globalChatController.editMessage(id, currentUser, newContent)){
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

    private String sendMessageGlobal(String message) {
        globalChatController.send(message, currentUser);
        return "message has been sent";
    }

    private String showMessagesGlobalChat() {
        ArrayList<ChatMessage> messages = globalChatController.showMessages(currentUser);
        StringBuilder str = new StringBuilder();
        for(ChatMessage message : messages){
            str.append(message).append("\n");
        }
        return str.toString();
    }
}
