package view;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.File;


public class PersonAnimation extends Transition {
    String directory;
    int frameCount=0;
    Pane pane;

    public PersonAnimation(String directory,Pane pane) {
        this.pane=pane;
        this.directory = directory;
        File fileDir = new File("\\C:\\Users\\Haj Ali\\Desktop\\project\\project-group-07\\target\\classes\\Images\\Game\\Soldiers\\ArabianSwordsman\\left");
        for(File file : fileDir.listFiles())
            frameCount++;
        setCycleDuration(Duration.millis(1000));
        setCycleCount(-1);
    }

    @Override
    protected void interpolate(double frac) {
        for(int i=0;i<frameCount;i++) {
            System.out.println(frac);
            if((double)i/frameCount<frac && frac<=((double)i+1)/frameCount)
                pane.setBackground(new Background(new BackgroundImage(new Image(PersonAnimation.class.getResource(directory+"/anim"+i+".png").toExternalForm()),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT)));
        }
    }
}
