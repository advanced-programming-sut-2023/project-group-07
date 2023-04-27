package model;

import java.util.ArrayList;
import java.util.Collections;

public class Person {
    private Government government;
    protected TypeOfPerson typeOfPerson;
    protected int hp;
    protected int[] currentLocation;
    protected UnitStance unitStance;
    protected ArrayList<int[]> movePattern = new ArrayList<>();
    protected int movesLeft;
    protected int[] patrolLocation;
    protected boolean patrolling;
    protected int damage;
    protected int bonusDamageRate;

    public Person(TypeOfPerson typeOfPerson, int[] currentLocation, Government government) {
        this.typeOfPerson = typeOfPerson;
        this.currentLocation = currentLocation;
        this.hp = typeOfPerson.getHp();
        this.government = government;
        this.movesLeft = typeOfPerson.getSpeed();
        unitStance= UnitStance.STAND_GROUND;
        patrolling=false;
        this.damage=typeOfPerson.getDamage();
        this.bonusDamageRate=1;
    }

    public int[] getCurrentLocation() {
        return currentLocation;
    }

    public TypeOfPerson getTypeOfPerson() {
        return typeOfPerson;
    }

    public void setPatrolling(boolean patrolling) {
        this.patrolling = patrolling;
    }

    public boolean isPatrolling() {
        return patrolling;
    }

    public void setPatrolLocation(int[] patrolLocation) {
        this.patrolLocation = patrolLocation;
    }

    public int[] getPatrolLocation() {
        return patrolLocation;
    }

    public UnitStance getUnitStance() {
        return unitStance;
    }

    public void setUnitStance(UnitStance unitStance) {
        this.unitStance = unitStance;
    }

    public ArrayList<int[]> getMovePattern() {
        return movePattern;
    }

    public void setMovePattern(ArrayList<int[]> movePattern) {
        this.movePattern = movePattern;
    }

    public void move() {
        int numberOfMoves = movesLeft;
        for (int i = 0; i < Math.min(numberOfMoves, movePattern.size()); i++) {
            if (i == Math.min(numberOfMoves, movePattern.size()) - 1)
                currentLocation = new int[] { movePattern.get(0)[0], movePattern.get(0)[1] };
            movePattern.remove(0);
            movesLeft--;
        }
    }

    public Government getGovernment() {
        return government;
    }
}