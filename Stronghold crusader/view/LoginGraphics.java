package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class LoginGraphics extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL url = LoginGraphics.class.getResource("/FXML/Login.fxml");
        BorderPane borderPane = FXMLLoader.load(url);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public void login(MouseEvent mouseEvent) {
    }

    public void forgotMyPassword(MouseEvent mouseEvent) {
    }

    public void back(MouseEvent mouseEvent) {
    }
}
