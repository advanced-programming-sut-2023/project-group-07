package Client.view;


import Client.controller.Controller;
import Client.controller.GameMenuController;
import Client.controller.Messages;
import Client.controller.TradeMenuController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import Client.model.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class GameGraphics extends Application {
    public static int SIZEFACTOR = 100;
    private Pane rootPane;
    private StackPane statusPane;
    private Pane mapPane;
    private ArrayList<HBox> createBuilding = new ArrayList<>();
    private ArrayList<StackPane> buildings = new ArrayList<>();
    private ArrayList<PersonPane> people = new ArrayList<>();
    private ArrayList<PersonPane> selectedUnits = new ArrayList<>();
    private String buildingToBeBuilt;
    private StackPane selectBuildingToBeBuilt;
    private Game game;
    private Map map;
    private GameMenuController gameMenuController = new GameMenuController();
    private VBox messageBar = new VBox(20);
    private HBox moveUnitShortcutHbox = new HBox();
    private HBox attackUnitShortcutHbox = new HBox();
    private VBox mapPixelDetailsShortcutVbox = new VBox();
    private double oldMouseX;
    private int oldX = -1;
    private double oldMouseY;
    private int oldY = -1;
    private boolean isDataShown;
    private Label dataBox = new Label();
    private Rectangle selectionArea = new Rectangle();
    private ArrayList<HBox> buildingMenus = new ArrayList<>();
    private HBox selectedUnitBar = new HBox(20);
    private Text popularityText = null;
    private Text goldText = null;
    private Text PopulationText = null;
    private HBox governmentActionsButtons = null;
    private ArrayList<HBox> governmentActionsMenus = new ArrayList<>();
    private HBox marketMenu = null;
    private ShoppingPage shoppingPage = null;
    private TradingPage tradingPage = null;
    private HBox shoppingPageMenu = null;
    private HBox tradingPageMenu = null;
    private HBox tradingRequestsMenu = null;
    private HBox tradeMenu = null;
    private ComboBox<TradeRequest> availableTradesComboBox;
    private ComboBox<TradeRequest> tradesHistoryComboBox;
    private ArrayList<Rectangle> selectionRectangles = new ArrayList<>();

    private ArrayList<PopularityFactor> popularityFactors = new ArrayList<>();
    private CursorAnimation cursorAnimation;
    private StackPane miniMapPane;

    @Override
    public void start(Stage stage) {
        Scene scene = prepareStageElements();
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        mapPaneEvent();
        buildingMenu();
        setUnitTimeline();
        createMiniMap();
        setMap();
        addNextTurn();
        setMoveMapTimeLine(mapPane, scene);
        addStageEventHandlers(stage);
        setHoverTimeLine();
        setShortcutHbox(moveUnitShortcutHbox, "Move to: ");
        setShortcutHbox(attackUnitShortcutHbox, "Attack to: ");
        setDetailsShortcut();
        Controller.createChatMenu(rootPane);
        scene.getStylesheets().add(GameGraphics.class.getResource("/CSS/slideBar.css").toExternalForm());
        setCheatShortCuts(scene);
    }

    private void setDetailsShortcut() {
        mapPixelDetailsShortcutVbox.setSpacing(20);
        mapPixelDetailsShortcutVbox.setAlignment(Pos.CENTER);
        mapPixelDetailsShortcutVbox.setPrefWidth(350);
        mapPixelDetailsShortcutVbox.setPrefHeight(350);
        mapPixelDetailsShortcutVbox.setStyle("-fx-background-color: lightblue");
        mapPixelDetailsShortcutVbox.setOpacity(0.8);
        Text text = new Text("Tile coordinates: ");
        text.setFill(Color.BLACK);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(15);
        hBox.getChildren().add(text);
        hBox.getChildren().addAll(getRowAndColumnFields());
        Label details = new Label();
        details.setPrefWidth(300);
        details.setPrefHeight(270);
        details.setStyle("-fx-border-color: black");
        mapPixelDetailsShortcutVbox.getChildren().addAll(hBox, details);
    }

    private void setShortcutHbox(HBox shortcutHbox, String textForShow) {
        shortcutHbox.setSpacing(15);
        shortcutHbox.setAlignment(Pos.CENTER);
        shortcutHbox.setPrefWidth(250);
        shortcutHbox.setPrefHeight(50);
        shortcutHbox.setStyle("-fx-background-color: black");
        shortcutHbox.setOpacity(0.6);
        Text text = new Text(textForShow);
        text.setFill(Color.WHITE);
        shortcutHbox.getChildren().add(text);
        shortcutHbox.getChildren().addAll(getRowAndColumnFields());
    }

    private TextField[] getRowAndColumnFields() {
        TextField row = new TextField();
        TextField column = new TextField();
        row.setPromptText("row");
        column.setPromptText("column");
        row.setPrefWidth(70);
        column.setPrefWidth(70);
        addCoordinatesListener(row);
        addCoordinatesListener(column);
        return new TextField[] {row, column};
    }

    private void addCoordinatesListener(TextField textField) {
        textField.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1.isEmpty())
                textField.setStyle("-fx-border-color: lightgray");
            else if (!t1.matches("\\d+"))
                textField.setStyle("-fx-border-color: red");
            else {
                int coordinate = Integer.parseInt(textField.getText());
                if (coordinate < 1 || coordinate > map.getSize())
                    textField.setStyle("-fx-border-color: red");
                else
                    textField.setStyle("-fx-border-color: lightgray");
            }
        }));
    }

    private Scene prepareStageElements() {
        game = Controller.currentGame;
        map = game.getMap();
        rootPane = new Pane();
        Pane pane = new Pane();
        mapPane = pane;
        ImageView image = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Tiles/Desert/map1.png").toExternalForm()));
        pane.getChildren().add(image);
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(GameGraphics.class.getResource("/CSS/SignUp.css").toString());
        rootPane.getChildren().add(pane);
        pane.setMaxHeight(Main.screenHeight - 215);
        initStatusBar();
        Image image1 = new Image(Objects.requireNonNull(GameGraphics.class.getResource("/Images/Game/Cursors/fook.png")).toExternalForm());
        scene.setCursor(new ImageCursor(image1));
        rootPane.getChildren().add(messageBar);
        messageBar.setLayoutX(20);
        messageBar.setLayoutY(300);
        createSelectionArea();
        cursorAnimation = new CursorAnimation(GameGraphics.class.getResource("/Images/Game/Cursors/moveUnit/").toExternalForm(), scene);
        mapPixelDetailsShortcutVbox.setLayoutX(0);
        mapPixelDetailsShortcutVbox.setLayoutY(0);
        return scene;
    }

    private void addStageEventHandlers(Stage stage) {
        mouseMoved(stage);
        mouseDragged(stage);
        mousePressed(stage);
        mouseReleased(stage);
        setShortcuts(stage);
    }

    private void setShortcuts(Stage stage) {
        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            String keyName = keyEvent.getCode().getName();
            if (keyName.matches("F\\d")) {
                int number = (int) (keyName.charAt(1) - 48);
                if (number < 6) {
                    clearStatusBar();
                    createBuilding.get(number).setVisible(true);
                }
            }
            else if (keyName.equals("G"))
                enterGovernmentActionButtonsMenu();
            else if (keyName.equals("M"))
                moveUnitShortcut();
            else if (keyName.equals("A"))
                attackUnitShortcut();
            else if (keyName.equals("I"))
                mapPixelDetailsShortcut();
        });
    }

    private void mapPixelDetailsShortcut() {
        if (rootPane.getChildren().contains(mapPixelDetailsShortcutVbox))
            return;
        rootPane.getChildren().add(mapPixelDetailsShortcutVbox);
        mapPixelDetailsShortcutVbox.requestFocus();
        mapPixelDetailsShortcutVbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().getName().equals("Enter")) {
                    String rowStr = ((TextField) ((HBox) mapPixelDetailsShortcutVbox.getChildren().get(0)).getChildren().get(1)).getText();
                    String columnStr = ((TextField) ((HBox) mapPixelDetailsShortcutVbox.getChildren().get(0)).getChildren().get(2)).getText();
                    if (checkCoordinates(rowStr, columnStr)) {
                        int row = Integer.parseInt(rowStr);
                        int column = Integer.parseInt(columnStr);
                        selectTiles(column, row, column, row);
                        ((Label) mapPixelDetailsShortcutVbox.getChildren().get(1)).setText(map.getMapPixel(row, column).details());
//                        mapPane.setLayoutX(40 * column - mapPane.getLayoutX());
//                        mapPane.setLayoutY(40 * row - mapPane.getLayoutY());
                    }
                }
            }
        });

    }

    private void attackUnitShortcut() {
        if (selectionRectangles.isEmpty() || selectedUnits.isEmpty())
            return;
        Rectangle selectionRectangle = selectionRectangles.get(0);
        attackUnitShortcutHbox.setLayoutX(selectionRectangle.getLayoutX());
        attackUnitShortcutHbox.setLayoutY(selectionRectangle.getLayoutY() + ((selectionRectangle.getLayoutY() < 100) ? 50 : -70));
        if (!mapPane.getChildren().contains(attackUnitShortcutHbox))
            mapPane.getChildren().add(attackUnitShortcutHbox);
        attackUnitShortcutHbox.requestFocus();
        attackUnitShortcutHbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().getName().equals("Enter")) {
                    String rowStr = ((TextField) attackUnitShortcutHbox.getChildren().get(1)).getText();
                    String columnStr = ((TextField) attackUnitShortcutHbox.getChildren().get(2)).getText();
                    if (checkCoordinates(rowStr, columnStr)) {
                        mapPane.getChildren().remove(attackUnitShortcutHbox);
                        int row = Integer.parseInt(rowStr);
                        int column = Integer.parseInt(columnStr);
                        moveUnits(row, column);
                    }
                }
            }
        });
    }

    private void moveUnitShortcut() {
        if (selectionRectangles.isEmpty() || selectedUnits.isEmpty())
            return;
        Rectangle selectionRectangle = selectionRectangles.get(0);
        moveUnitShortcutHbox.setLayoutX(selectionRectangle.getLayoutX());
        moveUnitShortcutHbox.setLayoutY(selectionRectangle.getLayoutY() + ((selectionRectangle.getLayoutY() < 100) ? 50 : -70));
        if (!mapPane.getChildren().contains(moveUnitShortcutHbox))
            mapPane.getChildren().add(moveUnitShortcutHbox);
        moveUnitShortcutHbox.requestFocus();
        moveUnitShortcutHbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().getName().equals("Enter")) {
                    String rowStr = ((TextField) moveUnitShortcutHbox.getChildren().get(1)).getText();
                    String columnStr = ((TextField) moveUnitShortcutHbox.getChildren().get(2)).getText();
                    if (checkCoordinates(rowStr, columnStr)) {
                        mapPane.getChildren().remove(moveUnitShortcutHbox);
                        int row = Integer.parseInt(rowStr);
                        int column = Integer.parseInt(columnStr);
                        moveUnits(row, column);
                    }
                }
            }
        });
    }

    private boolean checkCoordinates(String rowStr, String columnStr) {
        if (rowStr.isEmpty() || columnStr.isEmpty()) {
            addToMessageBar("There are empty fields!");
            return false;
        }
        if (!rowStr.matches("\\d+") || !columnStr.matches("\\d+")) {
            addToMessageBar("Enter whole numbers!");
            return false;
        }
        int row = Integer.parseInt(rowStr);
        int column = Integer.parseInt(columnStr);
        if (row < 1 || row > map.getSize() || column < 1 || column > map.getSize()) {
            addToMessageBar("Invalid coordinates");
            return false;
        }
        return true;
    }

    private void mouseReleased(Stage stage) {
        stage.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && selectionArea.getWidth() > 1) {
                int y1 = (int) Math.floor(selectionArea.getLayoutY() / 40);
                int y2 = (int) Math.floor(selectionArea.getLayoutY() / 40 + selectionArea.getHeight() / 40);
                int x1 = (int) Math.floor(selectionArea.getLayoutX() / 40);
                int x2 = (int) Math.floor(selectionArea.getLayoutX() / 40 + selectionArea.getWidth() / 40);
                selectTiles(x1, y1, x2, y2);
                gameMenuController.getSelectedUnit().clear();
                selectedUnits.clear();
                gameMenuController.selectUnit(y1, x1, y2, x2);
                for (Person person : gameMenuController.getSelectedUnit()) {
                    for (PersonPane personPane : people) {
                        if (personPane.getPerson().equals(person)) {
                            selectedUnits.add(personPane);
                            break;
                        }
                    }
                }
                if (!gameMenuController.getSelectedUnit().isEmpty()) {
                    unitSelectionBar();
                    clearStatusBar();
                    selectedUnitBar.setVisible(true);
                }
                selectionArea.setVisible(false);
            }
        });
    }

    private void mousePressed(Stage stage) {
        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                resetSelectionArea();
                removeTilesSelection();
                removeDetailsShortcut();
                oldMouseX = mouseEvent.getX() + Math.abs(mapPane.getLayoutX());
                oldMouseY = mouseEvent.getY() + Math.abs(mapPane.getLayoutY());
                Robot robot = new Robot();
                int x = (int) Math.floor(robot.getMouseX() / 40 + Math.abs(mapPane.getLayoutX()) / 40);
                int y = (int) Math.floor(robot.getMouseY() / 40 + Math.abs(mapPane.getLayoutY()) / 40);
                selectTiles(x, y, x, y);
            }
            else{
                resetCursor();
                removeTilesSelection();
                removeDetailsShortcut();
            }
        });
    }

    private void mouseDragged(Stage stage) {
        stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                removeTilesSelection();
                selectionArea.setVisible(true);
                selectionArea.setLayoutX(Math.min(mouseEvent.getX() + Math.abs(mapPane.getLayoutX()), oldMouseX));
                selectionArea.setLayoutY(Math.min(mouseEvent.getY() + Math.abs(mapPane.getLayoutY()), oldMouseY));
                selectionArea.setWidth(Math.abs(mouseEvent.getX() + Math.abs(mapPane.getLayoutX()) - oldMouseX));
                selectionArea.setHeight(Math.abs(mouseEvent.getY() + Math.abs(mapPane.getLayoutY()) - oldMouseY));
            }
        });
    }

    private void mouseMoved(Stage stage) {
        stage.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent -> {
            if (buildingToBeBuilt != null) {
                selectBuildingToBeBuilt.setLayoutX(40 * ((int) ((mouseEvent.getX() + Math.abs(mapPane.getLayoutX()) - selectBuildingToBeBuilt.getWidth() / 2) / 40)));
                selectBuildingToBeBuilt.setLayoutY(40 * ((int) ((mouseEvent.getY() + Math.abs(mapPane.getLayoutY()) - selectBuildingToBeBuilt.getWidth() / 2) / 40)));
                selectBuildingToBeBuilt.setVisible(true);
                boolean intersects = false;
                for (StackPane stackPane : buildings)
                    if (customIntersection(selectBuildingToBeBuilt, stackPane)) {
                        if (selectBuildingToBeBuilt.getChildren().size() == 1) {
                            ImageView imageView = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Buildings/error.png").toExternalForm()));
                            imageView.setOpacity(0.4);
                            imageView.setFitWidth(selectBuildingToBeBuilt.getWidth());
                            imageView.setPreserveRatio(true);
                            selectBuildingToBeBuilt.getChildren().add(imageView);
                        }
                        intersects = true;
                        break;
                    }
                if (!intersects) {
                    if (selectBuildingToBeBuilt.getChildren().size() > 1)
                        selectBuildingToBeBuilt.getChildren().remove(1);
                }
            }
            if (!gameMenuController.getSelectedUnit().isEmpty()) {
                boolean isCursorOnGround = isCursorOnGround();
                if (isCursorOnGround && !cursorAnimation.isPlaying) {
                    cursorAnimation.play();
                    cursorAnimation.isPlaying = true;
                } else if (!isCursorOnGround)
                    resetCursor();
                System.out.println(stage.getScene().getCursor());
            } else
                resetCursor();
        });
    }

    private void setMoveMapTimeLine(Pane pane, Scene scene) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), actionEvent -> {
            Robot robot = new Robot();
            if (robot.getMouseX() > scene.getWidth() - 50 && pane.getLayoutX() > -game.getMap().getSize() * 40 + Main.getScreenWidth())
                pane.setLayoutX(pane.getLayoutX() - 10);
            if (robot.getMouseX() < 50 && pane.getLayoutX() < 0)
                pane.setLayoutX(pane.getLayoutX() + 10);
            if (robot.getMouseY() > scene.getHeight() - 10 && pane.getLayoutY() > -game.getMap().getSize() * 40 + Main.getScreenHeight())
                pane.setLayoutY(pane.getLayoutY() - 10);
            if (robot.getMouseY() < 50 && pane.getLayoutY() < 0)
                pane.setLayoutY(pane.getLayoutY() + 10);
            Rectangle miniMapRectangle = (Rectangle) miniMapPane.getChildren().get(miniMapPane.getChildren().size() - 1);
            miniMapRectangle.setTranslateY(16 + 225 * Math.abs(pane.getLayoutY()) / (game.getMap().getSize() * 40));
            miniMapRectangle.setTranslateX(12.5 + 225 * Math.abs(pane.getLayoutX()) / (game.getMap().getSize() * 40));
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private void selectTiles(int x1, int y1, int x2, int y2) {
        removeTilesSelection();
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++) {
                Rectangle rectangle = new Rectangle(40, 40);
                rectangle.setFill(Color.DARKBLUE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setOpacity(0.3);
                rectangle.setLayoutX(40 * i);
                rectangle.setLayoutY(40 * j);
                mapPane.getChildren().add(rectangle);
                selectionRectangles.add(rectangle);
            }
    }

    private void removeTilesSelection() {
        for (Rectangle rectangle : selectionRectangles)
            if (mapPane.getChildren().contains(rectangle))
                mapPane.getChildren().remove(rectangle);
        selectionRectangles.clear();
        if (mapPane.getChildren().contains(moveUnitShortcutHbox))
            mapPane.getChildren().remove(moveUnitShortcutHbox);
        if (mapPane.getChildren().contains(attackUnitShortcutHbox))
            mapPane.getChildren().remove(attackUnitShortcutHbox);
    }

    private void removeDetailsShortcut() {
        if (rootPane.getChildren().contains(mapPixelDetailsShortcutVbox))
            rootPane.getChildren().remove(mapPixelDetailsShortcutVbox);
    }

    private void setHoverTimeLine() {
        dataBox.setStyle("-fx-border-color: red; -fx-font-size: 17; -fx-background-color: darkorange");
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> {
            Robot robot = new Robot();
            int X = (int) (robot.getMouseX() + Math.abs(mapPane.getLayoutX()));
            int Y = (int) (robot.getMouseY() + Math.abs(mapPane.getLayoutY()));
            if (X == oldX && Y == oldY) {
                if (!isDataShown) {
                    isDataShown = true;
                    int row = (int) Math.floor(Y / 40);
                    int column = (int) Math.floor(X / 40);
                    dataBox.setText(map.getMapPixel(row, column).details());
                    dataBox.setLayoutX(X);
                    dataBox.setLayoutY(Y);
                    mapPane.getChildren().add(dataBox);
                }
            } else {
                isDataShown = false;
                if (mapPane.getChildren().contains(dataBox))
                    mapPane.getChildren().remove(dataBox);
            }
            oldX = X;
            oldY = Y;
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private void setMap() {
        ArrayList<ArrayList<MapPixel>> field = map.getWholeField();
        int size = field.size();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                MapPixel pixel = field.get(i).get(j);
                if (!pixel.getTexture().equals(Texture.LAND))
                    addTexture(pixel.getTexture(), i, j);
                for (Building building : pixel.getBuildings()) {
                    int width = building.getTypeOfBuilding().getWidth();
                    int length = building.getTypeOfBuilding().getLength();
                    MapPixel pixel1;
                    if (!(pixel1 = field.get(i + width - 1).get(j + length - 1)).getBuildings().isEmpty() &&
                            pixel1.getBuildings().get(0).getTypeOfBuilding().equals(building.getTypeOfBuilding()))
                        addBuilding(building, i + (int) (width / 2), j + (int) (length / 2));
                }
                if (pixel.getLordKeep() != null) {
                    MapPixel pixel1;
                    if ((pixel1 = field.get(i + 6).get(j + 6)).getLordKeep() != null &&
                            pixel1.getLordKeep().equals(pixel.getLordKeep()))
                        addKeep(pixel.getLordKeep(), i - 2, j + 5);
                }
                for (Person person : pixel.getPeople())
                    addPerson(person, i, j);
            }
    }

    private void addKeep(LordColor lordKeep, int i, int j) {
        ImageView imageView = new ImageView(GameGraphics.class.getResource("/Images/Game/Buildings/keep.png").toString());
        StackPane stackPane = new StackPane(imageView);
        addEventHandlerForBuilding(stackPane);
        mapPane.getChildren().add(stackPane);
        stackPane.setLayoutX(40 * j);
        stackPane.setLayoutY(40 * i);
        buildings.add(stackPane);
        imageView.setPreserveRatio(true);
    }

    private void addBuilding(Building building, int i, int j) {
        ImageView imageView = new ImageView(GameGraphics.class.getResource("/Images/Game/Buildings/" + building.getTypeOfBuilding().toString() + ".png").toString());
        StackPane stackPane = new StackPane(imageView);
        addEventHandlerForBuilding(stackPane);
        mapPane.getChildren().add(stackPane);
        stackPane.setLayoutX(40 * j);
        stackPane.setLayoutY(40 * i);
        buildings.add(stackPane);
        imageView.setFitWidth(40 * TypeOfBuilding.getBuilding(building.getTypeOfBuilding().toString()).getWidth());
        imageView.setPreserveRatio(true);
        addImageToMiniMap(stackPane.getLayoutX(), stackPane.getLayoutY(), TypeOfBuilding.getBuilding(building.getTypeOfBuilding().toString()).getWidth());
    }

    private void addPerson(Person person, int i, int j) {
        String name;
        if (person instanceof Unit)
            name = ((Unit) person).getType().toString();
        else
            name = ((NonMilitary) person).getType().toString();
        PersonPane personPane = new PersonPane(name, person, gameMenuController);
        personPane.setLayoutX(40 * j);
        personPane.setLayoutY(40 * i);
        mapPane.getChildren().add(personPane);
        people.add(personPane);
        if (person instanceof Unit)
            gameMenuController.addHealthBarListener(personPane.getHealthBar(), (Unit) personPane.getPerson());
    }

    private void addTexture(Texture texture, int i, int j) {
        ImageView imageView = new ImageView(new Image(texture.getImagePath()));
        imageView.setLayoutX(40 * j);
        imageView.setLayoutY(40 * i);
        mapPane.getChildren().add(imageView);
    }

    private void setCheatShortCuts(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.Q) && keyEvent.isControlDown())
                    clearStatusBar();
            }
        });
    }

    private boolean isCursorOnGround() {
        boolean isCursorOnGround = true;
        for (StackPane stackPane : buildings)
            if (stackPane.isHover())
                isCursorOnGround = false;
        if (statusPane.isHover())
            isCursorOnGround = false;
        return isCursorOnGround;
    }

    private void resetCursor() {
        cursorAnimation.stop();
        cursorAnimation.isPlaying = false;
        Scene scene = rootPane.getScene();
        Image image1 = new Image(GameGraphics.class.getResource("/Images/Game/Cursors/fook.png").toExternalForm());
        scene.setCursor(new ImageCursor(image1));

    }

    private void createSelectionArea() {
        selectionArea.setFill(Color.BLUE);
        selectionArea.setOpacity(0.2);
        mapPane.getChildren().add(selectionArea);
    }

    private void resetSelectionArea() {
        selectionArea.setLayoutX(50000);
        selectionArea.setLayoutY(50000);
        selectionArea.setWidth(0);
        selectionArea.setHeight(0);
    }

    private void initStatusBar() {
        StackPane stackPane = new StackPane();
        statusPane = stackPane;
        stackPane.setLayoutY(Main.screenHeight - 215);
        stackPane.setLayoutX(100);
        ImageView imageView = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Menu/menu.png").toExternalForm()));
        imageView.setScaleX(1.4);
        imageView.setScaleY(1.4);
        stackPane.getChildren().add(imageView);
        rootPane.getChildren().add(stackPane);
        setCreateBuilding();
        statusBarButtons();
        setGovernmentDetails();
        setMarketMenu();
        setTradeMenu();
        statusPane.getChildren().add(selectedUnitBar);
        selectedUnitBar.setVisible(false);
    }

    private void updateGovernmentInfo() {
        updateGoldText();
        updatePopularityText();
        updatePopulationText();
    }

    private void setMarketMenu() {
        if (marketMenu == null) {
            marketMenu = new HBox();
            marketMenu.setTranslateY(40);
        }
        setMarketItems();
        setShoppingMenu();
        setEnterTradeMenuButton();
    }

    private void setTradeMenu() {
        initializingTradeMenu();
        tradeMenu.getChildren().add(new Text("trade menu\t"));
        setTradeMenuItems();
        setTradingPage();
        setEnterTradeRequestsMenu();
        setTradeRequestsMenu();

    }

    private void setTradeRequestsMenu() {
        tradingRequestsMenu = makeAHBoxMenu();
        setTradeRequestMenuActions();
        tradingRequestsMenu.setVisible(false);
    }

    private void setTradeRequestMenuActions() {
        setAvailableTradesComboBox();
        TextField messageTextField = new TextField();
        messageTextField.setPromptText("write your message here");
        Button acceptButton = setAcceptButtonInTradeRequestMenu(messageTextField);
        Button rejectButton = setRejectButtonInTradeRequestMenu();
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        tradingRequestsMenu.getChildren().add(vBox);
        vBox.getChildren().add(messageTextField);
        vBox.getChildren().add(rejectButton);
        vBox.getChildren().add(acceptButton);
        setTradesHistoryComboBox();
    }

    private void setTradesHistoryComboBox() {
        tradesHistoryComboBox = new ComboBox<>();
        tradesHistoryComboBox.setPromptText("trade's history");
        tradingRequestsMenu.getChildren().add(tradesHistoryComboBox);
    }

    private Button setRejectButtonInTradeRequestMenu() {
        Button rejectButton = new Button("reject");
        rejectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TradeRequest selectedRequest = availableTradesComboBox.getValue();
                if (selectedRequest == null) return;
                tradeMenuController().rejectTrade(selectedRequest.getId());
                addToMessageBar("You have successfully rejected a trade.");
            }
        });
        return rejectButton;
    }

    private Button setAcceptButtonInTradeRequestMenu(TextField messageTextField) {
        Button acceptButton = new Button("accept");
        acceptButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TradeRequest selectedRequest = availableTradesComboBox.getValue();
                if (selectedRequest == null) return;
                switch (tradeMenuController().acceptTrade(selectedRequest.getId(), messageTextField.getText())) {
                    case ACCEPT_TRADE_SUCCESSFUL:
                        addToMessageBar("You have successfully accepted a trade.");
                        updateGovernmentInfo();
                        break;
                    case POOR_RECEIVER:
                        addToMessageBar("You don't have enough recourse for this trade.");
                        break;
                    case POOR_REQUESTER:
                        addToMessageBar("Requester don't have enough gold to pay you right now.");
                        break;
                }
            }
        });
        return acceptButton;
    }

    private void setAvailableTradesComboBox() {
        availableTradesComboBox = new ComboBox<>();
        availableTradesComboBox.setPromptText("available requests");
        tradingRequestsMenu.getChildren().add(availableTradesComboBox);
    }

    private void setEnterTradeRequestsMenu() {
        Button enterRequestsMenuButton = new Button("trade requests");
        enterRequestsMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                enterRequestsMenu();
            }
        });
        tradeMenu.getChildren().add(enterRequestsMenuButton);
    }

    private void enterRequestsMenu() {
        clearStatusBar();
        ArrayList<TradeRequest> availableTrades = tradeMenuController().getAvailableTrades();
        availableTradesComboBox.getItems().clear();
        availableTradesComboBox.getItems().addAll(availableTrades);
        tradesHistoryComboBox.getItems().clear();
        tradesHistoryComboBox.getItems().addAll(tradeMenuController().getTradeHistory());
        tradingRequestsMenu.setVisible(true);

    }

    private void initializingTradeMenu() {
        tradeMenu = makeAHBoxMenu();
        tradeMenu.setVisible(false);
        tradeMenu.setTranslateY(90);
        tradeMenu.setTranslateX(-300);
    }

    private void setTradingPage() {
        tradingPage = new TradingPage(Resources.IRON);
        tradingPageMenu = makeAHBoxMenu();
        tradingPageMenu.getChildren().add(tradingPage.menu());
        tradingPageMenu.setVisible(false);
        tradingPage.requestButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tradingPage.lordColor() == null) {
                    addToMessageBar("choose color.");
                    return;
                }
                switch (tradeMenuController().requestTrade(tradingPage.resource().toString()
                        , tradingPage.lordColor().toString()
                        , tradingPage.message()
                        , tradingPage.tradingAmount()
                        , tradingPage.goldAmount())) {
                    case INVALID_COLOR:
                        addToMessageBar("choose color.");
                        break;
                    case NEGATIVE_PRICE:
                        addToMessageBar("Price can't be negative.");
                        break;
                    case NO_LORD_WITH_THIS_COLOR:
                        addToMessageBar("No one has this color.");
                        break;
                    case REQUEST_YOURSELF:
                        addToMessageBar("You can't request yourself.");
                        break;
                    case REQUEST_TRADE_SUCCESSFUL:
                        addToMessageBar("You have successfully made a trade request.");
                        break;
                }
            }
        });
    }

    private TradeMenuController tradeMenuController;

    public TradeMenuController tradeMenuController() {
        if (tradeMenuController == null) tradeMenuController = new TradeMenuController();
        return tradeMenuController;
    }


    private void setTradeMenuItems() {
        int numberOfRows = 2;
        ArrayList<HBox> rowsHBox = getMenuRowsHBox(numberOfRows, tradeMenu);
        addItemsToMenu(rowsHBox, numberOfRows, true);
    }


    private void setEnterTradeMenuButton() {
        Button enterTradeMenuButton = new Button();
        enterTradeMenuButton.setText("enter trade menu");
        enterTradeMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                enterTradeMenu();
            }
        });
        marketMenu.getChildren().add(enterTradeMenuButton);
    }

    private void enterTradingItemMenu(String resourceName) {
        clearStatusBar();
        Resources resource = Resources.getResource(resourceName);
        tradingPage.setResource(resource);
        tradingPage.setItemAmount(gameMenuController.getResourceAmount(resource));
        tradingPageMenu.setVisible(true);
    }

    private void enterTradeMenu() {
        clearStatusBar();
        tradeMenu.setVisible(true);
    }

    private void setShoppingMenu() {
        shoppingPage = new ShoppingPage(Resources.IRON);
        addClickActionsForShoppingMenu();
        shoppingPageMenu = makeAHBoxMenu();
        shoppingPageMenu.getChildren().add(shoppingPage.menu());
        shoppingPageMenu.setVisible(false);
    }

    private void addClickActionsForShoppingMenu() {
        shoppingPage.buyingButton().setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (gameMenuController.buyCommodity(shoppingPage.resource().getName(), 1)
                        == Messages.NOT_ENOUGH_GOLD)
                    addToMessageBar("not enough gold");
                else {
                    updateGoldText();
                    shoppingPage.setItemAmount(gameMenuController.getResourceAmount(shoppingPage.resource()));
                    addToMessageBar("Buy commodity successful!");
                }
            }
        });
        shoppingPage.sellingButton().setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (gameMenuController.sellCommodity(shoppingPage.resource().getName(), 1)
                        == Messages.NOT_ENOUGH_RESOURCES)
                    addToMessageBar("not enough resources");
                else {
                    updateGoldText();
                    shoppingPage.setItemAmount(gameMenuController.getResourceAmount(shoppingPage.resource()));
                    addToMessageBar("Sell commodity successful!");
                }

            }
        });
    }

    private void setMarketItems() {
        int numberOfRows = 2;
        ArrayList<HBox> rowsHBox = getMenuRowsHBox(numberOfRows, marketMenu);
        addItemsToMenu(rowsHBox, numberOfRows, false);
    }

    private ArrayList<HBox> getMenuRowsHBox(int numberOfRows, HBox menu) { // TODO: 6/28/2023 change
        VBox rowsPane = new VBox();
        rowsPane.setSpacing(20);
        menu.getChildren().add(rowsPane);
        ArrayList<HBox> rowsHBox = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            rowsHBox.add(hBox);
            rowsPane.getChildren().add(hBox);
        }
        return rowsHBox;
    }

    private void addItemsToMenu(ArrayList<HBox> rowsHBox, int numberOfRows, boolean isForTrade) { // boolean is bad practice
        // this method is used for trade and market
        File[] itemsList = filesListMaker("/Images/Game/market/items");
        int i = 0;
        for (File image : itemsList) {
            Rectangle itemImage = new Rectangle(35, 35);
            itemImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    String name = image.getName();
                    name = name.substring(0, name.indexOf("."));
                    if (isForTrade) enterTradingItemMenu(name);
                    else enterShoppingAnItemMenu(name);
                }
            });
            itemImage.setFill(new ImagePattern(new Image(image.getAbsolutePath())));
            rowsHBox.get(i).getChildren().add(itemImage);
            i = (i + 1) % numberOfRows;
        }
    }

    private void enterShoppingAnItemMenu(String resourceName) {
        clearStatusBar();
        Resources resource = Resources.getResource(resourceName);
        shoppingPage.setResource(resource);
        shoppingPage.setItemAmount(gameMenuController.getResourceAmount(resource));
        shoppingPageMenu.setVisible(true);
    }

    private void setGovernmentDetails() {
        setGovernmentInfo();
        setEnterGovernmentActionButton();
        setGovernmentActions();
    }

    private void setGovernmentActions() {
        setGovernmentActionButton();
    }

    private void setGovernmentActionButton() {
        governmentActionsButtons = makeAHBoxMenu();
        setFoodRateActions();
        setTaxActions();
        setFearRateActions();
        setPopularityActions();
        governmentActionsButtons.setVisible(false);
    }

    private void setFearRateActions() {
        HBox fearRateHBox = getFearRateHBox();
        governmentActionsMenus.add(fearRateHBox);
        Button fearRateButton = new Button();
        fearRateButton.setText("set fear rate");
        fearRateButton.setOnMouseClicked(mouseEvent -> {
            clearStatusBar();
            fearRateHBox.setVisible(true);
        });
        governmentActionsButtons.getChildren().add(fearRateButton);
    }

    private HBox getFearRateHBox() {
        HBox fearRateHBox = makeAHBoxMenu();
        Slider slider = makeSlider(-5, 5);
        slider.setValue(gameMenuController.getFearRate());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int roundedValue = (int) Math.round(newValue.doubleValue());
            slider.setValue(roundedValue);
            gameMenuController.setFearRate(roundedValue);
        });
        fearRateHBox.getChildren().add(new Text("fear rate\t"));
        fearRateHBox.getChildren().add(slider);
        fearRateHBox.setVisible(false);

        return fearRateHBox;
    }

    private void setPopularityActions() {
        HBox popularityFactorsHBox = getPopularityFactorsHBox();
        governmentActionsMenus.add(popularityFactorsHBox);
        Button popularityFactorsButton = new Button();
        popularityFactorsButton.setText("show popularity factors");
        popularityFactorsButton.setOnMouseClicked(mouseEvent -> {
            clearStatusBar();
            updatePopularityFactorsMenu();
            popularityFactorsHBox.setVisible(true);
        });
        governmentActionsButtons.getChildren().add(popularityFactorsButton);
    }

    private HBox getPopularityFactorsHBox() {
        HBox popularityHBox = makeAHBoxMenu();
        popularityHBox.setSpacing(20);
        VBox firstColumn = new VBox();
        VBox secondColumn = new VBox();
        firstColumn.setSpacing(20);
        secondColumn.setSpacing(20);
        firstColumn.getChildren().add(new Text());
        PopularityFactor foodFactor = new PopularityFactor("food", 0);
        firstColumn.getChildren().add(foodFactor.getHBox());
        popularityFactors.add(foodFactor);
        PopularityFactor taxFactor = new PopularityFactor("tax", 0);
        firstColumn.getChildren().add(taxFactor.getHBox());
        popularityFactors.add(taxFactor);
        PopularityFactor religionFactor = new PopularityFactor("religion", 0);
        firstColumn.getChildren().add(religionFactor.getHBox());
        popularityFactors.add(religionFactor);
        secondColumn.getChildren().add(new Text());
        PopularityFactor fearFactor = new PopularityFactor("fear factor", 0);
        secondColumn.getChildren().add(fearFactor.getHBox());
        popularityFactors.add(fearFactor);
        PopularityFactor aleFactor = new PopularityFactor("ale coverage", 0);
        secondColumn.getChildren().add(aleFactor.getHBox());
        popularityFactors.add(aleFactor);
        PopularityFactor allFactors = new PopularityFactor("in coming change in popularity", 0);
        secondColumn.getChildren().add(allFactors.getHBox());
        popularityFactors.add(allFactors);

        popularityHBox.getChildren().add(firstColumn);
        popularityHBox.getChildren().add(secondColumn);
        popularityHBox.setVisible(false);
        return popularityHBox;
    }

    private void updatePopularityFactorsMenu() {
        ArrayList<Integer> popularityFactorsAmounts = gameMenuController.getPopularityFactors();
        int popularitySum = 0;
        for (int i = 0; i < popularityFactorsAmounts.size(); i++) {
            popularitySum += popularityFactorsAmounts.get(i);
            popularityFactors.get(i).setAmount(popularityFactorsAmounts.get(i));
        }
        popularityFactors.get(popularityFactors.size() - 1).setAmount(popularitySum);

    }

    private void setTaxActions() {
        HBox taxRateHBox = getTaxRateHBox();
        governmentActionsMenus.add(taxRateHBox);
        Button taxRateButton = new Button();
        taxRateButton.setText("set tax rate");
        taxRateButton.setOnMouseClicked(mouseEvent -> {
            clearStatusBar();
            taxRateHBox.setVisible(true);
        });
        governmentActionsButtons.getChildren().add(taxRateButton);
    }

    private void setFoodRateActions() {
        HBox foodRateHBox = getFoodRateHBox();
        governmentActionsMenus.add(foodRateHBox);
        Button foodRateButton = new Button();
        foodRateButton.setText("set food rate");
        foodRateButton.setOnMouseClicked(mouseEvent -> {
            clearStatusBar();
            foodRateHBox.setVisible(true);
        });
        governmentActionsButtons.getChildren().add(foodRateButton);
    }

    private HBox getTaxRateHBox() {
        HBox taxRateHBox = makeAHBoxMenu();

        Slider slider = makeSlider(-3, 8);
        slider.setValue(gameMenuController.showTaxRate());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int roundedValue = (int) Math.round(newValue.doubleValue());
            slider.setValue(roundedValue);
            gameMenuController.setTax(roundedValue);
        });

        taxRateHBox.getChildren().add(new Text("tax rate\t"));
        taxRateHBox.getChildren().add(slider);
        taxRateHBox.setVisible(false);

        return taxRateHBox;
    }

    private HBox makeAHBoxMenu() {
        HBox hBox = new HBox();
        statusPane.getChildren().add(hBox);
        hBox.setTranslateY(60);
        hBox.setTranslateX(-400);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox getFoodRateHBox() {
        HBox foodRateHBox = makeAHBoxMenu();

        Slider slider = makeSlider(-2, 2);
        slider.setValue(gameMenuController.getFoodRate());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int roundedValue = (int) Math.round(newValue.doubleValue());
            if (gameMenuController.setFoodList(roundedValue) == Messages.NOT_ENOUGH_FOOD)
                addToMessageBar("not enough food");
            slider.setValue(gameMenuController.getFoodRate());
        });
        foodRateHBox.getChildren().add(new Text("food rate\t"));
        foodRateHBox.getChildren().add(slider);
        foodRateHBox.setVisible(false);

        return foodRateHBox;
    }

    private static Slider makeSlider(int minValue, int maxValue) {
        int numberOfValues = maxValue - minValue + 1;
        Slider slider = new Slider(minValue, maxValue, numberOfValues);
        slider.setPrefWidth(175);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        return slider;
    }

    private void setGovernmentInfo() {
        setPopularityText();
        setGoldText();
        setPopulationText();
        updateGovernmentInfo();
    }


    private void setEnterGovernmentActionButton() {
        // TODO: 6/27/2023  uncomment
        Button governmentActionButton = new Button();
        governmentActionButton.setPrefSize(162, 173);
        governmentActionButton.setTranslateY(45);
        governmentActionButton.setTranslateX(312);
        statusPane.getChildren().add(governmentActionButton);
        governmentActionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                enterGovernmentActionButtonsMenu();
            }
        });
        governmentActionButton.setOpacity(0);
    }

    private void enterGovernmentActionButtonsMenu() {
        clearStatusBar();
        governmentActionsButtons.setVisible(true);
    }

    private void setPopulationText() {
        PopulationText = new Text("1");
        PopulationText.setStyle("-fx-font-size: 16px;");
        popularityText.setFill(Color.GREEN);
        PopulationText.setTranslateY(75);
        PopulationText.setTranslateX(265);
        statusPane.getChildren().add(PopulationText);
    }

    private void updatePopulationText() {
        Integer population = gameMenuController.getPopulation();
        PopulationText.setText(population.toString());
    }

    private void setGoldText() {
        goldText = new Text("100");
        goldText.setFill(Color.GREEN);
        goldText.setStyle("-fx-font-size: 16px;");
        goldText.setTranslateY(50);
        goldText.setTranslateX(270);
        statusPane.getChildren().add(goldText);
    }

    private void updateGoldText() {
        Integer gold = gameMenuController.getGold();
        goldText.setText(gold.toString());
    }

    private void setPopularityText() {
        popularityText = new Text("100");
        popularityText.setStyle("-fx-font-size: 17px;");
        popularityText.setTranslateY(23);
        popularityText.setTranslateX(285);
        statusPane.getChildren().add(popularityText);
    }

    private void updatePopularityText() {
        Integer popularity = gameMenuController.getPopularity();
        popularityText.setText(popularity.toString());
        if (popularity < 50) popularityText.setFill(Color.RED);
        else if (popularity < 75) popularityText.setFill(Color.ORANGE);
        else popularityText.setFill(Color.GREEN);
    }

    private void statusBarButtons() {
        HBox hBox = new HBox(10);
        statusPane.getChildren().add(hBox);
        hBox.setTranslateY(-40);
        hBox.setTranslateX(-70);
        hBox.setAlignment(Pos.CENTER);
        for (int i = 1; i < 7; i++) {
            ImageView imageView = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Menu/button" + i + ".png").toExternalForm()));
            imageView.hoverProperty().addListener((observable -> {
                imageView.setOpacity(0.8);
                if (!imageView.isHover())
                    imageView.setOpacity(1);
            }));

            imageView.setFitHeight(75);
            imageView.setFitWidth(75);
            int j = i;
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        resetSelectionArea();
                        removeTilesSelection();
                        removeDetailsShortcut();
                        gameMenuController.getSelectedUnit().clear();
                        selectedUnits.clear();
                        clearStatusBar();
                        createBuilding.get(j - 1).setVisible(true);
                    }
                }
            });
            hBox.getChildren().add(imageView);
        }
    }

    private void addNextTurn() {
        Button button = new Button();
        button.setText("Next turn");
        button.setMaxWidth(200);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    gameMenuController.nextTurn();
                    updateGovernmentInfo();
                    eliminateDeadObjects();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        button.setLayoutX(Main.screenWidth - 170);
        button.setLayoutY(600);
        rootPane.getChildren().add(button);
    }

    private void eliminateDeadObjects() {
        for (PersonPane person : people)
            if (person.getPerson().getHp() <= 0 || person.getPerson().getGovernment().getDefeatedBy() != null)
                mapPane.getChildren().remove(person);
        for (StackPane buildingPane : buildings) {
            int row = (int) Math.floor(buildingPane.getLayoutY() / 40);
            int column = (int) Math.floor(buildingPane.getLayoutX() / 40);
            MapPixel pixel = map.getMapPixel(row, column);
            if (!pixel.getBuildings().isEmpty()) {
                Building building = pixel.getBuildings().get(0);
                if (building.getHp() <= -2 || building.getGovernment().getDefeatedBy() != null)
                    mapPane.getChildren().remove(buildingPane);
            } else {
                pixel = map.getMapPixel(row + 4, column - 2);
                LordColor keep = pixel.getLordKeep();
                if (game.getGovernmentByColor(keep).getDefeatedBy() != null)
                    mapPane.getChildren().remove(buildingPane);
            }
        }
    }

    private void setCreateBuilding() throws SecurityException {
        for (int i = 1; i < 7; i++) {
            HBox hBox = new HBox(20);
            hBox.setTranslateX(260);
            hBox.setTranslateY(60);
            statusPane.getChildren().add(hBox);
            createBuilding.add(hBox);
            File[] files = filesListMaker("/Images/Game/Menu/button" + i);
            for (File file1 : files) {
                ImageView imageView = new ImageView(new Image(file1.getPath()));
                StackPane stackPane = new StackPane(imageView);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(120);
                hBox.getChildren().add(stackPane);
                imageView.hoverProperty().addListener((observable -> {
                    imageView.setOpacity(0.7);
                    if (!stackPane.isHover())
                        imageView.setOpacity(1);
                }));
                stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                            if (selectBuildingToBeBuilt != null) {
                                selectBuildingToBeBuilt.setVisible(false);
                                mapPane.getChildren().remove(selectBuildingToBeBuilt);
                            }
                            buildingToBeBuilt = file1.getPath();
                            ImageView imageView1 = new ImageView(new Image(file1.getPath()));
                            imageView1.setFitWidth(40 * TypeOfBuilding.getBuilding(getPhotoName(file1.getPath())).getWidth());
                            imageView1.setPreserveRatio(true);
                            selectBuildingToBeBuilt = new StackPane(imageView1);
                            mapPane.getChildren().add(selectBuildingToBeBuilt);
                            selectBuildingToBeBuilt.setVisible(false);
                            resetSelectionArea();
                            gameMenuController.getSelectedUnit().clear();
                            selectedUnits.clear();
                        }
                    }
                });
            }
            hBox.setVisible(false);

        }
    }

    private void buildingMenu() {
        for (int i = 1; i < 7; i++) {
            File[] files = filesListMaker("/Images/Game/Menu/button" + i);
            for (File file : files) {
                HBox hBox = new HBox(20);
                hBox.setTranslateX(260);
                hBox.setTranslateY(60);
                statusPane.getChildren().add(hBox);
                buildingMenus.add(hBox);
                Text text = new Text(getPhotoName(file.getPath()));
                hBox.getChildren().add(text);
                text.setStyle("-fx-font-family: GothicE; -fx-font-size: 40; -fx-font-weight: bold");
                text.setTranslateY(10);
                if (getPhotoName(file.getPath()).equals("market")) {
                    if (marketMenu == null) marketMenu = hBox;
                    else
                        hBox.getChildren().add(marketMenu);
                }
                hBox.setVisible(false);

            }
        }
        createUnitMenu();
    }

    private void createUnitMenu() {
        for (MilitaryCampType militaryCampType : MilitaryCampType.values())
            for (HBox hbox : buildingMenus)
                if (((Text) hbox.getChildren().get(0)).getText().equals(militaryCampType.getName())) {
                    for (File file : filesListMaker("/Images/Game/Menu/" + militaryCampType.getName())) {
                        ImageView imageView = new ImageView(new Image(file.getPath()));
                        StackPane stackPane = new StackPane(imageView);
                        hbox.getChildren().add(stackPane);
                        if (militaryCampType.getName().equals("mercenary post")) {
                            stackPane.setTranslateX(-300);
                            stackPane.setTranslateY(10);
                        }
                        imageView.setFitWidth(70);
                        imageView.setPreserveRatio(true);
                        stackPane.hoverProperty().addListener((observable -> {
                            imageView.setOpacity(0.7);
                            if (!stackPane.isHover())
                                imageView.setOpacity(1);
                        }));
                        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                createUnit(file.getName().substring(0, file.getName().length() - ".png".length()));
                            }
                        });
                    }

                }
    }


    private void createUnit(String name) {
        Messages message = gameMenuController.createUnit(name, 1);
        switch (message) {
            case NOT_ENOUGH_PEASANTS -> addToMessageBar("Not enough peasants!");
            case NOT_ENOUGH_GOLD -> addToMessageBar("Not enough gold!");
            case NOT_ENOUGH_RESOURCES -> addToMessageBar("Not enough resources!");
            case UNIT_CREATED_SUCCESSFULLY -> {
                for (Unit unit : gameMenuController.createdUnit) {
                    PersonPane personPane = new PersonPane(name, unit, gameMenuController);
//                    personPane.getPersonAttacking().play();
//                    personPane.getBodyEffectFire().play();
                    people.add(personPane);
                    mapPane.getChildren().add(personPane);
                    gameMenuController.addHealthBarListener(personPane.getHealthBar(), unit);
                    gameMenuController.addUnitListener(unit);
                }
                updateGoldText();
                gameMenuController.createdUnit.clear();
            }
        }
    }

    private void unitSelectionBar() {
        selectedUnitBar.getChildren().clear();
        selectedUnitBar.setTranslateX(260);
        selectedUnitBar.setTranslateY(50);
        for (UnitTypes unitTypes : UnitTypes.values()) {
            int counter = 0;
            for (Person person : gameMenuController.getSelectedUnit())
                if (person instanceof Unit unit && unit.getType().equals(unitTypes))
                    counter++;
            if (counter > 0) {
                ImageView imageView = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Soldiers/selection/" + unitTypes.getType() + ".png").toExternalForm()));
//                ImageView archway = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Menu/archway.png").toExternalForm()));
//                archway.setPreserveRatio(true);
//                archway.setFitWidth(90);
                VBox vBox = new VBox();
                StackPane stackPane = new StackPane(vBox);
                vBox.getChildren().add(imageView);
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                vBox.hoverProperty().addListener((observable -> {
                    imageView.setOpacity(0.7);
                    if (!vBox.isHover())
                        imageView.setOpacity(1);
                }));
                vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                            for (int i = selectedUnits.size() - 1; i > -1; i--)
                                if (!((Unit) (selectedUnits.get(i).getPerson())).getType().getType().equals(unitTypes.getType()))
                                    selectedUnits.remove(i);
                            for (int i = gameMenuController.getSelectedUnit().size() - 1; i > -1; i--)
                                if (!((Unit) gameMenuController.getSelectedUnit().get(i)).getType().getType().equals(unitTypes.getType()))
                                    gameMenuController.getSelectedUnit().remove(i);
                            selectedUnitBar.getChildren().clear();
                            selectedUnitBar.getChildren().add(stackPane);
                        }
                        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                            for (int i = selectedUnits.size() - 1; i > -1; i--)
                                if (((Unit) (selectedUnits.get(i).getPerson())).getType().getType().equals(unitTypes.getType()))
                                    selectedUnits.remove(i);
                            for (int i = gameMenuController.getSelectedUnit().size() - 1; i > -1; i--)
                                if (((Unit) gameMenuController.getSelectedUnit().get(i)).getType().getType().equals(unitTypes.getType()))
                                    gameMenuController.getSelectedUnit().remove(i);
                            selectedUnitBar.getChildren().remove(stackPane);
                        }
                    }
                });
                Text text = new Text(counter + "");
                text.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
                vBox.getChildren().add(text);
                vBox.setAlignment(Pos.CENTER);
//                archway.setTranslateY(-20);
                selectedUnitBar.getChildren().add(stackPane);
            }
        }
        selectedUnitBar.setVisible(false);
    }

    private File[] filesListMaker(String resource) {
        String path = GameGraphics.class.getResource(resource).toString();
        path = path.replaceAll("%20", " ");
        path = path.substring("file:".length());
        File file = new File(path);
        return file.listFiles();
    }

    private void mapPaneEvent() {
        mapPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    resetCursor();
                    resetSelectionArea();
                    gameMenuController.getSelectedUnit().clear();
                    selectedUnits.clear();
                    emptySelection();
                    selectedUnitBar.setVisible(false);
                }
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (buildingToBeBuilt != null) {
//                    if(selectBuildingToBeBuilt.getLayoutX())
                        dropBuilding(mouseEvent);
                    }
                }
            }
        });
        mapPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (!gameMenuController.getSelectedUnit().isEmpty() && isCursorOnGround())
                       moveUnits((int) (mouseEvent.getY() / 40), (int) (mouseEvent.getX() / 40));
                }
            }
        });
    }

    private void moveUnits(int row, int column) {
        for (PersonPane personPane : selectedUnits) {
            Unit unit = (Unit) (personPane.getPerson());
            gameMenuController.moveUnit(row, column);
            if (unit.getMovePattern() != null && !unit.getMovePattern().isEmpty()) {
                personPane.getPersonDirection().play();
                personPane.getPersonMove().play();
                personPane.setMoving(true);
            }
        }
    }

    private void emptySelection() {
        buildingToBeBuilt = null;
    }

    private void dropBuilding(MouseEvent mouseEvent) {
        String string = getPhotoName(buildingToBeBuilt);
        Messages message = gameMenuController.dropBuilding((int) ((mouseEvent.getY() - selectBuildingToBeBuilt.getWidth() / 2) / 40),
                (int) ((mouseEvent.getX() - selectBuildingToBeBuilt.getWidth()/ 2) / 40),
                string);
        switch (message) {
            case THERES_ALREADY_BUILDING -> addToMessageBar("There's already a building here!");
            case THERES_ALREADY_UNIT -> addToMessageBar("There's already a unit here!");
            case CANT_PLACE_THIS -> addToMessageBar("Can't place this here!");
            case NOT_ENOUGH_GOLD -> addToMessageBar("Not enough gold!");
            case NOT_ENOUGH_RESOURCES -> addToMessageBar("Not enough resources!");
            case THERES_AN_ENEMY_CLOSE_BY -> addToMessageBar("There's an enemy close by!");
            case MUST_BE_ADJACENT_TO_BUILDINGS_OF_THE_SAME_TYPE ->
                    addToMessageBar("Must be adjacent to buildings of the same type!");
            case DEPLOYMENT_SUCCESSFUL -> {
                ImageView imageView = new ImageView(buildingToBeBuilt);
                StackPane stackPane = new StackPane(imageView);
                addEventHandlerForBuilding(stackPane);
                mapPane.getChildren().add(stackPane);
                stackPane.setLayoutX(selectBuildingToBeBuilt.getLayoutX());
                stackPane.setLayoutY(selectBuildingToBeBuilt.getLayoutY());
                buildingToBeBuilt = null;
                selectBuildingToBeBuilt.setVisible(false);
                buildings.add(stackPane);
                imageView.setFitWidth(40 * TypeOfBuilding.getBuilding(string).getWidth());
                imageView.setPreserveRatio(true);
                addImageToMiniMap(stackPane.getLayoutX(), stackPane.getLayoutY(), TypeOfBuilding.getBuilding(string).getWidth());
                updateGoldText();
            }
        }
    }

    private void addImageToMiniMap(double layoutX, double layoutY, int size) {
        Rectangle rectangle = new Rectangle(((double) size / game.getMap().getSize()) * 225, ((double) size / game.getMap().getSize()) * 225);
        rectangle.setFill(Color.RED);
        rectangle.setTranslateX((layoutX / (40 * game.getMap().getSize())) * 225 + 12.5);
        rectangle.setTranslateY((layoutY / (40 * game.getMap().getSize())) * 225 + 16);
        miniMapPane.getChildren().add(2, rectangle);
    }

    private void addEventHandlerForBuilding(StackPane stackPane) {
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    resetSelectionArea();
                    gameMenuController.getSelectedUnit().clear();
                    selectedUnits.clear();
                    ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                    String buildingName = getPhotoName(imageView.getImage().getUrl());
                    clearStatusBar();
                    for (HBox hBox : buildingMenus)
                        if (((Text) hBox.getChildren().get(0)).getText().equals(buildingName))
                            hBox.setVisible(true);
                }
            }
        });
    }

    private void clearStatusBar() {
        for (HBox hBox : buildingMenus)
            hBox.setVisible(false);
        for (HBox hBox : createBuilding)
            hBox.setVisible(false);
        selectedUnitBar.setVisible(false);
        governmentActionsButtons.setVisible(false);
        for (HBox menu : governmentActionsMenus)
            menu.setVisible(false);
        shoppingPageMenu.setVisible(false);
        tradingPageMenu.setVisible(false);
        tradeMenu.setVisible(false);
        tradingRequestsMenu.setVisible(false);


    }

    private String getPhotoName(String path) {
        File file = new File(path);
        String string = file.getName();
        string = string.substring(0, string.length() - ".png".length());
        return string;
    }

    private void addToMessageBar(String message) {
        Text text = new Text(message);
        text.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(0.4);
        text.setFill(Color.RED);
        messageBar.getChildren().add(text);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5000), actionEvent -> {
            if (messageBar.getChildren().contains(text))
                messageBar.getChildren().remove(text);
        }));
        timeline.setCycleCount(1);
        timeline.play();
        if (messageBar.getChildren().size() == 6)
            messageBar.getChildren().remove(0);
    }

    private boolean customIntersection(Node node1, Node node2) {
        if (intervalsIntersect(node1.getBoundsInParent().getMinX(), node1.getBoundsInParent().getMaxX(),
                node2.getBoundsInParent().getMinX(), node2.getBoundsInParent().getMaxX()) &&
                intervalsIntersect(node1.getBoundsInParent().getMinY(), node1.getBoundsInParent().getMaxY(),
                        node2.getBoundsInParent().getMinY(), node2.getBoundsInParent().getMaxY()))
            return true;
        return false;
    }

    private boolean intervalsIntersect(double a1, double b1, double a2, double b2) {
        return a1 < b2 && b1 > a2;
    }

    private void setUnitTimeline() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> {
            for (PersonPane personPane : people)
                if (!personPane.getPerson().getMovePattern().isEmpty() && !personPane.isMoving()) {
                    personPane.getPersonMove().play();
                    personPane.getPersonDirection().play();
                    personPane.setMoving(true);
                }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private void createMiniMap() {
        Rectangle rectangle = new Rectangle(225 * Main.screenWidth / (game.getMap().getSize() * 40), 225 * Main.screenHeight / (game.getMap().getSize() * 40));
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setTranslateX(12.5);
        rectangle.setTranslateY(16);
        ImageView miniMap = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/miniMap.png").toExternalForm()));
        ImageView miniMapBorder = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/miniMapBorder.png").toExternalForm()));
        miniMap.setPreserveRatio(true);
        miniMap.setFitWidth(225);
        miniMap.setTranslateX(12.5);
        miniMap.setTranslateY(16);
        miniMapBorder.setPreserveRatio(true);
        miniMapBorder.setFitWidth(250);
        miniMapPane = new StackPane(miniMap, miniMapBorder, rectangle);
        rootPane.getChildren().add(miniMapPane);
        miniMapPane.setAlignment(Pos.TOP_LEFT);
        miniMapPane.setLayoutY(Main.screenHeight - 250);
    }

    public Game getGame() {
        return game;
    }

    //    private void setZoomOut(Pane pane) {     //TODO just zooooooooooooooooooooooooooom
//        pane.setOnScroll(new EventHandler<ScrollEvent>() {
//            @Override
//            public void handle(ScrollEvent scrollEvent) {
//                if(scrollEvent.getDeltaY()>0 && pane.getScaleX()<2){
//                    pane.setScaleX(pane.getScaleX()+0.01);
//                    pane.setScaleY(pane.getScaleY()+0.01);
//                }
//                if(scrollEvent.getDeltaY()<0 && pane.getScaleX()>0.5){
//                    pane.setScaleX(pane.getScaleX()-0.01);
//                    pane.setScaleY(pane.getScaleY()-0.01);
//                }
//            }
//        });
//    }


    //    private Group createEnvironment(){
//        Group group = new Group();
//        Box ground = new Box();
//        ground.setHeight(1);
//        ground.setWidth(1000);
//        ground.setDepth(1000);
//        ground.setTranslateX(700);
//        ground.setTranslateY(1000);
//
//
//        Box box = new Box(100,100,100);
//        box.setTranslateX(900);
//        box.setTranslateZ(1000);
//        box.setTranslateY(1200);
//        ImageView image =new ImageView(new Image(GameGraphics.class.getResource("/Images/sc_gameinfo_buildings_22a.png").toExternalForm()));
////        for(int i=0;i<200;i++) {
////            for(int j=0;j<200;j++){
////                ImageView tile;
////                if((i+j)%2==0)
////                    tile = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Tiles/Desert/desert1.png").toExternalForm()));
////                else
////                    tile = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Tiles/Desert/desert2.png").toExternalForm()));
////                group.getChildren().add(tile);
////                if(i%2==0){
////                    tile.setTranslateY(20*i+500);
////                    tile.setTranslateX(10*j+500);
////                }
////                else {
////                    tile.setTranslateY(20*i+510);
////                    tile.setTranslateX(10*j+500);
////                }
////            }
////
////        }
////        group.getChildren().addAll(box);
//        group.getChildren().add(image);
//        image.setTranslateX(700);
//        image.setTranslateY(00);
//        Pane pane = new Pane();
//        pane.setTranslateX(500);
//        pane.setTranslateY(1000);
//        pane.setTranslateZ(1000);
//        pane.setLayoutX(500);
//        pane.setLayoutY(500);
//        pane.setPrefWidth(200);
//        pane.setPrefHeight(200);
//        pane.setScaleX(5);
//        pane.setScaleY(5);
//        group.getChildren().add(pane);
//        PersonAnimation personAnimation = new PersonAnimation("/Images/Game/Soldiers/ArabianSwordsman/left",pane);
//        personAnimation.setInterpolator(Interpolator.LINEAR);
//        personAnimation.play();
//
//
//        return group;
//    }
}
