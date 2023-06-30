package Client.view;

import Client.controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.net.URL;

public class MainMenuGraphics extends Application {
    public VBox menuVBox;
    public BorderPane borderPane;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(Controller.getBorderPane("/FXML/MainMenu.fxml")));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public void newGame(MouseEvent mouseEvent) throws Exception {
        new PreGameMenuGraphics().start(Main.stage);
    }

    public void createMap(MouseEvent mouseEvent) throws Exception {
        new CreateMapGraphics().start(Main.stage);
    }

    public void profileMenu(MouseEvent mouseEvent) throws Exception {
        new ProfileMenuGraphics().start(Main.stage);
    }

    public void settings(MouseEvent mouseEvent) {
    }

    public void logOut(MouseEvent mouseEvent) throws Exception {
        FileWriter file = new FileWriter("Stronghold Crusader/DB/stayLoggedIn");
        file.close();
        new LoginMenuGraphics().start(Main.stage);
    }

    public void exit(MouseEvent mouseEvent) {
        Main.stage.close();
    }

}
