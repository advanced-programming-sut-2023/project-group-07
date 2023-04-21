package model;

import java.util.*;
import model.Texture;

public class MapPixel {
    private Texture texture;
    private ArrayList<Person> people = new ArrayList<Person>();
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private LordColor lordKeep = null;
    private Tree tree = null;
    private Rock rock = null;
    private boolean doesHaveOil;
    private boolean isPassable;

    public MapPixel(Texture texture, boolean doesHaveOil, boolean isPassable) {
        this.texture = texture;
        this.doesHaveOil = doesHaveOil;
        this.isPassable = isPassable;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void backToDefault() {
        this.texture = Texture.LAND;
        this.doesHaveOil = false;
        this.isPassable = true;
        this.rock = null;
        this.tree = null;
        this.buildings.clear();
        this.people.clear();
    }

    public ArrayList<Person> getPeople() {
        return (ArrayList<Person>) this.people.clone();
    }

    public ArrayList<Building> getBuildings() {
        return (ArrayList<Building>) this.buildings.clone();
    }

    public Tree getTree() {
        return tree;
    }

    public void setRock(Rock rock) {
        this.rock = rock;
    }

    public void setPlayerKeep(LordColor lordKeep) {
        this.lordKeep = lordKeep;
    }

    public Rock getRock() {
        return rock;
    }

    public boolean canDropObject() {
        return (buildings.size() == 0 && rock == null && tree == null && lordKeep == null);
    }

    private boolean doesHaveSoldier() {
        for (Person person : people)
            if (person instanceof Unit)
                return true;
        return false;
    }

    private boolean doesHaveWall() {
        for (Building building : buildings) {
            if (building instanceof Tower || building instanceof GateHouse)
                return true;
            if (building.getTypeOfBuilding().equals(TypeOfBuilding.LOW_WALL))
                return true;
            if (building.getTypeOfBuilding().equals(TypeOfBuilding.STONE_WALL))
                return true;
            if (building.getTypeOfBuilding().equals(TypeOfBuilding.CRENELATED_WALL))
                return true;
        }
        return false;
    }

    public String objectToShow() {
        if(this.lordKeep != null)
            return "K";
        if (doesHaveSoldier())
            return "S";
        if (this.buildings.size() != 0)
            return doesHaveWall() ? "W" : "B";
        if (this.tree != null)
            return "T";
        if (this.rock != null)
            return "R";
        return "";
    }

}