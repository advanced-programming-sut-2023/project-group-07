package model;

public enum MilitaryCampType {
    BARRACKS("barracks"),
    ENGINEERS_GUILD("engineers guild"),
    MERCENARY_POST("mercenary post"),
    CATHEDRAL("cathedral");

    String name;

    private MilitaryCampType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}