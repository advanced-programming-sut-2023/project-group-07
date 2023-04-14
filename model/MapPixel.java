package model;
import java.util.*;
import model.Texture;

public class MapPixel {
    private Texture texture;
    private ArrayList <Soldier> soldiers = new ArrayList<Soldier>();
    private ArrayList <Building> buildings = new ArrayList<Building>();
    private boolean doesHaveOil;
    private boolean isPassable;
    public MapPixel(Texture texture, boolean doesHaveOil , boolean isPassable){
        this.texture = texture;
        this.doesHaveOil = doesHaveOil;
        this.isPassable = isPassable;
    }
    public Texture getTypeOfPixel() {
        return texture;
    }
    public void addBuilding(Building building){
        buildings.add(building);
    }

}