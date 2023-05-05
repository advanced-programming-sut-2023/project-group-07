package model;

public class NonMilitary extends Person {
    private NonMilitaryTypes type;
    Building workBuilding;
    public NonMilitary(int[] currentLocation,Government government,NonMilitaryTypes type,Building workBuilding) {
        super(currentLocation, government);
        this.type = type;
        this.workBuilding = workBuilding;
    }

    public Building getWorkBuilding() {
        return workBuilding;
    }

    public void changeDeliveredResource() {
        
    }

    public NonMilitaryTypes getType() {
        return type;
    }
}
