package model;

import java.util.ArrayList;

public class Person {
    protected Government government;
    protected LordColor lordColor;
    protected int hp;
    protected int[] currentLocation;
    protected ArrayList<int[]> movePattern = new ArrayList<>();
    protected int movesLeft;
    protected int[] patrolLocation;
    protected boolean patrolling;

    public Person(int[] currentLocation, Government government) {
        this.currentLocation = currentLocation;
        this.government = government;
        this.lordColor = government.getColor();
        patrolling = false;
    }

    public Person(int[] currentLocation, LordColor lordColor) {
        this.currentLocation = currentLocation;
        this.lordColor = lordColor;
        patrolling = false;
    }

    public int[] getCurrentLocation() {
        return currentLocation;
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

    public ArrayList<int[]> getMovePattern() {
        return movePattern;
    }

    public int getHp() {
        return hp;
    }

    public void changeHP(int change) {
        hp += change;
    }

    public void setGovernment(Government government) {
        this.government = government;
    }

    public void setMovePattern(ArrayList<int[]> movePattern) {
        if (movePattern == null)
            return;
        if (!movePattern.isEmpty())
            movePattern.remove(0);
        this.movePattern = movePattern;
    }

    public void move() {
        if (movePattern == null)
            return;
        int numberOfMoves = movesLeft;
        int size = movePattern.size();
        for (int i = 0; i < Math.min(numberOfMoves, size); i++) {
            if (i == Math.min(numberOfMoves, size) - 1)
                currentLocation = new int[] { movePattern.get(0)[0], movePattern.get(0)[1] };
            movePattern.remove(0);
            movesLeft--;
        }
    }

    public Government getGovernment() {
        return government;
    }

    public LordColor getLordColor() {
        return lordColor;
    }

    public void instantDie() {
        hp = 0;
    }

}