package view;


import controller.Controller;
import controller.GameMenuController;
import controller.Messages;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Game;
import model.Map;
import model.TypeOfBuilding;

import java.io.File;
import java.util.ArrayList;


public class GameGraphics extends Application {
    public static int SIZEFACTOR = 100;
    private Pane rootPane;
    private StackPane statusPane;
    private Pane mapPane;
    private ArrayList<HBox> createBuilding = new ArrayList<>();
    private String buildingToBeBuilt;
    private StackPane selectBuildingToBeBuilt;
    private Map map;
    private GameMenuController gameMenuController;
    @Override
    public void start(Stage stage) {
        rootPane = new Pane();
        Pane pane = new Pane();
        mapPane = pane;
        ImageView image =new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Tiles/Desert/map.png").toExternalForm()));
        Scene scene = new Scene(rootPane);
        pane.getChildren().add(image);
        rootPane.getChildren().add(pane);
        pane.setMaxHeight(Main.screenHeight-215);
        System.out.println(pane.getWidth());
        System.out.println(image.getBoundsInParent());
        initStatusBar();
        Image image1 = new Image(GameGraphics.class.getResource("/Images/Game/Cursors/fook.png").toExternalForm());
        scene.setCursor(new ImageCursor(image1));
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        mapPaneEvent();
        stage.addEventHandler(MouseEvent.MOUSE_MOVED,mouseEvent -> {
            if(buildingToBeBuilt!=null) {
                selectBuildingToBeBuilt.setLayoutX(40*((int)((mouseEvent.getX()+Math.abs(mapPane.getLayoutX())-selectBuildingToBeBuilt.getWidth()/2)/40)));
                selectBuildingToBeBuilt.setLayoutY(40*((int)((mouseEvent.getY()+Math.abs(mapPane.getLayoutY())-selectBuildingToBeBuilt.getWidth()/2)/40)));
                selectBuildingToBeBuilt.setVisible(true);
            }
            if(mouseEvent.getSceneX()>scene.getWidth()-100 && pane.getLayoutX()>/*map.getSize()*10*/ -400*10)
                pane.setLayoutX(pane.getLayoutX()-10);
            if(mouseEvent.getSceneX()<100 && pane.getLayoutX()<0)
                pane.setLayoutX(pane.getLayoutX()+10);
            if(mouseEvent.getSceneY()> scene.getHeight()-270 && mouseEvent.getSceneY()< scene.getHeight()-215 && pane.getLayoutY()>/*map.getSize()*10*/ -400*10)
                pane.setLayoutY(pane.getLayoutY()-10);
            if(mouseEvent.getSceneY()<100 && pane.getLayoutY()<0)
                pane.setLayoutY(pane.getLayoutY()+10);
        });
        stage.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseEvent -> {
        });
        stage.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseEvent -> {
        });
    }

    private void initStatusBar() {
        StackPane stackPane = new StackPane();
        statusPane = stackPane;
        stackPane.setLayoutY(Main.screenHeight-215);
        stackPane.setLayoutX(100);
        ImageView imageView = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Menu/menu.png").toExternalForm()));
        imageView.setScaleX(1.4);
        imageView.setScaleY(1.4);
        stackPane.getChildren().add(imageView);
        rootPane.getChildren().add(stackPane);
        setCreateBuilding();
        statusBarButtons();
    }

    private void statusBarButtons() {
        HBox hBox = new HBox(10);
        statusPane.getChildren().add(hBox);
        hBox.setTranslateY(-40);
        hBox.setTranslateX(-20);
        hBox.setAlignment(Pos.CENTER);
        for(int i =1;i<7;i++){
            ImageView imageView = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Menu/button"+i+".png").toExternalForm()));
            imageView.hoverProperty().addListener((observable -> {
                imageView.setOpacity(0.8);
                if(!imageView.isHover())
                    imageView.setOpacity(1);
            }));

            imageView.setFitHeight(75);
            imageView.setFitWidth(75);
            int j=i;
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for(HBox hBox1: createBuilding)
                        hBox1.setVisible(false);
                    createBuilding.get(j-1).setVisible(true);
                }
            });
            hBox.getChildren().add(imageView);
        }
    }

    private void setCreateBuilding() throws SecurityException {
        for(int i=1;i<7;i++){
            HBox hBox = new HBox(20);
            hBox.setTranslateX(260);
            hBox.setTranslateY(80);
            statusPane.getChildren().add(hBox);
            createBuilding.add(hBox);
            String path = GameGraphics.class.getResource("/Images/Game/Menu/button"+i).toString();
            path = path.replaceAll("%20"," ");
            path = path.substring("file:".length());
            File file = new File(path);
            File[] files = file.listFiles();
            for(File file1 : files){
                ImageView imageView = new ImageView(new Image(file1.getPath()));
                StackPane stackPane = new StackPane(imageView);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(120);
                hBox.getChildren().add(stackPane);
                stackPane.hoverProperty().addListener((observable -> {
                    imageView.setOpacity(0.7);
                    if(!stackPane.isHover())
                        imageView.setOpacity(1);
                }));
                stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(selectBuildingToBeBuilt!=null){
                            selectBuildingToBeBuilt.setVisible(false);
                            mapPane.getChildren().remove(selectBuildingToBeBuilt);
                        }
                        buildingToBeBuilt = file1.getPath();
                        ImageView imageView1 =  new ImageView(new Image(file1.getPath()));
                        imageView1.setFitWidth(40*TypeOfBuilding.getBuilding(getPhotoName(file1.getPath())).getWidth());
                        imageView1.setPreserveRatio(true);
                        selectBuildingToBeBuilt = new StackPane(imageView1);
                        mapPane.getChildren().add(selectBuildingToBeBuilt);
                        selectBuildingToBeBuilt.setVisible(false);
                    }
                });
            }
            hBox.setVisible(false);

        }
    }

    private void mapPaneEvent() {
        mapPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                    emptySelection();
                }
                if(buildingToBeBuilt!=null) {
//                    if(selectBuildingToBeBuilt.getLayoutX())
                    dropBuilding(mouseEvent);
                }
            }
        });
    }

    private void emptySelection() {
        buildingToBeBuilt=null;
    }

    private void dropBuilding(MouseEvent mouseEvent) {
        ImageView imageView = new ImageView(buildingToBeBuilt);
        mapPane.getChildren().add(imageView);
        imageView.setLayoutX(selectBuildingToBeBuilt.getLayoutX());
        imageView.setLayoutY(selectBuildingToBeBuilt.getLayoutY());
        String string = getPhotoName(buildingToBeBuilt);
        buildingToBeBuilt = null;
        selectBuildingToBeBuilt.setVisible(false);
        imageView.setFitWidth(40* TypeOfBuilding.getBuilding(string).getWidth());
        imageView.setPreserveRatio(true);
        Messages message =  gameMenuController.dropBuilding((int)(mouseEvent.getSceneY()+Math.abs(mapPane.getLayoutY())),
                (int)(mouseEvent.getSceneX()+Math.abs(mapPane.getLayoutX())),
                string);
        switch (message) {
            case THERES_ALREADY_BUILDING:
                break;
            case THERES_ALREADY_UNIT:
                break;
            case CANT_PLACE_THIS:
                break;
            case NOT_ENOUGH_GOLD:
                break;
            case NOT_ENOUGH_RESOURCES:
                break;
            case THERES_AN_ENEMY_CLOSE_BY:
                break;
            case MUST_BE_ADJACENT_TO_BUILDINGS_OF_THE_SAME_TYPE:
                break;
            case DEPLOYMENT_SUCCESSFUL:

        }

    }
    public void setGameMenuController(GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
    }

    private String getPhotoName(String path) {
        File file = new File(path);
        String string = file.getName();
        string = string.substring(0,string.length()-".png".length());
        return string;
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
