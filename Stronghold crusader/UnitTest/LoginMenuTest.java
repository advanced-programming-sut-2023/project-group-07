package UnitTest;

import controller.LoginMenuController;
import controller.Messages;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import view.LoginMenu;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginMenuTest {
    /*
    we have te following user
        user create -u FOR_UNIT_TEST -p Poop@2 Poop@2 -e URichard85@gmail.com -n "U Lion heart"
       question pick -q 1 -a "Richard Senior" -c "Richard Senior"
    */

    private LoginMenuController controller;
    @Before
    public void setup() throws IOException {
        User.loadUsers();
        controller = new LoginMenuController();
    }
    @Test
    public void testSignup() throws IOException, NoSuchAlgorithmException {
        Messages message = controller.signUp("username","password",
                "","","","");
        Assert.assertEquals(message,Messages.EMPTY_FIELD);

        message = controller.signUp("username","password",
                "","email","","nickname");
        Assert.assertNotEquals(message,Messages.EMPTY_FIELD);

        message = controller.signUp("#sername","password",
                "confirm","email","slogan","nickname");
        Assert.assertEquals(message,Messages.INVALID_USERNAME);

        message = controller.signUp("Asername","rd",
                "confirm","email","slogan","nickname");
        Assert.assertEquals(message,Messages.WEAK_PASSWORD_LENGTH);

        message = controller.signUp("Asername","rdAAAA",
                "confirm","email","slogan","nickname");
        Assert.assertEquals(message,Messages.WEAK_PASSWORD_CHARACTERS);

        message = controller.signUp("Asername","rAAa8546",
                "confirm","email","slogan","nickname");
        Assert.assertEquals(message,Messages.WEAK_PASSWORD_CHARACTERS);

        message = controller.signUp("Asername","r!@#$%",
                "confirm","email","slogan","nickname");
        Assert.assertEquals(message,Messages.WEAK_PASSWORD_CHARACTERS);

        message = User.isPasswordStrong("!@a1A564");
        Assert.assertEquals(message,Messages.STRONG_PASSWORD);

        message = controller.signUp("Asername","r!@#$45865AA%",
                "confirm","email","slogan","nickname");
        Assert.assertEquals(message,Messages.PASSWORD_NOT_CONFIRMED);

        message = controller.signUp("Asername","r!@#$45865AA%",
                "r!@#$45865AA%","email","slogan","nickname");
        Assert.assertEquals(message,Messages.INVALID_EMAIL_FORMAT);

    }

    @Test
    public void testLogin() throws IOException, NoSuchAlgorithmException {
        Messages message;

        message = controller.login("FOR_UNIT_TEST!@#$%$#@$%$#@","Poop@2", false);
        Assert.assertEquals(message, Messages.USERNAME_NOT_FOUND);

        message = controller.login("FOR_UNIT_TEST","Poop)@2", false);
        Assert.assertEquals(message, Messages.INCORRECT_PASSWORD);

        message = controller.login("FOR_UNIT_TEST","Poop@2", false);
        Assert.assertEquals(message, Messages.WAIT_FOR_LOGIN);

    }
}