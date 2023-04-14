package model;
import java.util.*; 

public class Map {
<<<<<<< HEAD
    private int size;
    private ArrayList<ArrayList<MapPixel>> field = new ArrayList<ArrayList<MapPixel>>();
    private ArrayList<Building> buildings = new ArrayList<Building>();
    private void buildMap() {
        for (int i = 0; i < size; i++) {
            field.add(new ArrayList<MapPixel>());
            for (int j = 0; j < size; j++)
                field.get(i).add(new MapPixel(TypeOfPixel.LAND, false, true));
        }
    }
    public Map(int size) {
        this.size = size;
        buildMap();
    }
    public ArrayList<ArrayList<MapPixel>> getField(int x1 , int y1 , int x2 , int y2) {
        ArrayList<ArrayList<MapPixel>> output = new ArrayList<ArrayList<MapPixel>>();
        for(int i = x1 ; i<=x2 ; i++) {
            output.add(new ArrayList<MapPixel>());
            for(int j = y1 ; j<=y2 ; j++)output.get(i-x1).add(this.field.get(i).get(j));
        }
        return output;
    }
    public int getSize(){
        return this.size;
    }
    public void addBuilding(Building building){
        buildings.add(building);
    }
    public MapPixel getMapPixel(int row, int column){
        return field.get(row).get(column);
    }
=======
    private ArrayList<ArrayList<MapPixel>> field = new ArrayList<ArrayList<MapPixel>>();
    public Map() {

    }
    public String showMap(int x , int y){
        String output = "";
        
    }    
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
}