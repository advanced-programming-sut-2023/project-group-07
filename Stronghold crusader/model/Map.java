package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Map {
    private static ArrayList<Map> maps = new ArrayList<Map>();
    private static JsonArray allMaps = new JsonArray();
    private static int maxPlayerOfMaps = 2;
    private static final int MAX_DISTANCE = 1000;
    private int size;
    private ArrayList<ArrayList<MapPixel>> field = new ArrayList<ArrayList<MapPixel>>();
    private String name;
    private int numberOfPlayers;
    private HashMap<LordColor, int[]> keepsPositions;

    public Map(int size, String name, int numberOfPlayers, HashMap<LordColor, int[]> keepsPositions) {
        this.size = size;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.keepsPositions = keepsPositions;
        buildMap();
        for (LordColor lordColor : keepsPositions.keySet())
            field.get(keepsPositions.get(lordColor)[0]).get(keepsPositions.get(lordColor)[1]).setPlayerKeep(lordColor);
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

    public static void changeMaps(Map map, int index) throws IOException {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(map).getAsJsonObject();
        if (index >= maps.size()) {
            maps.add(map);
            allMaps.add(jsonElement);
        } else {
            maps.set(index, map);
            allMaps.set(index, jsonElement);
        }
        FileWriter file = new FileWriter("Stronghold crusader/DB/Maps");
        file.write(allMaps.toString());
        file.close();
    }

    public static void loadMaps() throws IOException {
        maps.clear();
        FileReader file = new FileReader("Stronghold crusader/DB/Maps");
        Scanner scanner = new Scanner(file);
        if (!scanner.hasNextLine()) {
            scanner.close();
            file.close();
            return;
        }
        String input = scanner.nextLine();
        scanner.close();
        file.close();
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(input, JsonArray.class);
        for (JsonElement jsonElement : jsonArray) {
            maps.add(gson.fromJson(jsonElement, Map.class));
        }
        allMaps = jsonArray;
    }

    public static int getMaxPlayerOfMaps() {
        return maxPlayerOfMaps;
    }

    public int[] getKeepPosition(int index) {
        return keepsPositions.get(LordColor.getLordColor(index));
    }

    public ArrayList<int[]> getAdj(int row,int col){
        ArrayList<int[]> adj = new ArrayList<>();
        if(row>0 && getMapPixel(row-1, col).getBuildings().isEmpty()){
            int[] arr = new int[2];
            arr[0]=row-1;
            arr[1]=col;
            adj.add(arr);
        }
        if(col>0 && getMapPixel(row, col-1).getBuildings().isEmpty()){
            int[] arr = new int[2];
            arr[0]=row;
            arr[1]=col-1;
            adj.add(arr);
        }
        if(row<size-1 && getMapPixel(row+1, col).getBuildings().isEmpty()){
            int[] arr = new int[2];
            arr[0]=row+1;
            arr[1]=col;
            adj.add(arr);
        }
        if(col<size-1 && getMapPixel(row, col+1).getBuildings().isEmpty()){
            int[] arr = new int[2];
            arr[0]=row;
            arr[1]=col+1;
            adj.add(arr);
        }
        return adj;
    }
    public void bfs(int srcRow, int srcCol,ArrayList<int[]>[][] parent) {
        
        int[][] distance = new int[size][size];
        Arrays.fill(distance, MAX_DISTANCE);

        Queue<Integer> queueX = new LinkedList<>();
        Queue<Integer> queueY = new LinkedList<>();
        queueX.offer(srcRow);
        queueY.offer(srcCol);

        parent[srcRow][srcCol].add(new int[]{-1,-1});
        distance[srcRow][srcCol]=0;

        while(!queueX.isEmpty()){
            int X = queueX.poll();
            int Y = queueY.poll();
            for(int[] adj : getAdj(X, Y)){
                int x = adj[0], y = adj[1];
                if(distance[x][y]>distance[X][Y]+1){
                    distance[x][y]=distance[X][Y]+1;
                    queueX.offer(x);
                    queueY.offer(y);
                    parent[x][y].clear();
                    parent[x][y].add(new int[]{X,Y});
                }
                else if(distance[x][y]==distance[X][Y]+1) parent[x][y].add(new int[]{X,Y});
            }
        }
    }

    public boolean findPath(ArrayList<int[]> path,ArrayList<int[]>[][] parent,int[] coordinates){
        if(coordinates[0]==-1) return true;
        int X = coordinates[0], Y = coordinates[1];
        if(parent[X][Y].size()==0){
            return false;
        }
        
        path.add(new int[]{parent[X][Y].get(0)[0],parent[X][Y].get(0)[1]});
        return findPath(path, parent, parent[X][Y].get(0));
    }
}