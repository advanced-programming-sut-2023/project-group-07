package Server;

import Client.model.User;

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

    public static void main(String[] args) {
//        System.out.println(GlobalChat.messages());
//        GlobalChat.sendMessage(new ChatMessage(null, "sklgjasg"));
//        GlobalChat.sendMessage(new ChatMessage(null, "sjgsklgsklgdj"));
//        GlobalChat.sendMessage(new ChatMessage(null, "orifjkcaskjd"));
//        GlobalChat.messages().clear();
//        GlobalChat.saveMessages();
//        System.out.println(GlobalChat.messages());
    }
}
