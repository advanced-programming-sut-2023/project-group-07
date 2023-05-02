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
    private ArrayList<Person> selectedUnit;
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

    public User getCurrentUser() {
        return currentGovernment.getUser();
    }

    public Government getCurrentGovernment() {
        return currentGovernment;
    }

    public ArrayList<Government> getGovernments() {
        return governments;
    }

    public Map getMap() {
        return this.map;
    }

    public int getIndexOfCurrentGovernment() {
        return indexOfCurrentGovernment;
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
    public void createTroop(UnitTypes unitType, int count) {
        currentGovernment.setGold(-count * unitType.getGoldNeeded());
        for (Resources resource : unitType.getResourcesNeeded())
            currentGovernment.changeResources(resource, -count);
        currentGovernment.changePeasant(-count);
        for (int i = 0; i < count; i++) {
            Unit unit = new Unit(unitType, map.getKeepPosition(indexOfCurrentGovernment), currentGovernment);
            map.getMapPixel(map.getKeepPosition(indexOfCurrentGovernment)[0],
                    map.getKeepPosition(indexOfCurrentGovernment)[1]).addPerson(unit);
            currentGovernment.addPerson(unit);
        }
    }

    public void setCurrentMilitaryCamp(MilitaryCampType currentMilitaryCamp) {
        this.currentMilitaryCamp = currentMilitaryCamp;
    }
    
    public MilitaryCampType getCurrentMilitaryCamp() {
        return currentMilitaryCamp;
    }

    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    public void setSelectedBuilding(Building selectedBuilding) {
        this.selectedBuilding = selectedBuilding;
    }


    public void changeArms() {
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
    }

    public void changeWorkingState() {
        if (currentGovernment.getNoLaborBuildings().contains(selectedBuilding.getTypeOfBuilding())) {
            currentGovernment.removeNoLaborBuildings(selectedBuilding.getTypeOfBuilding());
        } else {
            currentGovernment.addNoLaborBuildings(selectedBuilding.getTypeOfBuilding());
            for (Building building : currentGovernment.getBuildings()) {
                if (building.getTypeOfBuilding().equals(selectedBuilding.typeOfBuilding))
                    currentGovernment.addBuildingsWaitingForWorkers(building);
            }
        }
    }

    public void nextTurn() throws IOException {
        indexOfCurrentGovernment = (indexOfCurrentGovernment + 1) % governments.size();
        currentGovernment = governments.get(indexOfCurrentGovernment);
        if (indexOfCurrentGovernment == 0)
            endOfTurn();
    }

    public void selectUnit(int frow, int fcolumn, int srow, int scolumn) {
        ArrayList<Person> units = new ArrayList<>();
        for (int i = frow; i <= srow; i++)
            for (int j = fcolumn; j <= scolumn; j++)
                for (Person person : map.getMapPixel(i, j).getPeople())
                    if (person instanceof Unit)
                        units.add((Unit) person);
        this.selectedUnit = units;
        this.selectedUnitArea = new int[]{frow, fcolumn, srow, scolumn};
    }

    public void moveUnit(int row, int column) {
        for (Person person : selectedUnit)
            if(person instanceof Unit) {
                Unit unit = (Unit) person;
                if(unit.getGovernment().equals(currentGovernment)) {
                    unit.setMovePattern(map.getPathList(person.currentLocation[0], person.currentLocation[1], row, column));
                    unit.setPatrolling(false);
                    applyPersonMove(person);
                    unit.setAttacking(false);
                    unit.setPatrolling(false);
                }
            }
        selectedUnit.clear();
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
            applyPersonMove(person);
        }
    }

    public void patrolUnits(int frow, int fcolumn, int srow, int scolumn) {
        for (Person person : selectedUnit)
            if (person.getGovernment().equals(currentGovernment)) {
                person.setPatrolLocation(new int[]{frow, fcolumn, srow, scolumn});
                person.setPatrolling(true);
                if (person.currentLocation[0] == frow && person.currentLocation[1] == fcolumn)
                    person.setMovePattern(map.getPathList(frow, fcolumn, srow, scolumn));
                else
                    person.setMovePattern(
                            map.getPathList(person.currentLocation[0], person.currentLocation[1], frow, fcolumn));
                applyPersonMove(person);
                person.setPatrolling(false);
            }
        selectedUnit.clear();
    }

    public void applyPersonMove(Person person) {
        int firstRow = person.currentLocation[0], firstColumn = person.currentLocation[1];
        person.move();
        map.getMapPixel(firstRow, firstColumn).removePerson(person);
        map.getMapPixel(person.currentLocation[0], person.currentLocation[1]).addPerson(person);
    }

    public void setPatrolPattern(Government government) {
        for (Person person : government.getPeople()) {
            if (person.getMovePattern().size() != 0)
                continue;
            if (!person.isPatrolling())
                continue;
            if (person.currentLocation[0] == person.patrolLocation[2]
                    && person.currentLocation[1] == person.patrolLocation[3])
                person.setPatrolLocation(new int[]{person.patrolLocation[2], person.patrolLocation[3],
                        person.patrolLocation[0], person.patrolLocation[1]});
            int frow = person.patrolLocation[0], fcolumn = person.patrolLocation[1], srow = person.patrolLocation[2],
                    scolumn = person.patrolLocation[3];
            person.setMovePattern(map.getPathList(frow, fcolumn, srow, scolumn));
        }
    }

    public void setAttackPattern(Government government) {
        for (Person person : government.getPeople()) {
            if (person instanceof Unit && ((Unit) person).isAttacking()) {
                Unit unit = (Unit) person;
                if (unit.getPersonBeingAttacked().hp == -1) {
                    unit.setAttacking(false);
                    continue;
                }
                unit.setMovePattern(map.getPathList(unit.currentLocation[0], unit.currentLocation[1],
                        unit.getPersonBeingAttacked().currentLocation[0],
                        unit.getPersonBeingAttacked().currentLocation[0]));
            }
        }
    }

    public void stopUnit() {
        for (Person person : selectedUnit)
            if (person.getGovernment().equals(currentGovernment)) {
                person.setMovePattern(new ArrayList<int[]>());
                person.setPatrolling(false);
            }
        selectedUnit.clear();
    }

    public void setStance(int row, int column, UnitStance unitStance) {
        for (Person person : map.getMapPixel(row, column).getPeople()) {
            if (person.getGovernment().equals(currentGovernment)) {
                ((Unit) person).setUnitStance(unitStance);
            }
        }
        selectedUnit.clear();
    }

    public void attackEnemy(int row, int column) {
        Person person = map.getMapPixel(row, column).getPeople().get(0);
        for (Person person2 : selectedUnit) {
            Unit unit = (Unit) person2;
            unit.setAttacking(true);
            unit.setMovePattern(map.getPathList(unit.currentLocation[0], unit.currentLocation[1], row, column));
            unit.setPersonBeingAttacked(person);
            applyPersonMove(unit);
            unit.setPatrolling(false);
        }
        selectedUnit.clear();
    }

    public void areaAttack(int row, int column) {
        for (Person person2 : selectedUnit) {
            Unit unit = (Unit) person2;
            unit.setPatrolling(false);
            unit.setAttacking(false);
            unit.setAreaAttacking(true);
            unit.setAreaAttackLocation(new int[]{row, column});
        }
        selectedUnit.clear();
    }

    public void disbandUnit() {
        map.getMapPixel(selectedUnitArea[0], selectedUnitArea[1]).getPeople().removeAll(selectedUnit);
        currentGovernment.getPeople().removeAll(selectedUnit);
        int size = selectedUnit.size();
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

    public ArrayList<Person> getSelectedUnit() {
        return selectedUnit;
    }

    public void pourOil(int x, int y) {
        map.pourOil(x, y);
    }

    public ArrayList<int[]> getNearestOilSmelterPath(int[] currentLocation, Government owner) {
        int x = currentLocation[0], y = currentLocation[1];
        ArrayList<int[]> path = null;
        ArrayList<Building> buildings = map.getAllBuildingsOfSomeone(owner);
        for (Building building : buildings){
            if (building.getTypeOfBuilding().equals(TypeOfBuilding.OIL_SMELTER)){
                ArrayList<int[]> pathTemp = map.getPathList(x,y,building.row(),building.column());
                if (path == null) {
                    path = pathTemp;
                    continue;
                }
                if (pathTemp.size() < path.size()) path = pathTemp;
            }
        }
        return path;
    }

    public ArrayList<Person> getPersonOfAPixel(int x, int y) {
        return map.getMapPixel(x, y).getPeople();
    }
    public void endGame() throws IOException{
        Map.loadMaps();
    } 

}
