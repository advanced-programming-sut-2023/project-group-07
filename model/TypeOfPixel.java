<<<<<<< HEAD
package model;
import view.Colors;
public enum TypeOfPixel {
    LAND("land" , Colors.YELLOW_BACKGROUND_BRIGHT),
    PEBBLE("pebble" , Colors.PURPLE_BACKGROUND),
    ROCK("rock" , Colors.PURPLE_BACKGROUND_BRIGHT),
    STONE("stone" , Colors.RED_BACKGROUND),
    GRASS("grass" , Colors.GREEN_BACKGROUND_BRIGHT),
    FIELD("field" , Colors.GREEN_BACKGROUND),
    MEADOW("meadow" , Colors.CYAN_BACKGROUND);

    private String type;
    private final Colors color;
    private TypeOfPixel (String type , Colors color) {
        this.type = type;
        this.color = color;
=======
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
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
    }
    public static String getTypeOfPixel(TypeOfPixel typeOfPixel){
        return typeOfPixel.type;
    }
<<<<<<< HEAD
    public static Colors getColor(TypeOfPixel typeOfPixel){
        return typeOfPixel.color;
    }
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
}