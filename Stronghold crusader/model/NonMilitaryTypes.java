package model;

public enum NonMilitaryTypes {
    JESTER(20,12),
    WOODCUTTER(100,12),
    HUNTER(100,12),
    FARMER(100,12),
    PEASANT(20,12),
    ORDINARY_PERSON(20,8),
    STONE_MASON(100,8),
    IRON_MINER(100,8),
    PITCH_DIGGER(100,12),
    MILL_BOY(100,16),
    BAKER(100,12),
    BREWER(100,12),
    INNKEEPER(100,12),
    FLETCHER(100,8),
    ARMOURER(100,8),
    BLACKSMITH(100,8),
    POLETURNER(100,8),
    TANNER(100,8),
    PRIEST(100,8);

    private int hp;
    private int speed;
    
    private NonMilitaryTypes(int hp,int speed) {
        this.hp = hp;
        this.speed = speed;
    }

    public int getHp() {
        return hp;
    }
    
    public int getSpeed() {
        return speed;
    }
    
}
