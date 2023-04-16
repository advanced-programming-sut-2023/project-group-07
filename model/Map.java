package model;

import java.util.*;

public class Map {
    private static ArrayList<Map> maps = new ArrayList<Map>();
    private static int maxPlayerOfMaps = 2;


    private final int size;
    private ArrayList<ArrayList<MapPixel>> field = new ArrayList<ArrayList<MapPixel>>();
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private final String name;
    private final int numberOfPlayers;


    public Map(int size, String name, int numberOfPlayers) {
        this.size = size;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        if (numberOfPlayers > maxPlayerOfMaps) maxPlayerOfMaps = numberOfPlayers;
        buildMap();
    }

    public static ArrayList<String> getNamesOfMaps() {
        ArrayList<String> out = new ArrayList<String>();
        for (Map map : maps) out.add(map.getName());
        return out;
    }

    public static Map getMapByName(String mapName) {
        for (Map map : maps) {
            if (map.name.equals(mapName)) return map;
        }
        return null;
    }

    public static ArrayList<Map> getMaps() {
        return (ArrayList<Map>) maps.clone();
    }

    public static int maxPlayerOfMaps() {
        return maxPlayerOfMaps;
    }


    private void buildMap() {
        for (int i = 0; i < size; i++) {
            field.add(new ArrayList<MapPixel>());
            for (int j = 0; j < size; j++)
                field.get(i).add(new MapPixel(Texture.LAND, false, true));
        }
    }

    public ArrayList<ArrayList<MapPixel>> getField(int x1, int y1, int x2, int y2) {
        ArrayList<ArrayList<MapPixel>> output = new ArrayList<ArrayList<MapPixel>>();
        for (int i = x1; i <= x2; i++) {
            output.add(new ArrayList<MapPixel>());
            for (int j = y1; j <= y2; j++) output.get(i - x1).add(this.field.get(i).get(j));
        }
        return output;
    }

    public int getSize() {
        return this.size;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public MapPixel getMapPixel(int row, int column) {
        return field.get(row).get(column);
    }

    public boolean checkCordinates(int row, int column) {
        if (row < 0 || row > this.size || column < 0 || column > this.size) return false;
        return true;
    }

    public String getName() {
        return this.name;
    }

    public void setPixelTexture(int row, int column, Texture texture) {
        this.field.get(row).get(column).setTexture(texture);
    }

    public void setRegionTexture(int x1, int y1, int x2, int y2, Texture texture) {
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++)
                this.field.get(i).get(j).setTexture(texture);
        }
    }

    public void clearPixel(int row, int column) {
        this.field.get(row).get(column).backToDefault();
    }


}