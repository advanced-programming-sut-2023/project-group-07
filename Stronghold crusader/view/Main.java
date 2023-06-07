package view;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.Controller;
import controller.GameMenuController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.*;

import static javafx.application.Application.launch;

public class Main extends Application {
    static double screenWidth;
    static double screenHeight;
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
        screenHeight = screenBounds.getHeight();
        Image img = new Image(Main.class.getResource("/Images/Icon/icon.png").toExternalForm());
        stage.getIcons().add(img);
        stage.setFullScreen(true);
        Main.stage = stage;
        ArrayList<Government> governments = new ArrayList<>();
        LordColor currentLordColor = LordColor.getLordColor(0);
        governments.add(new Government(LordColor.getLordColor(0), Controller.currentUser, 0,
                Map.getMaps().get(0).getKeepPosition(currentLordColor)[0],
                Map.getMaps().get(0).getKeepPosition(currentLordColor)[1]));
        Controller.currentGame = new Game(Map.getMaps().get(0),governments,2000);
        GameGraphics gameGraphics = new GameGraphics();
        GameMenuController gameMenuController= new GameMenuController();
        gameGraphics.setGameMenuController(gameMenuController);
        gameGraphics.start(stage);
//        new LoginMenuGraphics().start(stage);
//        new LoginMenuGraphics().start(stage);
    }
}
