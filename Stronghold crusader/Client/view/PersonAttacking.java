package Client.view;

import Client.model.Unit;
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
                if(((Unit)personPane.getPerson()).getType().getRange()==1)
                    attackingDirection = showDirection(personPane.getPerson().getCurrentLocation()[0],
                            personPane.getPerson().getCurrentLocation()[1],
                            personPane.getPerson().getMovePattern().get(0)[0],
                            personPane.getPerson().getMovePattern().get(0)[1]);
                personPane.getPersonImage().setImage(new Image(directory+"attacking "+attackingDirection+"/anim ("+(i+1)+").png",30,60,false,false));
            }
        }
    }
    public String showDirection(int x1,int y1,int x2,int y2) {
        if(x1==x2){
            if(y1>y2)
                return "left";
            if(y1<y2)
                return "right";
        }
        if(x1>x2)
            return "up";
        if(x1<x2)
            return "down";
        return null;
    }
}
