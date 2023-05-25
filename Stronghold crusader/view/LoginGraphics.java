package view;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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



    public void back(MouseEvent mouseEvent) throws Exception {
        new LoginMenuGraphics().start(Main.stage);
    }

    private Stage createStage(BorderPane borderPane) {
        Scene scene = new Scene(borderPane,300,150);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.stage);
        return stage;
    }
}
