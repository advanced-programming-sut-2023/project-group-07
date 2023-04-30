package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

import javax.management.StandardEmitterMBean;

import controller.*;

import model.Game;
import model.Government;
import model.Resources;

public class GameMenu {
    private final GameMenuController controller = new GameMenuController();
    private final Shop shop = new Shop(controller);
    private final MapMenu mapMenu = new MapMenu();
    private final TradeMenu tradeMenu = new TradeMenu();
    private Scanner scanner;

    public void run(Scanner scanner) {
        controller.refreshGame();
        this.scanner = scanner;
        Matcher matcher;
        while (true) {
            String input = scanner.nextLine();
            if (GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING) != null)
                System.out.println(dropBuilding(input));
            else if (MapMenuCommands.getMatcher(input, MapMenuCommands.SHOW_MAP) != null)
                mapMenu.run(scanner, input);
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING) != null)
                System.out.println(selectBuilding(input));
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_PIXEL_UNIT)!=null)
                System.out.println(selectPixelUnit(input));
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_REGION_UNIT)!=null)
                System.out.println(selectRegionUnit(input));
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.MOVE_UNIT)!=null)
                System.out.println(moveUnit(input));
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.PATROL_UNIT)!=null)
                System.out.println(patrolUnit(input));
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.STOP_UNIT)!=null)
                System.out.println(stopUnit());
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.SET_STANCE)!=null)
                System.out.println(setStance(input));
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_ENEMY)!=null)
                System.out.println(attackEnemy(input));
            else if(GameMenuCommands.getMatcher(input, GameMenuCommands.AREA_ATTACK)!=null)
                System.out.println(areaAttack(input));
            else if ((matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.POUR_OIL)) != null)
                System.out.println(pourOil(matcher));
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.GIVE_OIL) != null)
                System.out.println(giveOil());
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_POPULARITY) != null)
                System.out.println("Your popularity is : " + controller.getPopularity());
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_POPULARITY_FACTORS) != null)
                System.out.println(getPopularityFactors());
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.FOOD_RATE_SHOW) != null)
                System.out.println("Your food rate is : " + controller.getFoodRate());
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SHOW_FOOD_LIST) != null)
                System.out.print(getFoodList());
            else if ((matcher = GameMenuCommands.getMatcher(input, GameMenuCommands.FOOD_RATE)) != null)
                System.out.println(setFoodList(matcher));
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.ENTER_TRADE_MENU) != null) {
                System.out.println("You have entered");
                tradeMenu.run(scanner);
            } else
                System.out.println("Invalid command!");
        }

    }

    private String giveOil() {
        switch (controller.giveOil()){
            case DONT_HAVE_OIL_SMELTER:
                return "You don't have oil smelter.";
            case NO_ONE_TO_GIVE_OIL_TO:
                return "None of selected units can get oil.";
            case GIVING_OIL_SUCESSFUL:
                return "Engineers will get oil";
        }
        return null;
    }

    private String pourOil(Matcher matcher) {
        String directionString = matcher.group("direction");
        Directions direction = Directions.getByName(directionString);
        if (direction == null) return "Invalid direction.";
        switch (controller.pourOil(direction)){
            case NO_ONE_HAS_OIL :
                return "There is no engineer with oil in selected units.";
            case BAD_DIRECTION:
                return "You can't pour in this direction";
            case POUR_OIL_SUCCESSFUL:
                return "You have successfully poured oil.";
        }
        return null;
    }

    private String getPopularityFactors() { // todo : check this
        ArrayList<Integer> factorsInOrder = controller.getPopularityFactors();
        return "Popularity factors:\n" +
                "Food : " + factorsInOrder.get(0) + "\n" +
                "Tax : " + factorsInOrder.get(1) + "\n" +
                "Religion : " + factorsInOrder.get(2) + "\n" +
                "Fear factor : " + factorsInOrder.get(3) + "\n" +
                "Effect of buildings : " + factorsInOrder.get(4);
    }

    private String setFoodList(Matcher matcher) {
        int rate = Integer.parseInt(matcher.group("rate"));
        switch (controller.setFoodList(rate)) {
            case INVALID_RATE:
                return "Rate is not in valid range.";
            case SET_FOOD_RATE_SUCCESSFUL:
                return "Setting food rate was successful.";
            case NOT_ENOUGH_FOOD:
                return "You don't have any food. So food rate must be -2";
        }
        return null;
    }

    private String getFoodList() { // todo: haven't been checked
        String result = "";
        HashMap<Resources, Integer> foodList = controller.getFoodList();
        for (Resources food : foodList.keySet()) {
            result += "you have " + foodList.get(food) + " of " + food.getName() + "\n";
        }
        return result;
    }

    private String getPopulation() {
        return null;
    }

    private String getPopularity() {
        // System.out.println("Your popularity: "+.getPopularity()+);
        return null;
    }

    private String setTaxRate() {
        return null;
    }

    private String showTaxRate() {
        Government government = Controller.currentGame.getCurrentGovernment();
        return ("Your tax rate: " + government.getTaxAmount() + "coins\n" +
                "Popularity effect: " + government.getTaxEffectOnPopularity());
    }

    private void endOfTurn() {

    }

    private void showMap() {

    }

    private String dropBuilding(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        Matcher typeMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.TYPE);
        if(rowMatcher == null)
            return "Enter the row number!";
        if(columnMatcher == null)
            return "Enter the column number!";
        if(typeMatcher == null)
            return "Enter the type!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        String type = typeMatcher.group("type").trim();
        switch (controller.dropBuilding(row, column, type)) {
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

    private String selectBuilding(String input) {
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
        Messages returnMessage = controller.selectBuilding(row, column);
        if (returnMessage.equals(Messages.NO_BUILDING_HERE))
            return "No building here!";
        else if (returnMessage.equals(Messages.ENEMY_BUILDING))
            return "Enemy building!";
        System.out.println("You have entered " + controller.getSelectedBuilding() + "!");
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
                shop.run(scanner);
                break;
            case ENTERED_BUILDING_SUCCESSFULLY:
                enteredBuilding();
            default:
                break;
        }
        return "";
    }

    public void towerAndGate(boolean isGate) {
        while (true) {
            String command = scanner.nextLine();
            if (GameMenuCommands.getMatcher(command, GameMenuCommands.REPAIR) != null) {
                Messages message = controller.repair();
                switch (message) {
                    case NOT_ENOUGH_RESOURCES:
                        System.out.println("Not enough resources!");
                        break;
                    case THERES_AN_ENEMY_CLOSE_BY:
                        System.out.println("Can't repair! There's an enemy close by!");
                        break;
                    case ALREADY_AT_FULL_HP:
                        System.out.println("This building already has maximum HP!");
                        break;
                    case REPAIRED_SUCCESSFULLY:
                        controller.repair();
                        System.out.println("Repaired successfully!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.CLOSE_GATE) != null && isGate) {
                Messages message = controller.closeGate();
                switch (message) {
                    case GATE_ALREADY_CLOSED:
                        System.out.println("Gate already closed!");
                        break;
                    case GATE_HAS_BEEN_CLOSED:
                        System.out.println("Gate has been closed!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.OPEN_GATE) != null && isGate) {
                Messages message = controller.openGate();
                switch (message) {
                    case GATE_ALREADY_OPEN:
                        System.out.println("Gate is already open!");
                        break;
                    case GATE_HAS_BEEN_OPENED:
                        System.out.println("Gate has been opened!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                System.out.println("Exit was successful!");
                break;
            } else
                System.out.println("Invalid command!");
        }
    }

    public void militaryCamp() {
        while (true) {
            String command = scanner.nextLine();
            if (GameMenuCommands.getMatcher(command, GameMenuCommands.CREATE_UNIT) != null) {
                Matcher typeMatcher = GameMenuCommands.getMatcher(command, GameMenuCommands.TYPE);
                Matcher countMatcher = GameMenuCommands.getMatcher(command, GameMenuCommands.COUNT);
                if(typeMatcher == null){
                    System.out.println("Enter the unit type!");
                    continue;
                }
                if(countMatcher == null){
                    System.out.println("Enter the unit count!");
                    continue;
                }
                String type = typeMatcher.group("type").trim();
                int count = Integer.parseInt(countMatcher.group("count"));
                switch (controller.createUnit(type, count)) {
                    case INVALID_UNIT_NAME:
                        System.out.println("Invalid unit name!");
                        break;
                    case INVALID_NUMBER:
                        System.out.println("Invalid number!");
                        break;
                    case NOT_ENOUGH_GOLD:
                        System.out.println("Not enough gold!");
                        break;
                    case NOT_ENOUGH_RESOURCES:
                        System.out.println("Not enough resources!");
                        break;
                    case CANT_CREATE_THIS_UNIT_HERE:
                        System.out.println("Can't create this unit here!");
                        break;
                    case UNIT_CREATED_SUCCESSFULLY:
                        System.out.println("Unit created successfully!");
                        break;
                    default:
                        break;
                }
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.SHOW_UNITS) != null) {
                System.out.print(controller.getUnitsInfo(controller.getSelectedBuilding()));
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_WORKING_STATE) != null
                    && controller.getSelectedBuilding().equals("cathedral")) {
                System.out.println("Working state changed successfully!");
                controller.changeWorkingState();
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                System.out.println("Exit was successful!");
                break;
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    public void armsWorkshop() {
        while (true) {
            String command = scanner.nextLine();
            if (GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_ARMS) != null) {
                controller.changeArms();
                System.out.println("This workshop is now creating " + controller.getResources() + "!");
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_WORKING_STATE) != null) {
                controller.changeWorkingState();
                System.out.println("Working state changed successfully!");
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                System.out.println("Exit was successful!");
                break;
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    public void enteredBuilding() {
        while(true) {
            String command = scanner.nextLine();
            if(GameMenuCommands.getMatcher(command, GameMenuCommands.CHANGE_WORKING_STATE) != null){
                System.out.println("Working state changed successfully!");
                controller.changeWorkingState();
            }
            else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                System.out.println("Exit was successful!");
                break;
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    public String selectPixelUnit(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        switch (controller.selectUnit(row, column, row, column)) {
            case INVALID_COORDINATES:
                return "Invalid Coordinates!";
            case UNIT_SELECTED_SUCCESSFULLY:
                return "Unit selected successfully!";
            default:
                break;
        }
        return null;
    }

    public String selectRegionUnit(String input) {
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
        switch (controller.selectUnit(frow, fcolumn, srow, scolumn)) {
            case INVALID_COORDINATES:
                return "Invalid Coordinates!";
            case UNIT_SELECTED_SUCCESSFULLY:
                return "Unit selected successfully!";
            default:
                break;
        }
        return null;
    }

    public String moveUnit(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        switch(controller.moveUnit(row, column)){
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case CANT_MOVE_UNITS_TO_THIS_LOCATION:
                return "Can't move units to this location!";
            case UNIT_MOVED_SUCCESSFULLY:
                return "Units moved successfully!";
            default:
                break;
        }
        return null;
    }

    public String patrolUnit(String input) {
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
        switch (controller.selectUnit(frow, fcolumn, srow, scolumn)) {
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case CANT_MOVE_UNITS_TO_THIS_LOCATION:
                return "Can't move units to this location!";
            case UNIT_MOVED_SUCCESSFULLY:
                return "Patrol successful!";
            default:
                break;
        }
        return null;
    }

    public String stopUnit() {
        switch(controller.stopUnit()){
            case UNIT_STOPPED_SUCCESSFULLY:
                return "Units stopped successfully!";
            default:
                break;
        }
        return null;
    }

    public String setStance(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        Matcher stanceMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.STANCE);
        String checkCoordinates = Controller.checkCoordinatesFormat(rowMatcher, columnMatcher);
        if (checkCoordinates != null)
            return checkCoordinates;
        if(stanceMatcher==null)
            return "Please enter units stance!";
        int row = Integer.parseInt(rowMatcher.group("row"));
        int column = Integer.parseInt(columnMatcher.group("column"));
        String stance = stanceMatcher.group("stance");
        switch(controller.setStance(row, column, stance)){
            case INVALID_STANCE:
                return "Invalid stance!";
            case INVALID_COORDINATES:
                return "Invalid coordinates!";
            case STANCE_CHANGED_SUCCESSFULLY:
                return "Stance changed successfully!";
            default:
                break;
        }
        return null;
    }

    public String attackEnemy(String input) {
        int row = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_ENEMY).group("row"))-1;
        int column = Integer.parseInt(GameMenuCommands.getMatcher(input, GameMenuCommands.ATTACK_ENEMY).group("column"))-1;
        switch(controller.attackEnemy(row, column)) {
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

    public String areaAttack(String input) {
        return null;
    }

    public String disbandUnit(String input) {
        return null;
    }

    public String buildSiegeWeapon(String input) {
        Matcher rowMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW);
        Matcher columnMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN);
        Matcher typeMatcher = GameMenuCommands.getMatcher(input, GameMenuCommands.TYPE);
        if(rowMatcher == null)
            return "Enter the row number!";
        if(columnMatcher == null)
            return "Enter the column number!";
        if(typeMatcher == null)
            return "Enter the type!";
        int row = Integer.parseInt(rowMatcher.group("row")) - 1;
        int column = Integer.parseInt(columnMatcher.group("column")) - 1;
        String type = typeMatcher.group("type").trim();
        switch(controller.buildSiegeWeapon(type,row,column)){
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