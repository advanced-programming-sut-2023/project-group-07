package Client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//import org.mockito.internal.matchers.InstanceOf;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import Client.model.*;

public class GameMenuController {
    private static Game game = Controller.currentGame;
    private boolean[][] isDestinationOfUnit = new boolean[game.getMap().getSize()][game.getMap().getSize()];
    public void refreshGame() {
        this.game = Controller.currentGame;
    }

    public Messages taxRate(int rate, Government government) {
        if (rate < -3 || rate > 8)
            return Messages.INVALID_RATE;
        government.setTaxRate(rate);
        return Messages.RATE_CHANGE_SUCCESSFUL;
    }

    public ArrayList<Person> getSelectedUnit() {
        return game.getSelectedUnit();
    }

    public ArrayList<Government> getGovernments() {
        return game.getGovernments();
    }
    public ArrayList<Unit> createdUnit = new ArrayList<>();

    public Messages dropBuilding(int row, int column, String name) {
        Map map = game.getMap();
        Government currentGovernment = game.getCurrentGovernment();
        int indexOfCurrentGovernment = game.getIndexOfCurrentGovernment();
        TypeOfBuilding typeOfBuilding = TypeOfBuilding.getBuilding(name);
        if (typeOfBuilding == null) {
            return Messages.INVALID_BUILDING_NAME;
        }
        if (row < 0 || row > map.getSize() - typeOfBuilding.getWidth() + 1 || column < 0
                || column > map.getSize() - typeOfBuilding.getLength() + 1) {
            return Messages.INVALID_ROW_OR_COLUMN;
        }
        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                if (map.getMapPixel(row + j, column + i).getBuildings().size() != 0)
                    return Messages.THERES_ALREADY_BUILDING;

        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                if (map.getMapPixel(row + j, column + i).getPeople().size() != 0)
                    return Messages.THERES_ALREADY_UNIT;

        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++) {
                if (!map.getMapPixel(row + j, column + i).canDropObject()
                        || !map.getMapPixel(row + j, column + i).getTexture().canDropBuilding())
                    return Messages.CANT_PLACE_THIS;
            }
        if (typeOfBuilding.equals(TypeOfBuilding.APPLE_ORCHARD) ||
                typeOfBuilding.equals(TypeOfBuilding.DIARY_FARMER) ||
                typeOfBuilding.equals(TypeOfBuilding.HOPS_FARMER) ||
                typeOfBuilding.equals(TypeOfBuilding.WHEAT_FARMER)) {
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                for (int j = 0; j < typeOfBuilding.getWidth(); j++) {
                    if (!map.getMapPixel(row + j, column + i).getTexture().equals(Texture.MEADOW)
                            && !map.getMapPixel(row + j, column + i).getTexture().equals(Texture.MEADOW))
                        return Messages.CANT_PLACE_THIS;
                }
        }
        int acceptedPixels = 0;
        if (typeOfBuilding.equals(TypeOfBuilding.QUARRY) || typeOfBuilding.equals(TypeOfBuilding.IRON_MINE)
                || typeOfBuilding.equals(TypeOfBuilding.PITCH_RIG)) {
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                    if (map.getMapPixel(row + j, column + i).getTexture().equals(typeOfBuilding.getTexture()))
                        acceptedPixels++;
            if (acceptedPixels * 4 < typeOfBuilding.getLength() * typeOfBuilding.getWidth())
                return Messages.CANT_PLACE_THIS;
        }

        if (typeOfBuilding.getCost() > currentGovernment.getGold())
            return Messages.NOT_ENOUGH_GOLD;

        if (currentGovernment.getResources().containsKey(typeOfBuilding.getResourceNeeded()) &&
                typeOfBuilding.getResourceAmount() > currentGovernment.getResources()
                        .get(typeOfBuilding.getResourceNeeded()))
            return Messages.NOT_ENOUGH_RESOURCES;

        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                if (game.isAnEnemyCloseBy(row + j, column + i))
                    return Messages.THERES_AN_ENEMY_CLOSE_BY;

        if (typeOfBuilding.equals(TypeOfBuilding.GRANARY) || typeOfBuilding.equals(TypeOfBuilding.STOCK_PILE))
            if (doesHaveThisBuilding(typeOfBuilding)
                    && !map.isAdjacentToSameType(row, column, typeOfBuilding.getLength(), typeOfBuilding))
                return Messages.MUST_BE_ADJACENT_TO_BUILDINGS_OF_THE_SAME_TYPE;

        boolean isWorking = true;
        Building building;
        switch (typeOfBuilding.getType()) {
            case "gate":
                GateHouse gateHouse = new GateHouse(currentGovernment, typeOfBuilding, row, column);
                building = gateHouse;
                break;
            case "tower":
                Tower tower = new Tower(currentGovernment, typeOfBuilding, row, column);
                building = tower;
                break;
            case "military camp":
                MilitaryCamp militaryCamp = new MilitaryCamp(currentGovernment, typeOfBuilding, row, column);
                building = militaryCamp;
                break;
            case "converting resources":
                ConvertingResources convertingResources = new ConvertingResources(currentGovernment, typeOfBuilding,
                        row,
                        column, ConvertingResourcesTypes.getTypeByName(name));
                building = convertingResources;
                break;
            case "caged war dogs":
                CagedWarDogs cagedWarDogs = new CagedWarDogs(currentGovernment, typeOfBuilding, row, column);
                building = cagedWarDogs;
                break;
            default:
                building = new Building(currentGovernment, typeOfBuilding, row, column);
                break;
        }
        currentGovernment.changeGold(-building.getTypeOfBuilding().getCost());
        currentGovernment.changeResources(building.getTypeOfBuilding().getResourceNeeded(),
                -building.getTypeOfBuilding().getResourceAmount());
        currentGovernment.addBuilding(building);
        if (!currentGovernment.getNoLaborBuildings().contains(typeOfBuilding)) {
            if (currentGovernment.getPeasant() >= typeOfBuilding.getWorkerInUse()) {
                game.setBuildingWorker(building, currentGovernment);
            } else {
                isWorking = false;
                currentGovernment.addBuildingsWaitingForWorkers(building);
            }
        } else {
            isWorking = false;
        }
        building.workingState(isWorking);
        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                map.getMapPixel(row + j, column + i).addBuilding(building);
        if (building instanceof GateHouse) {
            map.getMapPixel(row, column + (int) (typeOfBuilding.getLength() / 2) - 1).setAccess();
            map.getMapPixel(row, column + (int) (typeOfBuilding.getLength() / 2)).setAccess();
            map.getMapPixel(row, column + (int) (typeOfBuilding.getLength() / 2) + 1).setAccess();
            map.getMapPixel(row + typeOfBuilding.getWidth() - 1, column + (int) (typeOfBuilding.getLength() / 2) - 1)
                    .setAccess();
            map.getMapPixel(row + typeOfBuilding.getWidth() - 1, column + (int) (typeOfBuilding.getLength() / 2))
                    .setAccess();
            map.getMapPixel(row + typeOfBuilding.getWidth() - 1, column + (int) (typeOfBuilding.getLength() / 2) + 1)
                    .setAccess();
        }
        if (typeOfBuilding.equals(TypeOfBuilding.HOVEL))
            game.getCurrentGovernment().setPopulation(game.getCurrentGovernment().getPopulation() + 8);
        return Messages.DEPLOYMENT_SUCCESSFUL;
    }

    private boolean doesHaveThisBuilding(TypeOfBuilding typeOfBuilding) {
        for (Building building : game.getCurrentGovernment().getBuildings())
            if (building.getTypeOfBuilding().equals(typeOfBuilding))
                return true;
        return false;
    }

    public Messages selectBuilding(int row, int column) {
        Map map = game.getMap();
        if (row < 0 || row > map.getSize() || column < 0 || column > map.getSize()) {
            return Messages.INVALID_ROW_OR_COLUMN;
        }
        if (map.getMapPixel(row, column).getBuildings().size() == 0)
            return Messages.NO_BUILDING_HERE;
        if (!map.getMapPixel(row, column).getBuildings().get(0).getGovernment().equals(game.getCurrentGovernment()))
            return Messages.ENEMY_BUILDING;
        game.setSelectedBuilding(map.getMapPixel(row, column).getBuildings().get(0));
        if (game.getSelectedBuilding() instanceof Tower) {
            return Messages.ENTERED_TOWER;
        }
        if (game.getSelectedBuilding() instanceof GateHouse) {
            return Messages.ENTERED_GATEHOUSE;
        }
        if (game.getSelectedBuilding() instanceof MilitaryCamp) {
            MilitaryCamp militaryCamp = (MilitaryCamp) map.getMapPixel(row, column).getBuildings().get(0);
            if (militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.BARRACKS))
                game.setCurrentMilitaryCamp(MilitaryCampType.BARRACKS);
            if (militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.MERCENARY_POST))
                game.setCurrentMilitaryCamp(MilitaryCampType.MERCENARY_POST);
            if (militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.ENGINEERS_GUILD))
                game.setCurrentMilitaryCamp(MilitaryCampType.ENGINEERS_GUILD);
            return Messages.ENTERED_MILITARY_CAMP;
        }
        if (game.getSelectedBuilding().getTypeOfBuilding().equals(TypeOfBuilding.BLACKSMITH) ||
                game.getSelectedBuilding().getTypeOfBuilding().equals(TypeOfBuilding.FLETCHER) ||
                game.getSelectedBuilding().getTypeOfBuilding().equals(TypeOfBuilding.POLETURNER)) {
            return Messages.ENTERED_ARMS_WORKSHOP;
        }
        if (game.getSelectedBuilding().getTypeOfBuilding().equals(TypeOfBuilding.MARKET))
            return Messages.ENTERED_MARKET;
        else
            return Messages.ENTERED_BUILDING_SUCCESSFULLY;
    }

    public String getSelectedBuilding() {
        return game.getSelectedBuilding().getTypeOfBuilding().getBuildingName();
    }

    public Messages buildSiegeWeapon(String type) { // todo : review this method
        Map map = game.getMap();
        Government currentGovernment = game.getCurrentGovernment();
        ArrayList<Person> selectedUnit = game.getSelectedUnit();
        SiegeWeaponType siegeWeaponType = SiegeWeaponType.getSiegeWeaponType(type);
        if (siegeWeaponType == null)
            return Messages.INVALID_SIEGE_WEAPON_TYPE;
        int[] location = new int[2];
        int counter = 0;
        for (Person person : selectedUnit) {
            if (!(person instanceof Unit))
                continue;
            if (((Unit) person).getType().equals(UnitTypes.ENGINEER)) {
                location = person.getCurrentLocation();
                counter++;
            }
        }
        if (counter < siegeWeaponType.getEngineersNeeded())
            return Messages.NEEDS_MORE_ENGINEERS;
        if (siegeWeaponType.getGoldNeeded() > currentGovernment.getGold())
            return Messages.NOT_ENOUGH_GOLD;
        ArrayList<Person> engineers = new ArrayList<>();
        for (Person person : map.getMapPixel(location[0], location[1]).getPeople()) {
            if (counter == 0)
                break;
            if (((Unit) person).getType().equals(UnitTypes.ENGINEER)) {
                counter--;
                engineers.add(person);
            }
        }
        map.getMapPixel(location[0], location[1]).getPeople().removeAll(engineers);
        currentGovernment.getPeople().removeAll(engineers);
        SiegeWeapon siegeWeapon = new SiegeWeapon(siegeWeaponType, location, game.getCurrentGovernment());
        currentGovernment.addPerson(siegeWeapon);
        game.getMap().getMapPixel(location[0], location[1]).addSiegeWeapon(siegeWeapon);
        return Messages.SIEGE_WEAPON_BUILT_SUCCESSFULLY;
    }

    public Messages repair() {
        if (!(game.getSelectedBuilding() instanceof Tower || game.getSelectedBuilding() instanceof GateHouse))
            return Messages.CANT_REPAIR_THIS;
        int buildResource = game.getSelectedBuilding().getTypeOfBuilding().getResourceAmount();
        int damagedResource = (int) (game.getSelectedBuilding().getTypeOfBuilding().getResourceAmount()
                * game.getSelectedBuilding().getHp()
                / game.getSelectedBuilding().getTypeOfBuilding().getHp());
        if (buildResource - damagedResource > game.getCurrentGovernment().getResources()
                .get(game.getSelectedBuilding().getTypeOfBuilding().getResourceNeeded()))
            return Messages.NOT_ENOUGH_RESOURCES;
        if (game.getSelectedBuilding().getHp() == game.getSelectedBuilding().getTypeOfBuilding().getHp())
            return Messages.ALREADY_AT_FULL_HP;
        for (int i = game.getSelectedBuilding().getColumn(); i < game.getSelectedBuilding().getColumn()
                + game.getSelectedBuilding().getTypeOfBuilding()
                        .getLength(); i++)
            for (int j = game.getSelectedBuilding().getRow(); j < game.getSelectedBuilding().getColumn()
                    + game.getSelectedBuilding().getTypeOfBuilding()
                            .getWidth(); j++)
                if (game.isAnEnemyCloseBy(j, i))
                    return Messages.THERES_AN_ENEMY_CLOSE_BY;
        game.getSelectedBuilding().repair();
        Resources resourceNeeded = game.getSelectedBuilding().getTypeOfBuilding().getResourceNeeded();
        game.getCurrentGovernment().getResources().put(resourceNeeded,
                game.getCurrentGovernment().getResources().get(resourceNeeded) - (buildResource - damagedResource));
        return Messages.REPAIRED_SUCCESSFULLY;
    }

    public Messages createUnit(String type, int count) {
        UnitTypes unitType = UnitTypes.getUnitTypeFromString(type);
        if (unitType == null || type.equals("lord"))
            return Messages.INVALID_UNIT_NAME;
        if (count < 0)
            return Messages.INVALID_NUMBER;
        if (count > game.getCurrentGovernment().getPeasant())
            return Messages.NOT_ENOUGH_PEASANTS;
        if (unitType.getGoldNeeded() * count > game.getCurrentGovernment().getGold())
            return Messages.NOT_ENOUGH_GOLD;
        for (Resources resource : unitType.getResourcesNeeded())
            if (game.getCurrentGovernment().getResources().get(resource) < count)
                return Messages.NOT_ENOUGH_RESOURCES;
        createdUnit=game.createTroop(unitType, count);
        isDestinationOfUnit[game.getMap().getKeepPosition(game.getCurrentGovernment().getColor())[0] + 7][game.getMap().getKeepPosition(game.getCurrentGovernment().getColor())[1] + 3] = true;
        return Messages.UNIT_CREATED_SUCCESSFULLY;
    }

    public Messages closeGate() {
        GateHouse gateHouse = (GateHouse) game.getSelectedBuilding();
        if (gateHouse.getState() == true)
            return Messages.GATE_ALREADY_CLOSED;
        gateHouse.setClosed(true);
        return Messages.GATE_HAS_BEEN_CLOSED;
    }

    public Messages openGate() {
        GateHouse gateHouse = (GateHouse) game.getSelectedBuilding();
        if (gateHouse.getState() == false)
            return Messages.GATE_ALREADY_OPEN;
        gateHouse.setClosed(false);
        return Messages.GATE_HAS_BEEN_OPENED;
    }

    public String getUnitsInfo(String militaryCamp) {
        String output = "";
        if (militaryCamp.equals("barracks")) {
            for (UnitTypes unitType : UnitTypes.values()) {
                if (unitType.getMilitaryCampType() != null
                        && unitType.getMilitaryCampType().equals(MilitaryCampType.BARRACKS))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("mercenary post")) {
            for (UnitTypes unitType : UnitTypes.values()) {
                if (unitType.getMilitaryCampType() != null
                        && unitType.getMilitaryCampType().equals(MilitaryCampType.MERCENARY_POST))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("engineers guild")) {
            for (UnitTypes unitType : UnitTypes.values()) {
                if (unitType.getMilitaryCampType() != null
                        && unitType.getMilitaryCampType().equals(MilitaryCampType.ENGINEERS_GUILD))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("cathedral")) {
            for (UnitTypes unitType : UnitTypes.values()) {
                if (unitType.getMilitaryCampType() != null
                        && unitType.getMilitaryCampType().equals(MilitaryCampType.CATHEDRAL))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        }
        return output;
    }

    public void changeArms() {
        game.changeArms();
    }

    public String getResources() {
        ConvertingResources convertingResources = (ConvertingResources) game.getSelectedBuilding();
        return convertingResources.getResource().getName();
    }

    public void changeWorkingState() {
        game.changeWorkingState();
    }

    public boolean areCoordinatesValid(int row, int column) {
        int size = game.getMap().getSize();
        if (row < 0 || row >= size || column < 0 || column >= size)
            return false;
        return true;
    }

    public Messages selectUnit(int frow, int fcolumn, int srow, int scolumn) {
        game.selectUnit(frow, fcolumn, srow, scolumn);
        return Messages.UNIT_SELECTED_SUCCESSFULLY;
    }

    public Messages moveUnit(int row, int column) {
        Map map = game.getMap();
        if (!areCoordinatesValid(row, column))
            return Messages.INVALID_COORDINATES;
        if (game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        if (!map.getMapPixel(row, column).getTexture().canDropBuilding()) {
            return Messages.CANT_MOVE_UNITS_TO_THIS_LOCATION;
        }
        isDestinationOfUnit[row][column]=true;
        for(Person person :game.getSelectedUnit()){
            if(person.getMovePattern().isEmpty())
                isDestinationOfUnit[person.getCurrentLocation()[0]][person.getCurrentLocation()[1]]=false;
            else
                isDestinationOfUnit[person.getMovePattern().get(person.getMovePattern().size()-1)[0]][person.getMovePattern().get(person.getMovePattern().size()-1)[1]]=false;
        }
        game.moveUnit(row, column);
        return Messages.UNIT_MOVED_SUCCESSFULLY;
    }

    public Messages patrolUnit(int frow, int fcolumn, int srow, int scolumn) {
        Map map = game.getMap();
        if (!areCoordinatesValid(frow, fcolumn) || !areCoordinatesValid(srow, scolumn))
            return Messages.INVALID_COORDINATES;
        if (game.getSelectedUnit().isEmpty())
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
        if (game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        game.stopUnit();
        return Messages.UNIT_STOPPED_SUCCESSFULLY;
    }

    public Messages setStance(int frow, int fcolumn, String stance) {
        if (!areCoordinatesValid(frow, fcolumn))
            return Messages.INVALID_COORDINATES;
        UnitStance unitStance = UnitStance.getStanceByName(stance);
        if (unitStance == null)
            return Messages.INVALID_STANCE;
        if (game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        game.setStance(frow, fcolumn, unitStance);
        return Messages.STANCE_CHANGED_SUCCESSFULLY;
    }

    public Messages attackEnemy(int row, int column) {
        if (!areCoordinatesValid(row, column))
            return Messages.INVALID_COORDINATES;
        if (game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        Person enemy = null;
        for (Person person : game.getMap().getMapPixel(row, column).getPeople())
            if (!person.getGovernment().equals(game.getCurrentGovernment())) {
                enemy = person;
                break;
            }
        if (enemy == null)
            return Messages.NO_ENEMY_HERE;
        game.attackEnemy(row, column, enemy);
        return Messages.ATTACKING_ENEMY_UNITS;
    }

    public Messages attackBuilding(int row, int column) {
        if (!areCoordinatesValid(row, column))
            return Messages.INVALID_COORDINATES;
        Building enemyBuilding = null;
        for (Building building : game.getMap().getMapPixel(row, column).getBuildings())
            if (!building.getGovernment().equals(game.getCurrentGovernment())) {
                enemyBuilding = building;
                break;
            }
        if (enemyBuilding == null)
            return Messages.NO_ENEMY_HERE;
        if (game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        boolean canAnyOneDamageBuilding = false;
        for (Person person : game.getSelectedUnit()) {
            if (game.canAttackBuilding((Unit) person)) {
                canAnyOneDamageBuilding = true;
                break;
            }
        }
        if (canAnyOneDamageBuilding == false)
            return Messages.NO_UNIT_CAN_ATTACK_BUILDINGS;
        game.attackBuildings(enemyBuilding);
        return Messages.ATTACKING_ENEMY_BUILDINGS;

    }

    public Messages areaAttack(int row, int column) {
        if (game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        for (Person person : game.getSelectedUnit()) {
            if (person instanceof Person) {
                Unit unit = (Unit) person;
                if (unit.getType().getRange() == 1)
                    return Messages.MUST_SELECT_RANGED_UNITS;
            }
        }
        for (Person person : game.getSelectedUnit()) {
            if (person instanceof Unit) {
                Unit unit = (Unit) person;
                if (unit.getType().getRange() < Math.abs(row - unit.getCurrentLocation()[0])
                        + Math.abs(column - unit.getCurrentLocation()[1]))
                    return Messages.OUT_OF_RANGE;
            }
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
        return ("*Name:" + resource +
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
        return Messages.SHOP_SUCCESSFUL;
    }

    public Messages sellCommodity(String item, int amount) {
        Resources resource = Resources.getResource(item);
        if (resource == null)
            return Messages.INVALID_ITEM;
        int resourceAmount = game.getCurrentGovernment().getResourceAmount(resource);
        if (resourceAmount < amount)
            return Messages.NOT_ENOUGH_RESOURCES;
        int price = amount * resource.getSellingPrice();
        game.getCurrentGovernment().changeGold(price);
        game.getCurrentGovernment().changeResources(resource, -amount);
        return Messages.SHOP_SUCCESSFUL;
    }
    public int getResourceAmount(Resources resource){
        return game.getCurrentGovernment().getResourceAmount(resource);
    }

    public String showGold() {
        return "Your gold: " + game.getCurrentGovernment().getGold();
    }

    public String showCurrentPlayer() {
        return "Current player: " + game.getCurrentGovernment().getUser().getUsername() + " (color "
                + game.getCurrentGovernment().getColor().toString() + ")";
    }

    public String nextTurn() throws IOException {
        String result = "";
        result += game.nextTurn();
        if (game.gameOver())
            result += game.endGame();
        return result.equals("") ? null : result;
    }

    public String nextTurnMessage() {
        return "Now " + game.getCurrentGovernment().getColor().toString() + " lord (player "
                + game.getCurrentGovernment().getUser().getUsername() + ") is playing!";
    }

    public int showTaxRate() {
        return game.getCurrentGovernment().getTaxRate();
    }

    public ArrayList<Integer> getPopularityFactors() {
        Government government = game.getCurrentGovernment();
        ArrayList<Integer> factorsInOrder = new ArrayList<>();
        factorsInOrder.add(government.getFoodEffectOnPopularity());
        factorsInOrder.add(government.getTaxEffectOnPopularity());
        factorsInOrder.add(government.getReligionEffectOnPopularity());
        factorsInOrder.add(government.getFearRate());
        factorsInOrder.add(government.getBuildingsEffectOnPopularity()); // don't change this order;
        return factorsInOrder;
    }

    public int showFearRate() {
        return game.getCurrentGovernment().getFearRate();
    }

    public Messages pourOil(Directions direction) {
        Map map = game.getMap();
        Engineer engineer = null;
        boolean noOneHasOil = true;
        boolean badDirection = false;
        ArrayList<Person> selectedUnits = game.getSelectedUnit();
        for (Person person : selectedUnits) {
            Unit unit;
            if (person instanceof Unit)
                unit = (Unit) person;
            else
                continue;

            if (unit instanceof Engineer) {
                Engineer engineerTemp = (Engineer) unit;
                if (engineerTemp.hasOil()) {
                    noOneHasOil = false;
                    badDirection = false;
                    int[] engineerTempCurrentLocation = engineerTemp.getCurrentLocation();
                    if (engineerTempCurrentLocation[0] + direction.x() < 0 ||
                            engineerTempCurrentLocation[0] + direction.x() >= map.getSize() ||
                            engineerTempCurrentLocation[1] + direction.y() < 0 ||
                            engineerTempCurrentLocation[1] + direction.y() >= map.getSize()) {
                        badDirection = true;
                        continue;
                    }

                    engineer = engineerTemp;
                    break;
                }
            }
        }
        if (noOneHasOil)
            return Messages.NO_ONE_HAS_OIL;
        if (badDirection)
            return Messages.BAD_DIRECTION;
        engineer.pourOil(direction);
        sendToOilSmelter(engineer);
        return Messages.POUR_OIL_SUCCESSFUL;
    }

    protected static void sendToOilSmelter(Engineer engineer) {
        engineer.goToOilSmelter(game.sendToAOilSmelter(engineer));
    }

    public Messages giveOil() {
        Map map = game.getMap();
        if (!map.hasABuilding(game.getCurrentGovernment(), TypeOfBuilding.OIL_SMELTER))
            return Messages.DONT_HAVE_OIL_SMELTER;
        ArrayList<Person> selectedUnits = game.getSelectedUnit();
        ArrayList<Engineer> getterEngineers = new ArrayList<>();
        for (Person person : selectedUnits) {
            Unit unit;
            if (person instanceof Unit)
                unit = (Unit) person;
            else
                continue;

            if (unit instanceof Engineer) {
                Engineer engineerTemp = (Engineer) unit;
                if (!engineerTemp.hasOil()) {
                    getterEngineers.add(engineerTemp);
                }
            }
        }
        if (getterEngineers.size() == 0)
            return Messages.NO_ONE_TO_GIVE_OIL_TO;
        for (Engineer engineer : getterEngineers) {
            sendToOilSmelter(engineer);
        }
        return Messages.GIVING_OIL_SUCCESSFUL;
    }

    public Messages digTunnel(int x, int y) {
        int size = game.getMap().getSize();
        if (x < 0 || x > size - 1 || y < 0 || y > size - 1)
            return Messages.INVALID_COORDINATES;
        ArrayList<Person> selectedUnits = game.getSelectedUnit();
        Government owner = game.getCurrentGovernment();
        ArrayList<Tunneler> tunnelers = getTunnelers(selectedUnits, owner);

        if (tunnelers.size() == 0)
            return Messages.NO_AVAILABLE_TUNNELER;
        sendTunnelersToWork(x, y, tunnelers);
        return Messages.SUCCESSFUL_DIG_TUNNEL;
    }

    private void sendTunnelersToWork(int x, int y, ArrayList<Tunneler> tunnelers) {
        for (Tunneler tunneler : tunnelers) {
            tunneler.setAvailable(false);
            Controller.sendToCoordinate(x, y, tunneler);
        }
    }

    private ArrayList<Tunneler> getTunnelers(ArrayList<Person> selectedUnits, Government owner) {
        ArrayList<Tunneler> tunnelers = new ArrayList<>();
        for (Person person : selectedUnits) {
            if (!person.getGovernment().equals(owner))
                continue;
            if (person instanceof Tunneler) {
                Tunneler tunneler = (Tunneler) person;
                if (tunneler.isAvailable())
                    tunnelers.add(tunneler);
            }
        }
        return tunnelers;
    }

    public Messages disbandUnit() {
        ArrayList<Person> selectedUnits = game.getSelectedUnit();
        if (selectedUnits.size() == 0)
            return Messages.NO_UNITS_SELECTED;
        for (Person person : selectedUnits) {
            if (person instanceof Unit) {
                Unit unit = (Unit) person;
                disbandAUnit(unit);
            }
        }
        return Messages.UNITS_DISBANDED_SUCCESSFULLY;
    }

    private void disbandAUnit(Unit unit) {
        Government owner = unit.getGovernment();
        Map map = game.getMap();
        int[] keepPosition = map.getKeepPosition(owner.getColor());
        NonMilitary nonMilitary = new NonMilitary(new int[] { keepPosition[0] + 7, keepPosition[1] + 3 }, owner,
                NonMilitaryTypes.PEASANT, null);
        MapPixel personPixel = map.getMapPixel(keepPosition[0] + 7, keepPosition[1] + 3);
        personPixel.addPerson(nonMilitary);
        owner.addPerson(nonMilitary);
        int[] unitLocation = unit.getCurrentLocation();
        MapPixel unitPixel = map.getMapPixel(unitLocation[0], unitLocation[1]);
        unitPixel.removePerson(unit);
        owner.removePerson(unit);

    }

    public Messages setTax(int rate) {
        if (rate > 8 || rate < -3) {
            return Messages.INVALID_RATE;
        }
        game.getCurrentGovernment().setTaxRate(rate);
        return Messages.SETTING_TAX_SUCCESSFUL;
    }

    public int getPopulation() {
        return game.getCurrentGovernment().getPopulation();
    }

    public int getTaxEffectOnPopularity() {
        return game.getCurrentGovernment().getTaxEffectOnPopularity();
    }

    public int getGold() {
        return game.getCurrentGovernment().getGold();
    }

    public void resetMapsAndUsers() throws IOException {
        Map.loadMaps();
        User.sortUsers();
        User.updateUsers();
    }

    public Messages setFearRate(int rate) {
        if (rate < -5 || rate > 5)
            return Messages.INVALID_RATE;
        game.getCurrentGovernment().setFearRate(rate);
        return Messages.SET_FEAR_RATE_SUCCESSFUL;
    }

    public int getFearRate() {
        return game.getCurrentGovernment().getFearRate();
    }

    public void addHealthBarListener(ProgressBar healthBar,Unit unit) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),actionEvent -> {
            healthBar.setProgress((double)unit.getHp()/unit.getType().getHp());
            if(healthBar.progressProperty().get()<0.33)
                healthBar.setStyle("-fx-accent: red");
            else if(healthBar.progressProperty().get()<0.66)
                healthBar.setStyle("-fx-accent: yellow");
            else
                healthBar.setStyle("-fx-accent: green");
            healthBar.setVisible(getSelectedUnit().contains(unit));
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    public void addUnitListener(Unit unit) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300),actionEvent -> {
            if(unit.getMovePattern().isEmpty() && game.getMap().getMapPixel(unit.getCurrentLocation()[0],unit.getCurrentLocation()[1]).getUnits().size()>1){
                isDestinationOfUnit[unit.getCurrentLocation()[0]][unit.getCurrentLocation()[1]]=true;
                int[] destination = getNearestEmptyCell(unit);
                unit.setMovePattern(
                        game.getMap().getPathList(unit.getCurrentLocation()[0], unit.getCurrentLocation()[1], destination[0], destination[1],
                                unit.getType().equals(UnitTypes.ASSASSIN)));
                isDestinationOfUnit[destination[0]][destination[1]]=true;
            }
            if(!unit.getMovePattern().isEmpty() && isDestinationOfUnit[unit.getMovePattern().get(unit.getMovePattern().size()-1)[0]][unit.getMovePattern().get(unit.getMovePattern().size()-1)[1]]
            && game.getMap().getMapPixel(unit.getMovePattern().get(unit.getMovePattern().size()-1)[0],unit.getMovePattern().get(unit.getMovePattern().size()-1)[1]).getUnits().size()>1){
                int[] destination = getNearestEmptyCell(unit);
                unit.setMovePattern(
                        game.getMap().getPathList(unit.getCurrentLocation()[0], unit.getCurrentLocation()[1], destination[0], destination[1],
                                unit.getType().equals(UnitTypes.ASSASSIN)));
                isDestinationOfUnit[destination[0]][destination[1]]=true;
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private int[] getNearestEmptyCell(Unit unit){
        for (int i = 0; i <= 50; i++) {
            int row = unit.getCurrentLocation()[0], column = unit.getCurrentLocation()[1];
            if(!unit.getMovePattern().isEmpty()) {
                row = unit.getMovePattern().get(unit.getMovePattern().size()-1)[0];
                column = unit.getMovePattern().get(unit.getMovePattern().size()-1)[1];
            }
            for (int j = -i; j <= i; j++) {
                int y = column + i - Math.abs(j);
                if (row + j < 0 || row + j >= game.getMap().getSize() || y < 0
                        || y >= game.getMap().getSize() || !game.getMap().getMapPixel(row + j, y).getBuildings().isEmpty()
                        || !game.getMap().getMapPixel(row + j, y).getTexture().canDropUnit() || isDestinationOfUnit[row + j][y])
                    continue;
                else
                    return new int[]{row + j, y};
            }
            for (int j = i; j >= -i; j--) {
                int x = column - i + Math.abs(j);
                if (row + j < 0 || row + j >= game.getMap().getSize() || x < 0
                        || x >= game.getMap().getSize() || !game.getMap().getMapPixel(row + j, x).getBuildings().isEmpty()
                        || !game.getMap().getMapPixel(row + j, x).getTexture().canDropUnit() || isDestinationOfUnit[row + j][x])
                    continue;
                else
                    return new int[]{row + j, x};
            }
        }
        return null;
    }

    public static Game getGame() {
        return game;
    }

    public void applyPersonMove(Person person) {
        game.applyPersonMove(person);
    }


}
