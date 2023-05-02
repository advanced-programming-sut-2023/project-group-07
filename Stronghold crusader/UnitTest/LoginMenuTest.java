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
    private LoginMenuController controller;
    @Before
    public void setup(){
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
}