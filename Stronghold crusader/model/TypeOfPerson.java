package model;

import java.util.ArrayList;
import java.util.Arrays;

public enum TypeOfPerson {
    ARCHER("archer",MilitaryCampType.BARRACKS,12,new ArrayList<>(Arrays.asList(Resources.BOW))),
    CROSSBOWMAN("crossbowman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.CROSSBOW,Resources.LEATHER_ARMOR))),
    SPEARMAN("spearman",MilitaryCampType.BARRACKS,8,new ArrayList<>(Arrays.asList(Resources.SPEAR))),
    PIKEMAN("pikeman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.PIKE,Resources.METAL_ARMOR))),
    MACEMAN("maceman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.MACE,Resources.LEATHER_ARMOR))),
    SWORDSMAN("swordsman",MilitaryCampType.BARRACKS,40,new ArrayList<>(Arrays.asList(Resources.SWORD,Resources.METAL_ARMOR))),
    KNIGHT("knight",MilitaryCampType.BARRACKS,40,new ArrayList<>(Arrays.asList(Resources.SWORD,Resources.METAL_ARMOR,Resources.HORSE))),
    TUNNELER("tunneler",MilitaryCampType.ENGINEER_GUILD,30,new ArrayList<>(Arrays.asList())),
    LADDERMAN("ladderman",MilitaryCampType.ENGINEER_GUILD,4,new ArrayList<>(Arrays.asList())),
    ENGINEER("engineer",MilitaryCampType.ENGINEER_GUILD,30,new ArrayList<>(Arrays.asList())),
    BLACK_MONK("black_monk",MilitaryCampType.CATHEDRAL,10,new ArrayList<>(Arrays.asList())),
    ARABIAN_BOW("arabian bow",MilitaryCampType.MERCENARY_POST,75,new ArrayList<>(Arrays.asList())),
    SLAVE("slave",MilitaryCampType.MERCENARY_POST,5,new ArrayList<>(Arrays.asList())),
    SLINGER("slinger",MilitaryCampType.MERCENARY_POST,12,new ArrayList<>(Arrays.asList())),
    ASSASSIN("assassin",MilitaryCampType.MERCENARY_POST,60,new ArrayList<>(Arrays.asList())),
    HORSE_ARCHER("horse archer",MilitaryCampType.MERCENARY_POST,80,new ArrayList<>(Arrays.asList())),
    ARABIAN_SWORDSMAN("arabian swordsman",MilitaryCampType.MERCENARY_POST,80,new ArrayList<>(Arrays.asList())),
    FIRE_THROWERS("fire throwers",MilitaryCampType.MERCENARY_POST,100,new ArrayList<>(Arrays.asList()));
    
    private String type;
    private MilitaryCampType militaryCampType;
    private int goldNeeded;
    private ArrayList<Resources> resourcesNeeded;

    private TypeOfPerson(String type,MilitaryCampType militaryCampType,int goldNeeded,ArrayList<Resources> resourcesNeeded){
        this.type = type;
        this.militaryCampType=militaryCampType;
        this.goldNeeded=goldNeeded;
        this.resourcesNeeded=resourcesNeeded;
    }
    public static String getTypeOfPerson(TypeOfPerson typeOfPerson){
        return typeOfPerson.type;
    }
    public static TypeOfPerson getTypeOfPersonFromString(String type){
        for(TypeOfPerson typeOfPerson: TypeOfPerson.values())
            if(typeOfPerson.type.equals(type)) return typeOfPerson;
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
}