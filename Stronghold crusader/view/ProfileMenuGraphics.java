package view;

import controller.Controller;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public class ProfileMenuGraphics extends Application {
    private final ProfileMenuController controller = new ProfileMenuController();
    private static Stage stage;
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
    private TextField oldPasswordTextField;
    @FXML
    private TextField textFieldToShowPassword;
    @FXML
    private TextField textFieldToShowConfirmation;
    @FXML
    private TextField textFieldToShowOldPassword;

    @FXML
    private Label confirmationText;
    @FXML
    private Label oldPasswordText;
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
    private Button chooseFromDeviceButton;
    @FXML
    private Button chooseAnExistingAvatarButton;


    @FXML
    private Rectangle avatar;

    @FXML
    private CheckBox showPasswordCheckBox;


    @Override
    public void start(Stage stage) throws Exception {
        ProfileMenuGraphics.stage = stage;
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
        addNicknameListener();
        addPasswordFieldsListener();

    }

    private void addNicknameListener() {
        nicknameTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newNickname) {
                if (newNickname == null || newNickname.isEmpty()) nicknameWarningLabel.setText("enter nickname");
                else nicknameWarningLabel.setText("");
            }
        });
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
        textFieldToShowPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String newPassword) {
                passwordTextField.setText(newPassword);
            }
        });
        confirmationTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String confirmation) {
                textFieldToShowConfirmation.setText(confirmation);
                if (!confirmation.equals(passwordTextField.getText())) {
                    confirmationWarningLabel.setText("Passwords do not match!");
                } else {
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
        textFieldToShowOldPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldText, String oldPassword) {
                oldPasswordTextField.setText(oldPassword);
            }
        });
        oldPasswordTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldText, String oldPassword) {
                textFieldToShowOldPassword.setText(oldPassword);
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
//        Image image = new Image(ProfileMenuGraphics.class.getResource("/Images/Avatars/" + controller.getAvatarName()).toString(),
//                Main.screenWidth, Main.getScreenHeight, false, false);
//        avatar.setFill(new ImagePattern(image));
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
        chooseAnExistingAvatarButton.setVisible(false);
        chooseFromDeviceButton.setVisible(false);

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
        oldPasswordTextField.setText("");
        oldPasswordTextField.setVisible(false);
        textFieldToShowPassword.setVisible(false);
        textFieldToShowConfirmation.setVisible(false);
        textFieldToShowOldPassword.setVisible(false);
    }

    private void resetCheckBoxes() {
        showPasswordCheckBox.setVisible(false);
    }

    private void resetLabels() {
        confirmationText.setVisible(false);
        oldPasswordText.setVisible(false);
        nicknameWarningLabel.setText("");
        passwordWarningLabel.setText("");
        confirmationWarningLabel.setText("");
        emailWarningLabel.setText("");
        usernameWarningLabel.setText("");
    }

    private Image getBackGroundImage() {
        Image image = new Image(LoginMenuGraphics.class.getResource("/Images/Background/09.jpg").toString(),
                Main.screenWidth, Main.screenHeight, false, false);
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
        oldPasswordTextField.setVisible(true);
        showPasswordCheckBox.setVisible(true);
        showPasswordCheckBox.setSelected(false);
        confirmationText.setVisible(true);
        confirmationWarningLabel.setVisible(true);
        oldPasswordText.setVisible(true);
    }


    public void submitUsername(MouseEvent mouseEvent) throws IOException, NoSuchAlgorithmException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("changing username");
        switch (controller.changeUsername(usernameTextField.getText())) {
            case CHANGE_USERNAME_SUCCESSFUL:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("changing was successful");
                break;
            case USERNAME_EXISTS:
                alert.setHeaderText("username exists");
                break;
            case INVALID_USERNAME:
                alert.setHeaderText("username format is invalid");
                break;
            case NEW_USERNAME_IS_CURRENT_USERNAME:
                resetFields();
                return;
        }
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
        resetFields();
    }

    @FXML
    private void submitEmail(MouseEvent mouseEvent) throws IOException, NoSuchAlgorithmException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("changing email");
        switch (controller.changeEmail(emailTextField.getText())) {
            case CHANGE_EMAIL_SUCCESSFUL:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("changing was successful");
                break;
            case EMAIL_EXISTS:
                alert.setHeaderText("email exists");
                break;
            case INVALID_EMAIL_FORMAT:
                alert.setHeaderText("email format is invalid");
                break;
            case NEW_EMAIL_IS_CURRENT_EMAIL:
                resetFields();
                return;
        }
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
        resetFields();

    }

    public void submitSlogan(MouseEvent mouseEvent) throws IOException, NoSuchAlgorithmException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("changing slogan");
        controller.changeSlogan(sloganTextField.getText());
        alert.setHeaderText("changing was successful");
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
        resetFields();

    }

    public void submitNickname(MouseEvent mouseEvent) throws IOException, NoSuchAlgorithmException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("changing nickname");
        switch (controller.changeNickname(nicknameTextField.getText())) {
            case EMPTY_FIELD:
                alert.setHeaderText("enter nickname");
                break;
            case CHANGE_NICKNAME_SUCCESSFUL:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("changing was successful");
                break;
        }
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
        resetFields();
    }


    @FXML
    private void submitPassword(MouseEvent mouseEvent) throws IOException, NoSuchAlgorithmException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("changing password");
        switch (controller.changePassword(oldPasswordTextField.getText(),
                passwordTextField.getText(),
                confirmationTextField.getText())) {
            case INCORRECT_PASSWORD:
                alert.setHeaderText("old password is wrong!");
                break;
            case PASSWORD_NOT_CONFIRMED:
                alert.setHeaderText("passwords are not the same!");
                break;
            case CHANGE_PASSWORD_SUCCESSFUL:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("changing was successful!");
                break;
            case MODERATE_PASSWORD:
            case WEAK_PASSWORD:
                alert.setHeaderText("password is not the strong!");
                break;
        }
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
        resetFields();
    }


    @FXML
    private void showPasswordHandler(ActionEvent actionEvent) {
        if (showPasswordCheckBox.isSelected()) {
            textFieldToShowPassword.setVisible(true);
            passwordTextField.setVisible(false);
            textFieldToShowConfirmation.setVisible(true);
            confirmationTextField.setVisible(false);
            textFieldToShowOldPassword.setVisible(true);
            oldPasswordTextField.setVisible(false);
        } else {
            textFieldToShowPassword.setVisible(false);
            passwordTextField.setVisible(true);
            textFieldToShowConfirmation.setVisible(false);
            confirmationTextField.setVisible(true);
            textFieldToShowOldPassword.setVisible(false);
            oldPasswordTextField.setVisible(true);
        }
    }

    @FXML
    private void enableChoosingAvatar() {
        chooseAnExistingAvatarButton.setVisible(true);
        chooseFromDeviceButton.setVisible(true);
    }

    public void chooseAvatarFromDevice(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        stage.setFullScreen(false);
        File avatarFile = fileChooser.showOpenDialog(stage);
        stage.setFullScreen(true);
        if (avatarFile != null) {
            avatarFile = saveChosenAvatarToImages(avatarFile);
            controller.setAvatarName(avatarFile.getName());
            setAvatar();
        }
        resetFields();
    }

    private File saveChosenAvatarToImages(File file) {
        try{
            File destination = new File(ProfileMenuGraphics.class.getResource
                    ("/Images/Avatars").toString().substring("file:".length())
                    + file.getName());
            Integer numberToAdd = 2;
            while (destination.exists() && !destination.equals(file)) {
                destination = new File(ProfileMenuGraphics.class.getResource
                        ("/Images/Avatars").toString().substring("file:".length())
                        + numberToAdd + "_" + file.getName());
                numberToAdd++;
            }
            Files.copy(file.toPath(), destination.toPath());
            return destination;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void chooseAnExistingAvatar(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(
                ProfileMenuGraphics.class.getResource
                        ("/Images/Avatars").toString().substring("file:/".length())));
        stage.setFullScreen(false);
        File avatarFile = fileChooser.showOpenDialog(stage);
        stage.setFullScreen(true);
        if (avatarFile != null) {
            controller.setAvatarName(avatarFile.getName());
            setAvatar();
        }
        resetFields();
    }
    // TODO: 5/28/2023 captcha
    // TODO: 5/28/2023 add score board
}
