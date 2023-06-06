package view;

import javafx.animation.Transition;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.File;

public class CursorAnimation extends Transition {
    String directory;
    int frameCount=16;
    Scene scene;

    public CursorAnimation(String directory,Scene scene) {
        this.directory = directory;
        this.scene = scene;
        File fileDir = new File("C:\\Users\\Haj Ali\\Desktop\\project\\project-group-07\\resources\\Images\\Game\\Cursors\\moveUnit");
        setCycleDuration(Duration.millis(1000));
        setCycleCount(-1);
    }

    @Override
    protected void interpolate(double frac) {
//        for(int i=0;i<frameCount;i++) {
//            if((double)i/frameCount<frac && frac<=((double)i+1)/frameCount)
                if(frac<0.5)scene.setCursor(new ImageCursor(new Image("C:\\Users\\Haj Ali\\Desktop\\project\\project-group-07\\resources\\Images\\Game\\Cursors\\moveUnit\\anim0.png")));
//        }
    }
}
