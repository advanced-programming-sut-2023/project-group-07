package model;

import java.util.*;

public class Map {
    private static ArrayList<Map> maps = new ArrayList<Map>();
    private static int maxPlayerOfMaps = 2;

    private int size;
    private ArrayList<ArrayList<MapPixel>> field = new ArrayList<ArrayList<MapPixel>>();
    private String name;
    private int numberOfPlayers;
    private ArrayList<int[]> lordsPositions;

    public Map(int size, String name, int numberOfPlayers, ArrayList<int[]> lordsPositions) {
        this.size = size;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.lordsPositions = lordsPositions;
        buildMap();
    }

    public static Map getMapByName(String mapName) {
        for (Map map : maps) {
            if (map.name.equals(mapName))
                return map;
        }
        return null;
    }

    public static ArrayList<Map> getMaps() {
        return (ArrayList<Map>) maps.clone();
    }

    public static void removeMap(Map map) {
        maps.remove(map);
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
            for (int j = y1; j <= y2; j++)
                output.get(i - x1).add(this.field.get(i).get(j));
        }
        return output;
    }

    public int getSize() {
        return this.size;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public MapPixel getMapPixel(int row, int column) {
        return field.get(row).get(column);
    }

    public String getName() {
        return this.name;
    }

    public void clearRegion(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                field.get(i).get(j).backToDefault();
    }

    public void setRegionTexture(int x1, int y1, int x2, int y2, Texture texture) {
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                this.field.get(i).get(j).setTexture(texture);
    }

    public void clearPixel(int row, int column) {
        field.get(row).get(column).backToDefault();
    }

    public void setPixelTexture(int row, int column, Texture texture) {
        field.get(row).get(column).setTexture(texture);;
    }

    public static void changeMaps(Map map, int index) {
        if (index >= maps.size())
            maps.add(map);
        else
            maps.set(index, map);
    }

    public static int getMaxPlayerOfMaps() {
        return maxPlayerOfMaps;
    }

    public ArrayList<int[]> getLordsPositions() {
        return (ArrayList<int[]>) lordsPositions.clone();
    }
}