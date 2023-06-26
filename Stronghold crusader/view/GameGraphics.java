package view;


import controller.GameMenuController;
import controller.Messages;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.io.File;
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
    private Map map;
    private GameMenuController gameMenuController;
    private VBox messageBar = new VBox(20);
    private double oldMouseX;
    private double oldMouseY;
    private Rectangle selectionArea = new Rectangle();
    private ArrayList<HBox> buildingMenus = new ArrayList<>();
    private HBox selectedUnitBar = new HBox(20);
    private Text popularityText = null;
    private Text goldText = null;
    private Text PopulationText = null;
    private HBox governmentActionsButtons = null;
    private ArrayList<HBox> governmentActionsMenus = new ArrayList<>();
    ArrayList<PopularityFactor> popularityFactors = new ArrayList<>();
    private CursorAnimation cursorAnimation;

    @Override
    public void start(Stage stage) {
        rootPane = new Pane();
        Pane pane = new Pane();
        mapPane = pane;
        ImageView image = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Tiles/Desert/map.png").toExternalForm()));
        Scene scene = new Scene(rootPane);
        pane.getChildren().add(image);
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
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        mapPaneEvent();
        buildingMenu();
        setUnitTimeline();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), actionEvent -> {
            Robot robot = new Robot();
            if (robot.getMouseX() > scene.getWidth() - 50 && pane.getLayoutX() >/*-map.getSize()*40*/ -400*40) //TODO map is null
                pane.setLayoutX(pane.getLayoutX() - 10);
            if (robot.getMouseX() < 50 && pane.getLayoutX() < 0)
                pane.setLayoutX(pane.getLayoutX() + 10);
            if (robot.getMouseY() > scene.getHeight() - 10 && pane.getLayoutY() >/*-map.getSize()*40*/ -400*40)
                pane.setLayoutY(pane.getLayoutY() - 10);
            if (robot.getMouseY() < 50 && pane.getLayoutY() < 0)
                pane.setLayoutY(pane.getLayoutY() + 10);
        }));
        timeline.setCycleCount(-1);
        timeline.play();
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
            } else
                resetCursor();
        });
        stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                selectionArea.setVisible(true);
                selectionArea.setLayoutX(Math.min(mouseEvent.getX() + Math.abs(mapPane.getLayoutX()), oldMouseX));
                selectionArea.setLayoutY(Math.min(mouseEvent.getY() + Math.abs(mapPane.getLayoutY()), oldMouseY));
                selectionArea.setWidth(Math.abs(mouseEvent.getX() + Math.abs(mapPane.getLayoutX()) - oldMouseX));
                selectionArea.setHeight(Math.abs(mouseEvent.getY() + Math.abs(mapPane.getLayoutY()) - oldMouseY));
            }
        });
        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                resetSelectionArea();
                oldMouseX = mouseEvent.getX() + Math.abs(mapPane.getLayoutX());
                oldMouseY = mouseEvent.getY() + Math.abs(mapPane.getLayoutY());
            }
        });
        stage.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && selectionArea.getWidth() > 1) {
                int y1 = (int) Math.floor(selectionArea.getLayoutY() / 40);
                int y2 = (int) Math.floor(selectionArea.getLayoutY() / 40 + selectionArea.getHeight() / 40);
                int x1 = (int) Math.floor(selectionArea.getLayoutX() / 40);
                int x2 = (int) Math.floor(selectionArea.getLayoutX() / 40 + selectionArea.getWidth() / 40);
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
        scene.getStylesheets().add(GameGraphics.class.getResource("/CSS/slideBar.css").toExternalForm());
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
        statusPane.getChildren().add(selectedUnitBar);
        selectedUnitBar.setVisible(false);
    }

    private void updateGovernmentInto() {
        updateGoldText();
        updatePopularityText();
        updatePopulationText();
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

        Slider slider = makeSlider(-5,5);
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
        PopularityFactor foodFactor = new PopularityFactor("food",0);
        firstColumn.getChildren().add(foodFactor.getHBox());
        popularityFactors.add(foodFactor);
        PopularityFactor taxFactor = new PopularityFactor("tax",0);
        firstColumn.getChildren().add(taxFactor.getHBox());
        popularityFactors.add(taxFactor);
        PopularityFactor religionFactor = new PopularityFactor("religion",0);
        firstColumn.getChildren().add(religionFactor.getHBox());
        popularityFactors.add(religionFactor);
        secondColumn.getChildren().add(new Text());
        PopularityFactor fearFactor = new PopularityFactor("fear factor",0);
        secondColumn.getChildren().add(fearFactor.getHBox());
        popularityFactors.add(fearFactor);
        PopularityFactor aleFactor = new PopularityFactor("ale coverage",0);
        secondColumn.getChildren().add(aleFactor.getHBox());
        popularityFactors.add(aleFactor);
        PopularityFactor allFactors = new PopularityFactor("in coming change in popularity",0);
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
        for (int i=0; i<popularityFactorsAmounts.size(); i++){
            popularitySum += popularityFactorsAmounts.get(i);
            popularityFactors.get(i).setAmount(popularityFactorsAmounts.get(i));
        }
        popularityFactors.get(popularityFactors.size()-1).setAmount(popularitySum);

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

        Slider slider = makeSlider(-3,8);
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

        Slider slider = makeSlider(-2,2);
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

    private static Slider makeSlider(int minValue, int maxValue ) {
        int numberOfValues = maxValue - minValue +1;
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
        updateGovernmentInto();
    }


    private void setEnterGovernmentActionButton() {
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
        PopulationText = new Text("1/10");
        PopulationText.setStyle("-fx-font-size: 16px;");
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
        // TODO: 6/18/2023 change color base on popularity
    }

    private void statusBarButtons() {
        HBox hBox = new HBox(10);
        statusPane.getChildren().add(hBox);
        hBox.setTranslateY(-40);
        hBox.setTranslateX(-20);
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
                stackPane.hoverProperty().addListener((observable -> {
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
                        imageView.setFitWidth(100);
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
                    people.add(personPane);
                    mapPane.getChildren().add(personPane);
                    gameMenuController.addHealthBarListener(personPane.getHealthBar(), unit);
                    gameMenuController.addUnitListener(unit);
                }
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
            if (counter > 0 && unitTypes.getMilitaryCampType() != null) {
                ImageView imageView = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Menu/" + unitTypes.getMilitaryCampType().getName() + "/" + unitTypes.getType() + ".png").toExternalForm()));
                ImageView archway = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Menu/archway.png").toExternalForm()));
                archway.setPreserveRatio(true);
                archway.setFitWidth(90);
                VBox vBox = new VBox();
                StackPane stackPane = new StackPane(archway, vBox);
                vBox.getChildren().add(imageView);
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                vBox.hoverProperty().addListener((observable -> {
                    imageView.setOpacity(0.7);
                    if (!vBox.isHover())
                        imageView.setOpacity(1);
                }));
                Text text = new Text(counter + "");
                text.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
                vBox.getChildren().add(text);
                vBox.setAlignment(Pos.CENTER);
                archway.setTranslateY(-20);
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
                    if (!gameMenuController.getSelectedUnit().isEmpty() && isCursorOnGround()) {
                        for (PersonPane personPane : selectedUnits) {
                            Unit unit = (Unit) (personPane.getPerson());
                            gameMenuController.moveUnit((int) ((mouseEvent.getY() + Math.abs(mapPane.getLayoutY())) / 40), (int) ((mouseEvent.getX() + Math.abs(mapPane.getLayoutX())) / 40));
                            if (unit.getMovePattern() != null && !unit.getMovePattern().isEmpty()) {
                                personPane.getPersonDirection().play();
                                personPane.getPersonMove().play();
                                personPane.setMoving(true);
                            }
                        }
                    }
                }
            }
        });
    }

    private void emptySelection() {
        buildingToBeBuilt = null;
    }

    private void dropBuilding(MouseEvent mouseEvent) {
        String string = getPhotoName(buildingToBeBuilt);
        Messages message = gameMenuController.dropBuilding((int) (((mouseEvent.getSceneY() + Math.abs(mapPane.getLayoutY()))) / 40),
                (int) ((mouseEvent.getSceneX() + Math.abs(mapPane.getLayoutX())) / 40),
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
            }
        }
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

    }

    public void setGameMenuController(GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
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
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),actionEvent -> {
            for(PersonPane personPane:people)
                if(!personPane.getPerson().getMovePattern().isEmpty() && !personPane.isMoving()) {
                    personPane.getPersonMove().play();
                    personPane.getPersonDirection().play();
                    personPane.setMoving(true);
                }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }


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
