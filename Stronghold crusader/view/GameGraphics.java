package view;


import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;


public class GameGraphics extends Application {
    public static int SIZEFACTOR = 100;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group world = createEnvironment();

        Scene scene = new Scene(world);
        primaryStage.setScene(scene);
        primaryStage.setWidth(16 * SIZEFACTOR);
        primaryStage.setHeight(9 * SIZEFACTOR);

        Camera camera = new PerspectiveCamera();
        camera.setFarClip(2000);
        camera.setNearClip(1);

        scene.setCamera(camera);

        Rotate worldRotX = new Rotate(0, Rotate.X_AXIS);
        Rotate worldRotY = new Rotate(0, Rotate.Y_AXIS);

        Translate  worldTransX = new Translate();

        world.getTransforms().addAll(worldRotY, worldRotX);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch(event.getCode()){
                case LEFT:
                    worldRotY.setAngle(worldRotY.getAngle() + 10);
                    break;
                case RIGHT:
                    worldRotY.setAngle(worldRotY.getAngle() - 10);
                    break;
                case UP:
                    worldRotX.setAngle(worldRotX.getAngle() + 10);
                    break;
                case DOWN:
                    worldRotX.setAngle(worldRotX.getAngle() - 10);
                case W: //w/s is for z
                    world.setTranslateZ(world.getTranslateZ() + 10);
                    break;
                case S:
                    world.setTranslateZ(world.getTranslateZ() - 10);
                    break;
                case A:// a/d is x axis
                    world.setTranslateX(world.getTranslateX() + 10);
                    break;
                case D:
                    world.setTranslateX(world.getTranslateX() - 10);
                    break;
                case SHIFT:// shift/contr is for y axis
                    world.setTranslateY(world.getTranslateY() + 10);
                    break;
                case CONTROL:
                    world.setTranslateY(world.getTranslateY() - 10);
                    break;

            }
        });

        primaryStage.show();
    }

    private Group createEnvironment(){
        Group group = new Group();

        Box ground = new Box();
        ground.setHeight(10);
        ground.setWidth(1000);
        ground.setDepth(1000);

        ground.setTranslateX(700);
        ground.setTranslateZ(500);

        Box box = new Box(100,100,100);
        box.setTranslateX(800);
        box.setTranslateZ(600);
        new Rotate(200,Rotate.X_AXIS);

        group.getChildren().addAll(ground, box);

        return group;
    }
}
