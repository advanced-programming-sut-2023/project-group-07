package Client.model;

import Client.view.Colors;

public enum Texture {
    LAND("land", Colors.YELLOW_BACKGROUND, true, true, true),
    PEBBLE("pebble", Colors.MAGENTA_BACKGROUND_BRIGHT, false, true, true),
    ROCKS("rocks", Colors.RED_BACKGROUND_BRIGHT, false, false, false),
    STONE("stone", Colors.MAGENTA_BACKGROUND, false, true, true),
    IRON("iron", Colors.RED_BACKGROUND, false, true, true),
    GRASS("grass", Colors.GREEN_BACKGROUND_BRIGHT, true, true, true),
    FIELD("field", Colors.GREEN_BACKGROUND, true, true, true),
    MEADOW("meadow", Colors.CYAN_BACKGROUND, true, true, true),
    OIL("oil", Colors.PURPLE_BACKGROUND, false, true, true),
    LARGE_POND("large pond", Colors.BLUE_BACKGROUND, false, false, false),
    SMALL_POND("small pond", Colors.BLUE_BACKGROUND, false, false, false),
    BEACH("beach", Colors.YELLOW_BACKGROUND_BRIGHT, false, true, true),
    RIVER("river", Colors.BLUE_BACKGROUND, false, false, false),
    FORD("ford", Colors.BLUE_BACKGROUND_BRIGHT, false, false, true),
    MARSH("marsh", Colors.PURPLE_BACKGROUND_BRIGHT, true, false, true),
    SEA("sea", Colors.BLUE_BACKGROUND, false, false, false);

    private final String type;
    private final Colors color;
    private final boolean canHaveTree;
    private final boolean canDropBuilding;
    private final boolean canDropUnit;

    private Texture(String type, Colors color, boolean canHaveTree, boolean canDropBuilding, boolean canDropUnit) {
        this.type = type;
        this.color = color;
        this.canHaveTree = canHaveTree;
        this.canDropBuilding = canDropBuilding;
        this.canDropUnit = canDropUnit;
    }

    public Colors getColor() {
        return color;
    }

    public static Texture getTexture(String name) {
        for (Texture texture : Texture.values())
            if (texture.type.equals(name))
                return texture;
        return null;
    }

    public boolean canHaveTree() {
        return canHaveTree;
    }

    public boolean canDropBuilding() {
        return canDropBuilding;
    }

    public Texture cloneTexture() {
        return getTexture(this.type);
    }

    public boolean canDropUnit() {
        return canDropUnit;
    }

    @Override
    public String toString() {
        return this.type;
    }

}