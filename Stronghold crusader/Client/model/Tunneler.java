package Client.model;

import Client.controller.Controller;

public class Tunneler extends Unit {
    private boolean isAvailable;
    private boolean isTunneling;
    private int hittingDamage = 500; // todo : writing this in enum or something

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
        if (!isTunneling)
            super.move();
        else {
            int numberOfMoves = movesLeft;
            for (int i = 0; i < Math.min(numberOfMoves, movePattern.size()); i++) {
                int currentX = movePattern.get(0)[0], currentY = movePattern.get(0)[1];
                currentLocation = new int[] { currentX, currentY };
                movePattern.remove(0);
                movesLeft--;
                boolean gonnaDie = false;
                if (Controller.isThereOpponentWall(currentX, currentY, getGovernment())) {
                    Controller.breakOpponentWall(currentX, currentY, getGovernment());
                    gonnaDie = true;
                }
                if (Controller.isThereOpponentTower(currentX, currentY, getGovernment())) {
                    Controller.damageOpponentTower(currentX, currentY, getGovernment(), hittingDamage);
                    gonnaDie = true;
                }
                if (gonnaDie)
                    instantDie();

            }
        }
    }

    @Override
    public void endTurn() {
        super.endTurn();// todo : delete if it is not supposed to be executed
        if (!isAvailable && !isTunneling) {
            if (movePattern == null) {
                isTunneling = true;
                isInvisible = true;
                setMovePattern(Controller.getPathForTunneler(currentLocation[0], currentLocation[1], government));
            } else if (movePattern.size() == 0) {
                isTunneling = true;
                isInvisible = true;
                setMovePattern(Controller.getPathForTunneler(currentLocation[0], currentLocation[1], government));
            }
        }
    }
}
