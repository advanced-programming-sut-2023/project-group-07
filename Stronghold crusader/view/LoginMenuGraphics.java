package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class LoginMenuGraphics extends Application {
    public Button signUpButton;
    public HBox menuBox;
    private Pane rootPane;

    @Override
    public void start(Stage stage) throws Exception {
        URL url = LoginMenuGraphics.class.getResource("/FXML/LoginMenu.fxml");
        Pane pane = FXMLLoader.load(url);
        rootPane = pane;
        Scene scene = new Scene(pane);
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
        pane.setCenterShape(true);
        pane.getChildren().get(0).setLayoutX(Main.stage.getScene().getRoot().getBoundsInParent().getCenterX()-500);
        pane.getChildren().get(0).setLayoutY(Main.stage.getScene().getRoot().getBoundsInParent().getCenterY()-200);
    }

    public void initialize() {


    }

    public void forgotMyPassword(MouseEvent mouseEvent) {
    }

    public void loginMenu(MouseEvent mouseEvent) throws Exception {
//        menuBox.getChildren().get(0).setVisible(true);
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,true,menuBox.getBoundsInParent().getCenterX(), 0, 1);
        menuFadeTransition.play();
//        setVisibility(menuBox,0);
    }

    public void signUpMenu(MouseEvent mouseEvent) throws Exception {
//        menuBox.getChildren().get(2).setVisible(true);
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,false,menuBox.getBoundsInParent().getCenterX(), 2, 1);
        menuFadeTransition.play();
    }

    public void signUpBack(MouseEvent mouseEvent) {
//        menuBox.getChildren().get(1).setVisible(true);
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,true,menuBox.getBoundsInParent().getCenterX(), 1, 2);
        menuFadeTransition.play();
    }

    public void loginBack(MouseEvent mouseEvent) {
//        menuBox.getChildren().get(1).setVisible(true);
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,false,menuBox.getBoundsInParent().getCenterX(), 1, 0);
        menuFadeTransition.play();
    }


//    public void createWindow(BorderPane borderPane) {
//        Scene scene = new Scene(borderPane);
//        Stage stage = Main.stage;
//        stage.setScene(scene);
//        stage.show();
//    }

//    public void forgotMyPassword(MouseEvent mouseEvent) {
//        BorderPane borderPane = new BorderPane();
//        TextField textField = new TextField();
//        Button button = new Button();
//        VBox vBox = new VBox();
//        Text text = new Text("Title");
//        Text text = new Text(Controller.currentUser.getPasswordRecoveryQuestion().toString());
//        textField.setPromptText("Answer");
//        button.setText("Next");
//        vBox.getChildren().addAll(text,textField,button);
//        vBox.setSpacing(20);
//        vBox.setAlignment(Pos.CENTER);
//        borderPane.setCenter(vBox);
//        Stage stage = createStage(borderPane);
//        stage.show();
//    }
}
