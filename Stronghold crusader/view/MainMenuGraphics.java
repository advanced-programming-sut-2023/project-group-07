package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class MainMenuGraphics extends Application {
    public VBox menuVBox;
    public BorderPane borderPane;
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = LoginMenuGraphics.class.getResource("/FXML/MainMenu.fxml");
        borderPane = FXMLLoader.load(url);
        Scene scene = new Scene(borderPane);
        Background background = new Background(new BackgroundImage((new Image(LoginMenuGraphics.class.getResource("/Images/Background/1.jpg").toString(), Main.screenWidth, Main.screenHeight, false, false)),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT));
        borderPane.setBackground(background);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public void newGame(MouseEvent mouseEvent) {
    }

    public void createMap(MouseEvent mouseEvent) {

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
