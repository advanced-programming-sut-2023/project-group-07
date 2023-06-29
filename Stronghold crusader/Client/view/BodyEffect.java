package Client.view;

import Client.model.Unit;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BodyEffect extends Transition {
    String directory;
    PersonPane personPane;
    int frameCount = 5;
    public BodyEffect(String directory,PersonPane personPane){
        this.personPane = personPane;
        this.directory = directory;
        setCycleCount(-1);
        setCycleDuration(Duration.millis(1000));
        setInterpolator(Interpolator.LINEAR);
    }
    @Override
    protected void interpolate(double frac) {
        if(!personPane.getBodyEffectImage().isVisible())
            personPane.getBodyEffectImage().setVisible(true);
        for(int i=0;i<frameCount;i++) {
            if((double)i/frameCount<frac && frac<=((double)i+1)/frameCount){
                personPane.getBodyEffectImage().setImage(new Image(directory+"anim ("+(i+1)+").png",30,60,false,false));
            }
        }
//        personPane.getPerson().changeHP();
    }
}
