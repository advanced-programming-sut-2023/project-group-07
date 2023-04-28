
package model;

public enum TypeOfBuilding {
    WALL_STAIRS(250, 1, 1, 0, "wall stairs", null, 0, Resources.STONE, 1, false, false, false,null),
    LOW_WALL(250, 1, 1, 0, "low wall", null, 0, Resources.STONE, 1, false, false, false,null),
    STONE_WALL(800, 1, 1, 0, "stone wall", null, 0, Resources.STONE, 2, false, false, false,null),
    CRENELATED_WALL(1000, 1, 1, 0, "wall stairs", null, 0, Resources.STONE, 2, false, false, false,null),
    LITTLE_STONE_GATE(1000, 5, 5, 0, "little stone gate", null, 0, Resources.STONE, 20, false, false, true,null),
    LARGE_STONE_GATE(2000, 7, 7, 0, "large stone gate", null, 0, Resources.STONE, 10, false, false, true,null),
    DRAW_BRIDGE(-1, 5, 5, 0, "draw bridge", null, 0, Resources.WOOD, 10, false, false, false,null),
    LOOK_OUT_TOWER(250, 3, 3, 0, "look out tower", null, 0, Resources.STONE, 10, false, true, false,null),
    PERIMETER_TURRENT(1000, 4, 4, 0, "perimeter turret", null, 0, Resources.STONE, 10, false, true, false,null),
    DEFENCE_TURRENT(1200, 5, 5, 0, "defence turret", null, 0, Resources.STONE, 15, false, true, false,null),
    SQUARE_TOWER(1600, 6, 6, 0, "square tower", null, 0, Resources.STONE, 35, false, true, false,null),
    ROUND_TOWER(2000, 6, 6, 0, "round tower", null, 0, Resources.STONE, 40, false, true, false,null),
    ARMOURY(500, 4, 4, 0, "armoury", null, 0, Resources.WOOD, 5, false, false, false,null),
    MERCENARY_POST(400, 10, 10, 0, "mercenary post", null, 0, Resources.WOOD, 10, true, false, false,null),
    BARRACKS(500, 10, 10, 0, "barracks", null, 0, Resources.STONE, 15, true, false, false,null),
    ENGINEERS_GUILD(400, 5, 10, 0, "engineer's guild", null, 100, Resources.WOOD, 10, true, false, false,null),
    KILLING_PIT(-1, 1, 1, 0, "killing pit", null, 0, Resources.WOOD, 6, false, false, false,null),
    INN(400, 6, 6, 1, "inn", null, 100, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.INNKEEPER),
    MILL(200, 3, 3, 3, "mill", null, 0, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.MILL_BOY),
    IRON_MINE(300, 4, 4, 2, "iron mine", Texture.IRON, 0, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.IRON_MINER),
    OX_TETHER(100, 2, 2, 1, "ox tether", null, 0, Resources.WOOD, 5, false, false, false,NonMilitaryTypes.STONE_MASON),
    QUARRY(350, 6, 6, 3, "quarry", Texture.STONE, 0, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.STONE_MASON),
    MARKET(200, 5, 5, 0, "market", null, 0, Resources.WOOD, 5, false, false, false,null),
    PITCH_RIG(200, 4, 4, 2, "pitch rig", Texture.OIL, 0, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.PITCH_DIGGER),
    STOCK_PILE(-1, 5, 5, 0, "stock pile", null, 0, Resources.WOOD, 0, false, false, false,null),
    WOODCUTTER(150, 3, 3, 1, "woodcutter", null, 0, Resources.WOOD, 3, false, false, false,NonMilitaryTypes.WOODCUTTER),
    HOVEL(200, 4, 4, 0, "hovel", null, 0, Resources.WOOD, 6, false, false, false,NonMilitaryTypes.ORDINARY_PERSON),
    CHURCH(600, 9, 9, 1, "church", null, 500, Resources.WOOD, 0, false, false, false,NonMilitaryTypes.PRIEST),
    CATHEDRAL(1200, 13, 13, 1, "cathedral", null, 1000, Resources.WOOD, 0, false, false, false,NonMilitaryTypes.PRIEST),
    ARMOURER(500, 4, 4, 1, "armourer", null, 100, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.ARMOURER),
    BLACKSMITH(500, 4, 4, 1, "blacksmith", null, 200, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.BLACKSMITH),
    POLETURNER(500, 4, 4, 1, "poleturner", null, 100, Resources.WOOD, 10, false, false, false,NonMilitaryTypes.POLETURNER),
    FLETCHER(500, 4, 4, 1, "fletcher", null, 100, Resources.WOOD, 20, false, false, false,NonMilitaryTypes.FLETCHER),
    TANNER(500, 4, 4, 1, "tanner", null, 100, Resources.WOOD, 100, false, false, false,NonMilitaryTypes.TANNER),
    OIL_SMELTER(300, 4, 8, 0, "oil smelter", null, 100, Resources.IRON, 10, false, false, false,null),
    PITCH_DITCH(-1, 1, 1, 0, "pitch ditch", null, 0, Resources.PITCH, 1, false, false, false,null),
    CAGED_WAR_DOGS(-1, 2, 2, 0, "caged war dogs", null, 100, Resources.WOOD, 10, false, false, false,null),
    SIEGE_TENT(100, 3, 3, 0, "siege tent", null, 0, Resources.WOOD, 0, false, false, false,null),
    STABLE(300, 6, 6, 1, "stable", null, 400, Resources.WOOD, 20, false, false, false,null),
    APPLE_ORCHARD(200, 9, 9, 1, "apple orchard", Texture.MEADOW, 0, Resources.WOOD, 5, false, false, false,NonMilitaryTypes.FARMER),
    DIARY_FARMER(200, 9, 9, 1, "diary farmer", Texture.MEADOW, 0, Resources.WOOD, 10, false, false, false,NonMilitaryTypes.FARMER),
    WHEAT_FARMER(200, 9, 9, 1, "wheat farmer", Texture.MEADOW, 0, Resources.WOOD, 15, false, false, false,NonMilitaryTypes.FARMER),
    HOPS_FARMER(200, 9, 9, 1, "hops farmer", Texture.MEADOW, 0, Resources.WOOD, 15, false, false, false,NonMilitaryTypes.FARMER),
    HUNTER_POST(200, 9, 9, 1, "hunter post", null, 0, Resources.WOOD, 10, false, false, false,NonMilitaryTypes.HUNTER),
    BAKERY(300, 4, 4, 1, "bakery", null, 0, Resources.WOOD, 10, false, false, false,NonMilitaryTypes.BAKER),
    BREWER(300, 4, 4, 1, "brewer", null, 0, Resources.WOOD, 10, false, false, false,NonMilitaryTypes.BREWER),
    GRANARY(250, 4, 4, 1, "granary", null, 0, Resources.WOOD, 5, false, false, false,null),
    LORD_KEEP(-1, 7, 7, 0, "lord keep", null, 0, Resources.WOOD, 0, false, false, false,null);

    private int hp;
    private int length;
    private int width;
    private int workerInUse;
    private String buildingName;
    private Texture texture;
    private int cost;
    private Resources resourceNeeded;
    private int resourceAmount;
    private boolean isMilitaryCamp;
    private boolean isTower;
    private boolean isGate;
    private NonMilitaryTypes workerType;

    private TypeOfBuilding(int hp, int length, int width, int workerInUse, String buildingName, Texture texture,
            int cost, Resources resourceNeeded, int resourceAmount, boolean isMilitaryCamp, boolean isTower,
            boolean isGate,NonMilitaryTypes workerType) {
        this.hp = hp;
        this.length = length;
        this.width = width;
        this.workerInUse = workerInUse;
        this.buildingName = buildingName;
        this.cost = cost;
        this.resourceNeeded = resourceNeeded;
        this.isMilitaryCamp = isMilitaryCamp;
        this.isGate = isGate;
        this.isTower = isTower;
        this.workerType = workerType;
    }

    public boolean isMilitaryCamp() {
        return isMilitaryCamp;
    }

    public boolean isTower() {
        return isTower;
    }

    public boolean isGate() {
        return isGate;
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