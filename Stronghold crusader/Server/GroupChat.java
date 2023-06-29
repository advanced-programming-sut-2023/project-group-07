package Server;

import Client.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GroupChat {
    public static ArrayList<GroupChat> groupChats = new ArrayList<>();

    static {
        loadChats();
    }


    private static void loadChats() {
        FileReader file = null;
        try {
            URL url = GlobalChat.class.
                    getResource("/DB/group_chats");
            file = new FileReader(url.toExternalForm().substring("file:\\".length()));

            Scanner scanner = new Scanner(file);
            if (!scanner.hasNextLine()) {
                scanner.close();
                return;
            }
            String allChats = scanner.nextLine();
            scanner.close();
            file.close();
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(allChats, JsonArray.class);
            groupChats.clear();
            for (JsonElement jsonElement : jsonArray) {
                groupChats.add(gson.fromJson(jsonElement, GroupChat.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveChats() {
        try {
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();
            for (GroupChat chat : groupChats) {
                jsonArray.add(gson.toJsonTree(chat).getAsJsonObject());
            }
            FileWriter file = new FileWriter(GlobalChat.class.
                    getResource("/DB/group_chats").toExternalForm().substring("file:\\".length()));
            file.write(jsonArray.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GroupChat getChat(Set<User> users) {
        for (GroupChat groupChat : groupChats) {
            boolean flag = true;
            for (Iterator<User> it = users.iterator(); it.hasNext(); ) {
                User user = it.next();
                if (!groupChat.userSet.contains(user)) {
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

    public boolean deleteMessage(ChatMessage message, User currentUser) {
        if (!message.owner().equals(currentUser) || !messages.contains(message) ||
                !userSet.contains(currentUser)) return false; // todo change condition
        deleteMessage(message);
        return true;
    }

    private void deleteMessage(ChatMessage message) {
        messages.remove(message);
        saveChats();
    }

    public boolean editMessage(ChatMessage message, User currentUser, String newMessage) {
        if (!message.owner().equals(currentUser) || !messages.contains(message) ||
                !userSet.contains(currentUser)) return false; // TODO: 6/29/2023 change condition
        editMessage(message, newMessage);
        return true;
    }

    private void editMessage(ChatMessage message, String newMessage) {
        message.setContent(newMessage);
        saveChats();
    }

    public boolean sendMessage(ChatMessage message) {
        if (!userSet.contains(message.owner())) return false;
        messages.add(message);
        saveChats();
        return true;
    }


    public ArrayList<ChatMessage> messages() {
        return messages;
    }
}
