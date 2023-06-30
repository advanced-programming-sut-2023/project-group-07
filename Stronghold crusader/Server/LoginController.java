package Server;

import Client.model.User;

import java.security.NoSuchAlgorithmException;

public class LoginController {
    public static User login(String username, String password) {
        User user = User.getUserByUsername(username);
        try {
            if (user==null || !user.checkPassword(password)) return null;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static User singUp(String username, String nickname, String password, String email) {

    }
}
