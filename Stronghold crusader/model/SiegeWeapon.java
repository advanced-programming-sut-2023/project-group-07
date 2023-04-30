package model;

public class SiegeWeapon extends Person {
    private SiegeWeaponType siegeWeaponType;
    private int[] location;

    public SiegeWeapon(SiegeWeaponType siegeWeaponType,int[] location,Government government) {
        super(location, government);
        this.siegeWeaponType = siegeWeaponType;
        this.hp = siegeWeaponType.getHp();
        this.location = location;
    }
}
