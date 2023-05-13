package model;

import controller.Controller;
import controller.Directions;

import java.util.ArrayList;

import static model.UnitStance.DEFENSIVE;
import static model.UnitStance.OFFENSIVE;

public class Engineer extends Unit {
    private boolean hasOil;
    private boolean gonnaBringOil;
    private int[] returningLocation;

    public Engineer(UnitTypes unitType, int[] currentLocation, Government government) {
        super(unitType, currentLocation, government);
        hasOil = false;
        gonnaBringOil = false;
    }

    public Engineer(UnitTypes unitType, int[] currentLocation, LordColor lordColor) {
        super(unitType, currentLocation, lordColor);
        hasOil = false;
        gonnaBringOil = false;
    }

    @Override
    public void endTurn() {
        // System.out.println("has oil : " + hasOil + " gonnabringOil " + gonnaBringOil
        // +); // todo delete
        super.endTurn();
        if (gonnaBringOil) {
            if (movePattern == null) {
                if (government.getResourceAmount(Resources.PITCH) > 0) {
                    government.changeResources(Resources.PITCH, -1);
                    hasOil = true;
                }
                gonnaBringOil = false;
                Controller.sendToCoordinate(returningLocation[0], returningLocation[1], this);
            } else if (movePattern.size() == 0) {
                if (government.getResourceAmount(Resources.PITCH) > 0) {
                    government.changeResources(Resources.PITCH, -1);
                    hasOil = true;
                }
                gonnaBringOil = false;
                Controller.sendToCoordinate(returningLocation[0], returningLocation[1], this);
            }
        }
        if (!hasOil)
            return;

        int x = currentLocation[0], y = currentLocation[1];
        Government owner = government;
        ArrayList<Unit> opponentsUnits = Controller.getNearOpponentsUnits(x, y, range(), owner);
        if ((opponentsUnits.size() >= 3 && unitStance.equals(DEFENSIVE)) ||
                (opponentsUnits.size() >= 1 && unitStance.equals(OFFENSIVE))) {
            pourOil(Directions.NORTH); // todo: chose a better direction.
            Controller.sendToOilSmelter(this);
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

}
