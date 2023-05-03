package model;

public class Tunneler extends Engineer { // todo : when making tunneler, call this class not its super classes
    private boolean isAvailable;

    public Tunneler(UnitTypes unitType, int[] currentLocation, Government government) {
        super(unitType, currentLocation, government);
        isAvailable = true;
    }

    public Tunneler(UnitTypes unitType, int[] currentLocation, LordColor lordColor) {
        super(unitType, currentLocation, lordColor);
        isAvailable = true;

    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
        // todo : end turn: 1) braking wall and die. 2) go to nearest keep

}
