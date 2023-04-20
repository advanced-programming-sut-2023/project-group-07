package model;

import java.util.*;
import model.Texture;

public class MapPixel {
    private Texture texture;
    private ArrayList<Person> people = new ArrayList<Person>();
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private ArrayList<Tree> trees = new ArrayList<Tree>();
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

    public void addTree(Tree tree) {
        trees.add(tree);
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
        this.buildings.clear();
        this.people.clear();
        this.trees.clear();
    }

    public ArrayList<Person> getPeople() {
        return (ArrayList<Person>) this.people.clone();
    }

    public ArrayList<Building> getBuildings() {
        return (ArrayList<Building>) this.buildings.clone();
    }

    public ArrayList<Tree> getTrees() {
        return (ArrayList<Tree>) trees.clone();
    }

    public void setRock(Rock rock) {
        this.rock = rock;
    }

    public Rock getRock() {
        return rock;
    }

    public boolean canDropObject() {
        return (this.buildings.size() == 0 && this.rock == null && this.trees.size() == 0);
    }

}