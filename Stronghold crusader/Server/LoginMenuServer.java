package Server;

import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenuServer {
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public LoginMenuServer(DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
    }

    public User login() {
        User user;
        try {
            dataOutputStream.writeUTF("do you want to sign up or login?");
            String input = dataInputStream.readUTF();
            while (true) {
                if (input.equals("exit")) return null;
                if (input.matches(".*1.*") || input.matches(".*sign up.*")){
                    user = signUp();
                    break;
                }
                else if ((input.matches(".*2.*") || input.matches(".*login.*"))) {
                    user = loginWihExistingUser();
                    break;
                }
                dataOutputStream.writeUTF("sign up/login? 1/2?");
                input = dataInputStream.readUTF();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (user != null) user.setEntrance();
        return user;
    }

    private User loginWihExistingUser() {
        String regex = "login -u (?<username>\\S+) -p (?<password>\\S+)";
        try {
            dataOutputStream.writeUTF(regex);
            regex = "\\s*" + regex + "\\s*";
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("exit")) return null;
                Matcher matcher = Pattern.compile(regex).matcher(input);
                if (!matcher.find()) {
                    dataOutputStream.writeUTF("invalid input");
                    continue;
                }
                User user = LoginController.login(matcher.group("username"), matcher.group("password"));
                if (user == null) dataOutputStream.writeUTF("invalid username or password");
                else return user;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User signUp() {
        String regex = "sign up -u (?<username>\\S+) -n (?<nickname>\\S+) -p (?<password>\\S+) -e (?<email>\\S+)";
        try {
            dataOutputStream.writeUTF(regex);
            regex = "\\s*" + regex + "\\s*";
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("exit")) return null;
                Matcher matcher = Pattern.compile(regex).matcher(input);
                if (!matcher.find()) {
                    dataOutputStream.writeUTF("invalid input");
                    continue;
                }
                String username = matcher.group("username");
                OutputMessage message = LoginController.singUp(username,
                        matcher.group("nickname"),
                        matcher.group("password"),
                        matcher.group("email")
                );
                switch (message){
                    case WEEK_PASSWORD:
                        dataOutputStream.writeUTF("week password");
                        break;
                    case EMAIL_EXISTS:
                        dataOutputStream.writeUTF("email exists");
                        break;
                    case USERNAME_EXISTS:
                        dataOutputStream.writeUTF("username exists");
                        break;
                    case INVALID_USERNAME:
                        dataOutputStream.writeUTF("invalid username");
                        break;
                    case SUCCESSFUL :
                        dataOutputStream.writeUTF("successful");
                        return LoginController.getUser(username);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
