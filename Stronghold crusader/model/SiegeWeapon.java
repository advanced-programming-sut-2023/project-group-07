package model;

public class SiegeWeapon extends Unit {
    private SiegeWeaponType siegeWeaponType;

    public SiegeWeapon(SiegeWeaponType siegeWeaponType,int[] location,Government government) {
        super(UnitTypes.SIEGE_WEAPON, location, government);
        this.siegeWeaponType = siegeWeaponType;
        this.hp = siegeWeaponType.getHp();
        this.damage = siegeWeaponType.getDamage();
    }

    public SiegeWeaponType getSiegeWeaponType() {
        return siegeWeaponType;
    }

}
