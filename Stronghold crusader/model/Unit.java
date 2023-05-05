package model;

import static model.UnitTypes.ASSASSIN;

public class Unit extends Person {
    protected UnitTypes type;
    protected UnitStance unitStance;
    protected int damage;
    protected int bonusDamageRate;
    protected boolean isAttacking;
    protected Person personBeingAttacked;
    protected Building buildingBeingAttacked;
    protected boolean areaAttacking;
    protected int[] areaAttackLocation;
    protected boolean isInvisible;
    protected boolean isAttackingBuilding;

    public Unit(UnitTypes type, int[] currentLocation, Government government) {
        super(currentLocation, government);
        this.type = type;
        super.hp = type.getHp();
        movesLeft = type.getSpeed();
        unitStance = UnitStance.STAND_GROUND;
        this.damage = type.getDamage();
        this.bonusDamageRate = 1;
        this.isAttacking = false;
        this.isAttackingBuilding = false;
        this.areaAttacking = false;

        if (type.equals(ASSASSIN)) isInvisible = true;
        else isInvisible = false;

    }

    public Unit(UnitTypes type, int[] currentLocation, LordColor lordColor) {
        super(currentLocation, lordColor);
        this.type = type;
        super.hp = type.getHp();
        movesLeft = type.getSpeed();
        unitStance = UnitStance.STAND_GROUND;
        this.damage = type.getDamage();
        this.bonusDamageRate = 1;
        this.isAttacking = false;
        this.areaAttacking = false;
    }

    public int getDamage() {
        return damage;
    }

    public int getBonusDamageRate() {
        return bonusDamageRate;
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

    public void setAttackingBuilding(boolean isAttackingBuilding) {
        this.isAttackingBuilding = isAttackingBuilding;
    }

    public boolean isAttackingBuilding() {
        return isAttackingBuilding;
    }

    public void setPersonBeingAttacked(Person personBeingAttacked) {
        this.personBeingAttacked = personBeingAttacked;
    }

    public Person getPersonBeingAttacked() {
        return personBeingAttacked;
    }

    public void setBuildingBeingAttacked(Building buildingBeingAttacked) {
        this.buildingBeingAttacked = buildingBeingAttacked;
    }

    public Building getBuildingBeingAttacked() {
        return buildingBeingAttacked;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public UnitStance getUnitStance() {
        return unitStance;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    public void setUnitStance(UnitStance unitStance) {
        this.unitStance = unitStance;
    }

    public void endTurn() {
        // todo : attack depending on state
    }

    public int range() {
        return type.getRange();
    }

}