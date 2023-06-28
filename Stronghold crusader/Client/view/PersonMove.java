package Client.view;

import Client.controller.GameMenuController;
import Client.model.Unit;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.util.Duration;

public class PersonMove extends Transition {
    PersonPane personPane;
    GameMenuController gameMenuController;
    public PersonMove(PersonPane personPane, GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
        this.personPane = personPane;
        setCycleDuration(Duration.INDEFINITE);
        setCycleCount(-1);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        if(!personPane.getPerson().getMovePattern().isEmpty()) {
            personPane.getPersonDirection().direction = personPane.getPersonDirection().showDirection(personPane.getPerson().getCurrentLocation()[0],
                    personPane.getPerson().getCurrentLocation()[1],
                    personPane.getPerson().getMovePattern().get(0)[0],
                    personPane.getPerson().getMovePattern().get(0)[1]);
            switch (personPane.getPersonDirection().direction){
                case "left" -> {
                    if((double)personPane.getPerson().getMovePattern().get(0)[1]<personPane.getLayoutX()/40) {
                        personPane.setLayoutX(personPane.getLayoutX()-0.1*(((Unit)personPane.getPerson()).getType().getSpeed()));
                    }
                    else
                        gameMenuController.applyPersonMove(personPane.getPerson());
                }
                case "right" -> {
                    if((double)personPane.getPerson().getMovePattern().get(0)[1]>personPane.getLayoutX()/40) {
                        personPane.setLayoutX(personPane.getLayoutX()+0.1*(((Unit)personPane.getPerson()).getType().getSpeed()));
                    }
                    else
                        gameMenuController.applyPersonMove(personPane.getPerson());
                }
                case "down" -> {
                    if((double)personPane.getPerson().getMovePattern().get(0)[0]>personPane.getLayoutY()/40) {
                        personPane.setLayoutY(personPane.getLayoutY()+0.1*(((Unit)personPane.getPerson()).getType().getSpeed()));
                    }
                    else
                        gameMenuController.applyPersonMove(personPane.getPerson());
                }
                case "up" -> {
                    if((double)personPane.getPerson().getMovePattern().get(0)[0]<personPane.getLayoutY()/40) {
                        personPane.setLayoutY(personPane.getLayoutY()-0.1*(((Unit)personPane.getPerson()).getType().getSpeed()));
                    }
                    else
                        gameMenuController.applyPersonMove(personPane.getPerson());
                }
            }
        }
        else{
            personPane.setMoving(false);
            this.stop();
        }
    }
}
