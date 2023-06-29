package Server;

import Client.model.User;

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
        try {
            dataOutputStream.writeUTF("do you want to sign up or login?");
            String input = dataInputStream.readUTF();
            while (true) {
                if (input.matches(".*1.*") || input.matches(".*sign up.*"))
                    return signUp();
                else if ((input.matches(".*2.*") || input.matches(".*login.*")))
                    return loginWihExistingUser();
                dataOutputStream.writeUTF("sign up/login? 1/2?");
                input = dataInputStream.readUTF();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User loginWihExistingUser() {
        String regex = "login -u (?<username>\\S+) -p (?<password>\\S+)";
        try {
            dataOutputStream.writeUTF(regex);
            while (true) {
                String input = dataInputStream.readUTF();
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
        // TODO: 6/29/2023 complete
        return null;
    }
}
