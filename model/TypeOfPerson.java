package model;

public enum TypeOfPerson {
    ARCHER("archer"),
    CROSSBOWMAN("crossbowman"),
    SPEARMAN("spearman"),
    PIKEMAN("pikeman"),
    MACEMAN("maceman"),
    SWORDSMAN("swordsman"),
    KNIGHT("knight"),
    TUNNELER("tunneler"),
    LADDERMAN("ladderman"),
    ENGINEER("engineer"),
    BLACK_MONK("black_monk"),
    ARCHER_BOW("archer bow"),
    SLAVE("slave"),
    SLINGER("slinger"),
    ASSASSIN("assassin"),
    HORSE_ARCHER("horse archer"),
    ARABIAN_SWORDSMAN("arabian swordsman"),
    FIRE_THROWERS("fire throwers");
    
    private String type;
    private TypeOfPerson(String type){
        this.type = type;
    }
    public static String getTypeOfPerson(TypeOfPerson typeOfPerson){
        return typeOfPerson.type;
    }
    
}