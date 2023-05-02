package model;

public class NonMilitary extends Person {
    NonMilitaryTypes type;
    Resources resourceNeeded;
    Resources resourceDelivered;
    int neededResourceAmount;
    int deliveredResourceAmount;
    Building workBuilding;
    public NonMilitary(int[] currentLocation,Government government,NonMilitaryTypes type){
        super(currentLocation, government);
        this.type = type;
        //this.workBuilding = workBuilding;
    }

    public void changeDeliveredResource() {
        
    }
}
