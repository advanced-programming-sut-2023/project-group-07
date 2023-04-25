package model;

import controller.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Government {
    private final LordColor color;
    private final int row;
    private final int column;
    private int population;
    private int peasant;
    private int popularity;
    private User user;
    private double gold;
    private int taxRate;
    private int fearRate;
    private int foodRate;
    private int foodsNumber;
    private HashMap<Resources, Integer> resources = new HashMap<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private HashSet<Building> buildingsWaitingForWorkers = new HashSet<>();
    private HashSet<TypeOfBuilding> noLaborBuildings = new HashSet<>();
    private int numberOfBlessed = 0;

    public Government(LordColor color, User user, double gold, int row, int column) {
        population = 10;
        popularity = 100; // todo: we can make a variable that show starting population and popularity
        this.user = user;
        this.gold = gold;
        peasant = 10;
        this.row = row;
        this.column = column;
        this.color = color;
        for(Resources resource : Resources.values()) resources.put(resource,100); // todo covert 100 to 0 again. It's for test
    }

    public int getPopulation() {
        return population;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public User getUser() {
        return user;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }
    
    public void changeGold(double amount){
        this.gold += amount;
    }

    public double getTaxAmount() {
        int rate = getTaxRate();
        if (rate < 0) {
            return -(1 - (rate + 3) * 0.2);
        } else if (rate == 0) {
            return (0);
        } else {
            return 0.6 + (rate - 1) * 0.2;

        }
    }

    public int taxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    private int getTaxRate() {
        return taxRate;
    }

    public void setFearRate(int fearRate) {
        this.fearRate = fearRate;
    }

    public int getFearRate() {
        return fearRate;
    }

    public int getPeasant() {
        return peasant;
    }

    public LordColor color() {
        return color;
    }

    public void changePeasant(int count) {
        peasant += count;
        if (peasant < 0)
            peasant = 0;
    }

    public HashMap<Resources, Integer> getResources() {
        return resources;
    } // todo: make this clone.

    public void changeResources(Resources resource, int amount) {
        resources.put(resource, resources.get(resource) + amount);
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void removeBuilding(Building building) {
        this.buildings.remove(building);
    }

    public HashSet<Building> getBuildingsWaitingForWorkers() {
        return buildingsWaitingForWorkers;
    }

    public void addBuildingsWaitingForWorkers(Building building) {
        buildingsWaitingForWorkers.add(building);
    }

    public void removeBuildingsWaitingForWorkers(HashSet<Building> buildings) {
        buildingsWaitingForWorkers.removeAll(buildings);
    }

    public HashSet<TypeOfBuilding> getNoLaborBuildings() {
        return noLaborBuildings;
    }

    public void addNoLaborBuildings(TypeOfBuilding typeOfBuilding) {
        noLaborBuildings.add(typeOfBuilding);
    }

    public void removeNoLaborBuildings(TypeOfBuilding typeOfBuilding) {
        noLaborBuildings.remove(typeOfBuilding);
    }

    public int getFoodRate() {
        return foodRate;
    }

    public void setFoodRate(int foodRate) {
        this.foodRate = foodRate;
    }

    public HashMap<Resources, Integer> getFoodList() {
        HashMap<Resources, Integer> foodList = new HashMap<>();
        for (Resources resource : resources.keySet()) {
            if (resource.getType() == TypeOfResource.FOOD)
                foodList.put(resource, resources.get(resource));// todo : can be cleaner

        }
        return foodList;
    }

    public void updateFoodsNumber() {
        foodsNumber = 0;
        for (Resources resource : resources.keySet()) {
            if (resource.getType() == TypeOfResource.FOOD)
                foodsNumber += resources.get(resource);
        }
    }

    public int getFoodsNumber() {
        updateFoodsNumber();
        return foodsNumber;
    }

    public void giveFood() {
        double foodPerPerson = foodPerPerson();
        int foodsToGive = (int) (foodPerPerson * population);
        for (Resources resource : resources.keySet()) {
            if (resource.getType() == TypeOfResource.FOOD) {
                int amountOfResource = resources.get(resource);
                if (amountOfResource >= foodsToGive) {
                    foodsToGive = 0;
                    resources.replace(resource, amountOfResource - foodsToGive);
                } else {
                    foodsToGive -= amountOfResource;
                    resources.replace(resource, 0);
                }
            }
        }
    }

    public double foodPerPerson() {
        return (foodRate + 2) * 0.5;
    }

    public int getResourceAmount(Resources resource) {
        return resources.get(resource);
    }

    public int numberOfCathedrals() {
        int number = 0;
        for (Building building : buildings) {
            if (building.getTypeOfBuilding() == TypeOfBuilding.CATHEDRAL) number++;
        }
        return number;
    }

    public int numberOfChurches() {
        int number = 0;
        for (Building building : buildings) {
            if (building.getTypeOfBuilding() == TypeOfBuilding.CHURCH) number++;
        }
        return number;
    }

    public int getPercentOfBlessed() {
        return numberOfBlessed * 100 / population;
    }

    public int getChangesOnPopularity() {
        int change = getFoodEffectOnPopularity() +
                getTaxEffectOnPopularity() +
                getReligionEffectOnPopularity() +
                getFearEffectOnPopularity() +
                getBuildingsEffectOnPopularity();
        return change;
    }

    public int getBuildingsEffectOnPopularity() {
        int change = 0;
        for (Building building : buildings){
            if (building.getTypeOfBuilding() == TypeOfBuilding.INN)
                //change += building.getPopularityEffect();
                ; // todo : make inn class and set popularity effect
        }
        return change; //todo
    }

    public int getFearEffectOnPopularity() {
        int fearRate = getFearRate();
        return fearRate;
    }

    public int getReligionEffectOnPopularity() {
        int percentOfBlessed = getPercentOfBlessed();
        return (percentOfBlessed / 25) * 2 + numberOfChurches() * 2 + numberOfCathedrals() * 4;
    }

    public int getTaxEffectOnPopularity() {
        int taxRate = getTaxRate();
        if (taxRate <= 0) {
            return 7 - (taxRate + 3) * 2;
        } else {
            if (taxRate < 5)
                return -taxRate * 2;
            else
                return -(taxRate - 2) * 4;
        }
    }

    public int getFoodEffectOnPopularity() {
        return (int) (foodPerPerson() - 1) * 8;
    }
}
