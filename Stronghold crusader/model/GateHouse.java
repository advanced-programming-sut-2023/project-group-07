package model;

public class GateHouse extends Building{
    private boolean isClosed;
    public GateHouse(Government government,TypeOfBuilding typeOfBuilding,int row,int column) {
        super(government, typeOfBuilding, row, column);
    }
    public boolean getState() {
        return isClosed;
    }
    public void changeState() {
        if(isClosed) isClosed=false;
        else isClosed=true;
    }
}
