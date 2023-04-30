package model;

public class Unit extends Person {
    protected UnitTypes type;
    protected UnitStance unitStance;
    protected int damage;
    protected int bonusDamageRate;
    protected boolean isAttacking;
    protected Person personBeingAttacked;
    protected boolean areaAttacking;
    protected int[] areaAttackLocation;

    public Unit(UnitTypes type, int[] currentLocation, Government government) {
        super(currentLocation, government);
        this.type = type;
        super.hp = type.getHp();
        super.movesLeft = type.getSpeed();
        unitStance = UnitStance.STAND_GROUND;
        this.damage = type.getDamage();
        this.bonusDamageRate = 1;
        this.isAttacking=false;
        this.areaAttacking=false;
    }

    public Unit(UnitTypes type, int[] currentLocation, LordColor lordColor) {
        super(currentLocation, lordColor);
        this.type = type;
        super.hp = type.getHp();
        super.movesLeft = type.getSpeed();
        unitStance = UnitStance.STAND_GROUND;
        this.damage = type.getDamage();
        this.bonusDamageRate = 1;
        this.isAttacking=false;
        this.areaAttacking=false;
    }

    public int[] getAreaAttackLocation() {
        return areaAttackLocation;
    }

    public boolean isAreaAttacking() {
        return areaAttacking;
    }

    public void setAreaAttackLocation(int[] areaAttackLocation) {
        this.areaAttackLocation = areaAttackLocation;
    }

    public void setAreaAttacking(boolean areaAttacking) {
        this.areaAttacking = areaAttacking;
    }

    public UnitTypes getType() {
        return type;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void setPersonBeingAttacked(Person personBeingAttacked) {
        this.personBeingAttacked = personBeingAttacked;
    }

    public Person getPersonBeingAttacked() {
        return personBeingAttacked;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public UnitStance getUnitStance() {
        return unitStance;
    }

    public void setUnitStance(UnitStance unitStance) {
        this.unitStance = unitStance;
    }

    public void endTurn() {
        // todo : attack depending on state
    }

}