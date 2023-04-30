package model;

public class Building {

    protected Government government;
    protected int hp;
    protected TypeOfBuilding typeOfBuilding;
    protected int workers;
    protected int row;
    protected int column;
    protected boolean isWorking;
    protected LordColor lordColor;
    
    public Building(Government government,TypeOfBuilding typeOfBuilding,int row,int column) {
        this.government=government;
        this.lordColor = government.getColor();
        this.typeOfBuilding=typeOfBuilding;
        this.row=row;
        this.column=column;
        this.hp=typeOfBuilding.getHp();
    }

    public Building (LordColor lordColor, TypeOfBuilding typeOfBuilding, int row,int column){
        this.lordColor = lordColor;
        this.typeOfBuilding = typeOfBuilding;
        this.row=row;
        this.column = column;
        this.hp = typeOfBuilding.getHp();
    } 

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public TypeOfBuilding getTypeOfBuilding() {
        return typeOfBuilding;
    }
    public void repair(){
        hp = typeOfBuilding.getHp();
    }
    public int getHp() {
        return hp;
    }
    public void setWorkers(int workers) {
        this.workers = workers;
    }
    public int getWorkers() {
        return workers;
    }
    public void workingState(boolean isWorking) {
        this.isWorking = isWorking;
    }
    public void endOfTurn(){

    }
    public void setGovernment(Government government) {
        this.government = government;
    }
    public Government getGovernment(){
        return government;
    }
    public LordColor getLordColor() {
        return lordColor;
    }

    public int row() {
        return row;
    }

    public int column() {
        return column;
    }
}