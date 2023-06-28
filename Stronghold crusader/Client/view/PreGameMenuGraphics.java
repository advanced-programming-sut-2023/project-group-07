package Client.view;

import Client.controller.Controller;
import Client.controller.CreateMapMenuController;
import Client.controller.GameMenuController;
import Client.model.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PreGameMenuGraphics extends Application implements Initializable {
    @FXML
    private ComboBox numberOfPlayers;
    @FXML
    private ComboBox golds;
    @FXML
    private GridPane usernamesGridPane;
    @FXML
    private BorderPane menuPane;
    ScrollPane scrollPane;
    GridPane mapGrid = new GridPane();
    private static final int mapMonitorWidth = 550;
    private static final int mapMonitorHeight = 250;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private ArrayList<Label> usernameLabels = new ArrayList<>();
    private ArrayList<TextField> usernameFields = new ArrayList<>();
    private ArrayList<Text> errorTexts = new ArrayList<>();
    private int mapIndex = 0;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(Controller.getBorderPane("/FXML/PreGameMenu.fxml")));
        stage.setFullScreen(true);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialGolds();
        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(512);
        scrollPane.setLayoutY(100);
        scrollPane.setPrefWidth(mapMonitorWidth);
        scrollPane.setPrefHeight(mapMonitorHeight);
        initExistingMaps();
        scrollPane.setContent(mapGrid);
        menuPane.setCenter(scrollPane);
        setNumberOfPlayers();
    }

    private void initialGolds() {
        golds.getItems().clear();
        golds.getItems().addAll("2000", "5000", "10000", "40000");
        golds.setValue("5000");
    }

    public void initExistingMaps() {
        setMapGrid();
        ArrayList<Map> maps = Map.getMaps();
        System.out.println(maps.size());
        int counter = 1;
        for (Map map : maps) {
            Text number = new Text(counter + ".");
            Text name = new Text(map.getName());
            Text numberOfPlayers = new Text(String.valueOf(map.getNumberOfPlayers()));
            CheckBox checkBox = new CheckBox();
            checkBoxes.add(checkBox);
            if (counter == 1)
                checkBox.setSelected(true);
            setCheckBoxOnAction(checkBox);
            mapGrid.add(checkBox, 0, counter);
            mapGrid.add(number, 1 ,counter);
            mapGrid.add(name, 2, counter);
            mapGrid.add(numberOfPlayers, 3, counter);
            counter++;
        }
    }

    private void setMapGrid() {
        mapGrid.setAlignment(Pos.CENTER);
        mapGrid.setHgap(70);
        mapGrid.setVgap(10);
        mapGrid.setPrefHeight(mapMonitorHeight);
        mapGrid.setPrefWidth(mapMonitorWidth);
        mapGrid.setStyle("-fx-background-color: darkgray");
        mapGrid.add(new Text("number"), 1, 0);
        mapGrid.add(new Text("name"), 2, 0);
        mapGrid.add(new Text("number of players"), 3, 0);
    }

    private void setCheckBoxOnAction(CheckBox checkBox) {
        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!checkBox.isSelected())
                    checkBox.setSelected(true);
                else {
                    for (int i = 0 ; i < checkBoxes.size(); i++) {
                        CheckBox checkBox1 = checkBoxes.get(i);
                        if (checkBox1.equals(checkBox)) {
                            mapIndex = i;
                            setNumberOfPlayers();
                        }
                        else
                            checkBox1.setSelected(false);
                    }
                }
            }
        });
    }

    private void setNumberOfPlayers() {
        numberOfPlayers.getItems().clear();
        for (int i = 2; i <= Map.getMaps().get(mapIndex).getNumberOfPlayers(); i++)
            numberOfPlayers.getItems().add(String.valueOf(i));
        numberOfPlayers.setValue(String.valueOf(Map.getMaps().get(mapIndex).getNumberOfPlayers()));
        setUsernameFields(Map.getMaps().get(mapIndex).getNumberOfPlayers());

    }

    private void setUsernameFields(int numberOfPlayers) {
        clearGridPane();
        for (int i = 1 ; i < numberOfPlayers; i++) {
            Label label = new Label("   " + LordColor.getLordColor(i).toString() + " lord (player " + (i+1) + ") username");
            label.setStyle("-fx-font-size: 15");
            TextField textField = new TextField();
            textField.setPromptText("username");
            textField.setMaxWidth(200);
            Text text = new Text();
            text.setFill(Color.RED);
            usernameLabels.add(label);
            usernameFields.add(textField);
            errorTexts.add(text);
            usernamesGridPane.add(label, 0, i);
            usernamesGridPane.add(textField,1, i);
            usernamesGridPane.add(text, 2, i);
            addUsernameFieldListener(textField, text);
        }
    }

    private void addUsernameFieldListener(TextField textField, Text text) {
        textField.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                textField.setStyle("-fx-border-color: lightgray");
                text.setText("");
            } else if (User.getUserByUsername(t1) == null) {
                textField.setStyle("-fx-border-color: red");
                text.setText("Username doesn't exist!");
            } else {
                boolean found = false;
                if (t1.equals(Controller.currentUser.getUsername())) {
                    textField.setStyle("-fx-border-color: red");
                    text.setText("Repetitive username!");
                    found = true;
                }
                for (TextField textField1 : usernameFields)
                    if (!textField1.equals(textField) && textField1.getText().equals(t1)) {
                        textField.setStyle("-fx-border-color: red");
                        text.setText("Repetitive username!");
                        found = true;
                        break;
                    }
                if (!found) {
                    textField.setStyle("-fx-border-color: lightgray");
                    text.setText("");
                }

            }
        }));
    }

    private void clearGridPane() {
        for (Label label : usernameLabels)
            usernamesGridPane.getChildren().remove(label);
        for (TextField textField : usernameFields)
            usernamesGridPane.getChildren().remove(textField);
        for (Text text : errorTexts)
            usernamesGridPane.getChildren().remove(text);
        usernameLabels.clear();
        usernameFields.clear();
        errorTexts.clear();
    }

    public void start(MouseEvent mouseEvent) {
        checkForEmptyFields();
        if (!checkForErrors()) {
            Map map = Map.getMaps().get(mapIndex);
            ArrayList<Government> governments = getGovernments(map);
            Integer earlyGameGolds = Integer.parseInt(golds.getValue().toString());
            Game game = new Game(map, governments, earlyGameGolds);
            Controller.currentGame = game;
            Controller.currentGame = new Game(map,governments,earlyGameGolds);
            new GameGraphics().start(Main.stage);
        }
    }

    private ArrayList<Government> getGovernments(Map map) {
        ArrayList<Government> governments = new ArrayList<>();
        LordColor currentLordColor = LordColor.getLordColor(0);
        governments.add(new Government(LordColor.getLordColor(0), Controller.currentUser, 0,
                map.getKeepPosition(currentLordColor)[0],
                map.getKeepPosition(currentLordColor)[1]));
        for (int i = 1; i < usernameFields.size(); i++) {
            currentLordColor = LordColor.getLordColor(i);
            String username = usernameFields.get(i).getText();
            User user = User.getUserByUsername(username);
            governments.add(new Government(LordColor.getLordColor(i), user, 0, map.getKeepPosition(currentLordColor)[0],
                    map.getKeepPosition(currentLordColor)[1]));
        }
        return governments;
    }

    private boolean checkForErrors() {
        for (Text text : errorTexts)
            if (!text.getText().isEmpty())
                return true;
        return false;
    }

    private void checkForEmptyFields() {
        for (int i = 0; i < usernameFields.size(); i++)
            if (usernameFields.get(i).getText().isEmpty()) {
                usernameFields.get(i).setStyle("-fx-border-color: red");
                errorTexts.get(i).setText("This field is empty!");
            }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenuGraphics().start(Main.stage);
    }

    public void changeNumberOfPlayers(ActionEvent actionEvent) {
        if (numberOfPlayers.getValue() != null)
            setUsernameFields(Integer.parseInt(numberOfPlayers.getValue().toString()));
    }
}
