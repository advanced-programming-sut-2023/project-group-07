package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.io.IOException;
import controller.Controller;
import controller.Messages;

public class Game {
    private final ArrayList<Government> governments;
    private final Map map;
    private Government currentGovernment;
    private MilitaryCampType currentMilitaryCamp;
    private Building selectedBuilding;
    private ArrayList<Unit> selectedUnit;
    private int[] selectedUnitArea;
    private int indexOfCurrentGovernment = 0;

    public Game(Map map, ArrayList<Government> governments, int goldToBeginWith) {
        this.map = map;
        this.governments = governments;
        currentGovernment = governments.get(0); // todo: we can randomize order of players
        for (Government government : this.governments) {
            government.setGold(goldToBeginWith);
        }
        HashMap<LordColor, Government> governmentsMap = new HashMap<LordColor, Government>();
        for (Government government : this.governments)
            governmentsMap.put(government.getColor(), government);
        map.startGame(governmentsMap);
    }

    public Messages taxRate(int rate, Government government) {
        if (rate < -3 || rate > 8)
            return Messages.INVALID_RATE;
        government.setTaxRate(rate);
        return Messages.RATE_CHANGE_SUCCESSFUL;
    } // todo : remove this?

    public User getCurrentUser() {
        return currentGovernment.getUser();
    }

    public Government getCurrentGovernment() {
        return currentGovernment;
    }

    public Messages fearRate(int rate, Government government) {
        if (rate > 5 || rate < -5)
            return Messages.INVALID_RATE;
        government.setFearRate(rate);
        return Messages.RATE_CHANGE_SUCCESSFUL;
    }

    public Map getMap() {
        return this.map;
    }

    public boolean isAnEnemyCloseBy(int row, int column) {
        for (int i = -5; i < 6; i++) {
            for (int j = Math.abs(i) - 5; j < 6 - Math.abs(i); j++) {
                if (row + i >= 0 && column + j >= 0 && row + i < map.getSize() && column + j < map.getSize())
                    for (Person person : map.getMapPixel(row + i, column + j).getPeople())
                        if (person instanceof Unit && !((Unit) person).getGovernment().equals(currentGovernment))
                            return true;
            }
        }
        return false;
    }

    public Messages dropBuilding(int row, int column, TypeOfBuilding typeOfBuilding) {
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
                if (isAnEnemyCloseBy(row + j, column + i))
                    return Messages.THERES_AN_ENEMY_CLOSE_BY;

        if (typeOfBuilding.equals(TypeOfBuilding.GRANARY) || typeOfBuilding.equals(TypeOfBuilding.STOCK_PILE))
            if (!map.isAdjacentToSameType(row, column, typeOfBuilding.getLength(), typeOfBuilding))
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
                            new int[] { map.getKeepPosition(indexOfCurrentGovernment)[0],
                                    map.getKeepPosition(indexOfCurrentGovernment)[1] },
                            currentGovernment, typeOfBuilding.getWorkerType());
                    map.getMapPixel(map.getKeepPosition(indexOfCurrentGovernment)[0],
                            map.getKeepPosition(indexOfCurrentGovernment)[1]).addPerson(nonMilitary);
                    nonMilitary.setMovePattern(map.getPathList(nonMilitary.currentLocation[0],
                            nonMilitary.currentLocation[1], row, column));
                    nonMilitary.setPatrolLocation(
                            new int[] { row, column, map.getKeepPosition(indexOfCurrentGovernment)[0],
                                    map.getKeepPosition(indexOfCurrentGovernment)[1] });
                    nonMilitary.setPatrolling(true);
                    int  frow=nonMilitary.currentLocation[0], fcolumn=nonMilitary.currentLocation[1];
                    nonMilitary.move();
                    map.getMapPixel(frow,fcolumn).removePerson(nonMilitary);
                    map.getMapPixel(nonMilitary.currentLocation[0],nonMilitary.currentLocation[1]).addPerson(nonMilitary);
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

    public Messages selectBuilding(int row, int column) {
        if (row < 0 || row > map.getSize() || column < 0 || column > map.getSize()) {
            return Messages.INVALID_ROW_OR_COLUMN;
        }
        if (map.getMapPixel(row, column).getBuildings().size() == 0)
            return Messages.NO_BUILDING_HERE;
        if (!map.getMapPixel(row, column).getBuildings().get(0).government.equals(currentGovernment))
            return Messages.ENEMY_BUILDING;
        this.selectedBuilding = map.getMapPixel(row, column).getBuildings().get(0);
        if (selectedBuilding instanceof Tower) {
            return Messages.ENTERED_TOWER;
        }
        if (selectedBuilding instanceof GateHouse) {
            return Messages.ENTERED_GATEHOUSE;
        }
        if (selectedBuilding instanceof MilitaryCamp) {
            MilitaryCamp militaryCamp = (MilitaryCamp) map.getMapPixel(row, column).getBuildings().get(0);
            if (militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.BARRACKS))
                currentMilitaryCamp = MilitaryCampType.BARRACKS;
            if (militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.MERCENARY_POST))
                currentMilitaryCamp = MilitaryCampType.MERCENARY_POST;
            if (militaryCamp.getTypeOfBuilding().equals(TypeOfBuilding.ENGINEERS_GUILD))
                currentMilitaryCamp = MilitaryCampType.ENGINEER_GUILD;
            return Messages.ENTERED_MILITARY_CAMP;
        }
        if (selectedBuilding.getTypeOfBuilding().equals(TypeOfBuilding.BLACKSMITH) ||
                selectedBuilding.getTypeOfBuilding().equals(TypeOfBuilding.FLETCHER) ||
                selectedBuilding.getTypeOfBuilding().equals(TypeOfBuilding.POLETURNER)) {
            return Messages.ENTERED_ARMS_WORKSHOP;
        }
        if (selectedBuilding.getTypeOfBuilding().equals(TypeOfBuilding.MARKET))
            return Messages.ENTERED_MARKET;
        return null;
    }

    public Messages closeGate() {
        GateHouse gateHouse = (GateHouse) selectedBuilding;
        if (gateHouse.getState() == true)
            return Messages.GATE_ALREADY_CLOSED;
        gateHouse.setClosed(true);
        return Messages.GATE_HAS_BEEN_CLOSED;
    }

    public Messages openGate() {
        GateHouse gateHouse = (GateHouse) selectedBuilding;
        if (gateHouse.getState() == false)
            return Messages.GATE_ALREADY_OPEN;
        gateHouse.setClosed(false);
        return Messages.GATE_HAS_BEEN_OPENED;
    }

    public Messages createTroop(String type, int count) {
        TypeOfPerson typeOfPerson = TypeOfPerson.getTypeOfPersonFromString(type);
        if (typeOfPerson.equals(null))
            return Messages.INVALID_UNIT_NAME;
        if (count < 0 || count > currentGovernment.getPeasant())
            return Messages.INVALID_NUMBER;
        if (typeOfPerson.getGoldNeeded() * count > currentGovernment.getGold())
            return Messages.NOT_ENOUGH_GOLD;
        for (Resources resource : typeOfPerson.getResourcesNeeded())
            if (currentGovernment.getResources().get(resource) < count)
                return Messages.NOT_ENOUGH_RESOURCES;
        if (!currentMilitaryCamp.equals(typeOfPerson.getMilitaryCampType()))
            return Messages.CANT_CREATE_THIS_UNIT_HERE;
        currentGovernment.setGold(-count * typeOfPerson.getGoldNeeded());
        for (Resources resource : typeOfPerson.getResourcesNeeded())
            currentGovernment.changeResources(resource, -count);
        currentGovernment.changePeasant(-count);
        for (int i = 0; i < count; i++) {
            Unit unit = new Unit(typeOfPerson, map.getKeepPosition(indexOfCurrentGovernment), currentGovernment);
            map.getMapPixel(map.getKeepPosition(indexOfCurrentGovernment)[0],
                    map.getKeepPosition(indexOfCurrentGovernment)[1]).addPerson(unit);
            currentGovernment.addPeople(unit);
        }
        return Messages.UNIT_CREATED_SUCCESSFULLY;
    }

    public MilitaryCampType getCurrentMilitaryCamp() {
        return currentMilitaryCamp;
    }

    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    public Messages repair() {
        if (!(selectedBuilding instanceof Tower || selectedBuilding instanceof GateHouse))
            return Messages.CANT_REPAIR_THIS;
        int buildResource = selectedBuilding.getTypeOfBuilding().getResourceAmount();
        int damagedResource = (int) (selectedBuilding.getTypeOfBuilding().getResourceAmount() * selectedBuilding.getHp()
                / selectedBuilding.getTypeOfBuilding().getHp());
        if (buildResource - damagedResource > currentGovernment.getResources()
                .get(selectedBuilding.getTypeOfBuilding().getResourceNeeded()))
            return Messages.NOT_ENOUGH_RESOURCES;
        if (selectedBuilding.getHp() == selectedBuilding.getTypeOfBuilding().getHp())
            return Messages.ALREADY_AT_FULL_HP;
        for (int i = selectedBuilding.column; i < selectedBuilding.getTypeOfBuilding().getLength(); i++)
            for (int j = selectedBuilding.row; j < selectedBuilding.getTypeOfBuilding().getWidth(); j++)
                if (isAnEnemyCloseBy(j, i))
                    return Messages.THERES_AN_ENEMY_CLOSE_BY;
        selectedBuilding.repair();
        Resources resourceNeeded = selectedBuilding.getTypeOfBuilding().getResourceNeeded();
        currentGovernment.getResources().put(resourceNeeded,
                currentGovernment.getResources().get(resourceNeeded) - (buildResource - damagedResource));
        return Messages.REPAIRED_SUCCESSFULLY;
    }

    public Messages changeArms() {
        ConvertingResources workshop = (ConvertingResources) selectedBuilding;
        switch (selectedBuilding.getTypeOfBuilding()) {
            case BLACKSMITH:
                if (workshop.getResource().equals(Resources.SWORD))
                    workshop.setResource(Resources.MACE);
                else
                    workshop.setResource(Resources.SWORD);
                break;
            case POLETURNER:
                if (workshop.getResource().equals(Resources.BOW))
                    workshop.setResource(Resources.CROSSBOW);
                else
                    workshop.setResource(Resources.BOW);
                break;
            case FLETCHER:
                if (workshop.getResource().equals(Resources.PIKE))
                    workshop.setResource(Resources.SPEAR);
                else
                    workshop.setResource(Resources.PIKE);
                break;
            default:
                break;
        }
        return Messages.ARMS_CHANGED_SUCCESSFULLY;
    }

    public Messages changeWorkingState() {
        if (currentGovernment.getNoLaborBuildings().contains(selectedBuilding.getTypeOfBuilding())) {
            currentGovernment.removeNoLaborBuildings(selectedBuilding.getTypeOfBuilding());
        } else {
            currentGovernment.addNoLaborBuildings(selectedBuilding.getTypeOfBuilding());
            for (Building building : currentGovernment.getBuildings()) {
                if (building.getTypeOfBuilding().equals(selectedBuilding.typeOfBuilding))
                    currentGovernment.addBuildingsWaitingForWorkers(building);
            }
        }
        return Messages.BUILDING_WORKING_STATE_CHANGED;
    }

    public void nextTurn() throws IOException {
        indexOfCurrentGovernment = (indexOfCurrentGovernment + 1) % governments.size();
        currentGovernment = governments.get(indexOfCurrentGovernment);
        if (indexOfCurrentGovernment == 0)
            endOfTurn();
    }

    public Messages selectUnit(int frow, int fcolumn, int srow, int scolumn) {
        if (frow < 0 || frow >= map.getSize() || fcolumn < 0 || fcolumn >= map.getSize())
            return Messages.INVALID_COORDINATES;
        if (srow < 0 || srow >= map.getSize() || scolumn < 0 || scolumn >= map.getSize())
            return Messages.INVALID_COORDINATES;
        if (frow > srow || fcolumn > scolumn)
            return Messages.INVALID_COORDINATES;
        ArrayList<Unit> units = new ArrayList<>();
        for (int i = frow; i <= srow; i++)
            for (int j = fcolumn; j <= scolumn; j++)
                for (Person person : map.getMapPixel(i, j).getPeople())
                    if (person instanceof Unit)
                        units.add((Unit) person);
        this.selectedUnit = units;
        this.selectedUnitArea = new int[] { frow, fcolumn, srow, scolumn };
        return Messages.UNIT_SELECTED_SUCCESSFULLY;
    }

    public Messages moveUnit(int row, int column) {
        if (row < 0 || row >= map.getSize() || column < 0 || column >= map.getSize()) {
            return Messages.INVALID_COORDINATES;
        }
        if (!map.getMapPixel(row, column).canDropObject()
                || !map.getMapPixel(row, column).getTexture().canDropBuilding()) {
            return Messages.CANT_MOVE_UNITS_TO_THIS_LOCATION;
        }
        for (Unit person : selectedUnit)
            if (person.getGovernment().equals(currentGovernment)) {
                person.setMovePattern(
                        map.getPathList(person.currentLocation[0], person.currentLocation[1], row, column));
                person.setPatrolling(false);
                int frow = person.currentLocation[0],fcolumn=person.currentLocation[1];
                person.move();
                map.getMapPixel(frow, fcolumn).removePerson(person);
                map.getMapPixel(person.currentLocation[0], person.currentLocation[1]).addPerson(person);
            }
        selectedUnit.clear();
        return Messages.UNIT_MOVED_SUCCESSFULLY;
    }

    public void updateMovePatterns(Government government) {
        for (Person person : government.getPeople()) {
            if (person.getMovePattern().size() == 0)
                return;
            int[] destination = person.getMovePattern().get(person.getMovePattern().size() - 1);
            person.setMovePattern(map.getPathList(person.currentLocation[0], person.currentLocation[1], destination[0],
                    destination[1]));
        }
    }

    public void moveUnitsInQueue(Government government) {
        for (Person person : government.getPeople()) {
            int frow = person.currentLocation[0],fcolumn=person.currentLocation[1];
            person.move();
            map.getMapPixel(frow, fcolumn).removePerson(person);
            map.getMapPixel(person.currentLocation[0], person.currentLocation[1]).addPerson(person);
        }
    }

    public Messages patrolUnits(int frow, int fcolumn, int srow, int scolumn) {
        if (frow < 0 || frow >= map.getSize() || fcolumn < 0 || fcolumn >= map.getSize() ||
                srow < 0 || srow >= map.getSize() || scolumn < 0 || scolumn >= map.getSize()) {
            return Messages.INVALID_COORDINATES;
        }
        if (!map.getMapPixel(frow, fcolumn).canDropObject()
                || !map.getMapPixel(frow, fcolumn).getTexture().canDropBuilding()
                || !map.getMapPixel(srow, scolumn).canDropObject()
                || !map.getMapPixel(srow, scolumn).getTexture().canDropBuilding()) {
            return Messages.CANT_MOVE_UNITS_TO_THIS_LOCATION;
        }
        for (Unit person : selectedUnit)
            if (person.getGovernment().equals(currentGovernment)) {
                person.setPatrolLocation(new int[] { frow, fcolumn, srow, scolumn });
                person.setPatrolling(true);
                if (person.currentLocation[0] == frow && person.currentLocation[1] == fcolumn)
                    person.setMovePattern(map.getPathList(frow, fcolumn, srow, scolumn));
                else
                    person.setMovePattern(
                            map.getPathList(person.currentLocation[0], person.currentLocation[1], frow, fcolumn));
                int firstRow = person.currentLocation[0],firstColumn=person.currentLocation[1];
                person.move();
                map.getMapPixel(firstRow, firstColumn).removePerson(person);
                map.getMapPixel(person.currentLocation[0], person.currentLocation[1]).addPerson(person);
            }
        selectedUnit.clear();
        return Messages.UNIT_MOVED_SUCCESSFULLY;
    }

    public void setPatrolPattern(Government government) {
        for (Person person : government.getPeople()) {
            if (person.getMovePattern().size() != 0)
                continue;
            if (!person.isPatrolling())
                continue;
            if (person.currentLocation[0] == person.patrolLocation[2]
                    && person.currentLocation[1] == person.patrolLocation[3])
                person.setPatrolLocation(new int[] { person.patrolLocation[2], person.patrolLocation[3],
                        person.patrolLocation[0], person.patrolLocation[1] });
            int frow = person.patrolLocation[0], fcolumn = person.patrolLocation[1], srow = person.patrolLocation[2],
                    scolumn = person.patrolLocation[3];
            person.setMovePattern(map.getPathList(frow, fcolumn, srow, scolumn));
        }
    }

    public Messages stopUnit() {
        for (Unit person : selectedUnit)
            if (person.getGovernment().equals(currentGovernment)) {
                person.setMovePattern(new ArrayList<int[]>());
                person.setPatrolling(false);
            }
        selectedUnit.clear();
        return Messages.UNIT_STOPPED_SUCCESSFULLY;
    }

    public Messages setStance(int row, int column, UnitStance unitStance) {
        if (row < 0 || row >= map.getSize() || column < 0 || column >= map.getSize())
            return Messages.INVALID_COORDINATES;
        if (unitStance == null) {
            return Messages.INVALID_STANCE;
        }
        for (Person person : map.getMapPixel(row, column).getPeople()) {
            if (person.getGovernment().equals(currentGovernment)) {
                ((Unit) person).setUnitStance(unitStance);
            }
        }
        selectedUnit.clear();
        return Messages.STANCE_CHANGED_SUCCESSFULLY;
    }

    public Messages buildSiegeWeapon(SiegeWeaponType siegeWeapontType) {
        int[] location = new int[2];
        int counter = 0;
        for (Unit person : selectedUnit) {
            if (person.getTypeOfPerson().equals(TypeOfPerson.ENGINEER)) {
                location = person.getCurrentLocation();
                counter++;
            }
        }
        if (counter < siegeWeapontType.getEngineersNeeded())
            return Messages.NEEDS_MORE_ENGINEERS;
        ArrayList<Person> engineers = new ArrayList<>();
        for (Person person : map.getMapPixel(location[0], location[1]).getPeople()) {
            if (counter == 0)
                break;
            if (((Unit) person).getTypeOfPerson().equals(TypeOfPerson.ENGINEER)) {
                counter--;
                engineers.add(person);
            }
        }
        map.getMapPixel(location[0], location[1]).getPeople().removeAll(engineers);
        currentGovernment.getPeople().removeAll(engineers);
        SiegeWeapon siegeWeapon = new SiegeWeapon(siegeWeapontType, location);
        return Messages.SIEGE_WEAPON_BUILT_SUCCESSFULLY;
    }

    public Messages disbandUnit() {
        map.getMapPixel(selectedUnitArea[0], selectedUnitArea[1]).getPeople().removeAll(selectedUnit);
        currentGovernment.getPeople().removeAll(selectedUnit);
        return Messages.UNITS_DISBANDED_SUCCESSFULLY;
    }

    public void endOfTurn() throws IOException {
        for (Government government : governments) {
            government.setPopularity(government.getPopularity() + government.getTaxEffectOnPopularity()); // todo:
                                                                                                          // update
            government.setGold((int) (government.getGold() + government.getTaxAmount() * government.getPopulation()));
            government.giveFood();
            government.changePeasant(0); // todo: number of added peasants each turn
            HashSet<Building> startedWorkingBuildings = new HashSet<>();
            for (Building building : currentGovernment.getBuildingsWaitingForWorkers()) {
                if (currentGovernment.getBuildingsWaitingForWorkers().contains(building)
                        && building.getTypeOfBuilding().getWorkerInUse() >= government.getPeasant()) {
                    startedWorkingBuildings.add(building);
                    building.setWorkers(building.getTypeOfBuilding().getWorkerInUse());
                    building.workingState(true);
                    government.changePeasant(-building.getTypeOfBuilding().getWorkerInUse());
                }
                if (government.getPeasant() == 0)
                    break;
            }
            government.removeBuildingsWaitingForWorkers(startedWorkingBuildings);
            if ((int) (government.getPopularity() / 10) - 5 >= 0) {
                if (government.getPeasant() < 24) {
                    government.changePeasant(
                            Math.min(24 - government.getPeasant(), (int) (government.getPopularity() / 10) - 5));
                }
            } else
                government.changePeasant((int) (government.getPopularity() / 10) - 5);

            setPatrolPattern(government);
            updateMovePatterns(government);
            moveUnitsInQueue(government);
            government.resetMovesLeft();

        }
        Map.loadMaps(); // todo : why should this be here. we should load everything in the beginning
                        // and dont open any file again. answer: Too goshad to clone the maps

        Government government = getCurrentGovernment();
        int currentGovernmentIndex = governments.indexOf(government);
        int nextGovernmentIndex = (currentGovernmentIndex + 1) % governments.size();
        currentGovernment = governments.get(nextGovernmentIndex);
        // todo: set new current military camp?

    }

    public Government getGovernmentByColor(LordColor color) {
        for (Government government : governments) {
            if (government.color().equals(color))
                return government;
        }
        return null;
    }

    public ArrayList<Unit> getSelectedUnit() {
        return selectedUnit;
    }

    public void pourOil(int x, int y) {
        map.pourOil(x,y));
    }
}
