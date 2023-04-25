
package model;

public enum TypeOfBuilding {
    WALL_STAIRS(250, 1, 1, 0, "wall stairs", null, 0, Resources.STONE, 1),
    LOW_WALL(250, 1, 1, 0, "low wall", null, 0, Resources.STONE, 1),
    STONE_WALL(800, 1, 1, 0, "stone wall", null, 0, Resources.STONE, 2),
    CRENELATED_WALL(1000, 1, 1, 0, "wall stairs", null, 0, Resources.STONE, 2),
    LITTLE_STONE_GATE(1000, 5, 5, 0, "little stone gate", null, 0, Resources.STONE, 20),
    LARGE_STONE_GATE(2000, 7, 7, 0, "large stone gate", null, 0, Resources.STONE, 10),
    DRAW_BRIDGE(-1, 5, 5, 0, "draw bridge", null, 0, Resources.WOOD, 10),
    LOOK_OUT_TOWER(250, 3, 3, 0, "look out tower", null, 0, Resources.STONE, 10),
    PERIMETER_TURRENT(1000, 4, 4, 0, "perimeter turret", null, 0, Resources.STONE, 10),
    DEFENCE_TURRENT(1200, 5, 5, 0, "defence turret", null, 0, Resources.STONE, 15),
    SQUARE_TOWER(1600, 6, 6, 0, "square tower", null, 0, Resources.STONE, 35),
    ROUND_TOWER(2000, 6, 6, 0, "round towernull", null, 0, Resources.STONE, 40),
    ARMOURY(500, 4, 4, 0, "armoury", null, 0, Resources.WOOD, 5),
    MERCENARY_POST(400, 10, 10, 0, "mercenary post", null, 0, Resources.WOOD, 10),
    BARRACKS(500, 10, 10, 0, "barracks", null, 0, Resources.STONE, 15),
    ENGINEERS_GUILD(400, 5, 10, 0, "engineer's guild", null, 100, Resources.WOOD, 10),
    KILLING_PIT(-1, 1, 1, 0, "killing pit", null, 0, Resources.WOOD, 6),
    INN(400, 6, 6, 1, "inn", null, 100, Resources.WOOD, 20),
    MILL(200, 3, 3, 3, "mill", null, 0, Resources.WOOD, 20),
    IRON_MINE(300, 4, 4, 2, "iron mine", Texture.IRON, 0, Resources.WOOD, 20),
    OX_TETHER(100, 2, 2, 1, "ox tether", null, 0, Resources.WOOD, 5),
    QUARRY(350, 6, 6, 3, "quarry", Texture.STONE, 0, Resources.WOOD, 20),
    MARKET(200, 5, 5, 0, "market", null, 0, Resources.WOOD, 5),
    PITCH_RIG(200, 4, 4, 2, "pitch rig", Texture.OIL, 0, Resources.WOOD, 20),
    STOCK_PILE(-1, 5, 5, 0, "stock pile", null, 0, Resources.WOOD, 0),
    WOODCUTTER(150, 3, 3, 1, "woodcutter", null, 0, Resources.WOOD, 3),
    HOVEL(200, 4, 4, 0, "hovel", null, 0, Resources.WOOD, 6),
    CHURCH(600, 9, 9, 1, "church", null, 500, Resources.WOOD, 0),
    CATHEDRAL(1200, 13, 13, 1, "cathedral", null, 1000, Resources.WOOD, 0),
    ARMOURER(500, 4, 4, 1, "armourer", null, 100, Resources.WOOD, 20),
    BLACKSMITH(500, 4, 4, 1, "blacksmith", null, 200, Resources.WOOD, 20),
    POLETURNER(500, 4, 4, 1, "poleturner", null, 100, Resources.WOOD, 10),
    FLETCHER(500, 4, 4, 1, "fletcher", null, 100, Resources.WOOD, 20),
    TANNER(500, 4, 4, 1, "tanner", null, 100, Resources.WOOD, 100),
    OIL_SMELTER(-1, 1, 1, 0, "oil smelter", null, 100, Resources.IRON, 10),
    PITCH_DITCH(300, 4, 8, 1, "pitch ditch", null, 0, Resources.PITCH, 1),
    CAGED_WAR_DOGS(-1, 2, 2, 0, "caged war dogs", null, 100, Resources.WOOD, 10),
    SIEGE_TENT(100, 3, 3, 0, "siege tent", null, 0, Resources.WOOD, 0),
    STABLE(300, 6, 6, 1, "stable", null, 400, Resources.WOOD, 20),
    APPLE_ORCHARD(200, 9, 9, 1, "apple orchard", Texture.MEADOW, 0, Resources.WOOD, 5),
    DIARY_FARMER(200, 9, 9, 1, "diary farmer", Texture.MEADOW, 0, Resources.WOOD, 10),
    WHEAT_FARMER(200, 9, 9, 1, "wheat farmer", Texture.MEADOW, 0, Resources.WOOD, 15),
    HOPS_FARMER(200, 9, 9, 1, "hops farmer", Texture.MEADOW, 0, Resources.WOOD, 15),
    HUNTER_POST(200, 9, 9, 1, "hunter post", null, 0, Resources.WOOD, 10),
    BAKERY(300, 4, 4, 1, "bakery", null, 0, Resources.WOOD, 10),
    BREWER(300, 4, 4, 1, "brewer", null, 0, Resources.WOOD, 10),
    GRANARY(250, 4, 4, 1, "granary", null, 0, Resources.WOOD, 5),
    LORD_KEEP(-1, 7, 7, 0, "lord keep", null, 0, Resources.WOOD, 0);

    private int hp;
    private int length;
    private int width;
    private int workerInUse;
    private String buildingName;
    private Texture texture;
    private int cost;
    private Resources resourceNeeded;
    private int resourceAmount;

    private TypeOfBuilding(int hp, int length, int width, int workerInUse, String buildingName, Texture texture,
            int cost, Resources resourceNeeded, int resourceAmount) {
        this.hp = hp;
        this.length = length;
        this.width = width;
        this.workerInUse = workerInUse;
        this.buildingName = buildingName;
        this.cost = cost;
        this.resourceNeeded = resourceNeeded;
    }

    public int getHp() {
        return hp;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public static TypeOfBuilding getBuilding(String input) { // todo : make this for
        for (TypeOfBuilding typeOfBuilding : TypeOfBuilding.values()) {
            if (typeOfBuilding.buildingName.equals(input))
                return typeOfBuilding;
        }
        return null;
    }

    public int getCost() {
        return cost;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public Resources getResourceNeeded() {
        return resourceNeeded;
    }

    public int getWorkerInUse() {
        return workerInUse;
    }
    @Override
    public String toString() {
        return buildingName;
    }
}