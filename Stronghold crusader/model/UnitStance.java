package model;

public enum UnitStance {
    STAND_GROUND("stand ground"),
    DEFENSIVE("defensive"),
    OFFENSIVE("offensive");
    
    String name;
    private UnitStance(String name) {
        this.name = name;
    }

    public static UnitStance getStanceByName(String name) {
        for(UnitStance unitStance: UnitStance.values())
            if(unitStance.name.equals(name))
                return unitStance;
        return null;
    }
    
}