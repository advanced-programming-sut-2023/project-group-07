package model;

import controller.Controller;
import controller.Directions;

public class Engineer extends Unit{ // todo : make an engineer when making one. not a Unit
    private boolean hasOil;
    private boolean gonnaBringOil;

    public Engineer(TypeOfPerson typeOfPerson, int[] currentLocation, Government government) {
        super(typeOfPerson, currentLocation, government);
        hasOil = false;
        gonnaBringOil = false;
    }

    public Engineer(TypeOfPerson typeOfPerson, int[] currentLocation, LordColor lordColor) {
        super(typeOfPerson, currentLocation, lordColor);
        hasOil = false;
        gonnaBringOil = false;
    }

    @Override
    public void endTurn() {
        // todo : if it has oil ...
    }

    public boolean hasOil() {
        return hasOil;
    }

    public void pourOil(Directions direction) {
        int[] oilLocation = new int[2];
        oilLocation[0] = currentLocation[0] + direction.x();
        oilLocation[1] = currentLocation[1] + direction.y();
        Controller.currentGame.pourOil(oilLocation[0], oilLocation[1]);
        hasOil = false;
        gonnaBringOil = true;
    }
}
