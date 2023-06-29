package Server;

import Client.model.User;

import java.io.IOException;
import java.util.Calendar;

public class ChatMessage {
    private User owner;
    private String content;
    private String sentTime;

    public ChatMessage(User owner, String message) {
        this.owner = owner;
        this.content = message;
        setSentTime();
    }

    private void setSentTime() {
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        String mmTime, hhTime;
        if (minutes < 10) mmTime = "0" + minutes;
        else mmTime = Integer.toString(minutes);
        if (hours < 10) hhTime = "0" + hours;
        else hhTime = Integer.toString(hours);
        sentTime = hhTime + ":" + mmTime;
    }

    public User owner() {
        return owner;
    }

    public String content() {
        return content;
    }

    public String sentTime() {
        return sentTime;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return owner.getUsername() + " " + sentTime + " : " + content;
    }

    public static void main(String[] args) {
        try {
            User.loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new ChatMessage(User.getUserByUsername("Alireza"),"salam be haem"));
    }
}
