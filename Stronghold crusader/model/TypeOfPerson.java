package model;

import java.util.ArrayList;
import java.util.Arrays;

public enum TypeOfPerson {
    ARCHER("archer",MilitaryCampType.BARRACKS,12,new ArrayList<>(Arrays.asList(Resources.BOW)),3,),
    CROSSBOWMAN("crossbowman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.CROSSBOW,Resources.LEATHER_ARMOR)),2,),
    SPEARMAN("spearman",MilitaryCampType.BARRACKS,8,new ArrayList<>(Arrays.asList(Resources.SPEAR)),3),
    PIKEMAN("pikeman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.PIKE,Resources.METAL_ARMOR)),2),
    MACEMAN("maceman",MilitaryCampType.BARRACKS,20,new ArrayList<>(Arrays.asList(Resources.MACE,Resources.LEATHER_ARMOR)),4),
    SWORDSMAN("swordsman",MilitaryCampType.BARRACKS,40,new ArrayList<>(Arrays.asList(Resources.SWORD,Resources.METAL_ARMOR)),1),
    KNIGHT("knight",MilitaryCampType.BARRACKS,40,new ArrayList<>(Arrays.asList(Resources.SWORD,Resources.METAL_ARMOR,Resources.HORSE)),5),
    TUNNELER("tunneler",MilitaryCampType.ENGINEER_GUILD,30,new ArrayList<>(Arrays.asList()),4),
    LADDERMAN("ladderman",MilitaryCampType.ENGINEER_GUILD,4,new ArrayList<>(Arrays.asList()),4),
    ENGINEER("engineer",MilitaryCampType.ENGINEER_GUILD,30,new ArrayList<>(Arrays.asList()),3),
    BLACK_MONK("black_monk",MilitaryCampType.CATHEDRAL,10,new ArrayList<>(Arrays.asList()),2),
    ARABIAN_BOW("arabian bow",MilitaryCampType.MERCENARY_POST,75,new ArrayList<>(Arrays.asList()),3),
    SLAVE("slave",MilitaryCampType.MERCENARY_POST,5,new ArrayList<>(Arrays.asList()),4),
    SLINGER("slinger",MilitaryCampType.MERCENARY_POST,12,new ArrayList<>(Arrays.asList()),4),
    ASSASSIN("assassin",MilitaryCampType.MERCENARY_POST,60,new ArrayList<>(Arrays.asList()),3),
    HORSE_ARCHER("horse archer",MilitaryCampType.MERCENARY_POST,80,new ArrayList<>(Arrays.asList()),5),
    ARABIAN_SWORDSMAN("arabian swordsman",MilitaryCampType.MERCENARY_POST,80,new ArrayList<>(Arrays.asList()),1),
    FIRE_THROWERS("fire throwers",MilitaryCampType.MERCENARY_POST,100,new ArrayList<>(Arrays.asList()),3);
    
    private String type;
    private MilitaryCampType militaryCampType;
    private int goldNeeded;
    private ArrayList<Resources> resourcesNeeded;
    private int speed;
    private int range;

    private TypeOfPerson(String type,MilitaryCampType militaryCampType,int goldNeeded,ArrayList<Resources> resourcesNeeded,int speed,int range){
        this.type = type;
        this.militaryCampType=militaryCampType;
        this.goldNeeded=goldNeeded;
        this.resourcesNeeded=resourcesNeeded;
        this.speed=speed;
        this.range=range;
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