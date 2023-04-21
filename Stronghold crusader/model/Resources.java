package model;

public enum Resources {
    WOOD("wood", null,4,1),
    STONE("stone", null,14,7),
    WHEAT("wheat", null,23,8),
    FLOUR("flour", null,32,10),
    HOPS("hops", null,15,8),
    ALE("ale", null,20,10),
    IRON("iron", null,45,23),
    PITCH("pitch", null,20,10),
    
    SWORD("sword", null,58,30),
    SPEAR("spear", null,20,10),
    PIKE("pike", null,36,18),
    MACE("mace", null,58,30),
    LEATHER_ARMOR("leather armor", null,25,12),
    METAL_ARMOR("metal armor", null,58,30),
    HORSE("horse", null,0,0),
    BOW("bow", null,31,15),
    CROSSBOW("crossbow", null,58,30),

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
