package Client.model;

public enum NonMilitaryTypes {
    JESTER(20, 12, "jester"),
    WOODCUTTER(100, 12, "woodcutter"),
    HUNTER(100, 12, "hunter"),
    FARMER(100, 12, "farmer"),
    PEASANT(20, 12, "peasant"),
    ORDINARY_PERSON(20, 8, "ordirary person"),
    STONE_MASON(100, 8, "stone mason"),
    IRON_MINER(100, 8, "iron miner"),
    PITCH_DIGGER(100, 12, "pitch digger"),
    MILL_BOY(100, 16, "mill boy"),
    BAKER(100, 12, "baker"),
    BREWER(100, 12, "brewer"),
    INNKEEPER(100, 12, "innkeeper"),
    FLETCHER(100, 8, "fletcher"),
    ARMOURER(100, 8, "armourer"),
    BLACKSMITH(100, 8, "blacksmith"),
    POLETURNER(100, 8, "poleturner"),
    TANNER(100, 8, "tanner"),
    PRIEST(100, 8, "priest");

    private int hp;
    private int speed;
    private String name;

    private NonMilitaryTypes(int hp, int speed, String name) {
        this.hp = hp;
        this.speed = speed;
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return name;
    }

}
