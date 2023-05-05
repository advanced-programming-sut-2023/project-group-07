package model;

public class NonMilitary extends Person {
    private NonMilitaryTypes type;
    Building workBuilding;
    private boolean movingResources = false;
    private boolean movingNeededResources = false;
    public NonMilitary(int[] currentLocation,Government government,NonMilitaryTypes type,Building workBuilding) {
        super(currentLocation, government);
        this.type = type;
        this.workBuilding = workBuilding;
    }

    public boolean isMovingNeededResources() {
        return movingNeededResources;
    }

    public void setMovingNeededResources(boolean movingNeededResources) {
        this.movingNeededResources = movingNeededResources;
    }

    public boolean isMovingResources() {
        return movingResources;
    }

    public void setMovingResources(boolean movingResources) {
        this.movingResources = movingResources;
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
