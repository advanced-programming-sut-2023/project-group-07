package model;
import java.util.*;
import model.Texture;

public class MapPixel {
    private Texture texture;
    private ArrayList <Person> people = new ArrayList<Person>();
    private ArrayList <Building> buildings = new ArrayList<Building>();
    private boolean doesHaveOil;
    private boolean isPassable;
    public MapPixel(Texture texture, boolean doesHaveOil , boolean isPassable){
        this.texture = texture;
        this.doesHaveOil = doesHaveOil;
        this.isPassable = isPassable;
    }
    public void addBuilding(Building building){
        buildings.add(building);
    }
    public ArrayList<Building> getBuildings() {
        return buildings;
    }
    public ArrayList<Person> getPeople() {
        return people;
    }
    public Texture getTexture() {
        return texture;
    }
    

}