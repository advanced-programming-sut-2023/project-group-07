package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import model.*;

public class GameMenuController {
    private Game game = Controller.currentGame;
    private User currentUser;
    private ArrayList<Government> governments = new ArrayList<>();
    private Map map;

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

    public void endOfTurn() {
        for (Government government : getGovernments()) {
            government.setPopularity(government.getPopularity() + government.getTaxEffectOnPopularity()); // todo :
            government.setGold((int) (government.getGold() + government.getTaxAmount() * government.getPopulation())); // todo :
        }
    }

    public Messages dropBuilding(String row, String column, String name) {
        if (row == null || column == null || name == null)
            return Messages.INVALID_COMMAND;
        int rowNum = Integer.parseInt(row);
        int columnNum = Integer.parseInt(column);
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

    public Messages createUnit(String input) {
        String type = "";
        int count = 0;
        Matcher matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.CREATE_UNIT);
        if (matcher.group("type1") == null) {
            type = matcher.group("type2");
            count = Integer.parseInt(matcher.group("count2"));
        } else {
            type = matcher.group("type1");
            count = Integer.parseInt(matcher.group("count1"));
        }
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
        result += "<< RAW MATERIALS >>\n";
        for (Resources resource : Resources.values())
            if (resource.getType().equals(TypeOfResource.RAW_MATERIAL))
                result += showResource(resource);
        result += "<< WEAPONS >>\n";
        for (Resources resource : Resources.values())
            if (resource.getType().equals(TypeOfResource.WEAPON))
                result += showResource(resource);
        return result;
    }

    private String showResource(Resources resource) {
        return ("* " + resource + ":" +
                "\nBuying price: " + resource.getBuyingPrice() +
                "\nSelling price: " + resource.getSellingPrice() +
                "\nAmount: " + game.getCurrentGovernment().getResourceAmount(resource) + "\n");
    }

    public Messages buyCommodity(String item, int amount) {
        Resources resource = Resources.getResource(item);
        if (item == null)
            return Messages.INVALID_ITEM;
        int price = amount * resource.getBuyingPrice();
        int gold = game.getCurrentGovernment().getGold();
        if (gold < price)
            return Messages.NOT_ENOUGH_GOLD;
        int resourceAmount = game.getCurrentGovernment().getResourceAmount(resource);
        game.getCurrentGovernment().setGold(gold - price);
        game.getCurrentGovernment().setResourceAmount(resource, resourceAmount + amount);
        return Messages.SHOP_SUCCESSFUL;
    }

    public Messages sellCommodity(String item, int amount) {
        Resources resource = Resources.getResource(item);
        if (item == null)
            return Messages.INVALID_ITEM;
        int resourceAmount = game.getCurrentGovernment().getResourceAmount(resource);
        if (resourceAmount < amount)
            return Messages.NOT_ENOUGH_RESOURCES;
        int price = amount * resource.getBuyingPrice();
        int gold = game.getCurrentGovernment().getGold();
        game.getCurrentGovernment().setGold(gold + price);
        game.getCurrentGovernment().setResourceAmount(resource, resourceAmount - amount);
        return Messages.SHOP_SUCCESSFUL;
    }

}
