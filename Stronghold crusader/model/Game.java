package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.contains;

import java.io.IOException;

import controller.Controller;
import controller.Messages;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;

public class Game {
    private final Map map;
    private ArrayList<Government> governments;
    private ArrayList<Government> nonPlayingGovernments = new ArrayList<Government>();
    private Government currentGovernment;
    private MilitaryCampType currentMilitaryCamp;
    private Building selectedBuilding;
    private ArrayList<Person> selectedUnit = new ArrayList<>();
    private int[] selectedUnitArea = new int[4];
    private int indexOfCurrentGovernment = 0;

    public Game(Map map, ArrayList<Government> governments, int goldToBeginWith) {
        this.map = map;
        this.governments = governments;
        for (Government government : this.governments)
            government.setGold(goldToBeginWith);
        HashMap<LordColor, Government> governmentsMap = new HashMap<LordColor, Government>();
        for (Government government : this.governments)
            governmentsMap.put(government.getColor(), government);
        map.startGame(governmentsMap);
        currentGovernment = governments.get(0);
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
        currentGovernment.changeGold(-count * unitType.getGoldNeeded());
        for (Resources resource : unitType.getResourcesNeeded())
            currentGovernment.changeResources(resource, -count);
        currentGovernment.changePeasant(-count);
        for (int i = 0; i < count; i++) {
            int[] location = new int[] { map.getKeepPosition(currentGovernment.getColor())[0] + 7,
                    map.getKeepPosition(currentGovernment.getColor())[1] + 3 };
            Unit unit;
            if (unitType.equals(UnitTypes.ENGINEER)) {
                Engineer engineer = new Engineer(unitType, new int[] { location[0], location[1] }, currentGovernment);
                unit = engineer;
            } else if (unitType.equals(UnitTypes.TUNNELER)) {
                Tunneler tunneler = new Tunneler(unitType, new int[] { location[0], location[1] }, currentGovernment);
                unit = tunneler;
            } else
                unit = new Unit(unitType, location, currentGovernment);
            map.getMapPixel(location[0], location[1]).addPerson(unit);
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

    public String nextTurn() throws IOException {
        indexOfCurrentGovernment = (indexOfCurrentGovernment + 1) % governments.size();
        currentGovernment = governments.get(indexOfCurrentGovernment);
        if (indexOfCurrentGovernment == 0)
            return endOfTurn();
        return "";
    }

    public boolean gameOver() {
        if (governments.size() > 1)
            return false;
        for (Government government : governments)
            nonPlayingGovernments.add(government);
        for (Government government : nonPlayingGovernments) {
            int score = calculateScore(government);
            government.setScore(score);
            government.getUser().changeHighScore(score);
        }
        sortGovernments();
        return true;
    }

    private void sortGovernments() {
        for (int i = 0; i < nonPlayingGovernments.size(); i++) {
            for (int j = i + 1; j < nonPlayingGovernments.size(); j++) {
                Government gov1 = nonPlayingGovernments.get(i);
                Government gov2 = nonPlayingGovernments.get(j);
                if (gov1.getScore() < gov2.getScore()) {
                    nonPlayingGovernments.set(i, gov2);
                    nonPlayingGovernments.set(j, gov1);
                }
            }
        }
    }

    public void selectUnit(int frow, int fcolumn, int srow, int scolumn) {
        ArrayList<Person> units = new ArrayList<>();
        for (int i = frow; i <= srow; i++)
            for (int j = fcolumn; j <= scolumn; j++)
                for (Person person : map.getMapPixel(i, j).getPeople())
                    if (person instanceof Unit)
                        units.add((Unit) person);
        this.selectedUnit = units;
        this.selectedUnitArea = new int[] { frow, fcolumn, srow, scolumn };
    }

    public void moveUnit(int row, int column) {
        for (Person person : selectedUnit)
            if (person instanceof Unit) {
                Unit unit = (Unit) person;
                if (unit.getGovernment().equals(currentGovernment)) {
                    unit.setMovePattern(
                            map.getPathList(person.currentLocation[0], person.currentLocation[1], row, column));
                    unit.setPatrolling(false);
                    applyPersonMove(person);
                    unit.setAttacking(false);
                    unit.setPatrolling(false);
                    unit.setAreaAttacking(false);
                    unit.setAttackingBuilding(false);
                }
            }
        selectedUnit.clear();
    }

    public void updateMovePatterns(Government government) {
        for (Person person : government.getPeople()) {
            if (person.getMovePattern().size() == 0)
                return;
            if (person instanceof Unit && ((Unit) person).isAttacking()) {
                Unit unit = (Unit) person;
                int[] destination = unit.getPersonBeingAttacked().getCurrentLocation();
                unit.setMovePattern(map.getPathList(person.currentLocation[0], person.currentLocation[1],
                        destination[0], destination[1]));
            } else {
                int[] destination = person.getMovePattern().get(person.getMovePattern().size() - 1);
                person.setMovePattern(map.getPathList(person.currentLocation[0], person.currentLocation[1],
                        destination[0], destination[1]));
            }
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
                person.setPatrolLocation(new int[] { frow, fcolumn, srow, scolumn });
                person.setPatrolling(true);
                if (person.currentLocation[0] == frow && person.currentLocation[1] == fcolumn)
                    person.setMovePattern(map.getPathList(frow, fcolumn, srow, scolumn));
                else
                    person.setMovePattern(
                            map.getPathList(person.currentLocation[0], person.currentLocation[1], frow, fcolumn));
                applyPersonMove(person);
                person.setPatrolling(true);
                if(person instanceof Unit)
                    ((Unit) person).setAttacking(false);
                    ((Unit) person).setAreaAttacking(false);
                    ((Unit) person).setAttackingBuilding(false);(false);
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
                person.setPatrolLocation(new int[] { person.patrolLocation[2], person.patrolLocation[3],
                        person.patrolLocation[0], person.patrolLocation[1] });
            int frow = person.patrolLocation[0], fcolumn = person.patrolLocation[1], srow = person.patrolLocation[2],
                    scolumn = person.patrolLocation[3];
            person.setMovePattern(map.getPathList(frow, fcolumn, srow, scolumn));
        }
    }

    public void setAttackPattern(Government government) {
        for (Person person : government.getPeople()) {
            if (person instanceof Unit && ((Unit) person).isAttacking()) {
                Unit unit = (Unit) person;
                if (unit.getPersonBeingAttacked().hp <= 0) {
                    unit.setAttacking(false);
                    continue;
                }
                unit.setMovePattern(map.getPathList(unit.currentLocation[0], unit.currentLocation[1],
                        unit.getPersonBeingAttacked().currentLocation[0],
                        unit.getPersonBeingAttacked().currentLocation[1]));
            } else if (person instanceof Unit && ((Unit) person).isAttackingBuilding()) {
                Unit unit = (Unit) person;
                if (unit.getBuildingBeingAttacked().getHp() <= 0) {
                    unit.setAttackingBuilding(false);
                    continue;
                }
                unit.setMovePattern(map.getPathList(unit.currentLocation[0], unit.currentLocation[1],
                        unit.getBuildingBeingAttacked().getRow(),
                        unit.getBuildingBeingAttacked().getColumn()));
            }
        }
    }

    public void stopUnit() {
        for (Person person : selectedUnit)
            if (person.getGovernment().equals(currentGovernment)) {
                person.setMovePattern(new ArrayList<int[]>());
                person.setPatrolling(false);
                if(person instanceof Unit){
                    ((Unit)person).setAttackingBuilding(false);
                    ((Unit)person).setAttacking(false);
                    ((Unit)person).setAreaAttacking(false);
                }

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

    public void attackEnemy(int row, int column, Person person) {
        for (Person person2 : selectedUnit) {
            Unit unit = (Unit) person2;
            unit.setAttacking(true);
            unit.setMovePattern(map.getPathList(unit.currentLocation[0], unit.currentLocation[1], row, column));
            unit.setPersonBeingAttacked(person);
            unit.setBuildingBeingAttacked(null);
            applyPersonMove(unit);
            unit.setPatrolling(false);
            unit.setAttackingBuilding(false);
            unit.setAreaAttacking(false);
        }
        selectedUnit.clear();
    }

    public void attackBuildings(Building building) {
        for (Person person2 : selectedUnit) {
            Unit unit = (Unit) person2;
            if(!canAttackBuilding(unit))
                continue;
            unit.setAttackingBuilding(true);
            ArrayList <int[]> path = new ArrayList<int[]>();
            int row = building.getRow();
            int column = building.getColumn();
            for(int i = row ; i < row + building.getTypeOfBuilding().getWidth() ; i++)
                if(!(path = map.getPathList(unit.currentLocation[0], unit.currentLocation[1], i, column-1)).isEmpty())
                    break;
            for(int i = row ; (i < row + building.getTypeOfBuilding().getWidth()) && path.isEmpty() ; i++)
                if(!(path = map.getPathList(unit.currentLocation[0], unit.currentLocation[1], i, column + building.getTypeOfBuilding().getLength())).isEmpty())
                    break;
            for(int j = column ; (j < column + building.getTypeOfBuilding().getLength()) && path.isEmpty() ; j++)
                 if(!(path = map.getPathList(unit.currentLocation[0], unit.currentLocation[1], row-1, j)).isEmpty())
                    break;
            for(int j = column ; (j < column + building.getTypeOfBuilding().getLength()) && path.isEmpty() ; j++)
                 if(!(path = map.getPathList(unit.currentLocation[0], unit.currentLocation[1], row+building.getTypeOfBuilding().getWidth(), j)).isEmpty())
                    break;
            unit.setMovePattern(path);
            unit.setBuildingBeingAttacked(building);
            unit.setPersonBeingAttacked(null);
            applyPersonMove(unit);
            unit.setPatrolling(false);
            unit.setAttacking(false);
            unit.setAreaAttacking(false);
        }
        selectedUnit.clear();
    }

    public void areaAttack(int row, int column) {
        for (Person person2 : selectedUnit) {
            Unit unit = (Unit) person2;
            unit.setPatrolling(false);
            unit.setAttacking(false);
            unit.setAttackingBuilding(false);
            unit.setAreaAttacking(true);
            unit.setAreaAttackLocation(new int[] { row, column });
        }
        selectedUnit.clear();
    }

    public void disbandUnit() {
        map.getMapPixel(selectedUnitArea[0], selectedUnitArea[1]).getPeople().removeAll(selectedUnit);
        currentGovernment.getPeople().removeAll(selectedUnit);
        int size = selectedUnit.size();
    }

    private int[] nearestEnemy(Unit unit) {
        for (int i = 0; i <= unit.type.getRange(); i++) {
            int row = unit.getCurrentLocation()[0], column = unit.getCurrentLocation()[1];
            for (int j = -i; j <= i; j++) {
                if (row + j < 0 || row + j >= map.getSize() || column + i - Math.abs(j) < 0
                        || column + i - Math.abs(j) >= map.getSize())
                    continue;
                for (Person person : map.getMapPixel(row + j, column + i - Math.abs(j)).getPeople()) {
                    if (!person.getGovernment().equals(unit.getGovernment())) {
                        if (person instanceof Unit && ((Unit) person).isInvisible()
                                && Math.abs(j) + Math.abs(i - Math.abs(j)) > 1)
                            continue;
                        return new int[] { row + j, column + i - Math.abs(j) };
                    }
                }
            }
            for (int j = i; j >= -i; j--) {
                if (row + j < 0 || row + j >= map.getSize() || column - i + Math.abs(j) < 0
                        || column - i + Math.abs(j) >= map.getSize())
                    continue;
                for (Person person : map.getMapPixel(row + j, column - i + Math.abs(j)).getPeople()) {
                    if (!person.getGovernment().equals(unit.getGovernment())) {
                        if (person instanceof Unit && ((Unit) person).isInvisible()
                                && Math.abs(j) + Math.abs(i - Math.abs(j)) > 1)
                            continue;
                        return new int[] { row + j, column - i + Math.abs(j) };
                    }
                }
            }
        }
        return null;
    }

    public void checkStance(Unit unit) {
        if (!unit.isAttacking() && !unit.isPatrolling() && unit.getMovePattern().isEmpty()) {
            if (unit.getUnitStance().equals(UnitStance.DEFENSIVE)) {
                // if()
            }
            if (unit.getUnitStance().equals(UnitStance.OFFENSIVE)) {

            }
        }
    }

    public void attackAllOthers(Government government) {
        for (Person person : government.getPeople()) {
            if (person instanceof Unit) {
                Unit unit = (Unit) person;
                int enemyLocation[] = nearestEnemy(unit);
                if (enemyLocation != null) {
                    for (Person person2 : map.getMapPixel(enemyLocation[0], enemyLocation[1]).getPeople()) {
                        if (!person2.getGovernment().equals(unit.getGovernment()))
                            person2.changeHP(-unit.getDamage() * unit.getBonusDamageRate());
                    }
                    if (getLordInPixel(enemyLocation) != null && getLordInPixel(enemyLocation).getHp() <= 0) {
                        if (getLordInPixel(enemyLocation).getGovernment().getDefeatedBy() == null)
                            getLordInPixel(enemyLocation).getGovernment().setDefeatedBy(government);
                    }
                } else {
                    Building building = unit.getBuildingBeingAttacked();
                    int row = unit.getCurrentLocation()[0], column = unit.getCurrentLocation()[1];
                    if (building == null || (Math.abs(building.getRow() - row)
                            + Math.abs(building.getColumn() - column)) > unit.type.getRange())
                        continue;
                    building.changeHp(-unit.getDamage() * unit.getBonusDamageRate());
                }
            }
        }
    }

    public boolean canAttackBuilding(Unit unit) {
        return !(unit.getType().equals(UnitTypes.ARABIAN_BOW) || unit.getType().equals(UnitTypes.ARCHER)
                || unit.getType().equals(UnitTypes.HORSE_ARCHER) || unit.getType().equals(UnitTypes.SLINGER)
                || unit.getType().equals(UnitTypes.CROSSBOWMAN));
    }


    private Unit getLordInPixel(int[] location) {
        for (Unit unit : map.getMapPixel(location[0], location[1]).getUnits())
            if (unit.getType().equals(UnitTypes.LORD))
                return unit;
        return null;
    }

    public void removeEliminatedPeople(Government government) {
        ArrayList<Person> eliminatedPeople = new ArrayList<>();
        for (Person person : government.getPeople()) {
            if (person.getHp() <= 0) {
                eliminatedPeople.add(person);
                map.getMapPixel(person.currentLocation[0], person.currentLocation[1]).removePerson(person);
            }
        }
        for(Person person : eliminatedPeople)
            government.removePerson(person);
    }

    public void removeEliminatedBuildings(Government government){
        ArrayList<Building> eliminatedBuildings = new ArrayList<>();
        for(Building building : government.getBuildings()){
            if(building.getHp() <= 0) {
                eliminatedBuildings.add(building);
                for(int i=0;i<building.getTypeOfBuilding().getWidth();i++)
                    for(int j=0;j<building.getTypeOfBuilding().getLength();j++){
                        MapPixel pixel =  map.getMapPixel(building.getRow()+i, building.getColumn()+j);
                        pixel.removeBuilding(building);
                        pixel.resetAccess();
                    }
            }
        }
        for(Building building : eliminatedBuildings)
            government.removeBuilding(building);

    }

    public void resourceDelivery(Government government) {
        for (Person person : government.getPeople()) {
            if (person instanceof NonMilitary
                    && person.currentLocation[0] == map.getKeepPosition(government.getColor())[0] + 7
                    && person.currentLocation[1] == map.getKeepPosition(government.getColor())[1] + 3) {
                        NonMilitary nonMilitary = (NonMilitary) person;
                        //government.changeResources(nonMilitary, );
            }
        }
    }

    public String endOfTurn() throws IOException {
        ArrayList<Government> randomGovernments = (ArrayList<Government>) governments.clone();
        Collections.shuffle(randomGovernments);
        for (Government government : randomGovernments) {
            government.setPopularity(government.getPopularity() + government.getTaxEffectOnPopularity()); // todo:
            // update
            government.changeGold((int) (government.getTaxAmount() * government.getPopulation()));
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
        for (Government government : governments) {
            attackAllOthers(government);
        }
        for (Government government : governments) {
            removeEliminatedPeople(government);
            removeEliminatedBuildings(government);
        }
        return eliminateDefeatedLords();

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
        for (Building building : buildings) {
            if (building.getTypeOfBuilding().equals(TypeOfBuilding.OIL_SMELTER)) {
                ArrayList<int[]> pathTemp = map.getPathList(x, y, building.row(), building.column());
                if (path == null) {
                    path = pathTemp;
                    continue;
                }
                if (pathTemp.size() < path.size())
                    path = pathTemp;
            }
        }
        return path;
    }

    public String endGame() throws IOException {
        String result = "GAME OVER!\nHere are the results:\n";
        int counter = 1;
        for (Government government : nonPlayingGovernments) {
            result += counter + ". " + government.getColor().toString() + " lord (player "
                    + government.getUser().getUsername() + ")\n" + "Over all score: " + government.getScore()
                    + "\nGold: " + government.getGold() + "\n";
            counter++;
        }

        return result;

    }

    public ArrayList<Person> getPersonOfAPixel(int x, int y) {
        return map.getMapPixel(x, y).getPeople();
    }

    private ArrayList<Government> getDefeatedGovernments() {
        ArrayList<Government> result = new ArrayList<Government>();
        for (Government government : governments)
            if (government.getLordHp() <= 0)
                result.add(government);
        return result;
    }

    private int calculateScore(Government government) {
        int result = 0;
        HashMap<Resources, Integer> resources = government.getResources();
        ArrayList<Person> people = government.getPeople();
        ArrayList<Building> buildings = government.getBuildings();
        result += government.getGold();
        result += government.getDefeatedLords() * 200;
        for (Resources resource : resources.keySet())
            result += resources.get(resource) * resource.getBuyingPrice();
        for (Person person : people)
            if (person instanceof Unit)
                result += ((Unit) person).getType().getGoldNeeded();
        for (Building building : buildings) {
            TypeOfBuilding typeOfBuilding = building.getTypeOfBuilding();
            result += typeOfBuilding.getCost()
                    + typeOfBuilding.getResourceAmount() * (typeOfBuilding.getResourceNeeded().getBuyingPrice());
        }
        return result;

    }

    private String eliminateDefeatedLords() {
        String result = "";
        for (Government government : getDefeatedGovernments()) {
            result += government.getColor() + " Lord has been defeated by " + government.getDefeatedBy().getColor()
                    + " lord!\n";
            this.governments.remove(government);
            this.nonPlayingGovernments.add(government);
            ArrayList<Building> buildings = government.getBuildings();
            ArrayList<Person> people = government.getPeople();
            for (Building building : buildings) {
                int row = building.getRow();
                int column = building.getColumn();
                for (int i = row; i < row + building.getTypeOfBuilding().getLength(); i++)
                    for (int j = column; j < column + building.getTypeOfBuilding().getWidth(); j++) {
                        MapPixel pixel = map.getMapPixel(i, j);
                        pixel.removeBuilding(building);
                        pixel.resetAccess();
                    }
            }
            for (Person person : people)
                map.getMapPixel(person.getCurrentLocation()[0], person.getCurrentLocation()[1]).removePerson(person);
            int[] keepPosition = map.getKeepPosition(government.getColor());
            for (int i = keepPosition[0]; i < keepPosition[0] + 7; i++)
                for (int j = keepPosition[1]; j < keepPosition[1] + 7; j++) {
                    MapPixel pixel = map.getMapPixel(i, j);
                    pixel.setPlayerKeep(null);
                    pixel.resetAccess();
                }

        }
        return result;

    }

}
