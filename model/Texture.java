package model;
import view.Colors;
public enum Texture {
    LAND("land" , Colors.YELLOW_BACKGROUND_BRIGHT),
    PEBBLE("pebble" , Colors.PURPLE_BACKGROUND),
    ROCK("rock" , Colors.PURPLE_BACKGROUND_BRIGHT),
    STONE("stone" , Colors.RED_BACKGROUND),
    GRASS("grass" , Colors.GREEN_BACKGROUND_BRIGHT),
    FIELD("field" , Colors.GREEN_BACKGROUND),
    MEADOW("meadow" , Colors.CYAN_BACKGROUND);

    private String type;
    private final Colors color;
    private Texture(String type , Colors color) {
        this.type = type;
        this.color = color;
    }
    public static String getTypeOfPixel(Texture texture){
        return texture.type;
    }
    public static Colors getColor(Texture texture){
        return texture.color;
    }

}