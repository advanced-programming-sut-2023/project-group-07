package Client.model;

public class Tower extends Building {
    public Tower(Government government, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(government, typeOfBuilding, row, column);
    }

    public Tower(LordColor lordColor, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(lordColor, typeOfBuilding, row, column);
    }
}
