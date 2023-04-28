package model;

public class Unit extends Person {
    protected TypeOfPerson typeOfPerson;
    protected UnitStance unitStance;
    protected int damage;
    protected int bonusDamageRate;
    public Unit(TypeOfPerson typeOfPerson, int[] currentLocation, Government government){
        super(typeOfPerson, currentLocation, government);
        this.typeOfPerson = typeOfPerson;
        unitStance= UnitStance.STAND_GROUND;
        this.damage=typeOfPerson.getDamage();
        this.bonusDamageRate=1;
    }
    public Unit(TypeOfPerson typeOfPerson, int[] currentLocation, LordColor lordColor){
        super(typeOfPerson, currentLocation, lordColor);
        this.typeOfPerson = typeOfPerson;
        unitStance= UnitStance.STAND_GROUND;
        this.damage=typeOfPerson.getDamage();
        this.bonusDamageRate=1;
    }

    public TypeOfPerson getTypeOfPerson() {
        return typeOfPerson;
    }

    public UnitStance getUnitStance() {
        return unitStance;
    }

    public void setUnitStance(UnitStance unitStance) {
        this.unitStance = unitStance;
    }

}