package model;

public class Government {
    private int population;
    private int popularity;
    private User user;
    private double gold;
    private double taxAmount;
    private int taxPopularity;
<<<<<<< HEAD
    private int fearRate;
    
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af

    public Government(User user,double gold){
        population=10;
        popularity=100;
        this.user=user;
        this.gold=gold;
        taxAmount=0;
        taxPopularity=1;
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
<<<<<<< HEAD
    public void setFearRate(int fearRate) {
        this.fearRate = fearRate;
    }
    public int getFearRate() {
        return fearRate;
    }
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
}
