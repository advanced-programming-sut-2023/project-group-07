package controller;

import java.util.ArrayList;
import java.util.regex.Matcher;

import model.Game;
import model.Government;
import model.Map;
import model.MilitaryCampType;
import model.TypeOfBuilding;
import model.TypeOfPerson;
import model.User;
public class GameMenuController {
    Game game = Controller.currentGame;
    private User currentUser;
    private ArrayList<Government> governments = new ArrayList<>();
    private Map map;

    public Messages taxRate(int rate, Government government) {
        if (rate < -3 || rate > 8) return Messages.INVALID_RATE;
        if (rate < 0) {
            government.setTaxAmount(1 - (rate + 3) * 0.2);
            government.setTaxPopularity(7 - (rate + 3) * 2);
        } else if (rate == 0) {
            government.setTaxAmount(0);
            government.setTaxPopularity(1);
        } else {
            government.setTaxAmount(0.6 + (rate - 1) * 0.2);
            if (rate < 5)
                government.setTaxPopularity(rate * 2);
            else
                government.setTaxPopularity((rate - 2) * 4);
        }
        return Messages.RATE_CHANGE_SUCCESSFULL;
    }

    public ArrayList<Government> getGovernments() {
        return governments;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void endOfTurn() {
        for (Government government : getGovernments()) {
            government.setPopularity(government.getPopularity() + government.getTaxPopularity());
            government.setGold(government.getGold() + government.getTaxAmount() * government.getPopulation());
        }
    }
    public Messages dropBuilding(int row,int column,String name) {
        return game.dropBuilding(row, column, TypeOfBuilding.getBuilding(name));
    }
    public Messages selectBuilding(int row,int column){
        return game.selectBuilding(row, column);
    }
    public String getCurrentMilitaryCamp(){
        if(game.getCurrentMilitaryCamp().equals(MilitaryCampType.BARRACKS)) return "barracks";
        if(game.getCurrentMilitaryCamp().equals(MilitaryCampType.MERCENARY_POST)) return "mercenary post";
        if(game.getCurrentMilitaryCamp().equals(MilitaryCampType.ENGINEER_GUILD)) return "engineer's guild";
        return null;
    }
    public Messages createUnit(String input){
        String type="";
        int count=0;
        Matcher matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.CREATE_UNIT);
        if(matcher.group("type1")==null){
            type = matcher.group("type2");
            count = Integer.parseInt(matcher.group("count2"));
        }
        else{
            type = matcher.group("type1");
            count = Integer.parseInt(matcher.group("count1"));
        }
        return game.createTroop(type, count);
    }
    public String getUnitsInfo(String militaryCamp){
        String output="";
        if(militaryCamp.equals("barracks")){
            for(TypeOfPerson typeOfPerson: TypeOfPerson.values()){
                if(typeOfPerson.getMilitaryCampType().equals(MilitaryCampType.BARRACKS))
                    output+=typeOfPerson.getType()+"    "+typeOfPerson.getGoldNeeded()+" gold\n";
            }
        }
        else if(militaryCamp.equals("mercenary post")){
            for(TypeOfPerson typeOfPerson: TypeOfPerson.values()){
                if(typeOfPerson.getMilitaryCampType().equals(MilitaryCampType.MERCENARY_POST))
                    output+=typeOfPerson.getType()+"    "+typeOfPerson.getGoldNeeded()+" gold\n";
            }
        }
        else if(militaryCamp.equals("engineer's guild")){
            for(TypeOfPerson typeOfPerson: TypeOfPerson.values()){
                if(typeOfPerson.getMilitaryCampType().equals(MilitaryCampType.ENGINEER_GUILD))
                    output+=typeOfPerson.getType()+"    "+typeOfPerson.getGoldNeeded()+" gold\n";
            }
        }
        return null;
    }
}
