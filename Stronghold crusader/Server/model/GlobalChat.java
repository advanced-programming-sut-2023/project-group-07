package Server.model;

import model.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GlobalChat {
    private static ArrayList<ChatMessage> messages = new ArrayList<>();

    static {
        loadMessages();
    }

    private static ListDAO<ChatMessage> dao = null;

    private static ListDAO<ChatMessage> dao() {
        if (dao == null) {
            String globalChatURL = "jdbc:sqlite:globalChatMessages.sqlite";
            dao = new ListDAO<ChatMessage>(globalChatURL,
                    messages,
                    "Server.model.ChatMessage"
            );
        }
        return dao;
    }

    private static void loadMessages() {
        try {
            dao().loadToList();
        } catch (Exception e){

        }
    }

    public static void saveMessages() {
        dao().saveToListDataBase();
    }

    public static void sendMessage(ChatMessage message) {
        messages.add(message);
        saveMessages();
    }

    public static ArrayList<ChatMessage> messages() {
        return messages;
    }

    public static boolean deleteMessage(ChatMessage message, User currentUser) {
        if (!message.owner().getUsername().equals(currentUser.getUsername())) return false;
        deleteMessage(message);
        return true;
    }

    private static void deleteMessage(ChatMessage message) {
        messages.remove(message);
        saveMessages();
    }

    public static boolean editMessage(ChatMessage message, User currentUser, String newMessage) {
        System.out.println(currentUser);
        System.out.println(message.owner());
        if (!message.owner().getUsername().equals(currentUser.getUsername())) return false;
        editMessage(message, newMessage);
        return true;
    }

    private static void editMessage(ChatMessage message, String newMessage) {
        message.setContent(newMessage);
        saveMessages();
    }

    public static void react(User currentUser, MessageReaction reaction, ChatMessage message) {
        message.setReaction(currentUser, reaction);
        saveMessages();
    }
}
