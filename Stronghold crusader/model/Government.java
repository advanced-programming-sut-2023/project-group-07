package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Government {
    private int population;
    private final int row;
    private final int column;
    private int peasant;
    private int popularity;
    private User user;
    private int gold;
    private double taxAmount;
    private int taxPopularity;
    private int fearRate;
    private int foodRate;
    private int foodsNumber;
    private HashMap<Resources, Integer> resources = new HashMap<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private HashSet<Building> buildingsWaitingForWorkers = new HashSet<>();
    private HashSet<TypeOfBuilding> noLaborBuildings = new HashSet<>();

    public Government(User user, int gold, int row, int column) {
        population = 10;
        popularity = 100; // todo: we can make a variable that show starting population and popularity
        this.user = user;
        this.gold = gold;
        taxAmount = 0;
        taxPopularity = 1;
        peasant = 10;
        this.row = row;
        this.column = column;
    }

    public int getPopulation() {
        return population;
    }

    public int getPopularity() {
        return popularity; // todo: must be calculated
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public User getUser() {
        return user;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public int getTaxPopularity() {
        return taxPopularity;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setTaxPopularity(int taxPopularity) {
        this.taxPopularity = taxPopularity;
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

    public void setResourceAmount(Resources resource , int amount) {
        resources.replace(resource, amount);
    } 

}
