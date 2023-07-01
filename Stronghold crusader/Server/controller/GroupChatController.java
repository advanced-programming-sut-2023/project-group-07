package Server.controller;

import Server.model.ChatMessage;
import Server.model.GroupChat;
import model.User;

import java.util.ArrayList;
import java.util.HashSet;

public class GroupChatController {
    private GroupChat groupChat;

    public User getUserByUsername(String username) {
        return User.getUserByUsername(username);
    }

    public void setGroupChat(HashSet<User> usersSet) {
        groupChat = GroupChat.getChat(usersSet);
        if (groupChat == null) groupChat = new GroupChat(usersSet);
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

    private ArrayList<ChatMessage> getMessages() {
        return groupChat.messages();
    }

    public void send(String messageContent, User currentUser) {
        int id;
        if (getMessages().size() == 0) id = 1;
        else id = getMessages().get(getMessages().size() - 1).id() + 1;
        ChatMessage message = new ChatMessage(currentUser, messageContent, id);
        groupChat.sendMessage(message);
    }

    public OutputMessage deleteMessage(int id, User currentUser) {
        ChatMessage message = getMessageById(id);
        if (message == null) return OutputMessage.INVALID_ID;
        if (groupChat.deleteMessage(message, currentUser)) return OutputMessage.SUCCESSFUL;
        return OutputMessage.NOT_YOUR_MESSAGE;
    }

    public OutputMessage editMessage(int id, User currentUser, String newContent) {
        ChatMessage message = getMessageById(id);
        if (message == null) return OutputMessage.INVALID_ID;
        if (groupChat.editMessage(message, currentUser, newContent)) return OutputMessage.SUCCESSFUL;
        return OutputMessage.NOT_YOUR_MESSAGE;
    }

    public ChatMessage getMessageById(int id) {
        for (ChatMessage chatMessage : getMessages()) {
            if (chatMessage.id() == id) return chatMessage;
        }
        return null;
    }

    public ArrayList<ChatMessage> showMessages(User currentUser) {
        for (ChatMessage message : getMessages()) {
            if (!message.owner().equals(currentUser)) message.setSeen();
        }
        return getMessages();
    }
}
