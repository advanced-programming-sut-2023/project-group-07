package model;

import java.util.*;

public class MapPixel {
    private Texture texture;
    private ArrayList<Unit> units = new ArrayList<Unit>();
    private ArrayList<NonMilitary> nonMilitaries = new ArrayList<NonMilitary>();
    private ArrayList<Building> normalBuildings = new ArrayList<Building>();
    private ArrayList<GateHouse> gateHouses = new ArrayList<GateHouse>();
    private ArrayList<Tower> towers = new ArrayList<Tower>();
    private ArrayList<MilitaryCamp> militaryCamps = new ArrayList<MilitaryCamp>();
    private ArrayList<ConvertingResources> convertingResources = new ArrayList<ConvertingResources>();
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

    public void resetAccess() {
        canBeAccessed = false;
    }

    public void setAccess() {
        canBeAccessed = true;
    }

    public void addSiegeWeapon(SiegeWeapon siegeWeapon) {
        this.siegeWeapons.add(siegeWeapon);
    }

    public void removeSiegeWeapon(SiegeWeapon siegeWeapon) {
        this.siegeWeapons.remove(siegeWeapon);
    }

    public LordColor getLordKeep() {
        return this.lordKeep;
    }

    public void addBuilding(Building building) {
        if (building instanceof GateHouse)
            gateHouses.add((GateHouse) building);
        else if (building instanceof Tower)
            towers.add((Tower) building);
        else if (building instanceof MilitaryCamp)
            militaryCamps.add((MilitaryCamp) building);
        else if (building instanceof ConvertingResources)
            convertingResources.add((ConvertingResources) building);
        else
            normalBuildings.add(building);
    }

    public void addPerson(Person person) {
        if (person instanceof Unit)
            units.add((Unit) person);
        else
            nonMilitaries.add((NonMilitary) person);
    }

    public void removePerson(Person person) {
        if (person instanceof Unit)
            units.remove((Unit) person);
        else
            nonMilitaries.remove((NonMilitary) person);
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
        this.units.clear();
        this.nonMilitaries.clear();
        this.militaryCamps.clear();
        this.towers.clear();
        this.convertingResources.clear();
        this.gateHouses.clear();
        this.normalBuildings.clear();
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Person> getPeople() {
        ArrayList<Person> people = new ArrayList<Person>();
        for (Unit unit : this.units)
            people.add(unit);
        for (NonMilitary nonMilitary : this.nonMilitaries)
            people.add(nonMilitary);
        return people;
    }

    public ArrayList<Building> getBuildings() {
        ArrayList<Building> buildings = new ArrayList<Building>();
        for (GateHouse gateHouse : this.gateHouses)
            buildings.add(gateHouse);
        for (Tower tower : this.towers)
            buildings.add(tower);
        for (MilitaryCamp militaryCamp : this.militaryCamps)
            buildings.add(militaryCamp);
        for (ConvertingResources convertingResources : this.convertingResources)
            buildings.add(convertingResources);
        for (Building building : this.normalBuildings)
            buildings.add(building);
        return buildings;
    }

    public void removeBuilding(Building building) {
        if (building instanceof GateHouse)
            gateHouses.remove((GateHouse) building);
        else if (building instanceof Tower)
            towers.remove((Tower) building);
        else if (building instanceof MilitaryCamp)
            militaryCamps.remove((MilitaryCamp) building);
        else if (building instanceof ConvertingResources)
            convertingResources.remove((ConvertingResources) building);
        else
            normalBuildings.remove(building);
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
        return (getBuildings().size() == 0 && rock == null && tree == null && lordKeep == null);
    }

    private boolean doesHaveWall() {
        for (Building building : normalBuildings) {
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

    private boolean doesHaveLord() {
        for (Unit unit : units)
            if (unit.getType().equals(UnitTypes.LORD))
                return true;
        return false;
    }

    private boolean doesHaveVisibleUnit(Government government) {
        if (government == null)
            return true;
        for (Unit unit : units)
            if (unit.getGovernment().equals(government))
                return true;
            else if (!unit.isInvisible())
                return true;
        return false;
    }

    public String objectToShow(Government government) {
        if (this.lordKeep != null)
            return "K";
        if (doesHaveVisibleUnit(government))
            return doesHaveLord() ? "L" : "S";
        if (getBuildings().size() != 0)
            return doesHaveWall() ? "W" : "B";
        if (!nonMilitaries.isEmpty())
            return "P";
        if (this.tree != null)
            return "T";
        if (this.rock != null)
            return "R";
        return "";
    }

    public String details() {
        String output = "";
        String buildingsStr = "";
        String unitsStr = "";
        String nonMilitariesStr = "";
        output += "type/texture: " + texture.toString() + "\n";
        output += "does have oil? " + ((this.doesHaveOil) ? "yes" : "no");
        for (Building building : getBuildings())
            buildingsStr += building.getTypeOfBuilding().toString() + " (color: " + building.getLordColor().toString()
                    + ") (Hp: " + building.getHp() + ")\n";
        if (!buildingsStr.equals(""))
            output += "\n<< BUILDINGS >>\n" + buildingsStr.trim();
        for (LordColor lordColor : LordColor.values())
            for (UnitTypes unitType : UnitTypes.values()) {
                int counter = 0;
                int hpSum = 0;
                for (Unit unit : units)
                    if (unit.getLordColor().equals(lordColor) && unit.getType().equals(unitType)) {
                        hpSum += unit.getHp();
                        counter++;
                    }
                if (counter > 0) {
                    if (!unitType.equals(UnitTypes.LORD))
                        unitsStr += unitType.toString() + " (color: " + lordColor.toString() + ") (count: " + counter
                                + ") (Average Hp: " + (int) (hpSum / counter) + ")\n";
                    else
                        unitsStr += unitType.toString() + " (color: " + lordColor.toString() + ") (Hp: " + hpSum
                                + ")\n";
                }
            }
        for (LordColor lordColor : LordColor.values())
            for (NonMilitaryTypes nonMilitaryType : NonMilitaryTypes.values()) {
                int counter = 0;
                int hpSum = 0;
                for (NonMilitary nonMilitary : nonMilitaries)
                    if (nonMilitary.getLordColor().equals(lordColor) && nonMilitary.getType().equals(nonMilitaryType)) {
                        hpSum += nonMilitary.getHp();
                        counter++;
                    }
                if (counter > 0) {
                    nonMilitariesStr += nonMilitaryType.toString() + " (color: " + lordColor.toString() + ") (count: "
                            + counter + ") (Average Hp: " + (int) (hpSum / counter) + ")\n";
                }
            }
        if (!unitsStr.equals(""))
            output += "\n<< UNITS >>\n" + unitsStr.trim();
        if (!nonMilitariesStr.equals(""))
            output += "\n<< NON-MILITARIES >>\n" + nonMilitariesStr.trim();
        return output;

    }

    public void pourOil() {
        doesHaveOil = true;
    }
}