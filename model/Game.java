package model;

import java.net.DatagramPacket;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicTableHeaderUI;

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
        if(row<0 || row>map.getSize() || column<0 || column>map.getSize()){
            return Messages.INVALID_ROW_OR_COLUMN;
        }
        for(int i = 0; i < typeOfBuilding.getLength();i++)
            for(int j = 0; j < typeOfBuilding.getWidth();j++)
                if(map.getMapPixel(row+j, column+i).getBuildings().size()!=0) return Messages.THERES_ALREADY_BUILDING;
        for(int i = 0; i < typeOfBuilding.getLength();i++)
            for(int j = 0; j < typeOfBuilding.getWidth();j++)
                if(map.getMapPixel(row+j, column+i).getPeople().size()!=0) return Messages.THERES_ALREADY_UNIT;
        for(int i = 0; i < typeOfBuilding.getLength();i++)
            for(int j = 0; j < typeOfBuilding.getWidth();j++){
                Texture landType = map.getMapPixel(row+j, column+i).getTexture();
                if(landType.equals(Texture.RIVER) ||
                landType.equals(Texture.SEA) ||
                landType.equals(Texture.ROCK) ||
                landType.equals(Texture.SMALL_POND) ||
                landType.equals(Texture.LARGE_POND) ||
                landType.equals(Texture.FORD)) return Messages.CANT_PLACE_THIS;
            }
        if(typeOfBuilding.equals(TypeOfBuilding.APPLE_ORCHARD) ||
           typeOfBuilding.equals(TypeOfBuilding.DIARY_FARMER) ||
           typeOfBuilding.equals(TypeOfBuilding.HOPS_FARMER) ||
           typeOfBuilding.equals(TypeOfBuilding.WHEAT_FARMER)){
               for(int i = 0; i < typeOfBuilding.getLength();i++)
                  for(int j = 0; j < typeOfBuilding.getWidth();j++){
                    if(!map.getMapPixel(row+j, column+i).getTexture().equals(Texture.MEADOW)) return  Messages.CANT_PLACE_THIS;
                  }
        }
        int acceptedPixels = 0;
        if(typeOfBuilding.equals(TypeOfBuilding.QUARRY) || typeOfBuilding.equals(TypeOfBuilding.IRON_MINE) || typeOfBuilding.equals(TypeOfBuilding.PITCH_RIG)){
            for(int i = 0; i < typeOfBuilding.getLength();i++)
                for(int j = 0; j < typeOfBuilding.getWidth();j++)
                    if(!map.getMapPixel(row+j, column+i).getTexture().equals(typeOfBuilding.getTexture())) acceptedPixels++;
        }
        if(acceptedPixels*4<typeOfBuilding.getLength()*typeOfBuilding.getWidth()) return Messages.CANT_PLACE_THIS;
        Building building = new Building(getCurrentGovernment(),typeOfBuilding, row, column);
        map.addBuilding(building);
        for(int i = 0; i < typeOfBuilding.getLength();i++)
            for(int j = 0; j < typeOfBuilding.getWidth();j++)
                map.getMapPixel(row+j, column+i).addBuilding(building);
        return Messages.DEPLOYMENT_SUCCESSFULL;
    }
    
}
