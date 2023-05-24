package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class LoginMenuGraphics extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL url = LoginMenuGraphics.class.getResource("/FXML/LoginMenu.fxml");
        BorderPane borderPane = FXMLLoader.load(url);
        Scene scene = new Scene(borderPane);
//        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
//        double width = resolution.getWidth();
//        double height = resolution.getHeight();
//        double w = width/1280;
//        double h = height/720;
//        Scale scale = new Scale(w,h,0,0);
//        scene.getRoot().getTransforms().setAll(scale);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public void forgotMyPassword(MouseEvent mouseEvent) {
    }

    public void loginMenu(MouseEvent mouseEvent) {
    }

    public void signUpMenu(MouseEvent mouseEvent) throws Exception {
        new SignUpGraphics().start(Main.stage);
    }

    public void login(MouseEvent mouseEvent) {
    }

//    public void createWindow(BorderPane borderPane) {
//        Scene scene = new Scene(borderPane);
//        Stage stage = Main.stage;
//        stage.setScene(scene);
//        stage.show();
//    }
}
