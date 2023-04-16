package model;

public class Building {

    protected Government government;
    protected int hp;
    protected TypeOfBuilding typeOfBuilding;
    protected int row;
    protected int column;
    
    public Building(Government government,TypeOfBuilding typeOfBuilding,int row,int column) {
        this.government=government;
        this.typeOfBuilding=typeOfBuilding;
        this.row=row;
        this.column=column;
        this.hp=typeOfBuilding.getHp();
    }
    public TypeOfBuilding getTypeOfBuilding() {
        return typeOfBuilding;
    }
}