package Client.view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class CustomAnimation extends Transition {
    int frameCount = 5;
    String directory = CustomAnimation.class.getResource("/Images/Game/BodyEffects/Disease").toExternalForm();
    ImageView imageView = new ImageView(new Image(directory+"/anim (1).png"));
    public CustomAnimation(int x , int y) {
        imageView.setLayoutX(x*40);
        imageView.setLayoutY(y*40);
        setCycleCount(-1);
        setCycleDuration(Duration.millis(1000));
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        for(int i=0;i<frameCount;i++) {
            if((double)i/frameCount<frac && frac<=((double)i+1)/frameCount){
                imageView.setImage(new Image(directory+"/anim ("+(i+1)+").png"));
            }
        }
    }

    public ImageView getImageView() {
        return imageView;
    }
}
