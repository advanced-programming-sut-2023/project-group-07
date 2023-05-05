package model;

public enum ConvertingResourcesTypes {
    INN(Resources.ALE,2,null,0),
    MILL(Resources.WHEAT,2,Resources.FLOUR,5),
    IRON_MINE(null,0,Resources.IRON,5),
    QUARRY(null,0,Resources.STONE,20),
    PITCH_RIG(null,0,Resources.PITCH,5),
    WOODCUTTER(null,0,Resources.WOOD,15),
    ARMOURER(Resources.IRON,1,Resources.METAL_ARMOR,3),
    BLACKSMITH(Resources.IRON,2,Resources.SWORD,3),
    POLETURNER(Resources.WOOD,3,Resources.SPEAR,3),
    FLETCHER(Resources.WOOD,3,Resources.BOW,3),
    TANNER(null,3,Resources.LEATHER_ARMOR,3),
    APPLE_ORCHARD(null,0,Resources.APPLE,20),
    DIARY_FARMER(null,0,Resources.CHEESE,20),
    WHEAT_FARMER(null,0,Resources.WHEAT,15),
    HOPS_FARMER(null,0,Resources.HOPS,15),
    HUNTER_POST(null,0,Resources.MEAT,20),
    BAKERY(Resources.FLOUR,1,Resources.BREAD,20),
    BREWER(Resources.HOPS,1,Resources.ALE,15);

    private Resources resourceNeeded;
    private int resourceNeededAmount;
    private Resources resourceDelivered;
    private int resourceDeliveredAmount;
    private String name;


    private ConvertingResourcesTypes(Resources resourceNeeded,int resourceNeededAmount,Resources resourceDelivered,int resourceDeliveredAmount) {
        this.resourceNeeded = resourceNeeded;
        this.resourceNeededAmount = resourceNeededAmount;
        this.resourceDelivered = resourceDelivered;
        this.resourceDeliveredAmount = resourceDeliveredAmount;
    }

    public Resources getResourceDelivered() {
        return resourceDelivered;
    }


    public static ConvertingResourcesTypes getTypeByName(String string) {
        for(ConvertingResourcesTypes convertingResourcesTypes : ConvertingResourcesTypes.values())
            if(convertingResourcesTypes.name.equals(string))
                return convertingResourcesTypes;
        return null;
    }

    public int getResourceDeliveredAmount() {
        return resourceDeliveredAmount;
    }

    public Resources getResourceNeeded() {
        return resourceNeeded;
    }

    public int getResourceNeededAmount() {
        return resourceNeededAmount;
    }
    
}
