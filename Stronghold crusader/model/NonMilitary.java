package model;

public class NonMilitary extends Person {
    NonMilitaryTypes type;
    public NonMilitary(int[] currentLocation,Government government,NonMilitaryTypes type){
        super(currentLocation, government);
        this.type = type;
    }
}
