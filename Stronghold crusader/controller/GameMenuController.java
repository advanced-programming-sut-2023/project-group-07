package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import model.*;

public class GameMenuController {
    private static Game game = Controller.currentGame;

    public void refreshGame() {
        this.game = Controller.currentGame;
    }

    public Messages taxRate(int rate, Government government) {
        if (rate < -3 || rate > 8)
            return Messages.INVALID_RATE;
        government.setTaxRate(rate);
        return Messages.RATE_CHANGE_SUCCESSFUL;
    }

    public ArrayList<Government> getGovernments() {
        return game.getGovernments();
    }

    public void endOfTurn() throws IOException {
        game.endOfTurn();
        for (Government government : getGovernments()) { // todo : must be in game object
            government.setPopularity(government.getPopularity() + government.getTaxEffectOnPopularity()); // todo :
            government.setGold((int) (government.getGold() + government.getTaxAmount() * government.getPopulation())); // todo
                                                                                                                       // :
        }
    }

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
                    if (!map.getMapPixel(row + j, column + i).getTexture().equals(Texture.MEADOW))
                        return Messages.CANT_PLACE_THIS;
                }
        }
        int acceptedPixels = 0;
        if (typeOfBuilding.equals(TypeOfBuilding.QUARRY) || typeOfBuilding.equals(TypeOfBuilding.IRON_MINE)
                || typeOfBuilding.equals(TypeOfBuilding.PITCH_RIG)) {
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                    if (!map.getMapPixel(row + j, column + i).getTexture().equals(typeOfBuilding.getTexture()))
                        acceptedPixels++;
            if (acceptedPixels * 4 > typeOfBuilding.getLength() * typeOfBuilding.getWidth())
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
            if (doesHaveThisBuilding(typeOfBuilding) && !map.isAdjacentToSameType(row, column, typeOfBuilding.getLength(), typeOfBuilding))
                return Messages.MUST_BE_ADJACENT_TO_BUILDINGS_OF_THE_SAME_TYPE;

        boolean isWorking = true;
        Building building;
        if (typeOfBuilding.isGate()) {
            GateHouse gateHouse = new GateHouse(currentGovernment, typeOfBuilding, row, column);
            building = gateHouse;
        } else if (typeOfBuilding.isTower()) {
            Tower tower = new Tower(currentGovernment, typeOfBuilding, row, column);
            building = tower;
        } else if (typeOfBuilding.isMilitaryCamp()) {
            MilitaryCamp militaryCamp = new MilitaryCamp(currentGovernment, typeOfBuilding, row, column);
            building = militaryCamp;
        } else
            building = new Building(currentGovernment, typeOfBuilding, row, column);
        if (!currentGovernment.getNoLaborBuildings().contains(typeOfBuilding)) {
            if (currentGovernment.getPeasant() >= typeOfBuilding.getWorkerInUse()) {
                currentGovernment.changePeasant(-typeOfBuilding.getWorkerInUse());
                building.setWorkers(typeOfBuilding.getWorkerInUse());
                for (int i = 0; i < typeOfBuilding.getWorkerInUse(); i++) {
                    NonMilitary nonMilitary = new NonMilitary(
                            new int[]{map.getKeepPosition(indexOfCurrentGovernment)[0],
                                    map.getKeepPosition(indexOfCurrentGovernment)[1]},
                            currentGovernment, typeOfBuilding.getWorkerType());
                    map.getMapPixel(map.getKeepPosition(indexOfCurrentGovernment)[0],
                            map.getKeepPosition(indexOfCurrentGovernment)[1]).addPerson(nonMilitary);
                    nonMilitary.setMovePattern(map.getPathList(nonMilitary.getCurrentLocation()[0],
                            nonMilitary.getCurrentLocation()[1], row, column));
                    nonMilitary.setPatrolLocation(
                            new int[]{row, column, map.getKeepPosition(indexOfCurrentGovernment)[0],
                                    map.getKeepPosition(indexOfCurrentGovernment)[1]});
                    nonMilitary.setPatrolling(true);
                    int frow = nonMilitary.getCurrentLocation()[0], fcolumn = nonMilitary.getCurrentLocation()[1];
                    nonMilitary.move();
                    map.getMapPixel(frow, fcolumn).removePerson(nonMilitary);
                    map.getMapPixel(nonMilitary.getCurrentLocation()[0], nonMilitary.getCurrentLocation()[1])
                            .addPerson(nonMilitary);
                }
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
        return Messages.DEPLOYMENT_SUCCESSFUL;
    }

    private boolean doesHaveThisBuilding(TypeOfBuilding typeOfBuilding) {
        for(Building building : game.getCurrentGovernment().getBuildings())
            if(building.getTypeOfBuilding().equals(typeOfBuilding))
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
                game.setCurrentMilitaryCamp(MilitaryCampType.ENGINEER_GUILD);
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

    public Messages buildSiegeWeapon(String type, int row , int column) {
        Map map = game.getMap();
        Government currentGovernment = game.getCurrentGovernment();
        ArrayList <Person> selectedUnit = game.getSelectedUnit();
        SiegeWeaponType siegeWeaponType = SiegeWeaponType.getSiegeWeaponType(type);
        if(!areCoordinatesValid(row, column))
            return Messages.INVALID_COORDINATES;
        if(siegeWeaponType == null)
            return Messages.INVALID_SIEGE_WEAPON_TYPE;
        int[] location = new int[2];
        int counter = 0;
        for (Person person : selectedUnit) {
            
            if (unit.getType().equals(UnitTypes.ENGINEER)) {
                location = unit.getCurrentLocation();
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
        SiegeWeapon siegeWeapon = new SiegeWeapon(siegeWeaponType, location,game.getCurrentGovernment());
        game.getMap().getMapPixel(row, column).addSiegeWeapon(siegeWeapon);
        return Messages.SIEGE_WEAPON_BUILT_SUCCESSFULLY;
    }

    public Messages repair() {
         if (!(game.getSelectedBuilding() instanceof Tower || game.getSelectedBuilding() instanceof GateHouse))
            return Messages.CANT_REPAIR_THIS;
        int buildResource = game.getSelectedBuilding().getTypeOfBuilding().getResourceAmount();
        int damagedResource = (int) (game.getSelectedBuilding().getTypeOfBuilding().getResourceAmount() * game.getSelectedBuilding().getHp()
                / game.getSelectedBuilding().getTypeOfBuilding().getHp());
        if (buildResource - damagedResource > game.getCurrentGovernment().getResources()
                .get(game.getSelectedBuilding().getTypeOfBuilding().getResourceNeeded()))
            return Messages.NOT_ENOUGH_RESOURCES;
        if (game.getSelectedBuilding().getHp() == game.getSelectedBuilding().getTypeOfBuilding().getHp())
            return Messages.ALREADY_AT_FULL_HP;
        for (int i = game.getSelectedBuilding().getColumn(); i < game.getSelectedBuilding().getTypeOfBuilding().getLength(); i++)
            for (int j = game.getSelectedBuilding().getRow(); j < game.getSelectedBuilding().getTypeOfBuilding().getWidth(); j++)
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
        if (unitType.equals(null))
            return Messages.INVALID_UNIT_NAME;
        if (count < 0 || count > game.getCurrentGovernment().getPeasant())
            return Messages.INVALID_NUMBER;
        if (unitType.getGoldNeeded() * count > game.getCurrentGovernment().getGold())
            return Messages.NOT_ENOUGH_GOLD;
        for (Resources resource : unitType.getResourcesNeeded())
            if (game.getCurrentGovernment().getResources().get(resource) < count)
                return Messages.NOT_ENOUGH_RESOURCES;
        if (!game.getCurrentMilitaryCamp().equals(unitType.getMilitaryCampType()))
            return Messages.CANT_CREATE_THIS_UNIT_HERE;
        game.createTroop(unitType, count);
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
                if (unitType.getMilitaryCampType().equals(MilitaryCampType.BARRACKS))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("mercenary post")) {
            for (UnitTypes unitType : UnitTypes.values()) {
                if (unitType.getMilitaryCampType().equals(MilitaryCampType.MERCENARY_POST))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("engineer's guild")) {
            for (UnitTypes unitType : UnitTypes.values()) {
                if (unitType.getMilitaryCampType().equals(MilitaryCampType.ENGINEER_GUILD))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        } else if (militaryCamp.equals("cathedral")) {
            for (UnitTypes unitType : UnitTypes.values()) {
                if (unitType.getMilitaryCampType().equals(MilitaryCampType.CATHEDRAL))
                    output += unitType.getType() + "    " + unitType.getGoldNeeded() + " gold\n";
            }
        }
        return null;
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
        if (!areCoordinatesValid(frow, fcolumn) || !areCoordinatesValid(srow, scolumn))
            return Messages.INVALID_COORDINATES;
        if (frow > srow || fcolumn > scolumn)
            return Messages.INVALID_COORDINATES;
        game.selectUnit(frow - 1, fcolumn - 1, srow - 1, scolumn - 1);
        return Messages.UNIT_SELECTED_SUCCESSFULLY;
    }

    public Messages moveUnit(int row, int column) {
        Map map = game.getMap();
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
        Map map = game.getMap();
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

    public Messages attackEnemy(int row,int column) {
        if(!areCoordinatesValid(row,column))
            return Messages.INVALID_COORDINATES;
        boolean isAnEnemy = false;
        for(Person person : game.getMap().getMapPixel(row, column).getPeople())
            if(!person.getGovernment().equals(game.getCurrentGovernment())){
                isAnEnemy = true;
                break;
            }
        if(!isAnEnemy)
            return Messages.NO_ENEMY_HERE;
        if(game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        attackEnemy(row, column);
        return Messages.ATTACKING_ENEMY_UNITS;
    }
    public Messages areaAttack(int row, int column) {
        if(game.getSelectedUnit().isEmpty())
            return Messages.NO_UNITS_SELECTED;
        for(Person person : game.getSelectedUnit()) {
            if(person instanceof Person){
                Unit unit = (Unit)person;
                if(unit.getType().getRange()==1)
                    return Messages.MUST_SELECT_RANGED_UNITS;
            }
        }
        for(Person person : game.getSelectedUnit()) {
            if(person instanceof Unit) {
                Unit unit = (Unit) person;
                if(unit.getType().getRange()<Math.abs(row-unit.getCurrentLocation()[0])+Math.abs(column-unit.getCurrentLocation()[1]))
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
        Map map = game.getMap();
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
        sendToOilSmelter(engineer);
        return Messages.POUR_OIL_SUCCESSFUL;
    }

    protected static void sendToOilSmelter(Engineer engineer) {
        ArrayList<int[]> path =
                game.getNearestOilSmelterPath(engineer.getCurrentLocation(), game.getCurrentGovernment());
        engineer.goToOilSmelter(path);
    }

    public Messages giveOil() {
        Map map = game.getMap(); 
        if (!map.hasABuilding(game.getCurrentGovernment(), TypeOfBuilding.OIL_SMELTER))
            return Messages.DONT_HAVE_OIL_SMELTER;
        ArrayList<Unit> selectedUnits = game.getSelectedUnit();
        ArrayList<Engineer> getterEngineers = new ArrayList<>();
        for (Unit unit : selectedUnits){
            if (unit instanceof Engineer){
                Engineer engineerTemp = (Engineer) unit;
                if (!engineerTemp.hasOil()){
                    getterEngineers.add(engineerTemp);
                }
            }
        }
        if (getterEngineers.size() == 0) return Messages.NO_ONE_TO_GIVE_OIL_TO;
        for (Engineer engineer : getterEngineers){
            sendToOilSmelter(engineer);
        }
        return Messages.GIVING_OIL_SUCESSFUL;
    }
}
