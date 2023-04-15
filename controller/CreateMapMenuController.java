package controller;
import java.util.ArrayList;
import model.Map;
public class CreateMapMenuController {
    private Map map;
    private final MapMenuController mapMenuController;
    private int indexOfMap;
    public CreateMapMenuController(MapMenuController mapMenuController){
        this.mapMenuController = mapMenuController;
    }
    public ArrayList<String> getMaps(){
        ArrayList<String> out = new ArrayList<String>();
        for(Map map : Map.maps)out.add(map.getName());
        return out;
    }
    public void setExistingMap(int index){
        this.map = Map.maps.get(index);
        this.indexOfMap = index;
        refreshMap();
    }
    public void setNewMap(int size , String name){
        this.map = new Map(size, name);
        this.indexOfMap = Map.maps.size();
        refreshMap();
    }
    public void refreshMap(){
        this.mapMenuController.refreshMap(this.map);
    }
    public void saveMap(){
        Map.maps.set(this.indexOfMap, this.map);
    }

}