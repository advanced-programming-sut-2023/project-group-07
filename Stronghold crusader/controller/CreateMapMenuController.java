package controller;

import java.util.ArrayList;
import model.Map;
import model.Texture;
import model.Tree;
import model.MapPixel;
import model.Rock;

public class CreateMapMenuController {
    private Map map;
    private final MapMenuController mapMenuController;
    private int indexOfMap;

    public CreateMapMenuController(MapMenuController mapMenuController) {
        this.mapMenuController = mapMenuController;
    }

    public ArrayList<String> getMaps() {
        ArrayList<String> out = new ArrayList<String>();
        for (Map map : Map.getMaps())
            out.add(map.getName());
        return out;
    }

    public void setExistingMap(int index) {
        this.map = Map.getMaps().get(index);
        this.indexOfMap = index;
        this.mapMenuController.refreshMap(this.map);
    }

    public void setNewMap(int size, String name, int numberOfPlayers, ArrayList<int[]> lordsPositions) {
        this.map = new Map(size, name, numberOfPlayers, lordsPositions);
        this.indexOfMap = Map.getMaps().size();
        this.mapMenuController.refreshMap(this.map);
    }

    public void saveMap() {
        Map.changeMaps(map, indexOfMap);
    }

    public Messages setPixelTexture(int row, int column, String textureName) {
        int size = map.getSize();
        Texture texture = Texture.getTexture(textureName.trim());
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_CORDINATES;
        if (texture == null)
            return Messages.INVALID_TEXTURE;
        if (map.getMapPixel(row, column).getBuildings().size() > 0)
            return Messages.BUILDING_EXIST;
        map.getMapPixel(row, column).setTexture(texture);
        if (texture.equals(Texture.LARGE_POND))
            map.getMapPixel(row, (column > 0) ? column - 1 : column + 1).setTexture(texture);
        return Messages.SET_TEXTURE_SUCCESSFUL;
    }

    public Messages setRegionTexture(int x1, int y1, int x2, int y2, String textureName) {
        int size = map.getSize();
        Texture texture = Texture.getTexture(textureName.trim());
        if (x1 < 0 || x1 >= size || y1 < 0 || y1 >= size)
            return Messages.INVALID_CORDINATES;
        if (x2 < 0 || x2 >= size || y2 < 0 || y2 >= size)
            return Messages.INVALID_CORDINATES;
        if (x1 > x2 || y1 > y2)
            return Messages.INVALID_CORDINATES;
        if (texture == null)
            return Messages.INVALID_TEXTURE;
        if (texture == Texture.SMALL_POND || texture == Texture.LARGE_POND)
            return Messages.INVALID_SET_POND;
        ArrayList<ArrayList<MapPixel>> region = map.getField(x1, y1, x2, y2);
        for (ArrayList<MapPixel> row : region)
            for (MapPixel pixel : row)
                if (pixel.getBuildings().size() > 0)
                    return Messages.BUILDING_EXIST;
        map.setRegionTexture(x1, y1, x2, y2, texture);
        return Messages.SET_TEXTURE_SUCCESSFUL;
    }

    public Messages clearPixel(int row, int column) {
        int size = map.getSize();
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_CORDINATES;
        map.getMapPixel(row, column).backToDefault();
        return Messages.CLEAR_PIXEL_SUCCESSFUL;
    }

    public boolean doesCordinatesExist(ArrayList<int[]> positions, int row, int column) {
        for (int[] cordinates : positions)
            if (cordinates[0] == row && cordinates[1] == column)
                return true;
        return false;
    }

    public Messages dropTree(int row, int column, String treeName) {
        int size = map.getSize();
        Tree tree = Tree.getTree(treeName);
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_CORDINATES;
        if (tree == null)
            return Messages.INVALID_TEXTURE;
        MapPixel pixel = map.getMapPixel(row, column);
        if (!pixel.canDropObject() || !pixel.getTexture().canHaveTree())
            return Messages.CANT_PLACE_THIS;
        pixel.addTree(tree);
        return Messages.DROP_TREE_SUCCESSFUL;
    }

    public Messages dropRock(int row, int column, String direction) {
        int size = map.getSize();
        Rock rock = Rock.getRock(direction);
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_CORDINATES;
        if (rock == null)
            return Messages.INVALID_DIRECTION;
        MapPixel pixel = map.getMapPixel(row, column);
        if (!pixel.canDropObject())
            return Messages.CANT_PLACE_THIS;
        pixel.setRock(rock);
        return Messages.DROP_ROCK_SUCCESSFUL;
    }

    public void removeMap(){
        Map.removeMap(map);
    }

}