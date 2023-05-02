package model;

import java.util.*;
import model.Texture;

public class MapPixel {
    private Texture texture;
    private ArrayList<Unit> units = new ArrayList<Unit>();
    private ArrayList<NonMilitary> nonMilitaries = new ArrayList<NonMilitary>();
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private ArrayList<SiegeWeapon> siegeWeapons = new ArrayList<SiegeWeapon>();
    private LordColor lordKeep = null;
    private Tree tree = null;
    private Rock rock = null;
    private boolean doesHaveOil;
    private boolean canBeAccessed = false;

    public MapPixel(Texture texture, boolean doesHaveOil) {
        this.texture = texture;
        this.doesHaveOil = doesHaveOil;
    }

    public boolean canBeAccessed() {
        return canBeAccessed;
    }

    public void resetAccess(){
        canBeAccessed = false;
    }

    public void setAccess(){
        canBeAccessed = true;
    }

    public void addSiegeWeapon(SiegeWeapon siegeWeapon) {
        this.siegeWeapons.add(siegeWeapon);
    }

    public void removeSiegeWeapon(SiegeWeapon siegeWeapon) {
        this.siegeWeapons.remove(siegeWeapon);
    }

    public LordColor getLordKeep(){
        return this.lordKeep;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void addPerson(Person person) {
        if(person instanceof Unit)
            units.add((Unit)person);
        else
            nonMilitaries.add((NonMilitary)person);
    }

    public void removePerson(Person person) {
        if(person instanceof Unit)
            units.remove((Unit)person);
        else
            nonMilitaries.remove((NonMilitary)person);
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
        this.rock = null;
        this.tree = null;
        this.buildings.clear();
        this.units.clear();
        this.nonMilitaries.clear();
    }

    public ArrayList<Person> getPeople() {
        ArrayList <Person> people = new ArrayList<Person>();
        if(!this.units.isEmpty())
        for(Unit unit : this.units)
        people.add(unit);
        if(!this.nonMilitaries.isEmpty())
            for(NonMilitary nonMilitary : this.nonMilitaries)
                people.add(nonMilitary);
        return people;
    }

    public ArrayList<Building> getBuildings() {
        return (ArrayList<Building>) this.buildings.clone();
    }

    public void removeBuilding(Building building) {
        this.buildings.remove(building);
    }

    public void removeUnit(Unit unit) {
        this.units.remove(unit);
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

    private boolean doesHaveLord(){
        for(Unit unit : units)
            if(unit.getType().equals(UnitTypes.LORD))
                return true;
        return false;
    }
    public String objectToShow() {
        if(this.lordKeep != null)
            return "K";
        if (!units.isEmpty())
            return doesHaveLord() ? "L" : "S";
        if (this.buildings.size() != 0)
            return doesHaveWall() ? "W" : "B";
        if (this.tree != null)
            return "T";
        if (this.rock != null)
            return "R";
        return "";
    }

    public String details(){
        String output = "";
        String buildingsStr = "";
        String unitsStr = "";
        output += "type/texture :" + texture.toString();
        for(Building building : this.buildings)
            buildingsStr += building.getTypeOfBuilding().toString() + " (color: " + building.getLordColor().toString() + ")\n";
        if (!buildingsStr.equals(""))
            output += "\n<< BUILDINGS >>\n" + buildingsStr.trim();
        for(LordColor lordColor : LordColor.values())
            for(UnitTypes unitType : UnitTypes.values()){
                int counter=0;
                for(Unit unit : units)
                    if(unit.getLordColor().equals(lordColor) && unit.getType().equals(unitType))
                        counter++;
                if(counter > 0){
                    if(!unitType.equals(unitType.LORD)) 
                        unitsStr += unitType.toString() + " (color: " + lordColor.toString() + ") (count: " + counter + ")\n";
                    else
                        unitsStr += unitType.toString() + " (color: " + lordColor.toString() + ")\n";
                }
            }
        if(!unitsStr.equals(""))
            output += "\n<< UNITS >>\n" + unitsStr.trim();
        return output;
    }

    public void pourOil() {
        doesHaveOil = true;
    }
}