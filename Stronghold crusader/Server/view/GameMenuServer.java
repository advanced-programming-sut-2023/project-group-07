package Server.view;

import controller.*;
import model.Game;
import model.Resources;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenuServer {
    User currentUser;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    private final GameMenuController gameMenuController;
    private final ShopServer shopServer;
    private final MapMenuServer mapMenuServer;
    private final TradeMenuServer tradeMenuServer = new TradeMenuServer();
    private final Game game;
    private Scanner scanner;
    public GameMenuServer(DataOutputStream dataOutputStream, DataInputStream dataInputStream,User currentUser,GameMenuController gameMenuController,Game game) {
        this.gameMenuController=gameMenuController;
        this.game=game;
        shopServer = new ShopServer(dataOutputStream,dataInputStream,gameMenuController);
        this.dataInputStream = dataInputStream;
        this.dataOutputStream=dataOutputStream;
        this.currentUser = currentUser;
        this.mapMenuServer = new MapMenuServer(dataInputStream, dataOutputStream,game);
    }
    public void gameHandler() throws IOException {
        dataOutputStream.writeUTF("you have entered the game");
        while (true) {
            if(dataInputStream.available()!=0){
                Matcher matcher;
                String input = dataInputStream.readUTF();
                if(!game.getCurrentGovernment().getUser().equals(currentUser))
                    dataOutputStream.writeUTF("it's not your turn!");
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING) != null)
                    dataOutputStream.writeUTF(dropBuilding(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_CURRENT_PLAYER) != null)
                    dataOutputStream.writeUTF(gameMenuController.showCurrentPlayer());
                else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_MAP) != null)
                    mapMenuServer.run(input);
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING) != null)
                    dataOutputStream.writeUTF(selectBuilding(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_PIXEL_UNIT) != null)
                    dataOutputStream.writeUTF(selectPixelUnit(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_REGION_UNIT) != null)
                    dataOutputStream.writeUTF(selectRegionUnit(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.BUILD_SIEGE_WEAPON) != null)
                    dataOutputStream.writeUTF(buildSiegeWeapon(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.MOVE_UNIT) != null)
                    dataOutputStream.writeUTF(moveUnit(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.PATROL_UNIT) != null)
                    dataOutputStream.writeUTF(patrolUnit(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.STOP_UNIT) != null)
                    dataOutputStream.writeUTF(stopUnit());
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SET_STANCE) != null)
                    dataOutputStream.writeUTF(setStance(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_ENEMY) != null)
                    dataOutputStream.writeUTF(attackEnemy(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_BUILDING) != null)
                    dataOutputStream.writeUTF(attackBuilding(input));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.AREA_ATTACK) != null)
                    dataOutputStream.writeUTF(areaAttack(input));
                else if ((matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.POUR_OIL)) != null)
                    dataOutputStream.writeUTF(pourOil(matcher));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.GIVE_OIL) != null)
                    dataOutputStream.writeUTF(giveOil());
                else if ((matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.DIG_TUNNEL)) != null
                        && matcher.group("x") != null && matcher.group("y") != null)
                    dataOutputStream.writeUTF(digTunnel(matcher));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.DISBAND_UNIT) != null)
                    dataOutputStream.writeUTF(disbandUnit());
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_POPULARITY) != null)
                    dataOutputStream.writeUTF(getPopularity());
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_POPULARITY_FACTORS) != null)
                    dataOutputStream.writeUTF(getPopularityFactors());
                else if ((matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.FEAR_RATE)) != null)
                    dataOutputStream.writeUTF(setFearRate(matcher));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.FEAR_RATE_SHOW) != null)
                    dataOutputStream.writeUTF(showFearRate());
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.FOOD_RATE_SHOW) != null)
                    dataOutputStream.writeUTF(showFoodRate());
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_FOOD_LIST) != null)
                    dataOutputStream.writeUTF(getFoodList());
                else if ((matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.FOOD_RATE)) != null)
                    dataOutputStream.writeUTF(setFoodList(matcher));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_GOLD) != null)
                    dataOutputStream.writeUTF(showGold());
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.TAX_RATE_SHOW) != null)
                    dataOutputStream.writeUTF(showTaxRate());
                else if ((matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.TAX_RATE)) != null)
                    dataOutputStream.writeUTF(setTaxRate(matcher));
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_POPULATION) != null)
                    dataOutputStream.writeUTF(getPopulation());
                else if (GameMenuCommands.getMatcher(input, GameMenuCommands.NEXT_TURN) != null) {
                    String returnMessage = gameMenuController.nextTurn();
                    if (returnMessage != null && returnMessage.contains("GAME OVER!")) {
                        dataOutputStream.writeUTF(returnMessage);
                        dataOutputStream.writeUTF("\033[1;33m");
                        dataOutputStream.writeUTF("Reseting maps and users...");
                        gameMenuController.resetMapsAndUsers();
                        dataOutputStream.writeUTF("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
                        dataOutputStream.writeUTF("\033[0m");
                        return;
                    } else if (returnMessage != null)
                        dataOutputStream.writeUTF(returnMessage + gameMenuController.nextTurnMessage());
                    else
                        dataOutputStream.writeUTF(gameMenuController.nextTurnMessage());
                } else if (GameMenuCommands.getMatcher(input, GameMenuCommands.ENTER_TRADE_MENU) != null) {
                    dataOutputStream.writeUTF("You have entered trade menu");
                    tradeMenuServer.run(dataInputStream,dataOutputStream);
                } else
                    dataOutputStream.writeUTF("Invalid command!");
            }
        }
    }

    private String setFearRate(Matcher matcher) {
        int rate;
        try {
            rate = Integer.parseInt(matcher.group("rate"));
        } catch (NumberFormatException e) {
            return "Number is not valid.";
        }
        switch (gameMenuController.setFearRate(rate)) {
            case INVALID_RATE:
                return "Rate is not in valid range.";
            case SET_FEAR_RATE_SUCCESSFUL:
                return "Setting fear rate was successful.";
            default:
                break;
        }
        return null;
    }

    private String showFoodRate() {
        return "Your food rate is : " + gameMenuController.getFoodRate();
    }

    private String showGold() {
        return "You have " + gameMenuController.getGold() + " gold.";
    }

    private String digTunnel(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x")),
                y = Integer.parseInt(matcher.group("y"));
        switch (gameMenuController.digTunnel(x - 1, y - 1)) {
            case INVALID_COORDINATES:
                return "Coordinates are invalid.";
            case NO_AVAILABLE_TUNNELER:
                return "There is no available tunneler in selected units.";
            case SUCCESSFUL_DIG_TUNNEL:
                return "Digging tunnel started successfully.";
            default:
                break;
        }
        return null;
    }

    private String giveOil() {
        switch (gameMenuController.giveOil()) {
            case DONT_HAVE_OIL_SMELTER:
                return "You don't have oil smelter.";
            case NO_ONE_TO_GIVE_OIL_TO:
                return "None of selected units can get oil.";
            case GIVING_OIL_SUCESSFUL:
                return "Engineers will get oil";
            default:
                break;
        }
        return null;
    }

    private String pourOil(Matcher matcher) {
        String directionString = matcher.group("direction");
        Directions direction = Directions.getByName(directionString);
        if (direction == null)
            return "Invalid direction.";
        switch (gameMenuController.pourOil(direction)) {
            case NO_ONE_HAS_OIL:
                return "There is no engineer with oil in selected units.";
            case BAD_DIRECTION:
                return "You can't pour in this direction";
            case POUR_OIL_SUCCESSFUL:
                return "You have successfully poured oil.";
            default:
                break;
        }
        return null;
    }

    private String getPopularityFactors() {
        ArrayList<Integer> factorsInOrder = gameMenuController.getPopularityFactors();
        return "Popularity factors:\n" +
                "Food : " + factorsInOrder.get(0) + "\n" +
                "Tax : " + factorsInOrder.get(1) + "\n" +
                "Religion : " + factorsInOrder.get(2) + "\n" +
                "Fear factor : " + factorsInOrder.get(3) + "\n" +
                "Effect of buildings : " + factorsInOrder.get(4);
    }

    private String setFoodList(Matcher matcher) {
        String rateString = Controller.trimmer(matcher.group("rate"));
        int rate = Integer.parseInt(rateString);
        switch (gameMenuController.setFoodList(rate)) {
            case INVALID_RATE:
                return "Rate is not in valid range.";
            case SET_FOOD_RATE_SUCCESSFUL:
                return "Setting food rate was successful.";
            case NOT_ENOUGH_FOOD:
                return "You don't have any food. So food rate must be -2";
            default:
                break;
        }
        return null;
    }

    private String getFoodList() {
        String result = "";
        HashMap<Resources, Integer> foodList = gameMenuController.getFoodList();
        for (Resources food : foodList.keySet()) {
            result += "you have " + foodList.get(food) + " of " + food.getName() + "\n";
        }
        return result;
    }

    private String getPopulation() {
        return "Your population is " + gameMenuController.getPopulation();
    }

    private String getPopularity() {
        return "Your popularity is : " + gameMenuController.getPopularity();

    }

    private String setTaxRate(Matcher matcher) {
        int rate;
        try {
            rate = Integer.parseInt(matcher.group("rate"));
        } catch (NumberFormatException e) {
            return "Invalid number format.";
        }
        switch (gameMenuController.setTax(rate)) {
            case INVALID_RATE:
                return "Rate is not in valid range";
            case SETTING_TAX_SUCCESSFUL:
                return "You have successfully set your tax.";
            default:
                break;
        }
        return null;
    }

    private String showFearRate() {
        return "Your fear rate: " + gameMenuController.showFearRate();
    }

    private String showTaxRate() {
        return ("Your tax rate: " + gameMenuController.showTaxRate() + " coins\n" +
                "Popularity effect: " + gameMenuController.getTaxEffectOnPopularity());
    }

    private String dropBuilding(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        Matcher typeMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.TYPE);
        if (rowMatcher == null)
            return "Enter the row number!";
        if (columnMatcher == null)
            return "Enter the column number!";
        if (typeMatcher == null)
            return "Enter the type!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        String type = typeMatcher.group("type").trim();
        switch (gameMenuController.dropBuilding(row, column, type)) {
            case INVALID_BUILDING_NAME:
                return "Invalid building name!";
            case INVALID_ROW_OR_COLUMN:
                return "Invalid row or column!";
            case THERES_ALREADY_BUILDING:
                return "There is already a building here!";
            case THERES_ALREADY_UNIT:
                return "You can't drop buildings on units!";
            case CANT_PLACE_THIS:
                return "You can't place this here!";
            case THERES_AN_ENEMY_CLOSE_BY:
                return "Too close to an enemy!";
            case NOT_ENOUGH_GOLD:
                return "Not enough gold!";
            case NOT_ENOUGH_RESOURCES:
                return "Not enough resources!";
            case MUST_BE_ADJACENT_TO_BUILDINGS_OF_THE_SAME_TYPE:
                return "Must be adjacent to buildings of the same type!";
            case DEPLOYMENT_SUCCESSFUL:
                return type + " deployed successfully!";
            case INVALID_COMMAND:
                return "Invalid command!";
            default:
                break;
        }
        return null;
    }

    private String selectBuilding(String input) throws IOException {
        int row, column;
        if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING).group("row1") == null) {
            row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING).group("row2"));
            column = Integer
                    .parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING).group("column2"));
        } else {
            row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING).group("row1"));
            column = Integer
                    .parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING).group("column1"));
        }
        Messages returnMessage = gameMenuController.selectBuilding(row, column);
        if (returnMessage.equals(Messages.NO_BUILDING_HERE))
            return "No building here!";
        else if (returnMessage.equals(Messages.ENEMY_BUILDING))
            return "Enemy building!";
        dataOutputStream.writeUTF("You have entered " + gameMenuController.getSelectedBuilding() + "!");
        switch (returnMessage) {
            case ENTERED_TOWER:
            case ENTERED_GATEHOUSE:
                towerAndGate(returnMessage.equals(Messages.ENTERED_GATEHOUSE));
                break;
            case ENTERED_MILITARY_CAMP:
                militaryCamp();
                break;
            case ENTERED_ARMS_WORKSHOP:
                armsWorkshop();
                break;
            case ENTERED_MARKET:
                shopServer.run();
                break;
            case ENTERED_BUILDING_SUCCESSFULLY:
                enteredBuilding();
            default:
                break;
        }
        return "";
    }

    private void towerAndGate(boolean isGate) throws IOException {
        while (true) {
            String command = dataInputStream.readUTF();
            if (GameMenuCommands.getMatcher(command, GameMenuCommands.REPAIR) != null) {
                Messages message = gameMenuController.repair();
                switch (message) {
                    case NOT_ENOUGH_RESOURCES:
                        dataOutputStream.writeUTF("Not enough resources!");
                        break;
                    case THERES_AN_ENEMY_CLOSE_BY:
                        dataOutputStream.writeUTF("Can't repair! There's an enemy close by!");
                        break;
                    case ALREADY_AT_FULL_HP:
                        dataOutputStream.writeUTF("This building already has maximum HP!");
                        break;
                    case REPAIRED_SUCCESSFULLY:
                        gameMenuController.repair();
                        dataOutputStream.writeUTF("Repaired successfully!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.CLOSE_GATE) != null && isGate) {
                Messages message = gameMenuController.closeGate();
                switch (message) {
                    case GATE_ALREADY_CLOSED:
                        dataOutputStream.writeUTF("Gate already closed!");
                        break;
                    case GATE_HAS_BEEN_CLOSED:
                        dataOutputStream.writeUTF("Gate has been closed!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.OPEN_GATE) != null && isGate) {
                Messages message = gameMenuController.openGate();
                switch (message) {
                    case GATE_ALREADY_OPEN:
                        dataOutputStream.writeUTF("Gate is already open!");
                        break;
                    case GATE_HAS_BEEN_OPENED:
                        dataOutputStream.writeUTF("Gate has been opened!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                dataOutputStream.writeUTF("Exit was successful!");
                break;
            } else
                dataOutputStream.writeUTF("Invalid command!");
        }
    }

    private void militaryCamp() throws IOException {
        while (true) {
            String command = dataInputStream.readUTF();
            if (GameMenuCommands.getMatcher(command, GameMenuCommands.CREATE_UNIT) != null) {
                Matcher typeMatcher = GameMenuCommands.getMatcher(command, GameMenuCommands.TYPE);
                Matcher countMatcher = GameMenuCommands.getMatcher(command, GameMenuCommands.COUNT);
                if (typeMatcher == null) {
                    dataOutputStream.writeUTF("Enter the unit type!");
                    continue;
                }
                if (countMatcher == null) {
                    dataOutputStream.writeUTF("Enter the unit count!");
                    continue;
                }
                String type = typeMatcher.group("type").trim();
                int count = Integer.parseInt(countMatcher.group("count"));
                switch (gameMenuController.createUnit(type, count)) {
                    case INVALID_UNIT_NAME:
                        dataOutputStream.writeUTF("Invalid unit name!");
                        break;
                    case INVALID_NUMBER:
                        dataOutputStream.writeUTF("Invalid number!");
                        break;
                    case NOT_ENOUGH_PEASANTS:
                        dataOutputStream.writeUTF("Not enough peasants!");
                        break;
                    case NOT_ENOUGH_GOLD:
                        dataOutputStream.writeUTF("Not enough gold!");
                        break;
                    case NOT_ENOUGH_RESOURCES:
                        dataOutputStream.writeUTF("Not enough resources!");
                        break;
                    case CANT_CREATE_THIS_UNIT_HERE:
                        dataOutputStream.writeUTF("Can't create this unit here!");
                        break;
                    case UNIT_CREATED_SUCCESSFULLY:
                        dataOutputStream.writeUTF("Unit created successfully!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.SHOW_UNITS) != null) {
                dataOutputStream.writeUTF(gameMenuController.getUnitsInfo(gameMenuController.getSelectedBuilding()));
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_WORKING_STATE) != null
                    && gameMenuController.getSelectedBuilding().equals("cathedral")) {
                dataOutputStream.writeUTF("Working state changed successfully!");
                gameMenuController.changeWorkingState();
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                dataOutputStream.writeUTF("Exit was successful!");
                break;
            } else {
                dataOutputStream.writeUTF("Invalid command!");
            }
        }
    }

    private void armsWorkshop() throws IOException {
        while (true) {
            String command = dataInputStream.readUTF();
            if (GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_ARMS) != null) {
                gameMenuController.changeArms();
                dataOutputStream.writeUTF("This workshop is now creating " + gameMenuController.getResources() + "!");
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_WORKING_STATE) != null) {
                gameMenuController.changeWorkingState();
                dataOutputStream.writeUTF("Working state changed successfully!");
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                dataOutputStream.writeUTF("Exit was successful!");
                break;
            } else {
                dataOutputStream.writeUTF("Invalid command!");
            }
        }
    }

    private void enteredBuilding() throws IOException {
        while (true) {
            String command = dataInputStream.readUTF();
            if (GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_WORKING_STATE) != null) {
                dataOutputStream.writeUTF("Working state changed successfully!");
                gameMenuController.changeWorkingState();
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                dataOutputStream.writeUTF("Exit was successful!");
                break;
            } else {
                dataOutputStream.writeUTF("Invalid command!");
            }
        }
    }

    private String selectPixelUnit(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        switch (gameMenuController.selectUnit(row, column, row, column)) {
            case INVALID_COORDINATES:
                return "Invalid Coordinates!";
            case NO_UNITS_HERE:
                return "No units here!";
            case UNIT_SELECTED_SUCCESSFULLY:
                return "Unit selected successfully!";
            default:
                break;
        }
        return null;
    }

    private String selectRegionUnit(String input) {
        Matcher x1Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.FIRST_ROW);
        Matcher y1Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.FIRST_COLUMN);
        Matcher x2Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.SECOND_ROW);
        Matcher y2Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.SECOND_COLUMN);
        String checkRegionCoordinates = Controller.checkRegionCoordinatesFormat(x1Matcher, y1Matcher, x2Matcher,
                y2Matcher);
        if (checkRegionCoordinates != null)
            return checkRegionCoordinates;
        int frow = Integer.parseInt(x1Matcher.group("frow")) - 1;
        int fcolumn = Integer.parseInt(y1Matcher.group("fcolumn")) - 1;
        int srow = Integer.parseInt(x2Matcher.group("srow")) - 1;
        int scolumn = Integer.parseInt(y2Matcher.group("scolumn")) - 1;
        switch (gameMenuController.selectUnit(frow, fcolumn, srow, scolumn)) {
            case INVALID_COORDINATES:
                return "Invalid Coordinates!";
            case UNIT_SELECTED_SUCCESSFULLY:
                return "Unit selected successfully!";
            default:
                break;
        }
        return null;
    }

    private String moveUnit(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        switch (gameMenuController.moveUnit(row, column)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case CANT_MOVE_UNITS_TO_THIS_LOCATION:
                return "Can't move units to this location!";
            case NO_UNITS_SELECTED:
                return "No units selected!";
            case UNIT_MOVED_SUCCESSFULLY:
                return "Units moved successfully!";
            default:
                break;
        }
        return null;
    }

    private String patrolUnit(String input) {
        Matcher x1Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.FIRST_ROW);
        Matcher y1Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.FIRST_COLUMN);
        Matcher x2Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.SECOND_ROW);
        Matcher y2Matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.SECOND_COLUMN);
        String checkRegionCoordinates = Controller.checkRegionCoordinatesFormat(x1Matcher, y1Matcher, x2Matcher,
                y2Matcher);
        if (checkRegionCoordinates != null)
            return checkRegionCoordinates;
        int frow = Integer.parseInt(x1Matcher.group("frow"));
        int fcolumn = Integer.parseInt(y1Matcher.group("fcolumn"));
        int srow = Integer.parseInt(x2Matcher.group("srow"));
        int scolumn = Integer.parseInt(y2Matcher.group("scolumn"));
        switch (gameMenuController.patrolUnit(frow, fcolumn, srow, scolumn)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case CANT_MOVE_UNITS_TO_THIS_LOCATION:
                return "Can't move units to this location!";
            case NO_UNITS_SELECTED:
                return "No units selected!";
            case UNIT_MOVED_SUCCESSFULLY:
                return "Patrol successful!";
            default:
                break;
        }
        return null;
    }

    public String stopUnit() {
        switch (gameMenuController.stopUnit()) {
            case UNIT_STOPPED_SUCCESSFULLY:
                return "Units stopped successfully!";
            default:
                break;
        }
        return null;
    }

    private String setStance(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        Matcher stanceMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.STANCE);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        if (stanceMatcher == null)
            return "Please enter units stance!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        String stance = stanceMatcher.group("stance");
        switch (gameMenuController.setStance(row, column, stance)) {
            case INVALID_STANCE:
                return "Invalid stance!";
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case NO_UNITS_SELECTED:
                return "No units selected!";
            case STANCE_CHANGED_SUCCESSFULLY:
                return "Stance changed successfully!";
            default:
                break;
        }
        return null;
    }

    private String attackEnemy(String input) {
        int row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_ENEMY).group("row")) - 1;
        int column = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_ENEMY).group("column"))
                - 1;
        switch (gameMenuController.attackEnemy(row, column)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case NO_ENEMY_HERE:
                return "No enemy here!";
            case NO_UNITS_SELECTED:
                return "No units selected!";
            case ATTACKING_ENEMY_UNITS:
                return "Attacking enemy units!";
            default:
                break;
        }
        return null;
    }

    private String attackBuilding(String input) {
        int row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_BUILDING).group("row"))
                - 1;
        int column = Integer
                .parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_BUILDING).group("column")) - 1;
        switch (gameMenuController.attackBuilding(row, column)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case NO_ENEMY_HERE:
                return "No enemy building here!";
            case NO_UNITS_SELECTED:
                return "No units selected!";
            case NO_UNIT_CAN_ATTACK_BUILDINGS:
                return "No unit can attack buildings!";
            case ATTACKING_ENEMY_BUILDINGS:
                return "Attacking enemy buildings!";
            default:
                break;
        }
        return null;
    }

    private String areaAttack(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        switch (gameMenuController.areaAttack(row, column)) {
            case NO_UNITS_SELECTED:
                return "No units selected!";
            case MUST_SELECT_RANGED_UNITS:
                return "Must select only ranged units!";
            case OUT_OF_RANGE:
                return "Out of range!";
            case AREA_ATTACKING_SET_SUCCESSFULLY:
                return "Area attacking set successfully!";
            default:
                break;
        }
        return null;
    }

    private String disbandUnit() {
        switch (gameMenuController.disbandUnit()) {
            case NO_UNITS_SELECTED:
                return "You don't have any selected unit.";
            case UNITS_DISBANDED_SUCCESSFULLY:
                return "You disbanded units successfully.";
            default:
                break;
        }
        return null;
    }

    private String buildSiegeWeapon(String input) {
        Matcher typeMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.BUILD_SIEGE_WEAPON);
        String type = typeMatcher.group("type").trim();
        if (type == null)
            return "Enter the type!";
        switch (gameMenuController.buildSiegeWeapon(type)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case INVALID_SIEGE_WEAPON_TYPE:
                return "Invalid Siege Weapon Type!";
            case NEEDS_MORE_ENGINEERS:
                return "Needs more engineers!";
            case NOT_ENOUGH_GOLD:
                return "Needs more gold!";
            case SIEGE_WEAPON_BUILT_SUCCESSFULLY:
                return "Siege Weapon built successfully!";
            default:
                break;
        }
        return null;

    }
}
