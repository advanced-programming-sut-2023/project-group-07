package model;

public enum LordColor {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    BLUE("blue"),
    GRAY("gray"),
    PURPLE("purple"),
    CYAN("cyan"),
    GREEN("green");

    private final String name;

    private LordColor(String name) {
        this.name = name;
    }

    public static LordColor getLordColor(int index) {
        return LordColor.values()[index];
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static LordColor getColorByName(String name) {
        for (LordColor color : LordColor.values())
            if (color.name.equals(name))
                return color;
        return null;
    }
}