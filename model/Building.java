package model;

public class Building {

    Government government;
    int hp;
    TypeOfBuilding typeOfBuilding;
    int row;
    int column;
    
    public Building(Government government,TypeOfBuilding typeOfBuilding,int row,int column) {
        this.government=government;
        this.typeOfBuilding=typeOfBuilding;
        this.row=row;
        this.column=column;
        this.hp=typeOfBuilding.getHp();
    }
}