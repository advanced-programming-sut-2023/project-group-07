package view;

import controller.LoginMenuController;
import javafx.animation.*;
import controller.Controller;
import controller.LoginMenuController;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Slogan;
import model.User;

import java.awt.*;
import java.net.URL;
import java.util.Random;

public class LoginMenuGraphics extends Application {

    private final LoginMenuController controller = new LoginMenuController();
    public Button signUpButton;
    public HBox menuBox;
    public GridPane loginMenu;
    public GridPane signUpMenu;
    public CheckBox showHideSignUp;
    public CheckBox showHideLogin;
    public Text loginError;
    public Text signUpError;
    public CheckBox sloganBox;
    public ImageView captchaImage;
    public Rectangle refreshImage;
    public HBox captchaBox;

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
//
        Background background = new Background(new BackgroundImage((new Image(LoginMenuGraphics.class.getResource("/Images/Background/1.jpg").toString(), Main.screenWidth, Main.getScreenHeight, false, false)),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT));
        pane.setBackground(background);
        stage.setScene(scene);
        stage.show();
        pane.setCenterShape(true);
        pane.getChildren().get(0).setLayoutX(Main.stage.getScene().getRoot().getBoundsInParent().getCenterX()-2450);
        pane.getChildren().get(0).setLayoutY(Main.stage.getScene().getRoot().getBoundsInParent().getCenterY()-300);
    }

    public void initialize() {
        addUsernameListener();
        passwordVisibilityToggle();
        addPasswordListener();
    }

    public void forgotMyPassword(MouseEvent mouseEvent) {
    }

    public void loginMenu(MouseEvent mouseEvent) throws Exception {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,true,menuBox.getBoundsInParent().getCenterX(), 2, 3);
        menuFadeTransition.play();
        initializeCaptcha();
//        setVisibility(menuBox,0);
    }

    public void signUpMenu(MouseEvent mouseEvent) throws Exception {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,false,menuBox.getBoundsInParent().getCenterX(), 4, 3);
        menuFadeTransition.play();
    }

    public void signUpBack(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,true,menuBox.getBoundsInParent().getCenterX(), 3, 4);
        menuFadeTransition.play();
        emptyFields(signUpMenu);
    }

    public void loginBack(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane)Main.stage.getScene().getRoot(),menuBox,false,menuBox.getBoundsInParent().getCenterX(), 3, 2);
        menuFadeTransition.play();
        emptyFields(loginMenu);
    }

    private void addUsernameListener() {
        TextField usernameLogin = (TextField) loginMenu.getChildren().get(0);
        Text usernameLoginCheck = (Text) loginMenu.getChildren().get(1);
        TextField usernameSignUp = (TextField) signUpMenu.getChildren().get(0);
        Text usernameSignUpCheck = (Text) signUpMenu.getChildren().get(1);
        usernameSignUp.textProperty().addListener(((observableValue, s, t1) -> {
            if(!User.isUsernameValid(usernameSignUp.getText()) && !usernameSignUp.getText().isEmpty())
                usernameSignUpCheck.setText("Invalid username format!");
            else if(controller.usernameExistenceCheck(usernameSignUp.getText())!=null)
                usernameSignUpCheck.setText("Username exists!");
            else
                usernameSignUpCheck.setText("");
        }));
        usernameLogin.textProperty().addListener(((observableValue, s, t1) -> {
            if(controller.usernameExistenceCheck(usernameLogin.getText())==null && !usernameLogin.getText().isEmpty())
                usernameLoginCheck.setText("Username doesn't exists!");
            else
                usernameLoginCheck.setText("");
        }));
    }

    private void passwordVisibilityToggle() {
        passwordFieldBinding((PasswordField) loginMenu.getChildren().get(2),(TextField) loginMenu.getChildren().get(3),showHideLogin);
        passwordFieldBinding((PasswordField) signUpMenu.getChildren().get(2),(TextField) signUpMenu.getChildren().get(4),showHideSignUp);
        passwordFieldBinding((PasswordField) signUpMenu.getChildren().get(3),(TextField) signUpMenu.getChildren().get(5),showHideSignUp);
    }
    private void passwordFieldBinding(PasswordField passwordField,TextField textField,CheckBox checkBox) {
        passwordField.managedProperty().bind(checkBox.selectedProperty().not());
        passwordField.visibleProperty().bind(checkBox.selectedProperty().not());
        textField.managedProperty().bind(checkBox.selectedProperty());
        textField.visibleProperty().bind(checkBox.selectedProperty());
        textField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    private void addPasswordListener() {
        PasswordField passwordFieldLogin = (PasswordField) loginMenu.getChildren().get(2);
        PasswordField passwordFieldSignUp = (PasswordField) signUpMenu.getChildren().get(2);
        PasswordField passwordFieldConfirmSignUp = (PasswordField) signUpMenu.getChildren().get(3);
        Text passwordSignUpCheck = (Text) signUpMenu.getChildren().get(6);
        Text passwordConfirmSignUpCheck = (Text) signUpMenu.getChildren().get(7);
        passwordFieldSignUp.textProperty().addListener(((observableValue, s, t1) -> {
            if(t1.isEmpty()){
                passwordSignUpCheck.setText("");
            }
            else {
                switch (User.isPasswordStrong(t1)) {
                    case WEAK_PASSWORD:
                        passwordSignUpCheck.setText("Weak");
                        passwordSignUpCheck.setFill(Color.RED);
                        break;
                    case MODERATE_PASSWORD:
                        passwordSignUpCheck.setText("Moderate");
                        passwordSignUpCheck.setFill(Color.ORANGE);
                        break;
                    case STRONG_PASSWORD:
                        passwordSignUpCheck.setText("Strong");
                        passwordSignUpCheck.setFill(Color.GREEN);
                        break;
                }
            }
            if(t1.equals(passwordFieldConfirmSignUp.getText()))
                passwordConfirmSignUpCheck.setText("");
            else
                passwordConfirmSignUpCheck.setText("Passwords do not match!");
        }));
        passwordFieldConfirmSignUp.textProperty().addListener(((observableValue, s, t1) -> {
            if(t1.equals(passwordFieldSignUp.getText()))
               passwordConfirmSignUpCheck.setText("");
            else
                passwordConfirmSignUpCheck.setText("Passwords do not match!");
        }));
    }

    public void randomPasswordButton(MouseEvent mouseEvent) {
        PasswordField passwordFieldSignUp = (PasswordField) signUpMenu.getChildren().get(2);
        PasswordField passwordFieldConfirmSignUp = (PasswordField) signUpMenu.getChildren().get(3);
        String randomPass = controller.randomPasswordGenerator();
        passwordFieldSignUp.setText(randomPass);
        passwordFieldConfirmSignUp.setText(randomPass);
    }

    public void emptyFields(GridPane gridPane) {
        for(Node node : gridPane.getChildren()){
            if(node instanceof TextField){
                TextField textField = (TextField)node;
                textField.setText("");
            }
            if(node instanceof CheckBox){
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        }
    }


    public void sloganCheckbox(MouseEvent mouseEvent) {
        if(sloganBox.isSelected()) {
            TextField textField = new TextField();
            textField.setPromptText("Slogan");
            Button button = new Button("Random slogan");
            button.setAlignment(Pos.CENTER);
            button.setScaleX(0.7);
            button.setScaleY(0.7);
            button.setPrefWidth(200);
            GridPane.setRowIndex(signUpMenu.getChildren().get(13),11);
            GridPane.setRowIndex(signUpMenu.getChildren().get(14),12);
            signUpMenu.add(textField,0,9);
            signUpMenu.add(button,0,10);
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    int i = new Random().nextInt(3)+1;
                    int counter=1;
                    for(Slogan slogan : Slogan.values()){
                        if(counter==i)
                            textField.setText(slogan.toString());
                        counter++;
                }
            }});
        }
        else {
            signUpMenu.getChildren().remove(signUpMenu.getChildren().size()-1);
            signUpMenu.getChildren().remove(signUpMenu.getChildren().size()-1);
            GridPane.setRowIndex(signUpMenu.getChildren().get(13),9);
            GridPane.setRowIndex(signUpMenu.getChildren().get(14),10);
        }
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
