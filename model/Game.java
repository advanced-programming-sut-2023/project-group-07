package model;

import java.util.ArrayList;

import controller.Messages;

public class Game {
    private User currentUser;
    private ArrayList<Government> governments = new ArrayList<>();
    private Map map;
    public Game (int size){
        this.map = new Map(size);
    }
    public Messages taxRate(int rate,Government government){
        if(rate<-3 || rate>8) return Messages.INVALID_RATE;
        if(rate<0){
            government.setTaxAmount(1-(rate+3)*0.2);
            government.setTaxPopularity(7-(rate+3)*2);
        }
        else if(rate==0){
            government.setTaxAmount(0);
            government.setTaxPopularity(1);
        }
        else{
            government.setTaxAmount(0.6+(rate-1)*0.2);
            if(rate<5)
                government.setTaxPopularity(rate*2);
            else
                government.setTaxPopularity((rate-2)*4);
        }
        return Messages.RATE_CHANGE_SUCCESSFULL;
    }
    public ArrayList<Government> getGovernments() {
        return governments;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void endOfTurn(){
        for(Government government : getGovernments()){
            government.setPopularity(government.getPopularity()+government.getTaxPopularity());
            government.setGold(government.getGold()+government.getTaxAmount()*government.getPopulation());
        }
    }
    public Government getCurrentGovernment(){
        for(Government government : governments){
            if(government.getUser().equals(currentUser)) return government;
        }
        return null;
    }
    public Messages fearRate(int rate,Government government){
        if(rate>5 || rate<-5) return Messages.INVALID_RATE;
        government.setFearRate(rate);
        return Messages.RATE_CHANGE_SUCCESSFULL;
    }
    public Map getMap(){
        return this.map;
    }
    public Messages dropBuilding(int row,int column,TypeOfBuilding typeOfBuilding){
        if(row<0 || row>399 || column<0 || column>399){
            return Messages.INVALID_ROW_OR_COLUMN;
        }
        Building building = new Building(getCurrentGovernment(),typeOfBuilding, row, column);
        map.addBuilding(building);
        for(int i = 0; i < typeOfBuilding.getLength();i++)
            for(int j = 0; j < typeOfBuilding.getWidth();j++)
                map.getMapPixel(row+j, column+i);
        return Messages.DEPLOYMENT_SUCCESSFULL;
    }
    
}
