package controller;

public enum Directions {
    NORTH("n",-1,0),
    SOUTH("s",1,0),
    WEST("w",0,-1),
    EAST("e",0,1),
    ;

    private final String name;
    private final int x;
    private final int y;
    Directions(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return name;
    }
    public static Directions getByName(String name){
        for (Directions direction : Directions.values()){
            if (direction.name.equals(name)) return direction;
        }
        return null;
    }
}
