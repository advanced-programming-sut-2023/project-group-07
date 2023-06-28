package Client.model;

import Client.view.Colors;

public enum Texture {
    LAND("land", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), true, true, true),
    PEBBLE("pebble", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, true, true),
    ROCKS("rocks", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, false, false),
    STONE("stone", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, true, true),
    IRON("iron", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, true, true),
    GRASS("grass", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), true, true, true),
    FIELD("field", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), true, true, true),
    MEADOW("meadow", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), true, true, true),
    OIL("oil", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, true, true),
    LARGE_POND("large pond", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, false, false),
    SMALL_POND("small pond", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, false, false),
    BEACH("beach", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, true, true),
    RIVER("river", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, false, false),
    FORD("ford", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, false, true),
    MARSH("marsh", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), true, false, true),
    SEA("sea", Texture.class.getResource("/Images/Game/Tiles/Desert/map.png").toString(), false, false, false);

    private final String type;
    private final String imagePath;
    private final boolean canHaveTree;
    private final boolean canDropBuilding;
    private final boolean canDropUnit;

    private Texture(String type, String imagePath, boolean canHaveTree, boolean canDropBuilding, boolean canDropUnit) {
        this.type = type;
        this.imagePath = imagePath;
        this.canHaveTree = canHaveTree;
        this.canDropBuilding = canDropBuilding;
        this.canDropUnit = canDropUnit;
    }

    public String getImagePath() {
        return imagePath;
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


    public boolean canDropUnit() {
        return canDropUnit;
    }

    @Override
    public String toString() {
        return this.type;
    }

}