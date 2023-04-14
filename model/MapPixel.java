package model;
import java.util.*;
import controller.TypeOfPixel;

public class MapPixel {
    private TypeOfPixel typeOfPixel;
    private ArrayList <Soldier> soldiers = new ArrayList<Soldier>();
    private ArrayList <Building> buildings = new ArrayList<Building>();
    private boolean doesHaveOil;
    private boolean isPassable;
    public MapPixel(TypeOfPixel typeOfPixel , boolean doesHaveOil , boolean isPassable){
        this.typeOfPixel = typeOfPixel;
        this.doesHaveOil = doesHaveOil;
        this.isPassable = isPassable;
    }
    public TypeOfPixel getTypeOfPixel() {
        return typeOfPixel;
    }
    public void addBuilding(Building building){
        buildings.add(building);
    }

}