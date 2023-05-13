package model;

public class MilitaryCamp extends Building {
    public MilitaryCamp(Government government, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(government, typeOfBuilding, row, column);
    }

    public MilitaryCamp(LordColor lordColor, TypeOfBuilding typeOfBuilding, int row, int column) {
        super(lordColor, typeOfBuilding, row, column);
    }

}
