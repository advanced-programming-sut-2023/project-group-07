package Client.model;

import Client.controller.Controller;

public enum Rock {
    NORTH_ROCK("upward rock", "n"),
    SOUTH_ROCK("downward rock", "s"),
    EAST_ROCK("leftward rock", "e"),
    WEST_ROCK("rightward rock", "w");

    private final String printingName;
    private final String direction;

    private Rock(String printingName, String direction) {
        this.printingName = printingName;
        this.direction = direction;
    }

    public static Rock getRock(String direction) {
        if (direction.equals("r")) {
            String[] directions = { "n", "s", "e", "w" };
            return getRock(directions[Controller.randomNumber(4)]);
        }
        for (Rock rock : Rock.values())
            if (rock.direction.equals(direction))
                return rock;
        return null;
    }

    @Override
    public String toString() {
        return this.printingName;
    }
}