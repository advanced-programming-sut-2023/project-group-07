package model;

public enum Resources {
    WOOD("wood", TypeOfResource.RAW_MATERIAL,4,1),
    STONE("stone", TypeOfResource.RAW_MATERIAL,14,7),
    WHEAT("wheat", TypeOfResource.RAW_MATERIAL,23,8),
    FLOUR("flour", TypeOfResource.RAW_MATERIAL,32,10),
    HOPS("hops", TypeOfResource.RAW_MATERIAL,15,8),
    ALE("ale", TypeOfResource.RAW_MATERIAL,20,10),
    IRON("iron", TypeOfResource.RAW_MATERIAL,45,23),
    PITCH("pitch", TypeOfResource.RAW_MATERIAL,20,10),
    
    SWORD("sword", TypeOfResource.WEAPON,58,30),
    SPEAR("spear", TypeOfResource.WEAPON,20,10),
    PIKE("pike", TypeOfResource.WEAPON,36,18),
    MACE("mace", TypeOfResource.WEAPON,58,30),
    LEATHER_ARMOR("leather armor", TypeOfResource.WEAPON,25,12),
    METAL_ARMOR("metal armor", TypeOfResource.WEAPON,58,30),
    HORSE("horse", TypeOfResource.WEAPON,0,0),
    BOW("bow", TypeOfResource.WEAPON,31,15),
    CROSSBOW("crossbow", TypeOfResource.WEAPON,58,30),

    MEAT("meat", TypeOfResource.FOOD,8,4),
    APPLE("apple", TypeOfResource.FOOD,8,4),
    CHEESE("cheese", TypeOfResource.FOOD,8,4),
    BREAD("bread", TypeOfResource.FOOD,8,4);

    private final String name;
    private final TypeOfResource type;
    private final int buyingPrice;
    private final int sellingPrice;

    private Resources(String name, TypeOfResource type,int buyingPrice,int sellingPrice) {
        this.name = name;
        this.type = type;
        this.buyingPrice=buyingPrice;
        this.sellingPrice=sellingPrice;
    }

    public String getName() {
        return name;
    }

    public TypeOfResource getType() {
        return type;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public static Resources getResource(String name) {
        for(Resources resource : Resources.values())
            if(resource.name.equals(name))
                return resource;
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
