package model;

public enum Resources {
    WOOD,
    STONE,
    WHEAL,
    FLOUR,
    HOPS,
    ALE,
    IRON,
    PITCH,
    ARMOR,
    SWORD,
    SPEAR,
    PIKE,
    MACE,
    LEATHER_ARMOR,
    METAL_ARMOR,
    HORSE,
    BOW,
    CROSS_BOW,

    MEAT("meat", TypeOfResource.FOOD),
    APPLE("apple", TypeOfResource.FOOD),
    CHEESE("cheese", TypeOfResource.FOOD),
    BREAD("bread", TypeOfResource.FOOD);
    private String printingName = null;
    private TypeOfResource type = null;

    private Resources(String name, TypeOfResource type) {
        this.printingName = name;
        this.type = type;
    }

    private Resources() {

    }

    public String printingName() {
        return printingName;
    }

    public TypeOfResource type() {
        return type;
    }
}
