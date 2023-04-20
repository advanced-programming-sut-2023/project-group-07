package model;

public class ConvertingResources extends Building {
    Resources resource;
    public ConvertingResources(Government government,TypeOfBuilding typeOfBuilding,int row,int column,Resources resource){
        super(government, typeOfBuilding, row, column);
        this.resource=resource;
    }
    public void setResource(Resources resource) {
        this.resource = resource;
    }
    public Resources getResource() {
        return resource;
    }
}