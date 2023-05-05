package model;

import controller.Controller;

public class Tunneler extends Engineer { // todo : when making tunneler, call this class not its super classes
    private boolean isAvailable;
    private boolean isTunneling;

    public Tunneler(UnitTypes unitType, int[] currentLocation, Government government) {
        super(unitType, currentLocation, government);
        isAvailable = true;
        isTunneling = false;
    }

    public Tunneler(UnitTypes unitType, int[] currentLocation, LordColor lordColor) {
        super(unitType, currentLocation, lordColor);
        isAvailable = true;
        isTunneling = false;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public void move() {
        if (!isTunneling) super.move();
        else {
            int numberOfMoves = movesLeft;
            for (int i = 0; i < Math.min(numberOfMoves, movePattern.size()); i++) {
                int currentX = movePattern.get(0)[0], currentY = movePattern.get(0)[1];
                currentLocation = new int[]{currentX, currentY};
                movePattern.remove(0);
                movesLeft--;

                if (Controller.isThereWall(currentX, currentY)) {
                    Controller.breakWall(currentX, currentY);
                    instantDie();
                }
            }
        }
    }

    @Override
    public void endTurn() {
        super.endTurn();// todo : delete if it is not supposed to be executed
        if (!isAvailable && !isTunneling && movePattern.size() == 0) {
            isTunneling = true;
            isInvisible = true;
            setMovePattern(Controller.getPathToWall(currentLocation[0], currentLocation[1], government));
        }
    }
}
