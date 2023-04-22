package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

import controller.Controller;
import controller.GameMenuCommands;

import controller.GameMenuController;
import controller.Messages;
import model.Game;
import model.Government;
import model.Resources;

public class GameMenu {
    private GameMenuController controller;
    private Shop shop;
    private TradeMenu tradeMenu;
    private Scanner scanner;

    public void run(Scanner scanner) {
        controller = new GameMenuController(Controller.currentGame, Controller.currentUser);
        shop = new Shop(controller);
        tradeMenu = new TradeMenu();
        this.scanner = scanner;
        Matcher matcher;
        while (true) {
            String input = scanner.nextLine();
            if (GameMenuCommands.getMatcher(input, GameMenuCommands.DROP_BUILDING) != null)
                System.out.println(dropBuilding(input));
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.SELECT_BUILDING) != null)
                System.out.println(selectBuilding(input));
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
            else if (GameMenuCommands.getMatcher(input, GameMenuCommands.ENTER_TRADE_MENU) != null){
                System.out.println("You have entered");
            }
            else
            System.out.println("Invalid command!");
        }

    }

    private String getPopularityFactors() { // todo : check this 
        ArrayList<Integer> factorsInOrder = controller.getPopularityFactors();
        return "Popularity factors:\n" +
                "Food : " + factorsInOrder.get(0) + "\n" +
                "Tax : " + factorsInOrder.get(1) + "\n" +
                "Religion : " + factorsInOrder.get(2) + "\n" +
                "Fear factor : " + factorsInOrder.get(3) + "\n" +
                "Effect of buildings : " + factorsInOrder.get(4)
                ;
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
        String row = GameMenuCommands.getMatcher(input, GameMenuCommands.ROW).group("row");
        String column = GameMenuCommands.getMatcher(input, GameMenuCommands.COLUMN).group("column");
        String type = GameMenuCommands.getMatcher(input, GameMenuCommands.TYPE).group("type");
        Messages returnMessage = controller.dropBuilding(row, column, type);
        switch (returnMessage) {
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
                Messages returnMessage = controller.createUnit(command);
                switch (returnMessage) {
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
                System.out.println(controller.changeWorkingState());
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
                System.out.println(controller.changeWorkingState());
            } else if (GameMenuCommands.getMatcher(command, GameMenuCommands.EXIT) != null) {
                System.out.println("Exit was successful!");
                break;
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    public void market() {

    }

}