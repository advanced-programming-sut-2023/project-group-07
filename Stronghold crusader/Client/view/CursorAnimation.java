package Client.view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class CursorAnimation extends Transition {
    String directory;
    int frameCount=16;
    Scene scene;
    boolean isPlaying=false;

    public CursorAnimation(String directory,Scene scene) {
        this.directory = directory;
        this.scene = scene;
        setCycleDuration(Duration.millis(1000));
        setCycleCount(-1);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        for(int i=0;i<frameCount;i++) {
            if((double)i/frameCount<frac && frac<=((double)i+1)/frameCount)
                scene.setCursor(new ImageCursor(new Image(directory+"anim"+i+".png")));
        }
    }
}
