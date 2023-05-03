package UnitTest;

import controller.Controller;
import controller.LoginMenuController;
import controller.Messages;
import controller.ProfileMenuController;
import model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ConcurrentModificationException;
import java.util.Currency;

public class ProfileMenuTest {
     /*
    we have te following user
        user create -u FOR_UNIT_TEST -p Poop@2 Poop@2 -e URichard85@gmail.com -n "U Lion heart"
       question pick -q 1 -a "Richard Senior" -c "Richard Senior"
       user create -u FOR_UNIT_TEST2 -p Poop@2 Poop@2 -e URichard85@gmail2.com -n "U2 Lion heart"
    */

    private ProfileMenuController controller;
    final String username = "FOR_UNIT_TEST", password = "Poop@2", email = "URichard85@gmail.com",
            nickname = "U Lion heart", slogan = "";
    String currentUsername, currentPassword, currentEmail, currentNickname, currentSlogan;
    User user;

    @Before
    public void setup() throws IOException {
        User.loadUsers();
        user = User.getUserByUsername(username);
        Controller.currentUser = user;
        controller = new ProfileMenuController();
        currentUsername = username;
        currentEmail = email;
        currentPassword = password;
        currentNickname = nickname;
        currentSlogan = slogan;
    }

    @After
    public void ending() throws IOException, NoSuchAlgorithmException {
        controller.changeUsername(username);
        controller.changeEmail(email);
        controller.changeNickname(nickname);
        controller.changePassword(currentPassword, password);
        controller.changeSlogan(slogan);

        Controller.currentUser = null;
    }

    @Test
    public void testShowInfo() {
        Assert.assertEquals("Username : " + currentUsername +
                        "\nNickname : " + currentNickname +
                        "\nEmail : " + currentEmail +
                        "\nSlogan : " + currentSlogan +
                        "\nHighest score : " + user.getHighScore() +
                        "\nRank : " + user.getRank()
                , controller.showInfo());
    }

    @Test
    public void testChangeUsername() throws IOException, NoSuchAlgorithmException {
        Assert.assertEquals(controller.changeUsername("!@#$"), Messages.INVALID_USERNAME);
        Assert.assertEquals(controller.changeUsername("FOR_UNIT_TEST2"), Messages.USERNAME_EXISTS);
        currentUsername = "A5";
        Assert.assertEquals(controller.changeUsername(currentUsername), Messages.CHANGE_USERNAME_SUCCESSFUL);
        testShowInfo();
    }

    @Test
    public void testChangeNickName() throws IOException, NoSuchAlgorithmException {
        currentNickname = "oiuytfgvb ugjcnm ";
        controller.changeNickname(currentNickname);
        testShowInfo();
    }

    @Test
    public void testChangeEmail() throws IOException, NoSuchAlgorithmException {
        Assert.assertEquals(controller.changeEmail("URichard85@gmail2.com"), Messages.EMAIL_EXISTS);
        Assert.assertEquals(controller.changeEmail("FO R_UN  IT_TES T2"), Messages.INVALID_EMAIL_FORMAT);
        currentEmail = "AG_o.875puiytr.0p9o765@9ijl.cas87dfom";
        Assert.assertEquals(controller.changeEmail(currentEmail), Messages.CHANGE_EMAIL_SUCCESSFUL);
        testShowInfo();
    }


}
