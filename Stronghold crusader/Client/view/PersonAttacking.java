package Client.view;

import Client.model.Game;
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
    Game game;
    int[] nearestEnemy = new int[2];

    public PersonAttacking(String directory, PersonPane personPane, String attackingDirection, Game game) {
        this.game =game;
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
                    nearestEnemy = game.nearestEnemy((Unit)personPane.getPerson(),((Unit)personPane.getPerson()).getType().getRange());
                    if(nearestEnemy!=null){
                        attackingDirection = showDirection(personPane.getPerson().getCurrentLocation()[0],
                                personPane.getPerson().getCurrentLocation()[1],
                                nearestEnemy[0],
                                nearestEnemy[1]);
                        personPane.getPersonImage().setImage(new Image(directory+"attacking "+attackingDirection+"/anim ("+(i+1)+").png",30,60,false,false));
                    }
            }
        }
    }
    public String showDirection(int row1, int column1, int row2, int column2) {
        if(column1 == column2){
            if(row1 > row2)
                    return "up";
            if(row1 < row2)
                return "down";
            return "left";
        }
        double arcTan = Math.atan((double)(row2-row1)/(column2-column1));
        if(column2>column1){
            if(arcTan<=1 && arcTan>=0) return "down";
            if(arcTan>=1 || arcTan<=-1) return "right";
            if(arcTan>=-1) return "up";
        }
        if(column2<column1){
            if(arcTan<=1 && arcTan>=0) return "up";
            if(arcTan>=1 || arcTan<=-1) return "left";
            if(arcTan>=-1) return "down";
        }
        return "left";
    }
}
