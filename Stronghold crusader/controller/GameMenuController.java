package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;

import model.*;

public class GameMenuController {
    private Game game = Controller.currentGame;
    private User currentUser = Controller.currentUser;
    private ArrayList<Government> governments = new ArrayList<>();
    private Map map;

    public void refreshGame() {
        this.game = Controller.currentGame;
        this.currentUser = Controller.currentUser;
    }

    public Messages taxRate(int rate, Government government) {
        if (rate < -3 || rate > 8)
            return Messages.INVALID_RATE;
        government.setTaxRate(rate);
        return Messages.RATE_CHANGE_SUCCESSFUL;
    }

    public ArrayList<Government> getGovernments() {
        return governments;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void endOfTurn() throws IOException {
        game.endOfTurn();
        for (Government government : getGovernments()) { // todo : must be in game object
            government.setPopularity(government.getPopularity() + government.getTaxEffectOnPopularity()); // todo :
            government.setGold((int) (government.getGold() + government.getTaxAmount() * government.getPopulation())); // todo
                                                                                                                       // :
        }
    }

    public Messages dropBuilding(String row, String column, String name) {
        if (row == null || column == null || name == null)
            return Messages.INVALID_COMMAND;
        int rowNum = Integer.parseInt(row) - 1;
        int columnNum = Integer.parseInt(column) - 1;
        return game.dropBuilding(rowNum, columnNum, TypeOfBuilding.getBuilding(name));
    }

    public Messages selectBuilding(int row, int column) {
        return game.selectBuilding(row, column);
    }

    public String getSelectedBuilding() {
        return game.getSelectedBuilding().getTypeOfBuilding().getBuildingName();
    }

    public Messages repair() {
        return game.repair();
    }

    public Messages createUnit(String type, int count) {
        return game.createTroop(type, count);
    }

    public Messages closeGate() {
        return game.closeGate();
    }

    public Messages openGate() {
        return game.openGate();
    }

    public String getUnitsInfo(String militaryCamp) {
        String output = "";
        if (militaryCamp.equals("barracks")) {
            for (TypeOfPerson typeOfPerson : TypeOfPerson.values()) {
                if (typeOfPerson.getMilitaryCampType().equals(MilitaryCampType.BARRACKS))
                    output += typeOfPerson.getType() + "    " + typeOfPerson.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("mercenary post")) {
            for (TypeOfPerson typeOfPerson : TypeOfPerson.values()) {
                if (typeOfPerson.getMilitaryCampType().equals(MilitaryCampType.MERCENARY_POST))
                    output += typeOfPerson.getType() + "    " + typeOfPerson.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("engineer's guild")) {
            for (TypeOfPerson typeOfPerson : TypeOfPerson.values()) {
                if (typeOfPerson.getMilitaryCampType().equals(MilitaryCampType.ENGINEER_GUILD))
                    output += typeOfPerson.getType() + "    " + typeOfPerson.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("cathedral")) {
            for (TypeOfPerson typeOfPerson : TypeOfPerson.values()) {
                if (typeOfPerson.getMilitaryCampType().equals(MilitaryCampType.CATHEDRAL))
                    output += typeOfPerson.getType() + "    " + typeOfPerson.getGoldNeeded() + " gold\n";
            }
        }
        return null;
    }

    public Messages changeArms() {
        return game.changeArms();
    }

    public String getResources() {
        ConvertingResources convertingResources = (ConvertingResources) game.getSelectedBuilding();
        return convertingResources.getResource().getName();
    }

    public Messages changeWorkingState() {
        return game.changeWorkingState();
    }

    public Messages selectUnit(int frow, int fcolumn, int srow, int scolumn) {
        return game.selectUnit(frow - 1, fcolumn - 1, srow - 1, scolumn - 1);
    }

    public Messages moveUnit(int row, int column) {
        return game.moveUnit(row - 1, column - 1);
    }

    public Messages patrolunit(int frow, int fcolumn, int srow, int scolumn) {
        return game.patrolUnits(frow - 1, fcolumn - 1, srow - 1, scolumn - 1);
    }

    public Messages stopUnit() {
        return game.stopUnit();
    }

    public Messages setStance(int frow, int fcolumn, String stance) {
        return game.setStance(frow, fcolumn, UnitStance.getStanceByName(stance));
    }

    public int getPopularity() {
        return game.getCurrentGovernment().getPopularity();
    }

    public int getFoodRate() {
        return game.getCurrentGovernment().getFoodRate();
    }

    public HashMap<Resources, Integer> getFoodList() {
        return game.getCurrentGovernment().getFoodList();
    }

    public Messages setFoodList(int rate) {
        Government government = game.getCurrentGovernment();
        if (rate < -2 || rate > 2)
            return Messages.INVALID_RATE;
        if (government.getFoodsNumber() == 0)
            return Messages.NOT_ENOUGH_FOOD;
        government.setFoodRate(rate);
        return Messages.SET_FOOD_RATE_SUCCESSFUL;
    }

    public String showPriceList() {
        String result = " ";
        result += "<< FOOD >>\n";
        for (Resources resource : Resources.values())
            if (resource.getType().equals(TypeOfResource.FOOD))
                result += showResource(resource);
        result += "\n<< RAW MATERIALS >>\n";
        for (Resources resource : Resources.values())
            if (resource.getType().equals(TypeOfResource.RAW_MATERIAL))
                result += showResource(resource);
        result += "\n<< WEAPONS >>\n";
        for (Resources resource : Resources.values())
            if (resource.getType().equals(TypeOfResource.WEAPON))
                result += showResource(resource);
        return result;
    }

    private String showResource(Resources resource) {
        return ("*Name:" + resource + ":" +
                "\nBuying price: " + resource.getBuyingPrice() +
                "\nSelling price: " + resource.getSellingPrice() +
                "\nAmount: " + game.getCurrentGovernment().getResourceAmount(resource) + "\n");
    }

    public Messages buyCommodity(String item, int amount) {
        Resources resource = Resources.getResource(item);
        if (resource == null)
            return Messages.INVALID_ITEM;
        int price = amount * resource.getBuyingPrice();
        double gold = game.getCurrentGovernment().getGold();
        if (gold < price)
            return Messages.NOT_ENOUGH_GOLD;
        game.getCurrentGovernment().changeGold(-price);
        game.getCurrentGovernment().changeResources(resource, amount);
        ;
        return Messages.SHOP_SUCCESSFUL;
    }

    public Messages sellCommodity(String item, int amount) {
        Resources resource = Resources.getResource(item);
        if (resource == null)
            return Messages.INVALID_ITEM;
        int resourceAmount = game.getCurrentGovernment().getResourceAmount(resource);
        if (resourceAmount < amount)
            return Messages.NOT_ENOUGH_RESOURCES;
        int price = amount * resource.getBuyingPrice();
        game.getCurrentGovernment().changeGold(price);
        game.getCurrentGovernment().changeResources(resource, -amount);
        return Messages.SHOP_SUCCESSFUL;
    }

    public ArrayList<Integer> getPopularityFactors() {
        Government government = game.getCurrentGovernment();
        ArrayList<Integer> factorsInOrder = new ArrayList<>();
        factorsInOrder.add(government.getFoodEffectOnPopularity());
        factorsInOrder.add(government.getTaxEffectOnPopularity());
        factorsInOrder.add(government.getReligionEffectOnPopularity());
        factorsInOrder.add(government.getFearEffectOnPopularity());
        factorsInOrder.add(government.getBuildingsEffectOnPopularity());
        return factorsInOrder;
    }
}
