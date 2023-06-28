package Client.model;

import java.util.ArrayList;

public class ConvertingResources extends Building {
    private Resources resource;
    protected ArrayList<Person> Workers = new ArrayList<>();
    private ConvertingResourcesTypes type;

    public ConvertingResources(Government government, TypeOfBuilding typeOfBuilding, int row, int column,
            ConvertingResourcesTypes convertingResourcesTypes) {
        super(government, typeOfBuilding, row, column);
        this.resource = convertingResourcesTypes.getResourceDelivered();
        this.type = convertingResourcesTypes;
    }

    public ConvertingResources(LordColor lordColor, TypeOfBuilding typeOfBuilding, int row, int column,
            ConvertingResourcesTypes convertingResourcesTypes) {
        super(lordColor, typeOfBuilding, row, column);
        this.resource = convertingResourcesTypes.getResourceDelivered();
        this.type = convertingResourcesTypes;
    }

    public ConvertingResourcesTypes getType() {
        return type;
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