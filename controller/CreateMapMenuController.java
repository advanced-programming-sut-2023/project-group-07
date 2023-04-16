package controller;
import java.util.ArrayList;
import model.Map;
import model.Texture;
public class CreateMapMenuController {
    private Map map;
    private final MapMenuController mapMenuController;
    private int indexOfMap;
    public CreateMapMenuController(MapMenuController mapMenuController){
        this.mapMenuController = mapMenuController;
    }
    public ArrayList<String> getMaps(){
       ArrayList <String> out = new ArrayList<String>();
       for(Map map : Map.getMaps()) out.add(map.getName());
       return out;
    }
    public void setExistingMap(int index){
        Map tmp = new Map(0 , null , 0);
        tmp.clone(Map.getMaps().get(index));
        this.map = tmp;
        this.indexOfMap = index;
        this.mapMenuController.refreshMap(this.map);
    }
    public void setNewMap(int size , String name){
        this.map = new Map(size, name,3);
        this.indexOfMap = Map.getMaps().size();
        this.mapMenuController.refreshMap(this.map);
    }
    public void saveMap(){
        Map savedMap = new Map(0 , null , 0);
        savedMap.clone(this.map);
        Map.changeMaps(savedMap, indexOfMap);
    }
    public Messages setPixelTexture(int row , int column , String textureName){
        int size = this.map.getSize();
        Texture texture = Texture.getTexture(textureName.trim());
        if(row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_CORDINATES;
        if(texture == null)
            return Messages.INVALID_TEXTURE;
        this.map.setPixelTexture(row, column, texture);
        return Messages.SET_TEXTURE_SUCCESSFULL;
    }
    public Messages setRegionTexture(int x1 , int y1 , int x2 , int y2 , String textureName){
        int size = this.map.getSize();
        Texture texture = Texture.getTexture(textureName.trim());
        if(x1 < 0 || x1 >= size || y1 < 0 || y1 >= size)
            return Messages.INVALID_CORDINATES;
        if(x2 < 0 || x2 >= size || y2 < 0 || y2 >= size)
            return Messages.INVALID_CORDINATES;
        if(x1 > x2 || y1 > y2)
            return Messages.INVALID_CORDINATES;
        if(texture == null)
            return Messages.INVALID_TEXTURE;
        this.map.setRegionTexture(x1, y1, x2, y2, texture);
        return Messages.SET_TEXTURE_SUCCESSFULL;
    }
    public Messages clearPixel(int row , int column){
        int size = this.map.getSize();
        if(row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_CORDINATES;
        this.map.clearPixel(row, column); 
        return Messages.CLEAR_PIXEL_SUCCESSFULL;
    }

}