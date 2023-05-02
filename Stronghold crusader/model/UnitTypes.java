package model;

import java.util.ArrayList;
import java.util.Arrays;

public enum UnitTypes {
    LORD ("lord",null,0,new ArrayList<>(Arrays.asList()),12,1,5000, 500),
    ARCHER("archer",MilitaryCampType.BARRACKS,12,new ArrayList<>(Arrays.asList(Resources.BOW)),12,20,350,40),
    CROSSBOWMAN("crossbowman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.CROSSBOW,Resources.LEATHER_ARMOR)),9,15,460,60),
    SPEARMAN("spearman",MilitaryCampType.BARRACKS,8,new ArrayList<>(Arrays.asList(Resources.SPEAR)),12,1,350,50),
    PIKEMAN("pikeman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.PIKE,Resources.METAL_ARMOR)),8,1,600,75),
    MACEMAN("maceman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.MACE,Resources.LEATHER_ARMOR)),16,1,460,100),
    SWORDSMAN("swordsman",MilitaryCampType.BARRACKS,40,new ArrayList<>(Arrays.asList(Resources.SWORD,Resources.METAL_ARMOR)),4,1,800,150),
    KNIGHT("knight",MilitaryCampType.BARRACKS,40,new ArrayList<>(Arrays.asList(Resources.SWORD,Resources.METAL_ARMOR,Resources.HORSE)),20,1,900,140),
    TUNNELER("tunneler",MilitaryCampType.ENGINEER_GUILD,30,new ArrayList<>(Arrays.asList()),16,1,150,50),
    LADDERMAN("ladderman",MilitaryCampType.ENGINEER_GUILD,4,new ArrayList<>(Arrays.asList()),16,1,150,20),
    ENGINEER("engineer",MilitaryCampType.ENGINEER_GUILD,30,new ArrayList<>(Arrays.asList()),12,1,150,20),
    BLACK_MONK("black monk",MilitaryCampType.CATHEDRAL,10,new ArrayList<>(Arrays.asList()),8,1,500,30),
    ARABIAN_BOW("arabian bow",MilitaryCampType.MERCENARY_POST,75,new ArrayList<>(Arrays.asList()),12,20,400,50),
    SLAVE("slave",MilitaryCampType.MERCENARY_POST,5,new ArrayList<>(Arrays.asList()),16,1,150,30),
    SLINGER("slinger",MilitaryCampType.MERCENARY_POST,12,new ArrayList<>(Arrays.asList()),16,12,250,35),
    ASSASSIN("assassin",MilitaryCampType.MERCENARY_POST,60,new ArrayList<>(Arrays.asList()),12,1,400,100),
    HORSE_ARCHER("horse archer",MilitaryCampType.MERCENARY_POST,80,new ArrayList<>(Arrays.asList()),20,25,460,50),
    ARABIAN_SWORDSMAN("arabian swordsman",MilitaryCampType.MERCENARY_POST,80,new ArrayList<>(Arrays.asList()),4,1,650,130),
    FIRE_THROWERS("fire throwers",MilitaryCampType.MERCENARY_POST,100,new ArrayList<>(Arrays.asList()),12,10,250,90);

    
    private final String type;
    private final MilitaryCampType militaryCampType;
    private final int goldNeeded;
    private final ArrayList<Resources> resourcesNeeded;
    private final int speed;
    private final int range;
    private final int hp;
    private final int damage;
    private UnitTypes(String type,MilitaryCampType militaryCampType,int goldNeeded,ArrayList<Resources> resourcesNeeded,int speed,int range,int hp,int damage){
        this.type = type;
        this.militaryCampType=militaryCampType;
        this.goldNeeded=goldNeeded;
        this.resourcesNeeded=resourcesNeeded;
        this.speed=speed;
        this.range=range;
        this.hp=hp;
        this.damage=damage;
    }
    
    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }
    
    public int getRange() {
        return range;
    }

    public int getHp() {
        return hp;
    }

    public static UnitTypes getUnitTypeFromString(String type){
        for(UnitTypes unitType: UnitTypes.values())
            if(unitType.type.equals(type)) return unitType;
        return null;
    }
    public String getType() {
        return type;
    }
    public int getGoldNeeded() {
        return goldNeeded;
    }
    public MilitaryCampType getMilitaryCampType() {
        return militaryCampType;
    }
    public ArrayList<Resources> getResourcesNeeded() {
        return resourcesNeeded;
    }
    
    @Override
    public String toString() {
        return type;
    }
}