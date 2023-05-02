package UnitTest;

import controller.LoginMenuController;
import controller.Messages;
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

//        message = controller.signUp("Asername","password",
//                "confirm","email","slogan","nickname");
//        Assert.assertNotEquals(message,Messages.INVALID_USERNAME);



    }
}