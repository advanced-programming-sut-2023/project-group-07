package controller;

public enum TypeOfPixel {
    LAND("land"),
    PEBBLE("pebble"),
    ROCK("rock"),
    STONE("stone"),
    GRASS("grass"),
    FIELD("field"),
    MEADOW("meadow");

    private String type;
    private TypeOfPixel (String type){
        this.type = type;
    }
    public static String getTypeOfPixel(TypeOfPixel typeOfPixel){
        return typeOfPixel.type;
    }
}