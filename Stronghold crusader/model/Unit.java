package model;

public class Unit extends Person {
    public Unit(TypeOfPerson typeOfPerson, int[] currentLocation, Government government){
        super(typeOfPerson, currentLocation, government);
    }
    public Unit(TypeOfPerson typeOfPerson, int[] currentLocation, LordColor lordColor){
        super(typeOfPerson, currentLocation, lordColor);
    }
}