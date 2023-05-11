package model;

public enum ConvertingResourcesTypes {
    INN(Resources.ALE,2,Resources.ALE,0,"inn"),
    MILL(Resources.WHEAT,2,Resources.FLOUR,5,"mill"),
    IRON_MINE(null,0,Resources.IRON,5,"iron mine"),
    QUARRY(null,0,Resources.STONE,20,"quarry"),
    PITCH_RIG(null,0,Resources.PITCH,5,"pitch rig"),
    WOODCUTTER(null,0,Resources.WOOD,15,"woodcutter"),
    ARMOURER(Resources.IRON,1,Resources.METAL_ARMOR,3,"armourer"),
    BLACKSMITH(Resources.IRON,2,Resources.SWORD,3,"blacksmith"),
    POLETURNER(Resources.WOOD,3,Resources.SPEAR,3,"poleturner"),
    FLETCHER(Resources.WOOD,3,Resources.BOW,3,"fletcher"),
    TANNER(null,3,Resources.LEATHER_ARMOR,3,"tanner"),
    APPLE_ORCHARD(null,0,Resources.APPLE,20,"apple orchard"),
    DIARY_FARMER(null,0,Resources.CHEESE,20,"diary farmer"),
    WHEAT_FARMER(null,0,Resources.WHEAT,15,"wheat farmer"),
    HOPS_FARMER(null,0,Resources.HOPS,15,"hops farmer"),
    HUNTER_POST(null,0,Resources.MEAT,20,"hunter post"),
    BAKERY(Resources.FLOUR,1,Resources.BREAD,20,"bakery"),
    BREWER(Resources.HOPS,1,Resources.ALE,15,"brewer");

    private Resources resourceNeeded;
    private int resourceNeededAmount;
    private Resources resourceDelivered;
    private int resourceDeliveredAmount;
    private String name;


    private ConvertingResourcesTypes(Resources resourceNeeded,int resourceNeededAmount,Resources resourceDelivered,int resourceDeliveredAmount,String name) {
        this.resourceNeeded = resourceNeeded;
        this.resourceNeededAmount = resourceNeededAmount;
        this.resourceDelivered = resourceDelivered;
        this.resourceDeliveredAmount = resourceDeliveredAmount;
        this.name = name;
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
