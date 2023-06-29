package Server;

import Client.model.User;

import java.util.ArrayList;
import java.util.HashSet;

public class GroupChatController {
     private static GroupChat groupChat;
    public static User getUserByUsername(String username){
        return User.getUserByUsername(username);
    }

    public static void setGroupChat(HashSet<User> usersSet) {
        groupChat = GroupChat.getChat(usersSet);
        if (groupChat == null) groupChat = new GroupChat(usersSet);
    }

    public static ArrayList<ChatMessage> getMessages(){
        return groupChat.messages();
    }
    public static void send(String messageContent, User currentUser){
        int id;
        if (getMessages().size() == 0) id = 1;
        else id = getMessages().get(getMessages().size()-1).id() +1;
        ChatMessage message = new ChatMessage(currentUser, messageContent, id);
        groupChat.sendMessage(message);
    }

    public static OutputMessage deleteMessage(int id, User currentUser) {
        ChatMessage message = getMessageById(id);
        if (message == null) return OutputMessage.INVALID_ID;
        if (groupChat.deleteMessage(message, currentUser)) return OutputMessage.SUCCESSFUL;
        return OutputMessage.NOT_YOUR_MESSAGE;
    }
    public static OutputMessage editMessage(int id, User currentUser, String newContent) {
        ChatMessage message = getMessageById(id);
        if (message == null) return OutputMessage.INVALID_ID;
        if (groupChat.editMessage(message, currentUser, newContent)) return OutputMessage.SUCCESSFUL;
        return OutputMessage.NOT_YOUR_MESSAGE;
    }

    public static ChatMessage getMessageById(int id){
        for (ChatMessage chatMessage : getMessages()){
            if (chatMessage.id() == id) return chatMessage;
        }
        return null;
    }
}
