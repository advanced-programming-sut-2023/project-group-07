package model;

public class SiegeWeapon {
    private SiegeWeaponType siegeWeaponType;
    private int hp;
    private int[] location;

    public SiegeWeapon(SiegeWeaponType siegeWeaponType, int[] location) {
        this.siegeWeaponType = siegeWeaponType;
        this.hp = siegeWeaponType.getHp();
        this.location = location;
    }
}
