package Server;

import Client.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GlobalChatController {
    public static ArrayList<ChatMessage> getMessages(){
        return GlobalChat.messages();
    }
    public static void send(String messageContent, User currentUser){
        ChatMessage message = new ChatMessage(currentUser, messageContent);
        GlobalChat.sendMessage(message);
    }
}
