package Server.model;

import model.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GroupChat {
    public static ArrayList<GroupChat> groupChats = new ArrayList<>();

    static {
        loadChats();
    }

    private static ListDAO<GroupChat> dao = null;
    private static ListDAO<GroupChat> dao(){
        if (dao == null) {
            String globalChatURL = "jdbc:sqlite:groupChat";
            dao = new ListDAO<GroupChat>(globalChatURL,
                    groupChats,
                    "Server.model.GroupChat"
            );
        }
        return dao;
    }

    private static void loadChats() {
        try {
            dao().loadToList();
        } catch (Exception e){

        }
    }

    public static void saveChats() {
        dao().saveToListDataBase();
    }

    public static GroupChat getChat(Set<User> users) {
        for (GroupChat groupChat : groupChats) {
            if (groupChat.userSet.size() != users.size()) continue;
            boolean flag = true;
            for (Iterator<User> it = users.iterator(); it.hasNext(); ) {
                User user1 = it.next();
                boolean groupContainsUser1 = false;
                for (Iterator<User> iter = groupChat.userSet.iterator(); iter.hasNext(); ) {
                    User user2 = iter.next();
                    if (user2.getUsername().equals(user1.getUsername())) {
                        groupContainsUser1 = true;
                        break;
                    }
                }
                if (!groupContainsUser1) {
                    flag = false;
                    break;
                }
            }
            if (flag) return groupChat;
        }
        return null;
    }

    private Set<User> userSet = new HashSet<>();
    private ArrayList<ChatMessage> messages = new ArrayList<>();

    public GroupChat(HashSet<User> usersSet) {
        this.userSet = usersSet;
        groupChats.add(this);
        saveChats();
    }

    public GroupChat(){}

    public boolean deleteMessage(ChatMessage message, User currentUser) {
        if (!message.owner().equals(currentUser) || !messages.contains(message)) return false;
        deleteMessage(message);
        return true;
    }

    private void deleteMessage(ChatMessage message) {
        messages.remove(message);
        saveChats();
    }

    public boolean editMessage(ChatMessage message, User currentUser, String newMessage) {
        if (!message.owner().equals(currentUser) || !messages.contains(message)) return false;
        editMessage(message, newMessage);
        return true;
    }

    private void editMessage(ChatMessage message, String newMessage) {
        message.setContent(newMessage);
        saveChats();
    }

    public void sendMessage(ChatMessage message) {
        messages.add(message);
        saveChats();
    }


    public ArrayList<ChatMessage> messages() {
        return messages;
    }
}