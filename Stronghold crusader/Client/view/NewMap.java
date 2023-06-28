package Client.view;

import Client.controller.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import Client.model.LordColor;
import Client.model.Map;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class NewMap extends Application implements Initializable {
    @FXML
    private Label error;
    @FXML
    private GridPane menuPane;
    @FXML
    private ComboBox mapSize;
    @FXML
    private ComboBox numberOfPlayers;
    @FXML
    private TextField mapName;
    private CreateMapMenuController controller = new CreateMapMenuController();
    private int size;
    private ArrayList<Label> lordLabels = new ArrayList<>();
    private ArrayList<TextField> keepCoordinates = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(Controller.getBorderPane("/FXML/SetNewMap.fxml")));
        stage.setFullScreen(true);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapSize.getItems().clear();
        numberOfPlayers.getItems().clear();
        mapSize.getItems().addAll("100 * 100", "150 * 150", "200 * 200");
        for (int i = 2 ; i <= 8 ; i++)
            numberOfPlayers.getItems().add(String.valueOf(i));
        mapSize.setValue("100 * 100");
        size = 100;
        numberOfPlayers.setValue("2");
        setKeepCoordinates(2);
        addMapNameListener();
    }

    private void addMapNameListener() {
        mapName.textProperty().addListener(((observableValue, s, t1) -> {
            error.setText("");
        }));
    }

    public void changeNumberOfPlayers(ActionEvent actionEvent) {
        for (Label label : lordLabels)
            menuPane.getChildren().remove(label);
        for (TextField textField : keepCoordinates)
            menuPane.getChildren().remove(textField);
        int numberOfPlayers = Integer.parseInt(this.numberOfPlayers.getValue().toString());
        setKeepCoordinates(numberOfPlayers);
    }

    private void setKeepCoordinates(int numberOfPlayers) {
        for (int i = 0 ; i < numberOfPlayers; i++) {
            Label label = new Label(LordColor.getLordColor(i).toString() + " lord keep coordinates");
            label.setStyle("-fx-font-size: 15");
            TextField row = getTextField("row", 10);
            TextField column = getTextField("column", 10);
            addTextFieldListener(row);
            addTextFieldListener(column);
            menuPane.add(label, 0, i+3);
            menuPane.add(row, 1, i+3);
            menuPane.add(column, 2, i+3);
            lordLabels.add(label);
            keepCoordinates.addAll(List.of(row, column));
        }
    }

    private TextField getTextField(String promptText, double maxWidth) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setMaxWidth(maxWidth);
        return textField;
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new CreateMapGraphics().start(Main.stage);
    }

    public void modify(MouseEvent mouseEvent) throws IOException {
        String mapName = this.mapName.getText();
        if (checkForFields(mapName)) {
            boolean canStartModify = true;
            int numberOfPlayers = Integer.parseInt(this.numberOfPlayers.getValue().toString());
            HashMap <LordColor, int[]> keepsPositions = new HashMap<>();
            for (int i = 0 ; i < numberOfPlayers ; i++) {
                int row = Integer.parseInt(keepCoordinates.get(2*i).getText());
                int column = Integer.parseInt(keepCoordinates.get(2*i + 1).getText());
                if (controller.canDropKeep(keepsPositions, row, column))
                    keepsPositions.put(LordColor.getLordColor(i), new int[]{row, column});
                else {
                    error.setText("Keeps coordinates are invalid!");
                    keepCoordinates.get(2*i).setStyle("-fx-border-color: red");
                    keepCoordinates.get(2*i + 1).setStyle("-fx-border-color: red");
                    canStartModify = false;
                }
            }
            if (canStartModify)
                controller.setNewMap(size, mapName, numberOfPlayers, keepsPositions);
        }
    }

    private boolean checkForFields(String mapName) {
        boolean result = true;
        if (mapName.isBlank()) {
            error.setText("There are empty fields!");
            this.mapName.setStyle("-fx-border-color: red");
            result = false;
        }
        else {
            for (Map map : Map.getMaps())
                if (map.getName().equals(mapName)) {
                    if (result) error.setText("A map with this name already exists!");
                    this.mapName.setStyle("-fx-border-color: red");
                    result = false;
                    break;
                }
        }
        int coordinate;
        for (TextField textField : keepCoordinates) {
            if (textField.getText().isEmpty()) {
                if (result) error.setText("There are empty fields!");
                textField.setStyle("-fx-border-color: red");
                result = false;
            }
            else if (!textField.getText().matches("\\d+"))
                result = false;
            else if ((coordinate = Integer.parseInt(textField.getText())) < 1) {
                if (result) error.setText("Keeps coordinates are invalid!");
                result = false;
            }
            else if (coordinate > ((textField.getPromptText().equals("row")) ? size - 7 : size - 12)) {
                if (result) error.setText("Keeps coordinates are invalid!");
                result = false;
            }
        }
        return result;
    }

    private void addTextFieldListener(TextField textField) {
        textField.textProperty().addListener(((observableValue, s, t1) -> {
            error.setText("");
            checkCoordinateFields(textField, t1);
        }));
    }

    public void changeSize(ActionEvent actionEvent) {
        String [] split = mapSize.getValue().toString().split("\\*");
        size = Integer.parseInt(split[0].trim());
        for (TextField textField : keepCoordinates)
            checkCoordinateFields(textField, textField.getText());
    }

    private void checkCoordinateFields(TextField textField, String text) {
        if (text.isEmpty())
            textField.setStyle("-fx-border-color: lightgray");
        else if (!text.matches("\\d+"))
            textField.setStyle("-fx-border-color: red");
        else {
            int coordinate = Integer.parseInt(text);
            if (coordinate < 1 || coordinate > ((textField.getPromptText().equals("row")) ? size - 7 : size - 12))
                textField.setStyle("-fx-border-color: red");
            else
                textField.setStyle("-fx-border-color: lightgray");
        }
    }
}
