package model;

public enum ConvertingResourcesTypes {
    INN,
    MILL,
    IRON_MINE,
    QUARRY,
    PITCH_RIG,
    WOODCUTTER,
    ARMOURER,
    BLACKSMITH,
    POLETURNER,
    FLETCHER,
    TANNER,
    APPLE_ORCHARD,
    DIARY_FARMER,
    WHEAT_FARMER,
    HOPS_FARMER,
    HUNTER_POST,
    BAKERY,
    BREWER;

    private Resources resourceNeeded;
    private int resourceNeededAmount;
    private Resources resourceDelivered;
    private int resourceDeliveredAmount;
    private String name;


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
