package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;

import javax.swing.RowFilter;

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

    public boolean areCoordinatesValid(int row, int column) {
        if (row < 0 || row >= map.getSize() || column < 0 || column >= map.getSize())
            return false;
        return true;
    }

    public Messages selectUnit(int frow, int fcolumn, int srow, int scolumn) {
        if (!areCoordinatesValid(frow, fcolumn) || !areCoordinatesValid(srow, scolumn))
            return Messages.INVALID_COORDINATES;
        if (frow > srow || fcolumn > scolumn)
            return Messages.INVALID_COORDINATES;
        return game.selectUnit(frow - 1, fcolumn - 1, srow - 1, scolumn - 1);
    }

    public Messages moveUnit(int row, int column) {
        if(!areCoordinatesValid(row, column))
            return Messages.INVALID_COORDINATES;
        if(game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        if (!map.getMapPixel(row, column).canDropObject()
                || !map.getMapPixel(row, column).getTexture().canDropBuilding()) {
            return Messages.CANT_MOVE_UNITS_TO_THIS_LOCATION;
        }
        game.moveUnit(row - 1, column - 1);
        return Messages.UNIT_MOVED_SUCCESSFULLY;
    }

    public Messages patrolUnit(int frow, int fcolumn, int srow, int scolumn) {
        if (!areCoordinatesValid(frow, fcolumn) || !areCoordinatesValid(srow, scolumn))
            return Messages.INVALID_COORDINATES;
        if(game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        if (!map.getMapPixel(frow, fcolumn).canDropObject()
                || !map.getMapPixel(frow, fcolumn).getTexture().canDropBuilding()
                || !map.getMapPixel(srow, scolumn).canDropObject()
                || !map.getMapPixel(srow, scolumn).getTexture().canDropBuilding()) {
            return Messages.CANT_MOVE_UNITS_TO_THIS_LOCATION;
        }
        game.patrolUnits(frow - 1, fcolumn - 1, srow - 1, scolumn - 1);
        return Messages.UNIT_MOVED_SUCCESSFULLY;
    }

    public Messages stopUnit() {
        if(game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        game.stopUnit();
        return Messages.UNIT_STOPPED_SUCCESSFULLY;
    }

    public Messages setStance(int frow, int fcolumn, String stance) {
        if(!areCoordinatesValid(frow, fcolumn))
            return Messages.INVALID_COORDINATES;
        UnitStance unitStance = UnitStance.getStanceByName(stance);
        if (unitStance == null)
            return Messages.INVALID_STANCE;
        if(game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        game.setStance(frow, fcolumn, unitStance);
        return Messages.STANCE_CHANGED_SUCCESSFULLY;
    }

    public Messages areaAttack(int row, int column) {
        if(game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        for(Unit unit : game.getSelectedUnit()) {
            if(unit.getTypeOfPerson().getRange()==1)
                return Messages.MUST_SELECT_RANGED_UNITS;
        }
        for(Unit unit : game.getSelectedUnit()) {
            if(unit.getTypeOfPerson().getRange()<Math.abs(row-unit.getCurrentLocation()[0])+Math.abs(column-unit.getCurrentLocation()[1]))
                return Messages.OUT_OF_RANGE;
        }
        game.areaAttack(row, column);
        return Messages.AREA_ATTACKING_SET_SUCCESSFULLY;
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

    public Messages pourOil(Directions direction) {
        Engineer engineer = null;
        boolean noOneHasOil = true;
        boolean badDirection = false;
        ArrayList<Unit> selectedUnits = game.getSelectedUnit();
        for (Unit unit : selectedUnits){
            if (unit instanceof Engineer){
                Engineer engineerTemp = (Engineer) unit;
                if (engineerTemp.hasOil()) {
                    noOneHasOil = false;
                    badDirection = false;
                    int[] engineerTempCurrentLocation = engineerTemp.getCurrentLocation();
                    if (engineerTempCurrentLocation[0] + direction.x() < 0 ||
                            engineerTempCurrentLocation[0] + direction.x() >= map.getSize() ||
                            engineerTempCurrentLocation[1] + direction.y() < 0 ||
                            engineerTempCurrentLocation[1] + direction.y() >= map.getSize()){
                        badDirection = true;
                        continue;
                    }

                    engineer = engineerTemp;
                    break;
                }
            }
        }
        if (noOneHasOil) return Messages.NO_ONE_HAS_OIL;
        if (badDirection) return Messages.BAD_DIRECTION;
        engineer.pourOil(direction);
    }
}
