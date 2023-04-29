package model;

import controller.Controller;
import controller.Directions;

import java.util.ArrayList;

public class Engineer extends Unit { // todo : make an engineer when making one. not a Unit
    private boolean hasOil;
    private boolean gonnaBringOil;
    private int[] returningLocation;

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
        if (movePattern.size() == 0 && gonnaBringOil) {
            if (government.getResourceAmount(Resources.PITCH) > 0) {
                government.changeResources(Resources.PITCH, -1);
                hasOil = true;
                gonnaBringOil = false;
                setPath(returningLocation[0], returningLocation[1]);
            }
        }
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

    }

    public void goToOilSmelter(ArrayList<int[]> path) {
        gonnaBringOil = true;
        returningLocation = currentLocation;
        setMovePattern(path);
    }

    private void setPath(int finalX, int finalY) {
        Map map = Controller.currentGame.getMap();
        ArrayList<int[]> path = map.getPathList(currentLocation[0], currentLocation[1], finalX, finalY);
        setMovePattern(path);
    }
}
