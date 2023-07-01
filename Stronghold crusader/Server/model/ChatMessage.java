package Server.model;

import model.User;

import java.util.Calendar;

public class ChatMessage {
    private User owner;
    private String content;
    private String sentTime;
    private int id;
    private SeenStatus status;

    public ChatMessage(User owner, String message, int id) {
        this.owner = owner;
        this.content = message;
        this.id = id;
        this.status = SeenStatus.SENT;
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

    public int id() {
        return id;
    }
    public void setSeen(){
        status = SeenStatus.SEEN;
    }
    private SeenStatus status(){
        if (status == null) status = SeenStatus.SENT;
        return status;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return "id)"+ id + " "  + owner.getUsername() + " " + sentTime + " : " + content + " " + status();
    }

}