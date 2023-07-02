package Client.model;

import com.google.gson.*;
import Client.controller.Controller;
import Client.controller.Messages;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

public class User {
    static {
        try {
            users = new ArrayList<>();
            loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String username;
    private String password;
    private String email;
    private String nickname;
    private RecoveryQuestion passwordRecoveryQuestion;
    private String slogan;
    private String passwordRecoveryAnswer;
    private String avatarName;
    private long lastAttempt;
    private int numberOfAttempts;
    private int highScore;
    private int rank;
    private String lastEntrance;

    private static ArrayList<User> users = new ArrayList<User>();
    private static JsonArray usersArray = new JsonArray();
    private final ArrayList<String> friends= new ArrayList<>();
    private final ArrayList<String> pendingFriendRequests = new ArrayList<>();

    public User(Information information, RecoveryQuestion passwordRecoveryQuestion, String passwordRecoveryAnswer) {
        this.username = information.getUsername();
        this.password = information.getPassword();
        this.email = information.getEmail();
        this.nickname = information.getNickname();
        this.slogan = information.getSlogan();
        this.passwordRecoveryQuestion = passwordRecoveryQuestion;
        this.passwordRecoveryAnswer = passwordRecoveryAnswer;
        this.lastAttempt = 0;
        this.numberOfAttempts = 0;
        this.highScore = 0;
        this.rank = 0; // TODO : CHECK THIS SHIT :)
        setEntrance();
        this.giveAAvatar();
    }

    public ArrayList<String> getFriends() {
        if(friends==null)
            return new ArrayList<>();
        return friends;
    }

    public ArrayList<String> getPendingFriendRequests() {
        if(pendingFriendRequests==null)
            return new ArrayList<>();
        return pendingFriendRequests;
    }

    public void addFriend(String username) {
        friends.add(username);
    }
    public void removeFriend(String username) {
        friends.remove(username);
    }
    public void acceptFriend(String username) {
        pendingFriendRequests.remove(username);
        addFriend(username);
    }
    public void addFriendRequest(String username) {
        pendingFriendRequests.add(username);
    }

    public String getUsername() {
        return username;
    }

    public void changeHighScore(int score) {
        highScore = Math.max(highScore, score);
    }

    public boolean checkPassword(String password) throws NoSuchAlgorithmException {
        return Controller.toSHA256(password).equals(this.password);
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSlogan() {
        if(slogan==null)
            return "";
        return slogan;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setLastAttempt() {
        lastAttempt = System.currentTimeMillis();
    }

    public long getLastAttempt() {
        return lastAttempt;
    }

    public void addAttempt() {
        numberOfAttempts++;
    }

    public void resetAttempt() {
        numberOfAttempts = 0;
    }

    public int getAttempt() {
        return numberOfAttempts;
    }

    public RecoveryQuestion getPasswordRecoveryQuestion() {
        return passwordRecoveryQuestion;
    }

    public String getPasswordRecoveryAnswer() {
        return passwordRecoveryAnswer;
    }

    public void setNewPassword(String newPassword) throws NoSuchAlgorithmException {
        this.password = Controller.toSHA256(newPassword);
    }

    public void setNewUsername(String newUsername) {
        this.username = newUsername;
    }

    public void setNewNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void setNewEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setNewSlogan(String newSlogan) {
        this.slogan = newSlogan;
    }

    public void removeSlogan() {
        this.slogan = "";
    }

    public static boolean isUsernameValid(String username) {
        return username.matches("[\\w_]+");
    }

    public static Messages isPasswordStrong(String password) {
        boolean containsSmallLetter = Pattern.compile("[a-z]").matcher(password).find();
        boolean containsCapitalLetter = Pattern.compile("[A-Z]").matcher(password).find();
        boolean containsDigit = Pattern.compile("\\d").matcher(password).find();
        boolean containsNonWord = Pattern.compile("[\\W_]").matcher(password).find();
        if (password.length() < 6 && (!containsSmallLetter || !containsCapitalLetter || !containsDigit || !containsNonWord))
            return Messages.WEAK_PASSWORD;
        if (password.length() < 6 || (!containsSmallLetter || !containsCapitalLetter || !containsDigit || !containsNonWord))
            return Messages.MODERATE_PASSWORD;
        return Messages.STRONG_PASSWORD;
    }

    public static boolean isEmailValid(String email) {
        return email.matches("[\\w_\\.]+@[\\w_\\.]+\\.[\\w_\\.]+");
    }

    public int getHighScore() {
        return highScore;
    }

    public int getRank() {
        return rank;
    }

    public String avatarName() {
        if (avatarName == null) this.giveAAvatar();
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public void giveAAvatar() {
        setAvatarName(Controller.giveARandomAvatar());
    }

    public String lastEntrance(){
        if (this.lastEntrance == null) this.lastEntrance = "unknown";
        return lastEntrance;
    }
    public void setEntrance(){
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        String mmTime, hhTime;
        if (minutes < 10) mmTime = "0" + minutes;
        else mmTime = Integer.toString(minutes);
        if (hours < 10) hhTime = "0" + hours;
        else hhTime = Integer.toString(hours);
        lastEntrance = hhTime + ":" + mmTime;
        User.updateUsers();
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void addUser(User user) throws IOException {
        if (!users.contains(user))
            users.add(user);
        sortUsers();
        updateUsers();
    }

    public static void loadUsers() throws IOException {
        FileReader file = new FileReader("Stronghold crusader/DB/Users");
        Scanner scanner = new Scanner(file);
        if (!scanner.hasNextLine()) {
            scanner.close();
            return;
        }
        String allUsers = scanner.nextLine();
        scanner.close();
        file.close();
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(allUsers, JsonArray.class);
        users.clear();
        for (JsonElement jsonElement : jsonArray) {
            users.add(gson.fromJson(jsonElement, User.class));
        }
        usersArray = jsonArray;
    }

    public static void updateUsers() {
        try {
            Gson gson = new Gson();
            usersArray = new JsonArray();
            for (User user : users) {
                usersArray.add(gson.toJsonTree(user).getAsJsonObject());
            }
            FileWriter file = new FileWriter("Stronghold crusader/DB/Users");
            file.write(usersArray.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User stayLoggedIn() throws IOException {
        Gson gson = new Gson();
        FileReader file = new FileReader("Stronghold crusader/DB/stayLoggedIn");
        Scanner scanner = new Scanner(file);
        if (!scanner.hasNextLine()) {
            scanner.close();
            file.close();
            return null;
        }
        String userString = scanner.nextLine();
        scanner.close();
        file.close();
        User user = gson.fromJson(userString, User.class);
        return user;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static User getUserByEmail(String email) {
        email = email.toLowerCase();
        for (User user : users) {
            if (user.getEmail().toLowerCase().equals(email))
                return user;
        }
        return null;
    }

    public static void sortUsers() {
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                if (doesHaveHigherRank(users.get(j), users.get(i))) {
                    Collections.swap(users, i, j);
                }
            }
        }
        for (int i = 0; i < users.size(); i++)
            users.get(i).setRank(i + 1);
    }

    private static boolean doesHaveHigherRank(User firstUser, User secondUser) {
        if (firstUser.getHighScore() > secondUser.getHighScore())
            return true;
        if (secondUser.getHighScore() > firstUser.getHighScore())
            return false;
        if (firstUser.getUsername().compareTo(secondUser.getUsername()) < 0)
            return true;
        return false;
    }

    public static class Information {
        private String username;
        private String password;
        private String email;
        private String nickname;
        private String slogan;

        public Information(String username, String password, String email, String nickname, String slogan) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.nickname = nickname;
            this.slogan = slogan;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public String getNickname() {
            return nickname;
        }

        public String getSlogan() {
            return slogan;
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof User)) return false;
        return ((User) obj).username.equals(this.username);
    }
}