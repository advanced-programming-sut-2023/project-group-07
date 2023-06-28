package view;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.User;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
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

    public void newGame(MouseEvent mouseEvent) {
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
