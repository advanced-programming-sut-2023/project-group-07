package model;

import java.util.ArrayList;

public class GamePlayBack {
    String id;
    ArrayList<String> content = new ArrayList<>();
    public GamePlayBack (String id) {
        this.id = id;
    }

    public void addData(String data) {
        content.add(data);
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getContent() {
        return content;
    }
}
