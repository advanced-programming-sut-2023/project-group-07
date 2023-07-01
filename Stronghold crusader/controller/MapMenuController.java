package controller;

import model.Map;
import Server.Colors;

import java.util.ArrayList;

import model.MapPixel;
import model.Government;

public class MapMenuController {
    private Map map;

    public ArrayList<ArrayList<Colors>> getMapColorList(int x, int y) {
        ArrayList<ArrayList<Colors>> output = new ArrayList<ArrayList<Colors>>();
        int x1 = getCornersRow(x)[0], x2 = getCornersRow(x)[1];
        int y1 = getCornersColumn(y)[0], y2 = getCornersColumn(y)[1];
        ArrayList<ArrayList<MapPixel>> field = map.getField(x1, y1, x2, y2);
        for (int i = 0; i < field.size(); i++) {
            output.add(new ArrayList<Colors>());
            for (int j = 0; j < field.get(i).size(); j++)
                output.get(i).add(field.get(i).get(j).getTexture().getColor());
        }
        return output;
    }

    public ArrayList<ArrayList<String>> getMapObjects(int x, int y, Government government) {
        ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
        int x1 = getCornersRow(x)[0], x2 = getCornersRow(x)[1];
        int y1 = getCornersColumn(y)[0], y2 = getCornersColumn(y)[1];
        ArrayList<ArrayList<MapPixel>> field = map.getField(x1, y1, x2, y2);
        for (int i = 0; i < field.size(); i++) {
            output.add(new ArrayList<String>());
            for (int j = 0; j < field.get(i).size(); j++)
                output.get(i).add(field.get(i).get(j).objectToShow(government));
        }
        return output;
    }

    private int[] getCornersRow(int x) {
        int[] output = new int[2];
        if (x >= map.getSize() - 3) {
            output[1] = map.getSize() - 1;
            output[0] = output[1] - 4;
        } else if (x <= 2) {
            output[0] = 0;
            output[1] = 4;
        } else {
            output[0] = x - 2;
            output[1] = x + 2;
        }
        return output;
    }

    private int[] getCornersColumn(int y) {
        int[] output = new int[2];
        if (y >= map.getSize() - 8) {
            output[1] = map.getSize() - 1;
            output[0] = output[1] - 14;
        } else if (y <= 7) {
            output[0] = 0;
            output[1] = 14;
        } else {
            output[0] = y - 7;
            output[1] = y + 7;
        }
        return output;
    }

    public boolean checkMoveCoordinates(int x, int y, int up, int down, int left, int right) {
        if (up > 0 || down < 0 || left > 0 || right < 0)
            return false;
        if ((x + up < 2 && up != 0) || (x + down > map.getSize() - 3 && down != 0))
            return false;
        if (x <= 2 && -up > down)
            return false;
        if (x >= map.getSize() - 3 && down > -up)
            return false;
        if ((y + left < 7 && left != 0) || (y + right > map.getSize() - 8 && right != 0))
            return false;
        if (y <= 7 && -left > right)
            return false;
        if (y >= map.getSize() - 8 && right > -left)
            return false;
        return true;
    }

    public boolean checkCordinates(int row, int column) {
        int size = map.getSize();
        if (row < 0 || row >= size || column < 0 || column >= size)
            return false;
        return true;
    }

    public String getDetails(int row, int column) {
        MapPixel pixel = map.getMapPixel(row, column);
        return "Pixel cordinates: " + (row + 1) + " , " + (column + 1) + "\n" + pixel.details();
    }

    public void refreshMap(Map map) {
        this.map = map;
    }

    public int setMoveX(int x, int up, int down) {
        if (x <= 2 && x + up + down <= 2 && down > -up)
            return (2 + up + down);
        if (x >= map.getSize() - 3 && x + up + down >= map.getSize() - 3 && -up > down)
            return (map.getSize() - 3 + up + down);
        return (x + up + down);
    }

    public int setMoveY(int y, int left, int right) {
        if (y <= 7 && y + left + right <= 7 && right > -left)
            return (7 + left + right);
        if (y >= map.getSize() - 8 && y + left + right >= map.getSize() - 8 && -left > right)
            return (map.getSize() - 8 + left + right);
        return (y + left + right);
    }
}