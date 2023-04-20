package model;

public class Building {

    protected Government government;
    protected int hp;
    protected TypeOfBuilding typeOfBuilding;
    protected int workers;
    protected int row;
    protected int column;
    protected boolean isWorking;
    
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
}