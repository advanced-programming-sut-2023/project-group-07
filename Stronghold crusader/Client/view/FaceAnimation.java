package Client.view;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FaceAnimation extends Transition {
    private static int faceAnimNumber = 0;
    private ImageView imageView;
    private int finalImageNumber;
    private double counter;
    private int i;

    public FaceAnimation(ImageView imageView, int popularity) {
        this.imageView = imageView;
        if (popularity < 50)
            finalImageNumber = 10;
        else if (popularity < 75)
            finalImageNumber = 5;
        else
            finalImageNumber = 0;
        if (finalImageNumber > faceAnimNumber)
            i = 1;
        else if (finalImageNumber < faceAnimNumber)
            i = -1;
        else
            i = 0;
        counter = i;
        this.setCycleDuration(Duration.millis(10000));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        try {
            int count = (int) Math.floor(counter);
            Image image = new Image(GameGraphics.class.getResource("/Images/Game/Menu/Face/anim (" + (faceAnimNumber + count) +").png").toString(), 147, 147, false, false);
            imageView.setImage(image);
            if (faceAnimNumber + count == finalImageNumber) {
                faceAnimNumber = finalImageNumber;
                this.stop();
            }
            counter += i * 0.035;
        }
        catch (Exception e) {
            this.stop();
        }


    }
}
