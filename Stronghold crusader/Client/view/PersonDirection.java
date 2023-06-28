package Client.view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;


public class PersonDirection extends Transition {
    String directory;
    int frameCount=8;
    Pane mapPane;
    PersonPane personPane;
    String direction;

    public PersonDirection(String directory, PersonPane personPane, String direction) {
        this.directory = directory;
        this.personPane = personPane;
        this.direction = direction;
        setCycleDuration(Duration.millis(1000));
        setCycleCount(-1);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        if(personPane.getPerson().getMovePattern().isEmpty()){
            this.stop();
            personPane.setMoving(false);
        }
        else{
            for(int i=0;i<frameCount;i++) {
                if((double)i/frameCount<frac && frac<=((double)i+1)/frameCount){
                    direction =showDirection(personPane.getPerson().getCurrentLocation()[0],
                            personPane.getPerson().getCurrentLocation()[1],
                            personPane.getPerson().getMovePattern().get(0)[0],
                            personPane.getPerson().getMovePattern().get(0)[1]);
                    personPane.getPersonImage().setImage(new Image(directory+direction+"/anim ("+(i+1)+").png"));
                }
            }
        }
    }

    public void setDirection(String direction) {
        this.direction = direction;
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
