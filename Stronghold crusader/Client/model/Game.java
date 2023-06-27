package Client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

//import static org.mockito.ArgumentMatchers.contains;

import java.io.IOException;
//import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;

public class Game {
    private final Map map;
    private ArrayList<Government> governments;
    private ArrayList<Government> nonPlayingGovernments = new ArrayList<Government>();
    private Government currentGovernment;
    private MilitaryCampType currentMilitaryCamp;
    private Building selectedBuilding;
    private ArrayList<Person> selectedUnit = new ArrayList<>();
    private ArrayList<MapPixel> accessChanged = new ArrayList<>();
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

    public ArrayList<Unit> createTroop(UnitTypes unitType, int count) {
        currentGovernment.changeGold(-count * unitType.getGoldNeeded());
        ArrayList<Unit> units = new ArrayList<>();
        for (Resources resource : unitType.getResourcesNeeded())
            currentGovernment.changeResources(resource, -count);
        for (int i = 0; i < count; i++) {
            int[] location = new int[] { map.getKeepPosition(currentGovernment.getColor())[0] + 7,
                    map.getKeepPosition(currentGovernment.getColor())[1] + 3 };
            Unit unit;
            if (unitType.equals(UnitTypes.TUNNELER)) {
                Tunneler tunneler = new Tunneler(unitType, new int[] { location[0], location[1] }, currentGovernment);
                unit = tunneler;
            } else if (unitType.equals(UnitTypes.ENGINEER)) {
                Engineer engineer = new Engineer(unitType, new int[] { location[0], location[1] }, currentGovernment);
                unit = engineer;
            } else
                unit = new Unit(unitType, location, currentGovernment);
            map.getMapPixel(location[0], location[1]).addPerson(unit);
            currentGovernment.addPerson(unit);
            units.add(unit);
        }
        changePeasant(-count, currentGovernment);
        return units;
    }



    private void changePeasant(int count, Government government) {
        government.changePeasant(count);
        if (count < 0)
            for (int i = government.getPeople().size() - 1; i >= 0 && count < 0; i--) {
                if (government.getPeople().get(i) instanceof NonMilitary) {
                    NonMilitary nonMilitary = (NonMilitary)government.getPeople().get(i);
                    if(nonMilitary.getType().equals(NonMilitaryTypes.PEASANT)){
                        government.removePerson(nonMilitary);
                        map.getMapPixel(nonMilitary.getCurrentLocation()[0], nonMilitary.getCurrentLocation()[1])
                                .removePerson(nonMilitary);
                        count++;
                    }
                }
            }
        else if (count > 0)
            for (int i = 0; i < count; i++) {
                int[] location = map.getKeepPosition(government.getColor());
                NonMilitary nonMilitary = new NonMilitary(new int[] { location[0] + 7, location[1] + 3 },
                        government, NonMilitaryTypes.PEASANT, null);
                government.addPerson(nonMilitary);
                map.getMapPixel(nonMilitary.getCurrentLocation()[0], nonMilitary.getCurrentLocation()[1])
                        .addPerson(nonMilitary);
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
                if (workshop.getResource().equals(Resources.SPEAR))
                    workshop.setResource(Resources.PIKE);
                else
                    workshop.setResource(Resources.BOW);
                break;
            case FLETCHER:
                if (workshop.getResource().equals(Resources.BOW))
                    workshop.setResource(Resources.CROSSBOW);
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
        resetAccess();
        indexOfCurrentGovernment = (indexOfCurrentGovernment + 1) % governments.size();
        String result = (indexOfCurrentGovernment == 0) ? endOfTurn() : "";
        currentGovernment = governments.get(indexOfCurrentGovernment);
        setAccess();
        return result;
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
                    if (person instanceof Unit) {
                        Unit unit = (Unit) person;
                        if (unit.getType().equals(UnitTypes.WAR_DOG))
                            continue;
                        units.add(unit);
                    }

        this.selectedUnit = units;
        this.selectedUnitArea = new int[] { frow, fcolumn, srow, scolumn };
    }

    public void moveUnit(int row, int column) {
        for (Person person : selectedUnit)
            if (person instanceof Unit) {
                if (person instanceof SiegeWeapon){
                    SiegeWeapon siegeWeapon = (SiegeWeapon)person;
                    if(siegeWeapon.getType().getSpeed() == 0)
                        continue;
                }
                Unit unit = (Unit) person;
                if (unit.getGovernment().equals(currentGovernment)) {
                    unit.setMovePattern(
                            map.getPathList(person.currentLocation[0], person.currentLocation[1], row, column,
                                    unit.getType().equals(UnitTypes.ASSASSIN)));
                    unit.setPatrolling(false);
                    unit.setAttacking(false);
                    unit.setPatrolling(false);
                    unit.setAreaAttacking(false);
                    unit.setAttackingBuilding(false);
                }
            }
    }

    public void updateMovePatterns(Government government) { // vaghti ke pattern dakhelesh divar bashe chi mishe?
        for (Person person : government.getPeople()) {
            if (person.getMovePattern().size() == 0)
                continue;
            if (person instanceof Tunneler)
                continue;
            if (person instanceof Unit && ((Unit) person).isAttacking()) {
                Unit unit = (Unit) person;
                int[] destination = unit.getPersonBeingAttacked().getCurrentLocation();
                unit.setMovePattern(map.getPathList(person.currentLocation[0], person.currentLocation[1],
                        destination[0], destination[1], unit.getType().equals(UnitTypes.ASSASSIN)));
            } else {
                int[] destination = person.getMovePattern().get(person.getMovePattern().size() - 1);
                person.setMovePattern(map.getPathList(person.currentLocation[0], person.currentLocation[1],
                        destination[0], destination[1], false));
            }
        }
    }

    public void moveUnitsInQueue(Government government) {
        for (Person person : government.getPeople())
            applyPersonMove(person);
    }

    public void patrolUnits(int frow, int fcolumn, int srow, int scolumn) {
        for (Person person : selectedUnit)
            if (person instanceof Unit) {
                Unit unit = (Unit) person;
                if (person.getGovernment().equals(currentGovernment)) {
                    person.setPatrolLocation(new int[] { frow, fcolumn, srow, scolumn });
                    person.setPatrolling(true);
                    if (person.currentLocation[0] == frow && person.currentLocation[1] == fcolumn)
                        person.setMovePattern(map.getPathList(frow, fcolumn, srow, scolumn,
                                unit.getType().equals(UnitTypes.ASSASSIN)));
                    else
                        person.setMovePattern(
                                map.getPathList(person.currentLocation[0], person.currentLocation[1], frow, fcolumn,
                                        unit.getType().equals(UnitTypes.ASSASSIN)));
                    applyPersonMove(person);
                    person.setPatrolling(true);
                    if (person instanceof Unit)
                        ((Unit) person).setAttacking(false);
                    ((Unit) person).setAreaAttacking(false);
                    ((Unit) person).setAttackingBuilding(false);
                }
            }
        selectedUnit.clear();
    }

    public void applyPersonMove(Person person) {
        if (person instanceof Unit) {
            Unit unit = (Unit) person;
            if(unit.isAttacking()){
                int range=0;
                if(unit instanceof  SiegeWeapon){
                    SiegeWeapon siegeWeapon = (SiegeWeapon) unit;
                    range = siegeWeapon.getType().getRange();
                }
                else unit.getType().getRange();
                if (Math.abs(unit.getCurrentLocation()[0] - unit.getPersonBeingAttacked().getCurrentLocation()[0])
                        + Math.abs(unit.getCurrentLocation()[1]
                                - unit.getPersonBeingAttacked().getCurrentLocation()[1]) <= range)
                    return;
            }
        }
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
            if (person instanceof Unit) {
                Unit unit = (Unit) person;
                if (person.currentLocation[0] == person.patrolLocation[2]
                        && person.currentLocation[1] == person.patrolLocation[3])
                    person.setPatrolLocation(new int[] { person.patrolLocation[2], person.patrolLocation[3],
                            person.patrolLocation[0], person.patrolLocation[1] });
                int frow = person.patrolLocation[0], fcolumn = person.patrolLocation[1],
                        srow = person.patrolLocation[2],
                        scolumn = person.patrolLocation[3];
                person.setMovePattern(
                        map.getPathList(frow, fcolumn, srow, scolumn, unit.getType().equals(UnitTypes.ASSASSIN)));
            } else {
                NonMilitary nonMilitary = (NonMilitary) person;
                if (nonMilitary.isMovingResources()) {
                    ArrayList<int[]> path = pathToBuilding(nonMilitary, nonMilitary.getWorkBuilding());
                    if (path.size() == 1 && isNextToBuilding(nonMilitary, nonMilitary.getWorkBuilding())) {
                        nonMilitary.setMovePattern(map.getPathList(person.getCurrentLocation()[0],
                                person.getCurrentLocation()[1], map.getKeepPosition(government.getColor())[0] + 7,
                                map.getKeepPosition(government.getColor())[1] + 3, false));
                        nonMilitary
                                .setPatrolLocation(
                                        new int[] { nonMilitary.getCurrentLocation()[0],
                                                nonMilitary.getCurrentLocation()[1],
                                                map.getKeepPosition(government.getColor())[0] + 7,
                                                map.getKeepPosition(government.getColor())[1] + 3 });
                    }
                } else if (nonMilitary.isMovingNeededResources()) {
                    ArrayList<int[]> path = pathToBuilding(nonMilitary, nonMilitary.getWorkBuilding());
                    nonMilitary.setMovePattern(path);
                    nonMilitary.setPatrolLocation(new int[] { map.getKeepPosition(government.getColor())[0] + 7,
                            map.getKeepPosition(government.getColor())[1] + 3,
                            path.get(path.size() - 1)[0], path.get(path.size() - 1)[1] });
                }
            }
        }
    }

    private boolean isNextToBuilding(Person person, Building building) {
        int row = person.getCurrentLocation()[0];
        int column = person.getCurrentLocation()[1];
        if (row > 0 && map.getMapPixel(row - 1, column).getBuildings().contains(building))
            return true;
        if (row < map.getSize() - 1 && map.getMapPixel(row + 1, column).getBuildings().contains(building))
            return true;
        if (column > 0 && map.getMapPixel(row, column - 1).getBuildings().contains(building))
            return true;
        if (column < map.getSize() - 1 && map.getMapPixel(row, column + 1).getBuildings().contains(building))
            return true;
        return false;
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
                        unit.getPersonBeingAttacked().currentLocation[1], unit.getType().equals(UnitTypes.ASSASSIN)));
            } else if (person instanceof Unit && ((Unit) person).isAttackingBuilding()) {
                Unit unit = (Unit) person;
                if (unit.getBuildingBeingAttacked().getHp() <= 0) {
                    unit.setAttackingBuilding(false);
                    continue;
                }
                unit.setMovePattern(map.getPathList(unit.currentLocation[0], unit.currentLocation[1],
                        unit.getBuildingBeingAttacked().getRow(),
                        unit.getBuildingBeingAttacked().getColumn(), unit.getType().equals(UnitTypes.ASSASSIN)));
            }
        }
    }

    public void stopUnit() {
        for (Person person : selectedUnit)
            if (person.getGovernment().equals(currentGovernment)) {
                person.setMovePattern(new ArrayList<int[]>());
                person.setPatrolling(false);
                if (person instanceof Unit) {
                    ((Unit) person).setAttackingBuilding(false);
                    ((Unit) person).setAttacking(false);
                    ((Unit) person).setAreaAttacking(false);
                }

            }
        selectedUnit.clear();
    }

    public void setStance(int row, int column, UnitStance unitStance) {
        for (Person person : map.getMapPixel(row, column).getPeople()) {
            if (person.getGovernment().equals(currentGovernment) && person instanceof Unit)
                ((Unit) person).setUnitStance(unitStance);
        }
        selectedUnit.clear();
    }

    public void attackEnemy(int row, int column, Person person) {
        for (Person person2 : selectedUnit) {
            Unit unit = (Unit) person2;
            unit.setAttacking(true);
            unit.setMovePattern(map.getPathList(unit.currentLocation[0], unit.currentLocation[1], row, column,
                    unit.getType().equals(UnitTypes.ASSASSIN)));
            unit.setPersonBeingAttacked(person);
            unit.setBuildingBeingAttacked(null);
            int range=0;
            if(unit instanceof  SiegeWeapon){
                SiegeWeapon siegeWeapon = (SiegeWeapon) unit;
                range = siegeWeapon.getType().getRange();
            }
            else unit.getType().getRange();
            if (Math.abs(unit.getCurrentLocation()[0] - person.getCurrentLocation()[0])
                    + Math.abs(unit.getCurrentLocation()[1] - person.getCurrentLocation()[1]) > range)
                applyPersonMove(unit);
            unit.setPatrolling(false);
            unit.setAttackingBuilding(false);
            unit.setAreaAttacking(false);
        }
        selectedUnit.clear();
    }

    public void attackBuildings(Building building) {
        for (Person person : selectedUnit) {
            Unit unit = (Unit) person;
            if (!canAttackBuilding(unit))
                continue;
            ArrayList<int[]> path = pathToBuilding(person, building);
            unit.setMovePattern(path);
            if (!path.isEmpty()) {
                unit.setBuildingBeingAttacked(building);
                unit.setAttackingBuilding(true);
                unit.setPersonBeingAttacked(null);
                applyPersonMove(unit);
                unit.setPatrolling(false);
                unit.setAttacking(false);
                unit.setAreaAttacking(false);
            }
        }
        selectedUnit.clear();
    }

    public ArrayList<int[]> pathToBuilding(Person person, Building building) {
        ArrayList<int[]> path = new ArrayList<int[]>();
        int row = building.getRow();
        int column = building.getColumn();
        for (int i = row; i < row + building.getTypeOfBuilding().getWidth(); i++)
            if (!(path = map.getPathList(person.currentLocation[0], person.currentLocation[1], i, column - 1, false))
                    .isEmpty())
                break;
        for (int i = row; (i < row + building.getTypeOfBuilding().getWidth()) && path.isEmpty(); i++)
            if (!(path = map.getPathList(person.currentLocation[0], person.currentLocation[1], i,
                    column + building.getTypeOfBuilding().getLength(), false)).isEmpty())
                break;
        for (int j = column; (j < column + building.getTypeOfBuilding().getLength()) && path.isEmpty(); j++)
            if (!(path = map.getPathList(person.currentLocation[0], person.currentLocation[1], row - 1, j, false))
                    .isEmpty())
                break;
        for (int j = column; (j < column + building.getTypeOfBuilding().getLength()) && path.isEmpty(); j++)
            if (!(path = map.getPathList(person.currentLocation[0], person.currentLocation[1],
                    row + building.getTypeOfBuilding().getWidth(), j, false)).isEmpty())
                break;
        return path;
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

    private int[] nearestEnemy(Unit unit, int range) {
        for (int i = 0; i <= range; i++) {
            int row = unit.getCurrentLocation()[0], column = unit.getCurrentLocation()[1];
            for (int j = -i; j <= i; j++) {
                if (row + j < 0 || row + j >= map.getSize() || column + i - Math.abs(j) < 0
                        || column + i - Math.abs(j) >= map.getSize()
                        || (range == 1 && !areOnTheSameLevel(
                                map.getMapPixel(unit.getCurrentLocation()[0], unit.getCurrentLocation()[1]),
                                map.getMapPixel(row + j, column + i - Math.abs(j)))))
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
                        || column - i + Math.abs(j) >= map.getSize() || (range == 1 && !areOnTheSameLevel(
                                map.getMapPixel(unit.getCurrentLocation()[0], unit.getCurrentLocation()[1]),
                                map.getMapPixel(row + j, column + i - Math.abs(j)))))
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

    private boolean areOnTheSameLevel(MapPixel mapPixel1, MapPixel mapPixel2) {
        if (mapPixel1.getBuildings().isEmpty() ^ mapPixel2.getBuildings().isEmpty())
            return false;
        if (mapPixel1.getBuildings().isEmpty() && mapPixel2.getBuildings().isEmpty())
            return true;
        if (mapPixel1.getBuildings().get(0).getTypeOfBuilding().getHeight() == mapPixel2.getBuildings().get(0)
                .getTypeOfBuilding().getHeight())
            return true;
        return false;

    }

    public void checkStance(Government government) {
        for (Person person : government.getPeople())
            if (person instanceof Unit) {
                Unit unit = (Unit)person;
                if (!unit.isAttacking() && !unit.isPatrolling() && unit.getMovePattern().isEmpty()) {
                    if (unit.getUnitStance().equals(UnitStance.DEFENSIVE)) {
                        if (nearestEnemy(unit, unit.getType().getRange()) != null)
                            return;
                        int range = (unit.getType().getRange() > 1) ? (int) (unit.getType().getRange() * (1.5))
                                : unit.getType().getRange() + 5;
                        int[] enemy = nearestEnemy(unit, range);
                        unit.setMovePattern(map.getPathList(unit.getCurrentLocation()[0], unit.getCurrentLocation()[1],
                                enemy[0], enemy[1], unit.getType().equals(UnitTypes.ASSASSIN)));
                        applyPersonMove(unit);
                        unit.setMovePattern(new ArrayList<>());
                    }
                    if (unit.getUnitStance().equals(UnitStance.OFFENSIVE)) {
                        if (nearestEnemy(unit, unit.getType().getRange()) != null)
                            return;
                        int range = (unit.getType().getRange() > 1) ? unit.getType().getRange() * 2
                                : unit.getType().getRange() + 10;
                        int[] enemy = nearestEnemy(unit, range);
                        unit.setMovePattern(map.getPathList(unit.getCurrentLocation()[0], unit.getCurrentLocation()[1],
                                enemy[0], enemy[1], unit.getType().equals(UnitTypes.ASSASSIN)));
                        applyPersonMove(unit);
                        unit.setMovePattern(new ArrayList<>());
                    }
                }
            }
    }

    public void attackAllOthers(Government government) {
        for (Person person : government.getPeople()) {
            if (person instanceof Unit) {
                Unit unit = (Unit) person;
                int range=0;
                if(unit instanceof  SiegeWeapon){
                    SiegeWeapon siegeWeapon = (SiegeWeapon) unit;
                    range = siegeWeapon.getSiegeWeaponType().getRange();
                }
                else unit.getType().getRange();
                if (!(unit instanceof SiegeWeapon) && unit.getType().getRange() > 1
                        && !map.getMapPixel(unit.currentLocation[0], unit.currentLocation[1]).getBuildings().isEmpty())
                    range = unit.getType().getRange()
                            + map.getMapPixel(unit.currentLocation[0], unit.currentLocation[1]).getBuildings().get(0)
                                    .getTypeOfBuilding().getHeight();
                if (unit instanceof SiegeWeapon) {
                    SiegeWeapon siegeWeapon =(SiegeWeapon)unit;
                    if(siegeWeapon.isAttackingBuilding()){
                        Building building = unit.getBuildingBeingAttacked();
                        int row = unit.getCurrentLocation()[0], column = unit.getCurrentLocation()[1];
                        if (Math.abs(building.getRow() - row) + Math.abs(building.getColumn() - column) <= siegeWeapon
                                .getSiegeWeaponType().getRange())
                            building.changeHp(-(int) (unit.getDamage() * (1 + government.getFearRate() * 5.0 / 100)));
                        continue;
                    }
                }
                int enemyLocation[] = nearestEnemy(unit, range);
                if (enemyLocation != null) {
                    for (Person person2 : map.getMapPixel(enemyLocation[0], enemyLocation[1]).getPeople()) {
                        if (!person2.getGovernment().equals(unit.getGovernment()))
                            person2.changeHP(-(int) (unit.getDamage() * (1 + government.getFearRate() * 5.0 / 100)));
                    }
                    if (getLordInPixel(enemyLocation) != null && getLordInPixel(enemyLocation).getHp() <= 0) {
                        if (getLordInPixel(enemyLocation).getGovernment().getDefeatedBy() == null) {
                            getLordInPixel(enemyLocation).getGovernment().setDefeatedBy(government);
                            government.defeatLord(getLordInPixel(enemyLocation).getGovernment().getGold());
                        }
                    }
                } else {
                    Building building = unit.getBuildingBeingAttacked();
                    int row = unit.getCurrentLocation()[0], column = unit.getCurrentLocation()[1];
                    if (building == null || (Math.abs(building.getRow() - row)
                            + Math.abs(building.getColumn() - column)) > unit.type.getRange())
                        continue;
                    building.changeHp(-(int) (unit.getDamage() * (1 + government.getFearRate() * 5.0 / 100)));
                }
            }
        }
    }

    private void setAccess() {
        for (Person person : currentGovernment.getPeople()) {
            if (person instanceof Unit && ((Unit) person).getType().equals(UnitTypes.LADDERMAN)) {
                Unit unit = (Unit) person;
                int row = unit.getCurrentLocation()[0], column = unit.getCurrentLocation()[1];
                MapPixel pixel = null;
                if (row - 1 >= 0 && enemyWallExists(row - 1, column))
                    pixel = map.getMapPixel(row - 1, column);
                else if (row + 1 < map.getSize() && enemyWallExists(row + 1, column))
                    pixel = map.getMapPixel(row + 1, column);
                else if (column - 1 >= 0 && enemyWallExists(row, column - 1))
                    pixel = map.getMapPixel(row, column - 1);
                else if (column + 1 < map.getSize() && enemyWallExists(row, column + 1))
                    pixel = map.getMapPixel(row, column + 1);
                if (pixel != null && !pixel.canBeAccessed()) {
                    pixel.setAccess();
                    accessChanged.add(pixel);
                }
            }
        }
    }

    private void resetAccess() {
        for (MapPixel pixel : accessChanged)
            pixel.resetAccess();
        accessChanged.clear();
    }

    private boolean enemyWallExists(int row, int column) {
        for (Building building : map.getMapPixel(row, column).getBuildings())
            if (!building.getGovernment().equals(currentGovernment)) {
                TypeOfBuilding type = building.getTypeOfBuilding();
                if (type.equals(TypeOfBuilding.LOW_WALL) || type.equals(TypeOfBuilding.STONE_WALL)
                        || type.equals(TypeOfBuilding.CRENELATED_WALL))
                    return true;
            }
        return false;
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
        for (Person person : eliminatedPeople)
            government.removePerson(person);
    }

    public void removeEliminatedBuildings(Government government) {
        ArrayList<Building> eliminatedBuildings = new ArrayList<>();
        for (Building building : government.getBuildings()) {
            if (building.getHp() <= 0) {
                eliminatedBuildings.add(building);
                for (int i = 0; i < building.getTypeOfBuilding().getWidth(); i++)
                    for (int j = 0; j < building.getTypeOfBuilding().getLength(); j++) {
                        MapPixel pixel = map.getMapPixel(building.getRow() + i, building.getColumn() + j);
                        pixel.removeBuilding(building);
                        pixel.resetAccess();
                    }
            }
        }
        for (Building building : eliminatedBuildings) {
            if (building instanceof CagedWarDogs){
                CagedWarDogs cagedWarDogs = (CagedWarDogs)building;
                cagedWarDogs.releaseDogs();
            }
            government.removeBuilding(building);
        }

    }

    public void resourceDelivery(Government government) {
        for (Person person : government.getPeople()) {
            if (person instanceof NonMilitary) {
                NonMilitary nonMilitary = (NonMilitary) person;
                if (nonMilitary.currentLocation[0] == map.getKeepPosition(government.getColor())[0] + 7
                        && nonMilitary.currentLocation[1] == map.getKeepPosition(government.getColor())[1] + 3) {
                    if (nonMilitary.getWorkBuilding() instanceof ConvertingResources) {
                        ConvertingResources convertingResources = (ConvertingResources) nonMilitary.getWorkBuilding();
                        if (nonMilitary.isMovingResources()) {
                            government.changeResources(convertingResources.getResource(),
                                    (int) (convertingResources.getType().getResourceDeliveredAmount()
                                            * (1 - government.getFearRate() * 1.0 / 5)));
                            nonMilitary.setMovingResources(false);
                        }
                        if (convertingResources.getType().getResourceNeeded() == null)
                            nonMilitary.setMovingNeededResources(true);
                        if (convertingResources.getType().getResourceNeeded() != null
                                && convertingResources.getType().getResourceNeededAmount() <= government
                                        .getResourceAmount(convertingResources.getType().getResourceNeeded())) {
                            government.changeResources(convertingResources.getType().getResourceNeeded(),
                                    -convertingResources.getType().getResourceNeededAmount());
                            nonMilitary.setMovingNeededResources(true);
                        }
                    }

                } else if (nonMilitary.isMovingNeededResources()
                        && nonMilitary.currentLocation[0] == nonMilitary.getPatrolLocation()[2]
                        && nonMilitary.currentLocation[1] == nonMilitary.getPatrolLocation()[3]) {
                    nonMilitary.setMovingNeededResources(false);
                    nonMilitary.setMovingResources(true);
                }
            }
        }
    }

    public void setBuildingWorker(Building building, Government government) {
        building.workingState(true);
        government.changePeasant(-building.getTypeOfBuilding().getWorkerInUse());
        building.setWorkers(building.getTypeOfBuilding().getWorkerInUse());
        for (int i = 0; i < building.getTypeOfBuilding().getWorkerInUse(); i++) {
            NonMilitary nonMilitary = new NonMilitary(
                    new int[] { map.getKeepPosition(government.getColor())[0] + 7,
                            map.getKeepPosition(government.getColor())[1] + 3 },
                    government, building.getTypeOfBuilding().getWorkerType(), building);
            government.addPerson(nonMilitary);
            ArrayList<int[]> path = pathToBuilding(nonMilitary, building);
            map.getMapPixel(map.getKeepPosition(government.getColor())[0] + 7,
                    map.getKeepPosition(government.getColor())[1] + 3).addPerson(nonMilitary);
            nonMilitary.setMovePattern(map.getPathList(nonMilitary.getCurrentLocation()[0],
                    nonMilitary.getCurrentLocation()[1], path.get(path.size() - 1)[0],
                    path.get(path.size() - 1)[1], false));
            nonMilitary.setPatrolLocation(
                    new int[] { map.getKeepPosition(government.getColor())[0] + 7,
                            map.getKeepPosition(government.getColor())[1] + 3,
                            path.get(path.size() - 1)[0], path.get(path.size() - 1)[1] });
            nonMilitary.setPatrolling(true);
            int frow = nonMilitary.getCurrentLocation()[0], fcolumn = nonMilitary.getCurrentLocation()[1];
            nonMilitary.move();
            map.getMapPixel(frow, fcolumn).removePerson(nonMilitary);
            map.getMapPixel(nonMilitary.getCurrentLocation()[0], nonMilitary.getCurrentLocation()[1])
                    .addPerson(nonMilitary);
        }
    }

    private void updatePopulation(Government government) {
        int counter = 0;
        for (Building building : government.getBuildings())
            if (building.getTypeOfBuilding().equals(TypeOfBuilding.HOVEL))
                counter++;
        government.setPopulation(counter * 8 + 10);
    }

    private void sendWorkerToNoLaborBuildings(Government government) {
        HashSet<Building> startedWorkingBuildings = new HashSet<>();
        for (Building building : government.getBuildingsWaitingForWorkers()) {
            if (!government.getNoLaborBuildings().contains(building.getTypeOfBuilding())
                    && building.getTypeOfBuilding().getWorkerInUse() <= government.getPeasant()) {
                startedWorkingBuildings.add(building);
                setBuildingWorker(building, government);
            }
            if (government.getPeasant() == 0)
                break;
        }
        government.removeBuildingsWaitingForWorkers(startedWorkingBuildings);
    }

    private void updatePeasant(Government government) {
        int remainingPopulation = government.getPopulation() - government.getPeople().size() + 1;
        if ((int) (government.getPopularity() / 10) - 5 >= 0) {
            if (government.getPeasant() < 24) {
                changePeasant(
                        Math.min(Math.min(remainingPopulation, 24 - government.getPeasant()),
                                (int) (government.getPopularity() / 10) - 5),
                        government);
            }
        } else
            changePeasant((int) (government.getPopularity() / 10) - 5, government);
    }

    public String endOfTurn() throws IOException {
        ArrayList<Government> randomGovernments = (ArrayList<Government>) governments.clone();
        Collections.shuffle(randomGovernments);
        for (Government government : randomGovernments) {
            government.setPopularity(government.getPopularity() + government.getChangesOnPopularity());
            government.changeGold((int) (government.getTaxAmount() * government.getPopulation()));
            government.giveFood();
            government.updateFoodRate();
            government.updateTaxRate();
            sendWorkerToNoLaborBuildings(government);
            updatePopulation(government);
            updatePeasant(government);
            setPatrolPattern(government);
            updateMovePatterns(government);
            moveUnitsInQueue(government);
            checkStance(government);
            resourceDelivery(government);
            government.resetMovesLeft();
        }
        for (Government government : governments)
            attackAllOthers(government);
        for (Government government : governments) {
            removeEliminatedPeople(government);
            removeEliminatedBuildings(government);
            government.increasePercentOfBlessed();
            for (Person person : government.getPeople()) {
                if (person instanceof Unit){
                    Unit unit = (Unit)person;
                    unit.endTurn();
                }
            }
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

    public ArrayList<int[]> sendToAOilSmelter(Engineer engineer) {
        Government owner = engineer.getGovernment();
        ArrayList<Building> buildings = map.getAllBuildingsOfSomeone(owner);
        ArrayList<int[]> path = null;
        for (Building building : buildings) {
            if (building.getTypeOfBuilding().equals(TypeOfBuilding.OIL_SMELTER)) {
                ArrayList<int[]> tempPath = pathToBuilding(engineer, building);
                if (path == null)
                    path = tempPath;
                else if (path.size() > tempPath.size())
                    path = tempPath;
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

    public void releaseDogs(int numberOfDogs, int x, int y, Government owner) {
        for (int i = 0; i < numberOfDogs; i++) {
            Unit unit = new Unit(UnitTypes.WAR_DOG, new int[] { x, y }, owner);
            unit.setUnitStance(UnitStance.OFFENSIVE);
            map.getMapPixel(x, y).addPerson(unit);
            owner.addPerson(unit);
        }
    }
}
