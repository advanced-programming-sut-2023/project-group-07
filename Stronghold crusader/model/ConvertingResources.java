package model;

import java.util.ArrayList;

public class ConvertingResources extends Building {
    Resources resource;
    protected ArrayList<Person> Workers = new ArrayList<>();

    public ConvertingResources(Government government, TypeOfBuilding typeOfBuilding, int row, int column,
            Resources resource) {
        super(government, typeOfBuilding, row, column);
        this.resource = resource;
    }

    public void setResource(Resources resource) {
        this.resource = resource;
    }

    public Resources getResource() {
        return resource;
    }

    public ArrayList<Person> getWorkers() {
        return Workers;
    }

    public void changeResources() {
        if (typeOfBuilding.equals(TypeOfBuilding.BLACKSMITH)) {
            if (resource.equals(Resources.SWORD))
                resource = Resources.MACE;
            else
                resource = Resources.SWORD;
        } else if (typeOfBuilding.equals(TypeOfBuilding.FLETCHER)) {
            if (resource.equals(Resources.BOW))
                resource = Resources.CROSSBOW;
            else
                resource = Resources.BOW;
        } else if (typeOfBuilding.equals(TypeOfBuilding.POLETURNER)) {
            if (resource.equals(Resources.PIKE))
                resource = Resources.SPEAR;
            else
                resource = Resources.PIKE;
        }
    }
}