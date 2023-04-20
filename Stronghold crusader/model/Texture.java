package model;
import view.Colors;
public enum Texture {
    LAND("land" , Colors.YELLOW_BACKGROUND , true,true),
    PEBBLE("pebble" , Colors.MAGENTA_BACKGROUND_BRIGHT , false,true),
    ROCKS("rocks" , Colors.RED_BACKGROUND_BRIGHT , false,false),
    STONE("stone" , Colors.MAGENTA_BACKGROUND , false,true),
    IRON("iron" , Colors.RED_BACKGROUND , false,true),
    GRASS("grass" , Colors.GREEN_BACKGROUND_BRIGHT , true,true),
    FIELD("field" , Colors.GREEN_BACKGROUND , true,true),
    MEADOW("meadow" , Colors.CYAN_BACKGROUND , true,true),
    OIL("oil" , Colors.PURPLE_BACKGROUND , false,true),
    LARGE_POND("large pond" , Colors.BLUE_BACKGROUND , false,false),
    SMALL_POND("small pond" , Colors.BLUE_BACKGROUND , false,false),
    BEACH("beach" , Colors.YELLOW_BACKGROUND_BRIGHT , false,true),
    RIVER("river" , Colors.BLUE_BACKGROUND , false,false),
    FORD ("ford" , Colors.BLUE_BACKGROUND_BRIGHT , false,false),
    MARSH("marsh" , Colors.PURPLE_BACKGROUND_BRIGHT , true,false),
    SEA("sea" , Colors.BLUE_BACKGROUND , false,false);
    private final String type;
    private final Colors color;
    private final boolean canHaveTree;
    private final boolean canDropBuilding;
    private Texture(String type , Colors color , boolean canHaveTree,boolean canDropBuilding) {
        this.type = type;
        this.color = color;
        this.canHaveTree = canHaveTree;
        this.canDropBuilding=canDropBuilding;
    }
    public Colors getColor(){
        return color;
    }
    public static Texture getTexture(String name){
        for(Texture texture : Texture.values())
            if(texture.type.equals(name))
                return texture;
        return null;
    }
    public boolean canHaveTree(){
        return canHaveTree;
    }
    public boolean canDropBuilding(){
        return canDropBuilding;
    }
    
    public Texture cloneTexture(){
        return getTexture(this.type);
    }
    @Override
    public String toString() {
        return this.type;
    }

}