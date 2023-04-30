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

    public static ArrayList<Map> getMaps() {
        return (ArrayList<Map>) maps.clone();
    }

    public static void removeMap(Map map) throws IOException {
        if(maps.contains(map)){ 
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(map).getAsJsonObject();
            maps.remove(map);
            allMaps.remove(jsonElement);
            FileWriter file = new FileWriter("Stronghold crusader/DB/Maps");
            file.write(allMaps.toString());
            file.close();
        }
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
        if(row>0 && getMapPixel(row-1, col).canDropObject() && getMapPixel(row-1,col).getTexture().canDropBuilding()){
            int[] arr = new int[2];
            arr[0]=row-1;
            arr[1]=col;
            adj.add(arr);
        }
        if(col>0 && getMapPixel(row, col-1).canDropObject() && getMapPixel(row,col-1).getTexture().canDropBuilding()){
            int[] arr = new int[2];
            arr[0]=row;
            arr[1]=col-1;
            adj.add(arr);
        }
        if(row<size-1 && getMapPixel(row+1, col).canDropObject() && getMapPixel(row+1,col).getTexture().canDropBuilding()){
            int[] arr = new int[2];
            arr[0]=row+1;
            arr[1]=col;
            adj.add(arr);
        }
        if(col<size-1 && getMapPixel(row, col+1).canDropObject() && getMapPixel(row,col+1).getTexture().canDropBuilding()){
            int[] arr = new int[2];
            arr[0]=row;
            arr[1]=col+1;
            adj.add(arr);
        }
        return adj;
    }
    public void bfs(int srcRow, int srcCol,ArrayList<ArrayList<ArrayList<int[]>>> parent) {
        
        int[][] distance = new int[size][size];
        for(int i=0; i<size; i++)
            for(int j=0; j<size;j++) 
                distance[i][j] = MAX_DISTANCE;

        Queue<Integer> queueX = new LinkedList<>();
        Queue<Integer> queueY = new LinkedList<>();
        queueX.offer(srcRow);
        queueY.offer(srcCol);

        parent.get(srcRow).get(srcCol).add(new int[]{-1,-1});
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
                    parent.get(x).get(y).clear();
                    parent.get(x).get(y).add(new int[]{X,Y});
                }
                else if(distance[x][y]==distance[X][Y]+1) parent.get(x).get(y).add(new int[]{X,Y});
            }
        }
    }

    public void findPath(ArrayList<int[]> path,ArrayList<ArrayList<ArrayList<int[]>>> parent,int[] coordinates){ // todo : isn't this a private method?
        if(coordinates[0]==-1) return;
        int X = coordinates[0], Y = coordinates[1];
        if(parent.get(X).get(Y).size()==0){
            return;
        }
        
        path.add(new int[]{parent.get(X).get(Y).get(0)[0],parent.get(X).get(Y).get(0)[1]});
        findPath(path, parent, parent.get(X).get(Y).get(0));
    }

    public ArrayList<int[]> getPathList(int firstRow,int firstColumn,int secondRow,int secondColumn){
        ArrayList<int[]> path = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<int[]>>> parent = new ArrayList<ArrayList<ArrayList<int[]>>>();
        for (int k = 0; k < size; k++) {
            parent.add(new ArrayList<>());
            for (int t = 0; t < size; t++) {
                parent.get(k).add(new ArrayList<>());
            }
        }
        bfs(firstRow, firstColumn, parent);
        findPath(path, parent, new int[] { secondRow, secondColumn });
        Collections.reverse(path);
        path.add(new int[] { secondRow, secondColumn });
        path.remove(0);
        return path;
    }

    public boolean doesHaveColor(LordColor color){
        return keepsPositions.keySet().contains(color);
    }

    public void startGame(HashMap<LordColor, Government> governments){
        for(ArrayList<MapPixel> row : field){
            for(MapPixel pixel : row){
                for(Building building : pixel.getBuildings()){
                    building.setGovernment(governments.get(building.getLordColor()));
                    governments.get(building.getLordColor()).addBuilding(building);
                }
                for(Person person : pixel.getPeople()){
                    person.setGovernment(governments.get(person.getLordColor()));
                    governments.get(person.getLordColor()).addPerson(person);
                }
            }
        }
    }

    public boolean isAdjacentToSameType(int row, int column, int size, TypeOfBuilding typeOfBuilding) {
        if (row > 0)
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                if (!getMapPixel(row - 1, column + i).getBuildings().isEmpty()
                        && getMapPixel(row - 1, column + i).getBuildings().get(0).getTypeOfBuilding()
                                .equals(typeOfBuilding))
                    return true;

        if (row < getSize() - typeOfBuilding.getWidth() + 1)
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                if (!getMapPixel(row + typeOfBuilding.getWidth(), column + i).getBuildings().isEmpty()
                        && getMapPixel(row + typeOfBuilding.getWidth(), column + i).getBuildings().get(0)
                                .getTypeOfBuilding().equals(typeOfBuilding))
                    return true;

        if (column > 0)
            for (int i = 0; i < typeOfBuilding.getWidth(); i++)
                if (!getMapPixel(row + i, column - 1).getBuildings().isEmpty()
                        && getMapPixel(row + i, column - 1).getBuildings().get(0).getTypeOfBuilding()
                                .equals(typeOfBuilding))
                    return true;

        if (column <getSize() - typeOfBuilding.getLength() + 1)
            for (int i = 0; i < typeOfBuilding.getWidth(); i++)
                if (!getMapPixel(row + i, column + typeOfBuilding.getLength()).getBuildings().isEmpty()
                        && getMapPixel(row + i, column + typeOfBuilding.getLength()).getBuildings().get(0)
                                .getTypeOfBuilding().equals(typeOfBuilding))
                    return true;

        return false;
    }

    public void pourOil(int x, int y) {
        field.get(x).get(y).pourOil();
    }

    public ArrayList<Building> getAllBuildingsOfSomeone(Government owner) {
        ArrayList<Building> buildings = new ArrayList<>();
        for (ArrayList<MapPixel> rowPixels : this.field){
            for (MapPixel pixel : rowPixels){
                for (Building building : pixel.getBuildings()){
                    if (building.getGovernment().equals(owner)) buildings.add(building);
                }
            }
        }
        return buildings;
    }

    public boolean hasABuilding(Government owner, TypeOfBuilding type) {
        for (Building building : getAllBuildingsOfSomeone(owner)){
            if (building.getTypeOfBuilding().equals(type)) return true;
        }
        return false;
    }
}