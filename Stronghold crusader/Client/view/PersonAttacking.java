package Client.view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class PersonAttacking extends Transition {
    String directory;
    int frameCount = 8;
    PersonPane personPane;
    String attackingDirection;

    public PersonAttacking(String directory,PersonPane personPane,String attackingDirection) {
        this.directory = directory;
        this.personPane = personPane;
        this.attackingDirection = attackingDirection;
        setCycleDuration(Duration.millis(1000));
        setCycleCount(3);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        for(int i=0;i<frameCount;i++) {
            if((double)i/frameCount<frac && frac<=((double)i+1)/frameCount){
                personPane.getPersonImage().setImage(new Image(directory+attackingDirection+"/anim ("+(i+1)+").png",30,60,false,false));
            }
        }
    }
}
