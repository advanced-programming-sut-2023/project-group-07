package view;

import controller.Controller;
import controller.LoginMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class LoginMenuGraphics extends Application {
    public Button signUpButton;
    public HBox menuBox;
    public ImageView captchaImage;
    public Rectangle refreshImage;
    public HBox captchaBox;

    private Pane rootPane;
    private LoginMenuController controller = new LoginMenuController();

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
        Background background = new Background(new BackgroundImage((new Image(LoginMenuGraphics.class.getResource("/Images/Background/1.jpg").toString(), Main.screenWidth, Main.getScreenHeight, false, false)),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT));
        pane.setBackground(background);
        stage.setScene(scene);
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
        initializeCaptcha();
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

    public void resetCaptcha() {
        captchaImage.setImage(controller.generateCaptcha());
    }

    public void initializeCaptcha() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.LIGHTBLUE);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(30);
        captchaBox.setStyle("-fx-background-color: white;");
        captchaBox.setEffect(borderGlow);
        ImagePattern imagePattern = new ImagePattern(new Image(LoginMenuGraphics.class.getResource("/Images/refresh.png").toString(), 40, 40, false, false));
        refreshImage.setWidth(30);
        refreshImage.setHeight(30);
        refreshImage.setFill(imagePattern);
        refreshImage.setStyle("-fx-background-color: white;");
        resetCaptcha();
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
