package model;

import java.net.DatagramPacket;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicTableHeaderUI;

import controller.Messages;

public class Game {
    private User currentUser;
    private ArrayList<Government> governments = new ArrayList<>();
    private ArrayList<User> users;
    private MilitaryCampType currentMilitaryCamp;
    private Map map;
    public Game (Map map, ArrayList<User> users){
        this.map = map;
        this.users = users;
        currentUser = users.get(0); // todo: we can randomize order of players
        for (User user : users){
            governments.add(new Government(user, ));// todo
        }
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
    public Messages selectBuilding(int row, int column){
        if(row<0 || row>map.getSize() || column<0 || column>map.getSize()){
            return Messages.INVALID_ROW_OR_COLUMN;
        }
        if(map.getMapPixel(row, column).getBuildings().size()==0) return Messages.NO_BUILDING_HERE;
        if(!map.getMapPixel(row, column).getBuildings().get(0).government.equals(getCurrentGovernment())) return Messages.ENEMY_BUILDING;
        if(map.getMapPixel(row, column).getBuildings().get(0) instanceof Tower){
            return Messages.ENTERED_TOWER;
        }
        if(map.getMapPixel(row,column).getBuildings().get(0) instanceof GateHouse){
            return Messages.ENTERED_GATEHOUSE;
        }
        if(map.getMapPixel(row, column).getBuildings().get(0) instanceof MilitaryCamp){
            MilitaryCamp militaryCamp = (MilitaryCamp)map.getMapPixel(row, column).getBuildings().get(0);
            if(militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.BARRACKS)) currentMilitaryCamp = MilitaryCampType.BARRACKS;
            if(militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.MERCENARY_POST)) currentMilitaryCamp = MilitaryCampType.MERCENARY_POST;
            if(militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.ENGINEERS_GUILD)) currentMilitaryCamp = MilitaryCampType.ENGINEER_GUILD;
            return Messages.ENTERED_MILITARY_CAMP;
        }
        return null;
        
    }
    public Messages createTroop(String type,int count){
        TypeOfPerson typeOfPerson = TypeOfPerson.getTypeOfPersonFromString(type);
        if(typeOfPerson.equals(null)) return Messages.INVALID_UNIT_NAME;
        if(count<0 || count>24) return Messages.INVALID_NUMBER;
        if(typeOfPerson.getGoldNeeded()*count<getCurrentGovernment().getGold()) return Messages.NOT_ENOUGH_GOLD;
        for(Resources resource: typeOfPerson.getResourcesNeeded())
            if(getCurrentGovernment().getResources().get(resource)<count) return Messages.NOT_ENOUGH_RESOURCES;
        if(!currentMilitaryCamp.equals(typeOfPerson.getMilitaryCampType())) return Messages.CANT_CREATE_THIS_UNIT_HERE;
        getCurrentGovernment().setGold(-count*typeOfPerson.getGoldNeeded());
        for(Resources resource: typeOfPerson.getResourcesNeeded())
            getCurrentGovernment().changeResources(resource, -count);
        return Messages.UNIT_CREATED_SUCCESSFULLY;
    }
    public MilitaryCampType getCurrentMilitaryCamp() {
        return currentMilitaryCamp;
    }

    
}
