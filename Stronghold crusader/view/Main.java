package view;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Map;
import model.User;

import static javafx.application.Application.launch;

public class Main extends Application {
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
        Image img = new Image(LoginMenu.class.getResource("/Images/Icon/icon.png").toExternalForm());
        stage.getIcons().add(img);
        Main.stage = stage;
        new LoginMenuGraphics().start(stage);
    }
}
