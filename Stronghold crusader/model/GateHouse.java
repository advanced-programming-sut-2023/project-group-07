package model;

public class GateHouse extends Building {
    private boolean isClosed;

    public GateHouse(Government government, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(government, typeOfBuilding, row, column);
    }

    public GateHouse(LordColor lordColor, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(lordColor, typeOfBuilding, row, column);
    }

    public boolean getState() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }
}
