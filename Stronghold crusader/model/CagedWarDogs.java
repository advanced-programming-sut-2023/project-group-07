package model;

import controller.Controller;

public class CagedWarDogs extends Building {
    public CagedWarDogs(Government government, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(government, typeOfBuilding, row, column);
    }

    public CagedWarDogs(LordColor lordColor, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(lordColor, typeOfBuilding, row, column);
    }

    public void releaseDogs() {
        Controller.releaseDogs(3, row, column);
    }
}
