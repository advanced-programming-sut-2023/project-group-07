package Client.view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class CustomAnimation extends Transition {
    int frameCount = 5;
    String directory;
    ImageView imageView;
    int oldAge = 3;
    int newAge = 3;

    public int getNewAge() {
        return newAge;
    }

    public int getOldAge() {
        return oldAge;
    }

    public void setNewAge(int newAge) {
        this.newAge = newAge;
    }

    public void setOldAge(int oldAge) {
        this.oldAge = oldAge;
    }

    public CustomAnimation(String directory, int x , int y) {
        this.directory = directory;
        imageView = new ImageView(new Image(directory+"/anim (1).png"));
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
