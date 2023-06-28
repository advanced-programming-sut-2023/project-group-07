package Client.view;

import Client.controller.LoginMenuController;
import Client.controller.Controller;
import Client.controller.Messages;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Client.model.RecoveryQuestion;
import Client.model.Slogan;
import Client.model.User;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginMenuGraphics extends Application {

    private final LoginMenuController controller = new LoginMenuController();
    @FXML
    private Button signUpButton;
    @FXML
    private HBox menuBox;
    @FXML
    private GridPane loginMenu;
    @FXML
    private GridPane signUpMenu;
    @FXML
    private GridPane recoveryQuestionMenu;
    @FXML
    private GridPane forgetPassword;
    @FXML
    private GridPane enteringNewPassword;
    @FXML
    private CheckBox showHideSignUp;
    @FXML
    private CheckBox showHideLogin;
    @FXML
    private Text loginError;
    @FXML
    private Text signUpError;
    @FXML
    private CheckBox sloganBox;
    private Text sloganCheck;
    private Button randomSloganButton;
    private TextField sloganField;
    private Label recoveryQuestion;
    private TextField recoveryAnswerField;
    private Text recoveryAnswerCheck;
    @FXML
    private CheckBox showHideNewPassword;
    private Pane rootPane;
    private Timer loginPasswordTimer;

    @Override
    public void start(Stage stage) throws Exception {
        URL url = LoginMenuGraphics.class.getResource("/FXML/LoginMenu.fxml");
        Pane pane = FXMLLoader.load(url);
        rootPane = pane;
        Scene scene = new Scene(pane);
        Background background = new Background(new BackgroundImage((new Image(LoginMenuGraphics.class.getResource("/Images/Background/1.jpg").toString(), Main.screenWidth, Main.screenHeight, false, false)),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT));
        pane.setBackground(background);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        pane.setCenterShape(true);
        pane.getChildren().get(0).setLayoutX(Main.stage.getScene().getRoot().getBoundsInParent().getCenterX() - 2450);
        pane.getChildren().get(0).setLayoutY(Main.stage.getScene().getRoot().getBoundsInParent().getCenterY() - 300);
    }

    public void initialize() {
        addUsernameListener();
        passwordVisibilityToggle();
        addSignUpPasswordListener();
        addLoginPasswordListener();
        addNewPasswordListener();
        addEmailListener();
        addNickNameListener();
        initializeCaptcha();
        addSignupRecoveryAnswerListener();
        addSignupCaptchaListener();
        addLoginCaptchaListener();
        addForgetPasswordUsernameListener();
        ((Label) getLoginChild(9)).setStyle("-fx-font-size: 15");
    }

    private void addLoginCaptchaListener() {
        TextField captchaField = (TextField) ((HBox) ((VBox) getLoginChild(7)).getChildren().get(1)).getChildren().get(0);
        Text captchaCheck = (Text) ((HBox) ((VBox) getLoginChild(7)).getChildren().get(1)).getChildren().get(1);
        captchaField.textProperty().addListener(((observableValue, s, t1) -> {
            captchaCheck.setText("");
            captchaField.setStyle("-fx-border-color: lightgray");
        }));
    }

    private void addLoginPasswordListener() {
        PasswordField passwordField = (PasswordField) getLoginChild(2);
        TextField passwordTextField = (TextField) getLoginChild(3);
        Text passwordLoginCheck = (Text) getLoginChild(4);
        passwordField.textProperty().addListener(((observableValue, s, t1) -> {
            passwordLoginCheck.setText("");
            passwordField.setStyle("-fx-border-color: lightgray");
            passwordTextField.setStyle("-fx-border-color: lightgray");
        }));
    }


    public void forgotMyPassword(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, true, menuBox.getBoundsInParent().getCenterX(), 1, 2, 1);
        menuFadeTransition.play();
    }

    public void loginMenu(MouseEvent mouseEvent) throws Exception {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, true, menuBox.getBoundsInParent().getCenterX(), 2, 3, 1);
        menuFadeTransition.play();
        resetLoginCaptcha();
    }

    private void setTimer() {
        if (loginPasswordTimer != null)
            loginPasswordTimer.cancel();
        loginPasswordTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (controller.getTimerCount() > 0) {
                            ((Label) getLoginChild(9)).setText("please try " + controller.getTimerCount() + " seconds later!");
                            controller.setTimerCount(controller.getTimerCount() - 1);
                        }
                        else if (controller.getTimerCount() == 0) {
                            ((Label) getLoginChild(9)).setText("");
                            controller.setTimerCount(-1);
                        }
                    }
                });
            }
        };
        loginPasswordTimer.schedule(timerTask, 0, 1000);
    }

    public void setLoginPasswordTimer(Timer loginPasswordTimer) {
        this.loginPasswordTimer = loginPasswordTimer;
    }

    public void signUpMenu(MouseEvent mouseEvent) throws Exception {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, false, menuBox.getBoundsInParent().getCenterX(), 4, 3, 1);
        menuFadeTransition.play();
        resetSignupCaptcha();
    }

    public void signUpBack(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, true, menuBox.getBoundsInParent().getCenterX(), 3, 4, 1);
        menuFadeTransition.play();
        emptyFields(signUpMenu);
        if (signUpMenu.getChildren().contains(sloganField))
            hideSlogan();
    }

    public void loginBack(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, false, menuBox.getBoundsInParent().getCenterX(), 3, 2, 1);
        menuFadeTransition.play();
        emptyFields(loginMenu);
    }

    private void addUsernameListener() {
        TextField usernameLogin = (TextField) getLoginChild(0);
        Text usernameLoginCheck = (Text) getLoginChild(1);
        TextField usernameSignUp = (TextField) getSignupChild(0);
        Text usernameSignUpCheck = (Text) getSignupChild(1);
        PasswordField passwordField = (PasswordField) getLoginChild(2);
        TextField passwordTextField = (TextField) getLoginChild(3);
        Text passwordLoginCheck = (Text) getLoginChild(4);
        usernameSignUp.textProperty().addListener(((observableValue, s, t1) -> {
            if (!User.isUsernameValid(usernameSignUp.getText()) && !usernameSignUp.getText().isEmpty()) {
                usernameSignUpCheck.setText("Invalid username format!");
                usernameSignUp.setStyle("-fx-border-color: red");
            } else if (controller.usernameExistenceCheck(usernameSignUp.getText()) != null) {
                usernameSignUpCheck.setText("Username exists!");
                usernameSignUp.setStyle("-fx-border-color: red");
            } else {
                usernameSignUpCheck.setText("");
                usernameSignUp.setStyle("-fx-border-color: lightgray");
            }
        }));
        usernameLogin.textProperty().addListener(((observableValue, s, t1) -> {
            ((Label) getLoginChild(9)).setText("");
            controller.setTimerCount(-1);
            if (controller.doeHaveTimer(t1))
                setTimer();
            else if (loginPasswordTimer != null)
                loginPasswordTimer.cancel();
            passwordLoginCheck.setText("");
            passwordField.setStyle("-fx-border-color: lightgray");
            passwordTextField.setStyle("-fx-border-color: lightgray");
            if (controller.usernameExistenceCheck(usernameLogin.getText()) == null && !usernameLogin.getText().isEmpty()) {
                usernameLoginCheck.setText("Username not found!");
                usernameLogin.setStyle("-fx-border-color: red");
            } else {
                usernameLoginCheck.setText("");
                usernameLogin.setStyle("-fx-border-color: lightgray");
            }
        }));
    }

    private void passwordVisibilityToggle() {
        passwordFieldBinding((PasswordField) getLoginChild(2), (TextField) getLoginChild(3), showHideLogin);
        passwordFieldBinding((PasswordField) getSignupChild(2), (TextField) getSignupChild(4), showHideSignUp);
        passwordFieldBinding((PasswordField) getSignupChild(3), (TextField) getSignupChild(5), showHideSignUp);
        passwordFieldBinding((PasswordField) getEnteringNewPasswordChild(1), (TextField) getEnteringNewPasswordChild(3), showHideNewPassword);
        passwordFieldBinding((PasswordField) getEnteringNewPasswordChild(2), (TextField) getEnteringNewPasswordChild(4), showHideNewPassword);
    }

    private void passwordFieldBinding(PasswordField passwordField, TextField textField, CheckBox checkBox) {
        passwordField.managedProperty().bind(checkBox.selectedProperty().not());
        passwordField.visibleProperty().bind(checkBox.selectedProperty().not());
        textField.managedProperty().bind(checkBox.selectedProperty());
        textField.visibleProperty().bind(checkBox.selectedProperty());
        textField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    private void addPasswordListener(PasswordField passwordFieldSignUp, PasswordField passwordConfirmFieldSignUp, TextField passwordTextFieldSignup,
                                     TextField passwordConfirmTextFieldSignup, Text passwordSignUpCheck, Text passwordConfirmSignUpCheck) {
        passwordFieldSignUp.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                passwordSignUpCheck.setText("");
                passwordFieldSignUp.setStyle("-fx-border-color: lightgray");
                passwordTextFieldSignup.setStyle("-fx-border-color: lightgray");
            } else {
                switch (User.isPasswordStrong(t1)) {
                    case WEAK_PASSWORD:
                        passwordSignUpCheck.setText("Weak");
                        passwordSignUpCheck.setFill(Color.RED);
                        passwordFieldSignUp.setStyle("-fx-border-color: red");
                        passwordTextFieldSignup.setStyle("-fx-border-color: red");
                        break;
                    case MODERATE_PASSWORD:
                        passwordSignUpCheck.setText("Moderate");
                        passwordSignUpCheck.setFill(Color.ORANGE);
                        passwordFieldSignUp.setStyle("-fx-border-color: orange");
                        passwordTextFieldSignup.setStyle("-fx-border-color: orange");
                        break;
                    case STRONG_PASSWORD:
                        passwordSignUpCheck.setText("Strong");
                        passwordSignUpCheck.setFill(Color.GREEN);
                        passwordFieldSignUp.setStyle("-fx-border-color: green");
                        passwordTextFieldSignup.setStyle("-fx-border-color: green");
                        break;
                }
            }
            if (t1.equals(passwordConfirmFieldSignUp.getText())) {
                passwordConfirmSignUpCheck.setText("");
                passwordConfirmFieldSignUp.setStyle("-fx-border-color: lightgray");
                passwordConfirmTextFieldSignup.setStyle("-fx-border-color: lightgray");
            } else {
                passwordConfirmSignUpCheck.setText("Passwords do not match!");
                passwordConfirmFieldSignUp.setStyle("-fx-border-color: red");
                passwordConfirmTextFieldSignup.setStyle("-fx-border-color: red");
            }
        }));
        passwordConfirmFieldSignUp.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.equals(passwordFieldSignUp.getText())) {
                passwordConfirmSignUpCheck.setText("");
                passwordConfirmFieldSignUp.setStyle("-fx-border-color: lightgray");
                passwordConfirmTextFieldSignup.setStyle("-fx-border-color: lightgray");
            } else {
                passwordConfirmSignUpCheck.setText("Passwords do not match!");
                passwordConfirmFieldSignUp.setStyle("-fx-border-color: red");
                passwordConfirmTextFieldSignup.setStyle("-fx-border-color: red");
            }
        }));
    }

    private void addSignUpPasswordListener() {
        PasswordField passwordFieldSignUp = (PasswordField) getSignupChild(2);
        PasswordField passwordConfirmFieldSignUp = (PasswordField) getSignupChild(3);
        TextField passwordTextFieldSignup = (TextField) getSignupChild(4);
        TextField passwordConfirmTextFieldSignup = (TextField) getSignupChild(5);
        Text passwordSignUpCheck = (Text) getSignupChild(6);
        Text passwordConfirmSignUpCheck = (Text) getSignupChild(7);
        addPasswordListener(passwordFieldSignUp, passwordConfirmFieldSignUp, passwordTextFieldSignup, passwordConfirmTextFieldSignup, passwordSignUpCheck, passwordConfirmSignUpCheck);
    }

    private void addNewPasswordListener() {
        PasswordField passwordField = (PasswordField) getEnteringNewPasswordChild(1);
        PasswordField passwordConfirmField = (PasswordField) getEnteringNewPasswordChild(2);
        TextField passwordTextField = (TextField) getEnteringNewPasswordChild(3);
        TextField passwordConfirmTextField = (TextField) getEnteringNewPasswordChild(4);
        Text passwordCheck = (Text) getEnteringNewPasswordChild(5);
        Text passwordConfirmCheck = (Text) getEnteringNewPasswordChild(6);
        addPasswordListener(passwordField, passwordConfirmField, passwordTextField, passwordConfirmTextField, passwordCheck, passwordConfirmCheck);
    }

    private void addEmailListener() {
        TextField emailField = (TextField) getSignupChild(10);
        Text emailCheck = (Text) getSignupChild(11);
        emailField.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                emailCheck.setText("");
                emailField.setStyle("-fx-border-color: lightgray");
            } else {
                if (!User.isEmailValid(t1)) {
                    emailCheck.setText("Invalid email format!");
                    emailField.setStyle("-fx-border-color: red");
                } else if (User.getUserByEmail(t1) != null) {
                    emailCheck.setText("Email exists!");
                    emailField.setStyle("-fx-border-color: red");
                } else {
                    emailCheck.setText("");
                    emailField.setStyle("-fx-border-color: lightgray");
                }
            }
        }));
    }

    private void addNickNameListener() {
        TextField nickNameField = (TextField) getSignupChild(12);
        Text nickNameCheck = (Text) getSignupChild(13);
        nickNameField.textProperty().addListener(((observableValue, s, t1) -> {
            nickNameCheck.setText("");
            nickNameField.setStyle("-fx-border-color: lightgray");
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
        for (Node node : gridPane.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.setText("");
                textField.setStyle("-fx-border-color: lightgray");
            }
            if (node instanceof PasswordField) {
                PasswordField passwordField = (PasswordField) node;
                passwordField.setText("");
                passwordField.setStyle("-fx-border-color: lightgray");
            }
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
            if (node instanceof Text) {
                Text text = (Text) node;
                text.setText("");
            }
            if (node instanceof VBox vBox) {
                HBox hBox = (HBox) vBox.getChildren().get(1);
                ((TextField) hBox.getChildren().get(0)).setText("");
                ((TextField) hBox.getChildren().get(0)).setStyle("-fx-border-color: lightgray");
                ((Text) hBox.getChildren().get(1)).setText("");
            }
        }
    }


    public void sloganCheckbox(MouseEvent mouseEvent) {
        if (sloganBox.isSelected()) {
            showSlogan();
            randomSloganButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    sloganField.setText(Slogan.getRandomSlogan().toString());
                }
            });
        } else {
            hideSlogan();
        }
    }

    private void hideSlogan() {
        signUpMenu.getChildren().remove(sloganField);
        signUpMenu.getChildren().remove(sloganCheck);
        signUpMenu.getChildren().remove(randomSloganButton);
        GridPane.setRowIndex(signUpMenu.getChildren().get(15), 9);
        GridPane.setRowIndex(signUpMenu.getChildren().get(16), 10);
        sloganField = null;
    }

    private void showSlogan() {
        sloganField = new TextField();
        sloganField.setPromptText("Slogan");
        sloganCheck = new Text();
        sloganCheck.setFill(Color.RED);
        randomSloganButton = new Button("Random slogan");
        randomSloganButton.setAlignment(Pos.CENTER);
        randomSloganButton.setScaleX(0.7);
        randomSloganButton.setScaleY(0.7);
        randomSloganButton.setPrefWidth(200);
        addSloganListener();
        GridPane.setRowIndex(signUpMenu.getChildren().get(15), 11);
        GridPane.setRowIndex(signUpMenu.getChildren().get(16), 12);
        signUpMenu.add(sloganField, 0, 9);
        signUpMenu.add(sloganCheck, 1, 9);
        signUpMenu.add(randomSloganButton, 0, 10);
    }

    private void addSloganListener() {
        sloganField.textProperty().addListener(((observableValue, s, t1) -> {
            sloganCheck.setText("");
            sloganField.setStyle("-fx-border-color: lightgray");
        }));
    }

    public void resetLoginCaptcha() {
        VBox captcha = (VBox) getLoginChild(7);
        HBox captchaBox = (HBox) captcha.getChildren().get(0);
        ImageView captchaImage = (ImageView) captchaBox.getChildren().get(0);
        captchaImage.setImage(controller.generateCaptcha());
    }

    public void resetSignupCaptcha() {
        VBox captcha = (VBox) getRecoveryQuestionChild(5);
        HBox captchaBox = (HBox) captcha.getChildren().get(0);
        ImageView captchaImage = (ImageView) captchaBox.getChildren().get(0);
        captchaImage.setImage(controller.generateCaptcha());
    }

    private void initializeCaptcha() {
        VBox loginCaptcha = (VBox) getLoginChild(7);
        HBox loginCaptchaBox = (HBox) loginCaptcha.getChildren().get(0);
        VBox signupCaptcha = (VBox) getRecoveryQuestionChild(5);
        HBox signupCaptchaBox = (HBox) signupCaptcha.getChildren().get(0);
        setCaptcha(loginCaptchaBox);
        setCaptcha(signupCaptchaBox);
        resetLoginCaptcha();
    }

    private void setCaptcha(HBox captchaBox) {
        captchaBox.setStyle("-fx-background-color: white;");
        captchaBox.setEffect(Controller.getBorderGlow(Color.LIGHTBLUE, 30));
        ImagePattern imagePattern = new ImagePattern(new Image(LoginMenuGraphics.class.getResource("/Images/refresh.png").toString(), 40, 40, false, false));
        Rectangle refreshImage = (Rectangle) captchaBox.getChildren().get(1);
        refreshImage.setWidth(30);
        refreshImage.setHeight(30);
        refreshImage.setFill(imagePattern);
        refreshImage.setStyle("-fx-background-color: white;");
    }

    public void getInformation(MouseEvent mouseEvent) throws IOException, NoSuchAlgorithmException {
        String username = ((TextField) signUpMenu.getChildren().get(0)).getText();
        String password = ((TextField) signUpMenu.getChildren().get(2)).getText();
        String passwordConfirm = ((TextField) signUpMenu.getChildren().get(3)).getText();
        String email = ((TextField) signUpMenu.getChildren().get(10)).getText();
        String nickname = ((TextField) signUpMenu.getChildren().get(12)).getText();
        String slogan = (sloganField == null) ? null : sloganField.getText();
        signupCheckForEmptyFields(username, password, passwordConfirm, email, nickname, slogan);
        if (controller.getInformation(username, password, passwordConfirm, email, nickname, slogan).equals(Messages.SUCCESS))
            pickRecoveryQuestion();
    }

    private void signupCheckForEmptyFields(String username, String password, String passwordConfirm, String email, String nickname, String slogan) {
        if (username.isBlank()) {
            ((Text) getSignupChild(1)).setText("this field is empty!");
            ((TextField) getSignupChild(0)).setStyle("-fx-border-color: red");
        }
        if (password.isBlank()) {
            ((Text) getSignupChild(6)).setText("this field is empty!");
            ((Text) getSignupChild(6)).setFill(Color.RED);
            ((PasswordField) getSignupChild(2)).setStyle("-fx-border-color: red;");
            ((TextField) getSignupChild(4)).setStyle("-fx-border-color: red;");
        }
        if (passwordConfirm.isBlank()) {
            ((Text) getSignupChild(7)).setText("this field is empty!");
            ((PasswordField) getSignupChild(3)).setStyle("-fx-border-color: red");
            ((TextField) getSignupChild(5)).setStyle("-fx-border-color: red");
        }
        if (email.isBlank()) {
            ((Text) getSignupChild(11)).setText("this field is empty!");
            ((TextField) getSignupChild(10)).setStyle("-fx-border-color: red");
        }
        if (nickname.isBlank()) {
            ((Text) getSignupChild(13)).setText("this field is empty!");
            ((TextField) getSignupChild(12)).setStyle("-fx-border-color: red");
        }
        if (sloganField != null && slogan.isBlank()) {
            sloganCheck.setText("this field is empty!");
            sloganField.setStyle("-fx-border-color: red");
        }
    }

    public void pickRecoveryQuestion() {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, false, menuBox.getBoundsInParent().getCenterX(), 5, 4, 1);
        menuFadeTransition.play();
        recoveryQuestionMenu.add(slogans(), 0, 1);
    }

    private void addSignupCaptchaListener() {
        TextField captchaField = (TextField) ((HBox) ((VBox) getRecoveryQuestionChild(5)).getChildren().get(1)).getChildren().get(0);
        Text captchaCheck = (Text) ((HBox) ((VBox) getRecoveryQuestionChild(5)).getChildren().get(1)).getChildren().get(1);
        captchaField.textProperty().addListener(((observableValue, s, t1) -> {
            captchaCheck.setText("");
            captchaField.setStyle("-fx-border-color: lightgray");
        }));

    }

    public void signUp(MouseEvent mouseEvent) throws IOException, NoSuchAlgorithmException {
        String recoveryQuestion = ((ComboBox) getRecoveryQuestionChild(8)).getValue().toString();
        String recoveryAnswer = ((TextField) getRecoveryQuestionChild(1)).getText();
        String recoveryAnswerConfirm = ((TextField) getRecoveryQuestionChild(2)).getText();
        String captcha = ((TextField) ((HBox) ((VBox) getRecoveryQuestionChild(5)).getChildren().get(1)).getChildren().get(0)).getText();
        recoveryQuestionCheckForEmptyFields(recoveryAnswer, recoveryAnswerConfirm, captcha);
        recoveryQuestionCheckCaptcha(captcha);
        if (controller.signUp(recoveryQuestion, recoveryAnswer, recoveryAnswerConfirm, captcha).equals(Messages.SUCCESS)) {
            emptyFields(signUpMenu);
            MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, true, menuBox.getBoundsInParent().getCenterX(), 3, 5, 2);
            menuFadeTransition.play();
        }
        else
            resetSignupCaptcha();

    }

    private void recoveryQuestionCheckCaptcha(String captcha) {
        if (!captcha.equals(String.valueOf(controller.getCurrentCaptcha()))) {
            ((Text) ((HBox) ((VBox) getRecoveryQuestionChild(5)).getChildren().get(1)).getChildren().get(1)).setText("incorrect captcha!");
            ((TextField) ((HBox) ((VBox) getRecoveryQuestionChild(5)).getChildren().get(1)).getChildren().get(0)).setStyle("-fx-border-color: red");
        }
        else {
            ((Text) ((HBox) ((VBox) getRecoveryQuestionChild(5)).getChildren().get(1)).getChildren().get(1)).setText("");
            ((TextField) ((HBox) ((VBox) getRecoveryQuestionChild(5)).getChildren().get(1)).getChildren().get(0)).setStyle("-fx-border-color: lightgray");
        }
    }

    private void recoveryQuestionCheckForEmptyFields(String recoveryAnswer, String recoveryAnswerConfirm, String captcha) {
        if (recoveryAnswer.isBlank()) {
            ((Text) getRecoveryQuestionChild(3)).setText("this field is empty!");
            ((TextField) getRecoveryQuestionChild(1)).setStyle("-fx-border-color: red");
        }
        if (recoveryAnswerConfirm.isBlank()) {
            ((Text) getRecoveryQuestionChild(4)).setText("this field is empty!");
            ((TextField) getRecoveryQuestionChild(2)).setStyle("-fx-border-color: red");
        }
    }

    private boolean checkRecoveryAnswer() {
        if (recoveryQuestion == null)
            return false;
        User user = User.getUserByUsername(((TextField) getForgetPasswordChild(1)).getText());
        String recoveryAnswer = recoveryAnswerField.getText();
        if (recoveryAnswer.equals(user.getPasswordRecoveryAnswer()))
            return true;
        else {
            recoveryAnswerCheck.setText("incorrect answer!");
            recoveryAnswerField.setStyle("-fx-border-color: red");
            return false;
        }
    }

    private boolean checkNewPasswordField(PasswordField passwordField, PasswordField passwordField1) {
        try {
            return
                    controller.forgotPassword(((TextField) getForgetPasswordChild(1)).getText(), passwordField.getText(), passwordField1.getText()).equals(Messages.SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void addForgetPasswordUsernameListener() {
        TextField usernameField = (TextField) getForgetPasswordChild(1);
        Text usernameCheck = (Text) getForgetPasswordChild(2);
        usernameField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isBlank()) {
                usernameField.setStyle("-fx-border-color: lightgray");
                usernameCheck.setText("");
                setRecoveryQuestion(null);
                return;
            }
            User user = User.getUserByUsername(t1);
            if (user == null) {
                usernameField.setStyle("-fx-border-color: red");
                usernameCheck.setText("username not found!");
            } else {
                usernameField.setStyle("-fx-border-color: lightgray");
                usernameCheck.setText("");
            }
            setRecoveryQuestion(user);
        });
    }

    private void setRecoveryQuestion(User user) {
        if (user != null) {
            recoveryQuestion = new Label(user.getPasswordRecoveryQuestion().toString());
            recoveryQuestion.setTextFill(Color.DARKORANGE);
            recoveryQuestion.setStyle("-fx-font-size: 15");
            recoveryAnswerCheck = new Text();
            recoveryAnswerCheck.setFill(Color.RED);
            recoveryAnswerField = new TextField();
            recoveryAnswerField.setPromptText("answer");
            recoveryAnswerField.setPrefWidth(120);
            addRecoveryAnswerListener();
            forgetPassword.add(recoveryQuestion, 0, 2);
            forgetPassword.add(recoveryAnswerField, 0, 3);
            forgetPassword.add(recoveryAnswerCheck, 1, 3);
            GridPane.setRowIndex(forgetPassword.getChildren().get(3), 4);
            GridPane.setRowIndex(forgetPassword.getChildren().get(4), 5);
        } else if (recoveryQuestion != null){
            forgetPassword.getChildren().remove(recoveryQuestion);
            forgetPassword.getChildren().remove(recoveryAnswerField);
            forgetPassword.getChildren().remove(recoveryAnswerCheck);
            GridPane.setRowIndex(forgetPassword.getChildren().get(3), 2);
            GridPane.setRowIndex(forgetPassword.getChildren().get(4), 3);
            recoveryQuestion = null;
            recoveryAnswerField = null;
        }
    }

    private void addRecoveryAnswerListener() {
        recoveryAnswerField.textProperty().addListener(((observableValue, s, t1) -> {
            recoveryAnswerCheck.setText("");
            recoveryAnswerField.setStyle("-fx-border-color: lightgray");
        }));
    }

    private void addSignupRecoveryAnswerListener() {
        TextField answerField = (TextField) getRecoveryQuestionChild(1);
        TextField answerConfirmField = (TextField) getRecoveryQuestionChild(2);
        Text answerCheck = (Text) getRecoveryQuestionChild(3);
        Text answerConfirmCheck = (Text) getRecoveryQuestionChild(4);
        answerField.textProperty().addListener(((observableValue, s, t1) -> {
            answerCheck.setText("");
            answerField.setStyle("-fx-border-color: lightgray");
            if (!answerConfirmField.getText().equals(t1)) {
                answerConfirmCheck.setText("answer do not match!");
                answerConfirmField.setStyle("-fx-border-color: red");
            } else {
                answerConfirmCheck.setText("");
                answerConfirmField.setStyle("-fx-border-color: lightgray");
            }

        }));
        answerConfirmField.textProperty().addListener(((observableValue, s, t1) -> {
            if (!answerField.getText().equals(t1)) {
                answerConfirmCheck.setText("answer do not match!");
                answerConfirmField.setStyle("-fx-border-color: red");
            } else {
                answerConfirmCheck.setText("");
                answerConfirmField.setStyle("-fx-border-color: lightgray");
            }

        }));
    }

    private ComboBox slogans() {
        ComboBox comboBox = new ComboBox<>();
        comboBox.getItems().clear();
        for (RecoveryQuestion recoveryQuestion : RecoveryQuestion.values())
            comboBox.getItems().add(recoveryQuestion.toString());
        comboBox.setValue(RecoveryQuestion.values()[0].toString());
        return comboBox;
    }


    private Node getSignupChild(int index) {
        return signUpMenu.getChildren().get(index);
    }

    private Node getLoginChild(int index) {
        return loginMenu.getChildren().get(index);
    }

    private Node getRecoveryQuestionChild(int index) {
        return recoveryQuestionMenu.getChildren().get(index);
    }

    private Node getForgetPasswordChild(int index) {
        return forgetPassword.getChildren().get(index);
    }

    private Node getEnteringNewPasswordChild(int index) {
        return enteringNewPassword.getChildren().get(index);
    }

    public void backToLogin(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, false, menuBox.getBoundsInParent().getCenterX(), 2, 1, 1);
        menuFadeTransition.play();
        emptyFields(forgetPassword);
        setRecoveryQuestion(null);
    }

    public void enteringNewPassword(MouseEvent mouseEvent) {
        forgetPasswordCheckForEmptyFields();
        if (checkRecoveryAnswer()) {
            MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, true, menuBox.getBoundsInParent().getCenterX(), 0, 1, 1);
            menuFadeTransition.play();
        }
    }

    private void forgetPasswordCheckForEmptyFields() {
        TextField usernameFiled = (TextField) getForgetPasswordChild(1);
        Text usernameCheck = (Text) getForgetPasswordChild(2);
        if (usernameFiled.getText().isBlank()) {
            usernameFiled.setStyle("-fx-border-color: red");
            usernameCheck.setText("this field is empty!");
        }
    }

    public void passwordChange(MouseEvent mouseEvent) {
        PasswordField passwordField = (PasswordField) getEnteringNewPasswordChild(1);
        PasswordField passwordConfirmField = (PasswordField) getEnteringNewPasswordChild(2);
        enteringNewPasswordCheckForEmptyFields(passwordField.getText(), passwordConfirmField.getText());
        if (checkNewPasswordField(passwordField, passwordConfirmField)) {
            MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, false, menuBox.getBoundsInParent().getCenterX(), 2, 0, 2);
            menuFadeTransition.play();
            emptyFields(enteringNewPassword);
            emptyFields(forgetPassword);
            setRecoveryQuestion(null);
        }
    }

    private void enteringNewPasswordCheckForEmptyFields(String password, String passwordConfirm) {
        if (password.isBlank()) {
            ((Text) getEnteringNewPasswordChild(5)).setText("this field is empty!");
            ((PasswordField) getEnteringNewPasswordChild(1)).setStyle("-fx-border-color: red");
            ((TextField) getEnteringNewPasswordChild(3)).setStyle("-fx-border-color: red");
        }
        if (passwordConfirm.isBlank()) {
            ((Text) getEnteringNewPasswordChild(6)).setText("this field is empty!");
            ((PasswordField) getEnteringNewPasswordChild(2)).setStyle("-fx-border-color: red");
            ((TextField) getEnteringNewPasswordChild(4)).setStyle("-fx-border-color: red");
        }

    }

    public void backToForgetPassword(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, false, menuBox.getBoundsInParent().getCenterX(), 1, 0, 1);
        menuFadeTransition.play();
        emptyFields(enteringNewPassword);
    }

    public void login(MouseEvent mouseEvent) throws Exception {
        String username = ((TextField) getLoginChild(0)).getText();
        String password = ((PasswordField) getLoginChild(2)).getText();
        String captcha = ((TextField) ((HBox) ((VBox) getLoginChild(7)).getChildren().get(1)).getChildren().get(0)).getText();
        boolean stayLoggedIn = ((CheckBox) getLoginChild(8)).isSelected();
        loginCheckForEmptyFields(username, password, captcha, stayLoggedIn);
        checkPassword(username, password);
        loginCheckCaptcha(captcha);
        switch (controller.login(username, password, stayLoggedIn, captcha)) {
            case WAIT_FOR_LOGIN -> {
                setTimer();
                resetLoginCaptcha();
            }
            case SUCCESS -> {
                new MainMenuGraphics().start(Main.stage);
            }
            default -> resetLoginCaptcha();
        }
    }

    private void checkPassword(String username, String password) throws NoSuchAlgorithmException {
        User user = User.getUserByUsername(username);
        if (user != null && !user.checkPassword(password)) {
            ((Text) getLoginChild(4)).setText("incorrect password!");
            ((PasswordField) getLoginChild(2)).setStyle("-fx-border-color: red");
            ((TextField) getLoginChild(3)).setStyle("-fx-border-color: red");
        }
    }

    private void loginCheckCaptcha(String captcha) {
        if (!captcha.equals(String.valueOf(controller.getCurrentCaptcha()))) {
            ((Text) ((HBox) ((VBox) getLoginChild(7)).getChildren().get(1)).getChildren().get(1)).setText("incorrect captcha!");
            ((TextField) ((HBox) ((VBox) getLoginChild(7)).getChildren().get(1)).getChildren().get(0)).setStyle("-fx-border-color: red");
        }
        else {
            ((Text) ((HBox) ((VBox) getLoginChild(7)).getChildren().get(1)).getChildren().get(1)).setText("");
            ((TextField) ((HBox) ((VBox) getLoginChild(7)).getChildren().get(1)).getChildren().get(0)).setStyle("-fx-border-color: lightgray");
        }
    }

    private void loginCheckForEmptyFields(String username, String password, String captcha, boolean stayLoggedIn) {
        if (username.isBlank()) {
            ((Text) getLoginChild(1)).setText("this field is empty!");
            ((TextField) getLoginChild(0)).setStyle("-fx-border-color: red");
        }
        if (password.isBlank()) {
            ((Text) getLoginChild(4)).setText("this field is empty!");
            ((PasswordField) getLoginChild(2)).setStyle("-fx-border-color: red");
            ((TextField) getLoginChild(3)).setStyle("-fx-border-color: red");
        }
    }

    public void backToSignUp(MouseEvent mouseEvent) {
        MenuFadeTransition menuFadeTransition = new MenuFadeTransition((Pane) Main.stage.getScene().getRoot(), menuBox, true, menuBox.getBoundsInParent().getCenterX(), 4, 5, 1);
        menuFadeTransition.play();
    }
}
