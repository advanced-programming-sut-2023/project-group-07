package Server.model;

import com.google.gson.Gson;
import model.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListDAO<T> {
    private String url;
    private Connection cnn;
    private List<T> list;
    private String className;

    public ListDAO(String url, List<T> list, String className) {
        this.url = url;
        this.list = list;
        this.className = className;
        cnn = getConnection();
        createTable();
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Objects (\n"
                + " json text\n"
                + ");";
        try {
            Statement stmt = cnn.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert(String json) {
        String sql = "INSERT INTO Objects(json) VALUES(?)";

        try {
            PreparedStatement pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1, json);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void clearTable() {
        String sql = "DELETE FROM Objects";
        try {
            PreparedStatement pstmt = cnn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadToList() {
        list.clear();
        String sql = "SELECT * FROM Objects";

        try {
            Statement stmt = cnn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String objectJson = rs.getString("json");
                T tObject = (T) gson().fromJson(objectJson, getTClass());
                System.out.println(tObject);
                list.add(tObject);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveToListDataBase() {
        clearTable();
        for (T tObj : list) {
            insert(gson().toJson(tObj));
        }
    }

    private Gson gson;

    private Gson gson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    private Class getTClass(){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return Object.class;
        }
    }

    public void showTable() {
        String sql = "SELECT * FROM Objects";

        try {
            Statement stmt = cnn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("json"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

   }
