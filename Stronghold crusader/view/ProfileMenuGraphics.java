package view;

import controller.LoginMenuController;
import controller.ProfileMenuController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static controller.Messages.CHANGE_USERNAME_SUCCESSFUL;

public class ProfileMenuGraphics extends Application {
    private final ProfileMenuController controller = new ProfileMenuController();
    private Stage stage;
    private Scene scene;
    private static Pane pane;

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField sloganTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmationTextField;
    @FXML
    private TextField textFieldToShowPassword;
    @FXML
    private TextField textFieldToShowConfirmation;

    @FXML
    private Label confirmationText;
    @FXML
    private Label usernameWarningLabel;
    @FXML
    private Label nicknameWarningLabel;
    @FXML
    private Label emailWarningLabel;
    @FXML
    private Label passwordWarningLabel;
    @FXML
    private Label confirmationWarningLabel;

    @FXML
    private Button submitUsername;
    @FXML
    private Button submitNickname;
    @FXML
    private Button submitEmail;
    @FXML
    private Button submitSlogan;
    @FXML
    private Button submitPassword;

    @FXML
    private Button changeUsername;
    @FXML
    private Button changeNickname;
    @FXML
    private Button changeEmail;
    @FXML
    private Button changeSlogan;
    @FXML
    private Button changePassword;

    @FXML
    private Rectangle avatar;

    @FXML
    private CheckBox showPasswordCheckBox;


    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        URL url = LoginMenuGraphics.class.getResource("/FXML/ProfileMenu.fxml");
        pane = FXMLLoader.load(url);
        Background background = new Background(new BackgroundImage(getBackGroundImage(),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT));
        pane.setBackground(background);

        scene = new Scene(pane);
        scene.getStylesheets().add(ProfileMenuGraphics.class.getResource("/CSS/ProfileMenu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void initialize() {
        resetFields();
        setAvatar();
        addTextFieldsListeners();
    }

    private void addTextFieldsListeners() {
        addUsernameFieldListener();
        addEmailFieldListener();
        addPasswordFieldsListener();

    }

    private void addPasswordFieldsListener() {
        passwordTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newPassword) {
                textFieldToShowPassword.setText(newPassword);
                if (newPassword == null || newPassword.isEmpty()) {
                    passwordWarningLabel.setText("enter password");
                    passwordWarningLabel.setStyle("-fx-text-fill: red");
                } else {
                    switch (User.isPasswordStrong(newPassword)) {
                        case WEAK_PASSWORD:
                            passwordWarningLabel.setText("Weak");
                            passwordWarningLabel.setStyle("-fx-text-fill: red");
                            break;
                        case MODERATE_PASSWORD:
                            passwordWarningLabel.setText("Moderate");
                            passwordWarningLabel.setStyle("-fx-text-fill: orange");
                            break;
                        case STRONG_PASSWORD:
                            passwordWarningLabel.setText("Strong");
                            passwordWarningLabel.setStyle("-fx-text-fill: green");
                            break;
                    }
                }
            }
        });
        confirmationTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String confirmation) {
                textFieldToShowConfirmation.setText(confirmation);
                if (!confirmation.equals(passwordTextField.getText())) {
                    confirmationWarningLabel.setText("Passwords do not match!");
                }
                else {
                    confirmationWarningLabel.setText("");
                }
            }
        });
        textFieldToShowConfirmation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String confirmation) {
                confirmationTextField.setText(confirmation);
            }
        });
        textFieldToShowPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newPassword) {
                passwordTextField.setText(newPassword);
            }
        });
    }

    private void addEmailFieldListener() {
        emailTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newEmail) {
                if (newEmail == null || newEmail.isEmpty()) {
                    emailWarningLabel.setText("enter email");
                } else if (!User.isEmailValid(newEmail)) emailWarningLabel.setText("Invalid email format!");
                else if (controller.doesEmailExist(newEmail))
                    emailWarningLabel.setText("this email is used");
                else emailWarningLabel.setText("");
            }
        });
    }

    private void addUsernameFieldListener() {
        usernameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue,
                                String oldUsername, String newUsername) {
                if (newUsername == null || newUsername.isEmpty()) {
                    usernameWarningLabel.setText("enter username");
                } else if (!User.isUsernameValid(newUsername)) usernameWarningLabel.setText("Invalid username format!");
                else if (controller.doesUsernameExist(newUsername))
                    usernameWarningLabel.setText("this username is used");
                else usernameWarningLabel.setText("");
            }
        });
    }

    private void setAvatar() {
        Image image = new Image(ProfileMenuGraphics.class.getResource("/Images/Avatars/" + controller.getAvatarName()).toString(),
                Main.screenWidth, Main.getScreenHeight, false, false);
        avatar.setFill(new ImagePattern(image));
    }

    @FXML
    private void resetFields() {
        resetTextFields();
        resetSubmitButtons();
        resetCheckBoxes();
        resetLabels();
    }

    private void resetSubmitButtons() {
        submitUsername.setVisible(false);
        submitNickname.setVisible(false);
        submitEmail.setVisible(false);
        submitSlogan.setVisible(false);
        submitPassword.setVisible(false);

    }

    private void resetTextFields() {
        usernameTextField.setText(controller.getUsername());
        usernameTextField.setDisable(true);
        nicknameTextField.setText(controller.getNickname());
        nicknameTextField.setDisable(true);
        emailTextField.setText(controller.getEmail());
        emailTextField.setDisable(true);
        sloganTextField.setText(controller.getSlogan());
        sloganTextField.setDisable(true);
        passwordTextField.setText("");
        passwordTextField.setDisable(true);
        confirmationTextField.setText("");
        confirmationTextField.setVisible(false);
        textFieldToShowPassword.setVisible(false);
        textFieldToShowConfirmation.setVisible(false);
    }

    private void resetCheckBoxes() {
        showPasswordCheckBox.setVisible(false);
    }

    private void resetLabels() {
        confirmationText.setVisible(false);
        nicknameWarningLabel.setText("");
        passwordWarningLabel.setText("");
        confirmationWarningLabel.setText("");
        emailWarningLabel.setText("");
        usernameWarningLabel.setText("");
    }

    private Image getBackGroundImage() {
        Image image = new Image(LoginMenuGraphics.class.getResource("/Images/Background/09.jpg").toString(),
                Main.screenWidth, Main.getScreenHeight, false, false);
        return image;
    }

    @FXML
    private void enableUsernameField(MouseEvent mouseEvent) {
        resetFields();
        usernameTextField.setDisable(false);
        submitUsername.setVisible(true);
    }

    @FXML
    public void enableEmailField(MouseEvent mouseEvent) {
        resetFields();
        emailTextField.setDisable(false);
        submitEmail.setVisible(true);
    }

    @FXML
    private void enableNickNameField(MouseEvent mouseEvent) {
        resetFields();
        nicknameTextField.setDisable(false);
        submitNickname.setVisible(true);
    }
    @FXML
    private void enableSloganField(MouseEvent mouseEvent) {
        resetFields();
        sloganTextField.setDisable(false);
        submitSlogan.setVisible(true);
    }

    @FXML
    private void enablePasswordField(MouseEvent mouseEvent) {
        resetFields();
        passwordTextField.setDisable(false);
        passwordTextField.setVisible(true);
        submitPassword.setVisible(true);
        confirmationTextField.setVisible(true);
        showPasswordCheckBox.setVisible(true);
        showPasswordCheckBox.setSelected(false);
        confirmationText.setVisible(true);
        confirmationWarningLabel.setVisible(true);
    }


    public void submitUsername(MouseEvent  mouseEvent) throws IOException, NoSuchAlgorithmException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (controller.changeUsername(usernameTextField.getText())){
            case CHANGE_USERNAME_SUCCESSFUL:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("changing username");
                alert.setHeaderText("changing was successful");
                break;
            case USERNAME_EXISTS:
                alert.setTitle("changing username");
                alert.setHeaderText("username exists");
                break;
            case INVALID_USERNAME:
                alert.setTitle("changing username");
                alert.setHeaderText("username format is invalid");
                break;
            case NEW_USERNAME_IS_CURRNET_USERNAME:
                resetFields();
                return;
        }
        alert.showAndWait(); // todo change alert, it close game and reopen
        resetFields();
    }

    public void submitEmail(MouseEvent mouseEvent) {

    }
    public void submitSlogan(MouseEvent mouseEvent) {

    }

    public void submitNickname(MouseEvent mouseEvent) {

    }


    @FXML
    private void submitPassword(MouseEvent mouseEvent) {
    }


    @FXML
    private void showPasswordHandler(ActionEvent actionEvent) {
        if (showPasswordCheckBox.isSelected()) {
            textFieldToShowPassword.setVisible(true);
            passwordTextField.setVisible(false);

            textFieldToShowConfirmation.setVisible(true);
            confirmationTextField.setVisible(false);
        } else {
            textFieldToShowPassword.setVisible(false);
            passwordTextField.setVisible(true);

            textFieldToShowConfirmation.setVisible(false);
            confirmationTextField.setVisible(true);
        }
    }

}
