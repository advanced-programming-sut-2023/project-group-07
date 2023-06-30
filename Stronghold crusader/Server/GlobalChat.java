package Server;

import Client.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class GlobalChat {
    private static ArrayList<ChatMessage> messages = new ArrayList<>();

    static {
        loadMessages();
    }

    private static void loadMessages() {
        FileReader file = null;
        try {
            String address = "Stronghold crusader/DB/global_chat_messages";
            file = new FileReader(address);

            Scanner scanner = new Scanner(file);
            if (!scanner.hasNextLine()) {
                scanner.close();
                return;
            }
            String allMessages = scanner.nextLine();
            scanner.close();
            file.close();
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(allMessages, JsonArray.class);
            messages.clear();
            for (JsonElement jsonElement : jsonArray) {
                messages.add(gson.fromJson(jsonElement, ChatMessage.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(ChatMessage message) {
        messages.add(message);
        saveMessages();
    }

    public static void saveMessages() {
        try {
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();
            for (ChatMessage message : messages) {
                jsonArray.add(gson.toJsonTree(message).getAsJsonObject());
            }
            FileWriter file = new FileWriter(GlobalChat.class.
                    getResource("/DB/global_chat_messages").toExternalForm().substring("file:\\".length()));
            file.write(jsonArray.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
