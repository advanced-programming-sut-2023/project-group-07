package model;

public enum SiegeWeaponType {
    PORTABLE_SHIELD("portable_shield", 1, 5, 4, 0, 1200, 0),
    BATTERING_RAM("battering_ram", 4, 150, 1, 1, 1500, 400),
    SIEGE_TOWER("siege tower", 4, 150, 1, 1, 1500, 0),
    CATAPULT("catapult", 2, 150, 2, 35, 1000, 200),
    TREBUCHET("trebuchet", 3, 150, 0, 54, 1000, 250),
    FIRE_BALLISTA("fire ballista", 2, 150, 2, 35, 1000, 250);

    private String type;
    private int engineersNeeded;
    private int goldNeeded;
    private int speed;
    private int range;
    private int hp;
    private int damage;

    private SiegeWeaponType(String type, int engineersNeeded, int goldNeeded, int speed, int range, int hp,
            int damage) {
        this.type = type;
        this.goldNeeded = goldNeeded;
        this.engineersNeeded = engineersNeeded;
        this.speed = speed;
        this.range = range;
        this.hp = hp;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    public int getEngineersNeeded() {
        return engineersNeeded;
    }

    public int getGoldNeeded() {
        return goldNeeded;
    }

    public static SiegeWeaponType getSiegeWeaponType(String type) {
        for (SiegeWeaponType siegeWeaponType : SiegeWeaponType.values())
            if (siegeWeaponType.type.equals(type))
                return siegeWeaponType;
        return null;
    }
}
