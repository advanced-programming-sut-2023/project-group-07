package model;
import java.util.*;
<<<<<<< HEAD

=======
import controller.TypeOfPixel;
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
public class MapPixel {
    private TypeOfPixel typeOfPixel;
    private ArrayList <Soldier> soldiers = new ArrayList<Soldier>();
    private ArrayList <Building> buildings = new ArrayList<Building>();
<<<<<<< HEAD
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
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af

}