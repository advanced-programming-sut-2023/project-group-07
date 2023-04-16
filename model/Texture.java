package model;
import view.Colors;
public enum Texture {
    LAND("land" , Colors.YELLOW_BACKGROUND),
    PEBBLE("pebble" , Colors.MAGENTA_BACKGROUND_BRIGHT),
    ROCK("rock" , Colors.RED_BACKGROUND_BRIGHT),
    STONE("stone" , Colors.MAGENTA_BACKGROUND),
    IRON("iron" , Colors.RED_BACKGROUND),
    GRASS("grass" , Colors.GREEN_BACKGROUND_BRIGHT),
    FIELD("field" , Colors.GREEN_BACKGROUND),
    MEADOW("meadow" , Colors.CYAN_BACKGROUND),
    OIL("oil" , Colors.PURPLE_BACKGROUND),
    LARGE_POND("large pond" , Colors.BLUE_BACKGROUND),
    SMALL_POND("small pond" , Colors.BLUE_BACKGROUND),
    BEACH("beach" , Colors.YELLOW_BACKGROUND_BRIGHT),
    RIVER("river" , Colors.BLUE_BACKGROUND),
    FORD ("ford" , Colors.BLUE_BACKGROUND_BRIGHT),
    MARSH("marsh" , Colors.PURPLE_BACKGROUND_BRIGHT),
    SEA("sea" , Colors.BLUE_BACKGROUND);
    private final String texture;
    private final Colors color;
    private Texture(String texture , Colors color) {
        this.texture = texture;
        this.color = color;
    }
    public static String getName(Texture texture){
        return texture.texture;
    }
    public static Colors getColor(Texture texture){
        return texture.color;
    }
    public static Texture getTexture(String name){
        for(Texture texture : Texture.values())
            if(Texture.getName(texture).equals(name))
                return texture;
        return null;
    }

}