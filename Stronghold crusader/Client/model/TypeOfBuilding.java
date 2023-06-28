
package Client.model;

public enum TypeOfBuilding {
    WALL_STAIRS(250, 1, 1, 0, "wall stairs", null, 0, Resources.STONE, 1, "wall", null, 1),
    LOW_WALL(250, 1, 1, 0, "low wall", null, 0, Resources.STONE, 1, "wall", null, 4),
    STONE_WALL(800, 1, 1, 0, "stone wall", null, 0, Resources.STONE, 2, "wall", null, 6),
    CRENELATED_WALL(1000, 1, 1, 0, "crenelated wall", null, 0, Resources.STONE, 2, "wall", null, 7),
    LITTLE_STONE_GATE(1000, 5, 5, 0, "little stone gate", null, 0, Resources.STONE, 20, "gate", null, 8),
    LARGE_STONE_GATE(2000, 7, 7, 0, "large stone gate", null, 0, Resources.STONE, 30, "gate", null, 11),
    DRAW_BRIDGE(-1, 5, 5, 0, "draw bridge", null, 0, Resources.WOOD, 10, "bridge", null, 0),
    LOOK_OUT_TOWER(250, 3, 3, 0, "look out tower", null, 0, Resources.STONE, 10, "tower", null, 18),
    PERIMETER_TURRENT(1000, 4, 4, 0, "perimeter turrent", null, 0, Resources.STONE, 10, "tower", null, 8),
    DEFENCE_TURRENT(1200, 5, 5, 0, "defence turrent", null, 0, Resources.STONE, 15, "tower", null, 9),
    SQUARE_TOWER(1600, 6, 6, 0, "square tower", null, 0, Resources.STONE, 35, "tower", null, 13),
    ROUND_TOWER(2000, 6, 6, 0, "round tower", null, 0, Resources.STONE, 40, "tower", null, 13),
    ARMOURY(500, 4, 4, 0, "armoury", null, 0, Resources.WOOD, 5, "building", null, 5),
    MERCENARY_POST(400, 5, 5, 0, "mercenary post", null, 0, Resources.WOOD, 10, "military camp", null, 3),
    BARRACKS(500, 6, 6, 0, "barracks", null, 0, Resources.STONE, 15, "military camp", null, 6),
    ENGINEERS_GUILD(400, 5, 5, 0, "engineers guild", null, 100, Resources.WOOD, 10, "military camp", null, 5),
    KILLING_PIT(-1, 1, 1, 0, "killing pit", null, 0, Resources.WOOD, 6, "building", null, 0),
    INN(400, 6, 6, 1, "inn", null, 100, Resources.WOOD, 20, "converting resources", NonMilitaryTypes.INNKEEPER, 6),
    MILL(200, 6, 6, 3, "mill", null, 0, Resources.WOOD, 20, "converting resources", NonMilitaryTypes.MILL_BOY, 8),
    IRON_MINE(300, 4, 4, 2, "iron mine", Texture.IRON, 0, Resources.WOOD, 20, "converting resources",
            NonMilitaryTypes.IRON_MINER, 4),
    OX_TETHER(100, 2, 2, 1, "ox tether", null, 0, Resources.WOOD, 5, "building", NonMilitaryTypes.STONE_MASON, 1),
    QUARRY(350, 6, 6, 3, "quarry", Texture.STONE, 0, Resources.WOOD, 20, "converting resources",
            NonMilitaryTypes.STONE_MASON, 6),
    MARKET(200, 5, 5, 0, "market", null, 0, Resources.WOOD, 5, "building", null, 5),
    PITCH_RIG(200, 4, 4, 2, "pitch rig", Texture.OIL, 0, Resources.WOOD, 20, "converting resources",
            NonMilitaryTypes.PITCH_DIGGER, 0),
    CATHEDRAL(1200, 13, 13, 1, "cathedral", null, 1000, Resources.WOOD, 0, "building", NonMilitaryTypes.PRIEST, 10),
    STOCK_PILE(-1, 5, 5, 0, "stock pile", null, 0, Resources.WOOD, 0, "building", null, 0),
    WOODCUTTER(150, 3, 3, 1, "woodcutter", null, 0, Resources.WOOD, 3, "converting resources",
            NonMilitaryTypes.WOODCUTTER, 3),
    HOVEL(200, 4, 4, 0, "hovel", null, 0, Resources.WOOD, 6, "building", NonMilitaryTypes.ORDINARY_PERSON, 5),
    CHURCH(600, 9, 9, 1, "church", null, 500, Resources.WOOD, 0, "building", NonMilitaryTypes.PRIEST, 8),
    ARMOURER(500, 4, 4, 1, "armourer", null, 100, Resources.WOOD, 20, "converting resources", NonMilitaryTypes.ARMOURER,
            5),
    BLACKSMITH(500, 4, 4, 1, "blacksmith", null, 200, Resources.WOOD, 20, "converting resources",
            NonMilitaryTypes.BLACKSMITH, 5),
    POLETURNER(500, 4, 4, 1, "poleturner", null, 100, Resources.WOOD, 10, "converting resources",
            NonMilitaryTypes.POLETURNER, 5),
    FLETCHER(500, 4, 4, 1, "fletcher", null, 100, Resources.WOOD, 20, "converting resources", NonMilitaryTypes.FLETCHER,
            5),
    TANNER(500, 4, 4, 1, "tanner", null, 100, Resources.WOOD, 100, "converting resources", NonMilitaryTypes.TANNER, 5),
    OIL_SMELTER(300, 4, 8, 0, "oil smelter", null, 100, Resources.IRON, 10, "building", null, 5),
    PITCH_DITCH(-1, 1, 1, 0, "pitch ditch", null, 0, Resources.PITCH, 1, "building", null, 2),
    CAGED_WAR_DOGS(1, 2, 2, 0, "caged war dogs", null, 100, Resources.WOOD, 10, "caged war dogs", null, 1),
    SIEGE_TENT(100, 3, 3, 0, "siege tent", null, 0, Resources.WOOD, 0, "building", null, 3),
    STABLE(300, 6, 6, 0, "stable", null, 400, Resources.WOOD, 20, "building", null, 4),
    APPLE_ORCHARD(200, 4, 4, 1, "apple orchard", Texture.MEADOW, 0, Resources.WOOD, 5, "converting resources",
            NonMilitaryTypes.FARMER, 4),
    DIARY_FARMER(200, 4, 4, 1, "diary farmer", Texture.MEADOW, 0, Resources.WOOD, 10, "converting resources",
            NonMilitaryTypes.FARMER, 4),
    WHEAT_FARMER(200, 4, 4, 1, "wheat farmer", Texture.MEADOW, 0, Resources.WOOD, 15, "converting resources",
            NonMilitaryTypes.FARMER, 4),
    HOPS_FARMER(200, 4, 4, 1, "hops farmer", Texture.MEADOW, 0, Resources.WOOD, 15, "converting resources",
            NonMilitaryTypes.FARMER, 4),
    HUNTER_POST(200, 3, 3, 1, "hunter post", null, 0, Resources.WOOD, 10, "converting resources",
            NonMilitaryTypes.HUNTER, 4),
    BAKERY(300, 4, 4, 1, "bakery", null, 0, Resources.WOOD, 10, "converting resources", NonMilitaryTypes.BAKER, 5),
    BREWER(300, 4, 4, 1, "brewer", null, 0, Resources.WOOD, 10, "converting resources", NonMilitaryTypes.BREWER, 5),
    GRANARY(250, 4, 4, 1, "granary", null, 0, Resources.WOOD, 5, "building", null, 5),
    LORD_KEEP(-1, 7, 7, 0, "lord keep", null, 0, Resources.WOOD, 0, "building", null, 7);

    private int hp;
    private int length;
    private int width;
    private int workerInUse;
    private String buildingName;
    private Texture texture;
    private int cost;
    private Resources resourceNeeded;
    private int resourceAmount;
    private String type;
    private NonMilitaryTypes workerType;
    private int height;

    private TypeOfBuilding(int hp, int length, int width, int workerInUse, String buildingName, Texture texture,
            int cost, Resources resourceNeeded, int resourceAmount, String type, NonMilitaryTypes workerType,
            int height) {
        this.hp = hp;
        this.length = length;
        this.width = width;
        this.workerInUse = workerInUse;
        this.buildingName = buildingName;
        this.cost = cost;
        this.resourceNeeded = resourceNeeded;
        this.resourceAmount = resourceAmount;
        this.texture = texture;
        this.type = type;
        this.workerType = workerType;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public NonMilitaryTypes getWorkerType() {
        return workerType;
    }

    public String getType() {
        return type;
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

    public static TypeOfBuilding getBuilding(String input) {
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