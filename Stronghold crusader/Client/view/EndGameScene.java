package Client.view;

import Client.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EndGameScene extends Application implements Initializable {
    private static String endGameMessage;
    @FXML
    private Label endGameLabel;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(Controller.getBorderPane("/FXML/EndGameScene.fxml")));
        stage.setFullScreen(true);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        endGameLabel.setText(endGameMessage);
    }

    public static void setEndGameMessage(String endGameMessage) {
        EndGameScene.endGameMessage = endGameMessage;
    }

    public void startANewGame(MouseEvent mouseEvent) throws Exception {
        new PreGameMenuGraphics().start(Main.stage);
    }

    public void backToMainMenu(MouseEvent mouseEvent) throws Exception {
        new MainMenuGraphics().start(Main.stage);
    }
}
