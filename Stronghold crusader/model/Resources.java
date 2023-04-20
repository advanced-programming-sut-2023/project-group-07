package model;

public enum Resources {
    WOOD("wood",null),
    STONE("stone",null),
    WHEAT("wheat",null),
    FLOUR("flour",null),
    HOPS("hops",null),
    ALE("ale",null),
    IRON("iron",null),
    PITCH("pitch",null),
    ARMOR("armor",null),
    SWORD("sword",null),
    SPEAR("spear",null),
    PIKE("pike",null),
    MACE("mace",null),
    LEATHER_ARMOR("leather armor",null),
    METAL_ARMOR("metal armor",null),
    HORSE("horse",null),
    BOW("bow",null),
    CROSSBOW("crossbow",null),

    MEAT("meat", TypeOfResource.FOOD),
    APPLE("apple", TypeOfResource.FOOD),
    CHEESE("cheese", TypeOfResource.FOOD),
    BREAD("bread", TypeOfResource.FOOD);
    private final String printingName;
    private final TypeOfResource type;

    private Resources(String name, TypeOfResource type) {
        this.printingName = name;
        this.type = type;
    }
    public String getPrintingName() {
        return printingName;
    }
    public TypeOfResource type() {
        return type;
    }
    
}
