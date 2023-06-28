package view;

import controller.Controller;
import controller.CreateMapMenuController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Map;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateMapGraphics extends Application implements Initializable{
    @FXML
    private AnchorPane menuPane;
    ScrollPane scrollPane;
    GridPane mapGrid = new GridPane();
    private static final int mapMonitorWidth = 550;
    private static final int mapMonitorHeight = 400;
    private CreateMapMenuController controller = new CreateMapMenuController();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private int mapIndex = 0;


    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(Controller.getBorderPane("/FXML/CreateMapMenu.fxml")));
        stage.setFullScreen(true);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       initialize();
    }

    private void initialize() {
        scrollPane = new ScrollPane();
        scrollPane.setLayoutX(512);
        scrollPane.setLayoutY(100);
        scrollPane.setPrefWidth(mapMonitorWidth);
        scrollPane.setPrefHeight(mapMonitorHeight);
        initExistingMaps();
        scrollPane.setContent(mapGrid);
        menuPane.getChildren().add(scrollPane);
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

    public void newMap(MouseEvent mouseEvent) throws Exception {
        new NewMap().start(Main.stage);
    }


    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenuGraphics().start(Main.stage);
    }

    public void modify(MouseEvent mouseEvent) {
        controller.setExistingMap(mapIndex);
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
                        if (checkBox1.equals(checkBox))
                            mapIndex = i;
                        else
                            checkBox1.setSelected(false);
                    }
                }
            }
        });
    }

    public void remove(MouseEvent mouseEvent) throws IOException {
        controller.removeMap(mapIndex);
        initialize();
    }
}
