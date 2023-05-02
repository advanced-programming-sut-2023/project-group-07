package model;

public enum NonMilitaryTypes {
    JESTER(20,3),
    WOODCUTTER(100,3),
    HUNTER(100,3),
    FARMER(100,3),
    PEASANT(20,3),
    ORDINARY_PERSON(20,2),
    STONE_MASON(100,2),
    IRON_MINER(100,2),
    PITCH_DIGGER(100,3),
    MILL_BOY(100,4),
    BAKER(100,3),
    BREWER(100,3),
    INNKEEPER(100,3),
    FLETCHER(100,2),
    ARMOURER(100,2),
    BLACKSMITH(100,2),
    POLETURNER(100,2),
    TANNER(100,2),
    PRIEST(100,2);

    private int hp;
    private int speed;
    
    private NonMilitaryTypes(int hp,int speed) {
        this.hp = hp;
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
    
}
