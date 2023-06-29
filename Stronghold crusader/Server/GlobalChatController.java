package Server;

import Client.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GlobalChatController {
    public static ArrayList<ChatMessage> getMessages(){
        return GlobalChat.messages();
    }
    public static void send(String messageContent, User currentUser){
        int id;
        if (getMessages().size() == 0) id = 1;
        else id = getMessages().get(getMessages().size()-1).id() +1;
        ChatMessage message = new ChatMessage(currentUser, messageContent, id);
        GlobalChat.sendMessage(message);
    }

    public static OutputMessage deleteMessage(int id, User currentUser) {
        ChatMessage message = getMessageById(id);
        if (message == null) return OutputMessage.INVALID_ID;
        if (GlobalChat.deleteMessage(message, currentUser)) return OutputMessage.SUCCESSFUL;
        return OutputMessage.NOT_YOUR_MESSAGE;
    }
    public static OutputMessage editMessage(int id, User currentUser, String newContent) {
        ChatMessage message = getMessageById(id);
        if (message == null) return OutputMessage.INVALID_ID;
        if (GlobalChat.editMessage(message, currentUser, newContent)) return OutputMessage.SUCCESSFUL;
        return OutputMessage.NOT_YOUR_MESSAGE;
    }

    public static ChatMessage getMessageById(int id){
        for (ChatMessage chatMessage : getMessages()){
            if (chatMessage.id() == id) return chatMessage;
        }
        return null;
    }
}
