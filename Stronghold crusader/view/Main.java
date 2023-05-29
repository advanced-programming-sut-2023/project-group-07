package view;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Map;
import model.User;

import static javafx.application.Application.launch;

public class Main extends Application {
    static double screenWidth;
    static double getScreenHeight;
    static Stage stage;
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
//        System.out.print(Colors.YELLOW_BOLD);
//        System.out.print("Loading maps and users...");
        User.loadUsers();
        Map.loadMaps();
//        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
//        System.out.print(Colors.RESET);
//        LoginMenu loginMenu = new LoginMenu();
//        loginMenu.run(new Scanner(System.in));
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenWidth = screenBounds.getWidth();
        getScreenHeight = screenBounds.getHeight();
        Image img = new Image(LoginMenu.class.getResource("/Images/Icon/icon.png").toExternalForm());
        stage.getIcons().add(img);
        stage.setFullScreen(true);
        Main.stage = stage;
//        new LoginMenuGraphics().start(stage); // TODO: 5/28/2023
        new LoginMenuGraphics().start(stage);
    }
}
