package model;

import controller.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Government {
    private final LordColor color;
    private final int column;
    private final int row;
    private Unit lord;
    private int population;
    private int peasant;
    private int popularity;
    private User user;
    private int gold;
    private int taxRate;
    private int fearRate;
    private int foodRate;
    private int foodsNumber;
    private HashMap<Resources, Integer> resources = new HashMap<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<Person> people = new ArrayList<>();
    private HashSet<Building> buildingsWaitingForWorkers = new HashSet<>();
    private HashSet<TypeOfBuilding> noLaborBuildings = new HashSet<>();
    private int numberOfBlessed = 0;
    private int defeatedLords = 0;
    private Government defeatedBy = null;
    private int score;

    public Government(LordColor color, User user, int gold, int row, int column) {
        population = 10;
        popularity = 100;
        this.user = user;
        this.gold = gold;
        peasant = 10;
        this.row = row;
        this.column = column;
        this.color = color;
        for (Resources resource : Resources.values())
            resources.put(resource, 0);
        resources.put(Resources.STONE,50);
        resources.put(Resources.WOOD,100);
        resources.put(Resources.BREAD,100);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDefeatedBy(Government defeatedBy) {
        this.defeatedBy = defeatedBy;
    }

    public Government getDefeatedBy() {
        return defeatedBy;
    }

    public int getScore() {
        return score;
    }

    public void defeatLord(int gold){
        defeatedLords++;
        this.gold += gold;
    }

    public int getDefeatedLords() {
        return defeatedLords;
    }

    public int getLordHp(){
        return lord.getHp();
    }

    public void setLord(Unit lord) {
        this.lord = lord;
    }

    public LordColor getColor() {
        return color;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void resetMovesLeft() {
        for(Person person: people){
            if(person instanceof Unit)
                person.movesLeft=((Unit)person).type.getSpeed();
            if(person instanceof NonMilitary)
                person.movesLeft=((NonMilitary)person).type.getSpeed();
        }
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

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void changeGold(int amount) {
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

    public int getTaxRate() {
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
            if (building.getTypeOfBuilding() == TypeOfBuilding.CATHEDRAL)
                number++;
        }
        return number;
    }

    public int numberOfChurches() {
        int number = 0;
        for (Building building : buildings) {
            if (building.getTypeOfBuilding() == TypeOfBuilding.CHURCH)
                number++;
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
        for (Building building : buildings) {
            if (building.getTypeOfBuilding() == TypeOfBuilding.INN)
                // change += building.getPopularityEffect();
                ; // todo : make inn class and set popularity effect
        }
        return change; // todo
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
