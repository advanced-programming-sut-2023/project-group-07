package controller;

import java.util.HashMap;
import java.util.ArrayList;
import model.Map;
import model.Texture;
import model.Tree;
import model.TypeOfBuilding;
import model.TypeOfPerson;
import model.MapPixel;
import model.Rock;
import model.Government;
import model.LordColor;
import model.Unit;
import model.Building;
import java.io.IOException;

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

    public void setNewMap(int size, String name, int numberOfPlayers, HashMap<LordColor, int[]> lordsKeeps) {
        this.map = new Map(size, name, numberOfPlayers, lordsKeeps);
        this.indexOfMap = Map.getMaps().size();
        this.mapMenuController.refreshMap(this.map);
    }

    public void saveMap() throws IOException{
        Map.changeMaps(map, indexOfMap);
    }

    public Messages setPixelTexture(int row, int column, String textureName) {
        int size = map.getSize();
        Texture texture = Texture.getTexture(textureName.trim());
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_COORDINATES;
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
            return Messages.INVALID_COORDINATES;
        if (x2 < 0 || x2 >= size || y2 < 0 || y2 >= size)
            return Messages.INVALID_COORDINATES;
        if (x1 > x2 || y1 > y2)
            return Messages.INVALID_COORDINATES;
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

    public Messages clearRegion(int x1, int y1, int x2, int y2) {
        int size = map.getSize();
        if (x1 < 0 || x1 >= size || y1 < 0 || y1 >= size)
            return Messages.INVALID_COORDINATES;
        if (x2 < 0 || x2 >= size || y2 < 0 || y2 >= size)
            return Messages.INVALID_COORDINATES;
        if (x1 > x2 || y1 > y2)
            return Messages.INVALID_COORDINATES;
        map.clearRegion(x1, y1, x2, y2);
        return Messages.CLEAR_SUCCESSFUL;
    }

    public Messages clearPixel(int row, int column) {
        int size = map.getSize();
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_COORDINATES;
        map.getMapPixel(row, column).backToDefault();
        return Messages.CLEAR_SUCCESSFUL;
    }

    public boolean canDropKeep(HashMap<LordColor, int[]> keepsPositions, int row, int column) {
        for(int[] position : keepsPositions.values())
            if(position[0] == row && position[1] == column)
                return false;
        return true;
    }

    public Messages dropTree(int row, int column, String treeName) {
        int size = map.getSize();
        Tree tree = Tree.getTree(treeName);
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_COORDINATES;
        if (tree == null)
            return Messages.INVALID_TEXTURE;
        MapPixel pixel = map.getMapPixel(row, column);
        if (!pixel.canDropObject() || !pixel.getTexture().canHaveTree())
            return Messages.CANT_PLACE_THIS;
        pixel.setTree(tree);
        return Messages.DROP_TREE_SUCCESSFUL;
    }

    public Messages dropRock(int row, int column, String direction) {
        int size = map.getSize();
        Rock rock = Rock.getRock(direction);
        if (row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_COORDINATES;
        if (rock == null)
            return Messages.INVALID_DIRECTION;
        MapPixel pixel = map.getMapPixel(row, column);
        if (!pixel.canDropObject())
            return Messages.CANT_PLACE_THIS;
        pixel.setRock(rock);
        return Messages.DROP_ROCK_SUCCESSFUL;
    }

    public void removeMap() {
        Map.removeMap(map);
    }

    public Messages dropUnit(int row, int column, int count, String type, String color) {
        int size = map.getSize();
        LordColor lordColor = LordColor.getColorByName(color);
        TypeOfPerson unitName = TypeOfPerson.getTypeOfPersonFromString(type);
        if(row < 0 || row >= size || column < 0 || column >= size)
            return Messages.INVALID_COORDINATES;
        if(unitName == null)
            return Messages.INVALID_UNIT_NAME;
        if(lordColor == null || !map.doesHaveColor(lordColor))
            return Messages.INVALID_COLOR;
        MapPixel pixel = map.getMapPixel(row, column);
        if(!pixel.getTexture().canDropUnit() || pixel.getRock()!= null)
            return Messages.CANT_PLACE_THIS;
        int[] currentLocation = {row, column};
        while(count-- > 0)
            pixel.addPerson(new Unit(unitName, currentLocation, lordColor));
        return Messages.DROP_UNIT_SUCCESSFUL;

    }

    public Messages dropBuilding (int row, int column, String type, String color){
        int size = map.getSize();
        LordColor lordColor = LordColor.getColorByName(color);
        TypeOfBuilding typeOfBuilding = TypeOfBuilding.getBuilding(type);
        if(typeOfBuilding == null)
            return Messages.INVALID_BUILDING_NAME;
        if(lordColor == null || !map.doesHaveColor(lordColor))
            return Messages.INVALID_COLOR;
        if (row < 0 || row > size - typeOfBuilding.getWidth() + 1 || column < 0
            || column > size - typeOfBuilding.getLength() + 1) {
            return Messages.INVALID_ROW_OR_COLUMN;
        }
        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                if (map.getMapPixel(row + j, column + i).getBuildings().size() != 0)
                    return Messages.THERES_ALREADY_BUILDING;

        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                if (map.getMapPixel(row + j, column + i).getPeople().size() != 0)
                    return Messages.THERES_ALREADY_UNIT;

        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++) {
                if (!map.getMapPixel(row + j, column + i).canDropObject()
                        || !map.getMapPixel(row + j, column + i).getTexture().canDropBuilding())
                    return Messages.CANT_PLACE_THIS;
            }
        if (typeOfBuilding.equals(TypeOfBuilding.APPLE_ORCHARD) ||
                typeOfBuilding.equals(TypeOfBuilding.DIARY_FARMER) ||
                typeOfBuilding.equals(TypeOfBuilding.HOPS_FARMER) ||
                typeOfBuilding.equals(TypeOfBuilding.WHEAT_FARMER)) {
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                for (int j = 0; j < typeOfBuilding.getWidth(); j++) {
                    if (!map.getMapPixel(row + j, column + i).getTexture().equals(Texture.MEADOW))
                        return Messages.CANT_PLACE_THIS;
                }
        }
        int acceptedPixels = 0;
        if (typeOfBuilding.equals(TypeOfBuilding.QUARRY) || typeOfBuilding.equals(TypeOfBuilding.IRON_MINE)
                || typeOfBuilding.equals(TypeOfBuilding.PITCH_RIG)) {
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                    if (!map.getMapPixel(row + j, column + i).getTexture().equals(typeOfBuilding.getTexture()))
                        acceptedPixels++;
            if (acceptedPixels * 4 > typeOfBuilding.getLength() * typeOfBuilding.getWidth())
                return Messages.CANT_PLACE_THIS;
        }

        /*if (typeOfBuilding.equals(TypeOfBuilding.GRANARY) || typeOfBuilding.equals(TypeOfBuilding.STOCK_PILE))
            if (!isAdjacentToSameType(row, column, typeOfBuilding.getLength(), typeOfBuilding))
                return Messages.MUST_BE_ADJACENT_TO_BUILDINGS_OF_THE_SAME_TYPE;*/

        Building building = new Building(lordColor, typeOfBuilding, row, column);
        for (int i = 0; i < typeOfBuilding.getLength(); i++)
            for (int j = 0; j < typeOfBuilding.getWidth(); j++)
                map.getMapPixel(row + j, column + i).addBuilding(building);
        return Messages.DEPLOYMENT_SUCCESSFUL;
    }

    public boolean isAdjacentToSameType(int row, int column, int size, TypeOfBuilding typeOfBuilding) {
        if (row > 0)
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                if (map.getMapPixel(row-1 , column + i).getBuildings().get(0).getTypeOfBuilding()
                        .equals(typeOfBuilding))
                    return true;

        if (row < size - typeOfBuilding.getWidth() + 1)
            for (int i = 0; i < typeOfBuilding.getLength(); i++)
                if (map.getMapPixel(row + typeOfBuilding.getWidth(), column + i).getBuildings().get(0)
                        .getTypeOfBuilding().equals(typeOfBuilding))
                    return true;

        if (column > 0)
            for (int i = 0; i < typeOfBuilding.getWidth(); i++)
                if (map.getMapPixel(row + i, column - 1).getBuildings().get(0).getTypeOfBuilding()
                        .equals(typeOfBuilding))
                    return true;

        if (column < size - typeOfBuilding.getLength() + 1)
            for (int i = 0; i < typeOfBuilding.getWidth(); i++)
                if (map.getMapPixel(row + i, column + typeOfBuilding.getLength()).getBuildings().get(0)
                        .getTypeOfBuilding().equals(typeOfBuilding))
                    return true;

        return false;
    }
    
    // todo : fix this
    // public void addGovernment(int row, int column, int index) {
    // map.getMapPixel(row, column).setPlayerKeep(government);
    // }

}