package model;

import java.util.HashMap;

public class Government {
    private int population;
    private int popularity;
    private User user;
    private double gold;
    private double taxAmount;
    private int taxPopularity;
    private int fearRate;
    private int foodRate;
    private int foodsNumber;
    private HashMap<Resources, Double> resources = new HashMap<>();

    public Government(User user, double gold) {
        population = 10;
        popularity = 100; // todo: we can make a variable that show starting population and popularity
        this.user = user;
        this.gold = gold;
        taxAmount = 0;
        taxPopularity = 1;
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

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
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

    public HashMap<Resources, Double> getResources() {
        return resources;
    } // todo: make this clone.

    public void changeResources(Resources resource, int amount) {
        resources.put(resource, resources.get(resource) + amount);
    }

    public int getFoodRate() {
        return foodRate;
    }

    public void setFoodRate(int foodRate) {
        this.foodRate = foodRate;
    }

    public HashMap<Resources, Double> getFoodList() {
        HashMap<Resources, Double> foodList = new HashMap<>();
        for (Resources resource : resources.keySet()) {
            if (resource.type() == TypeOfResource.FOOD)
                foodList.put(resource, resources.get(resource));// todo : can be cleaner

        }
        return foodList;
    }

    public void updateFoodsNumber() {
        foodsNumber = 0;
        for (Resources resource : resources.keySet()) {
            if (resource.type() == TypeOfResource.FOOD) foodsNumber += resources.get(resource);
        }
    }

    public int getFoodsNumber() {
        updateFoodsNumber();
        return foodsNumber;
    }

    public void giveFood() {
        double foodPerPerson = foodPerPerson();
        double foodsToGive = foodPerPerson * population;
        for (Resources resource : resources.keySet()) {
            if (resource.type() == TypeOfResource.FOOD) {
                double amountOfResource = resources.get(resource);
                if (amountOfResource >= foodsToGive) {
                    foodsToGive = 0;
                    resources.replace(resource, amountOfResource - foodsToGive);
                }
                else{
                    foodsToGive -= amountOfResource;
                    resources.replace(resource, (double)0);
                }
            }
        }
    }

    public double foodPerPerson() {
        return (foodRate + 2) * 0.5;
    }
}
