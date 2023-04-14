package controller;
import model.Map;
import view.Colors;
import java.util.ArrayList;
import model.MapPixel;
import model.TypeOfPixel;
public class MapMenuController {
    private Map map = Controller.currentGame.getMap();
<<<<<<< HEAD
  
    public ArrayList<ArrayList<Colors>> getMapColorList(int x , int y) {
        ArrayList<ArrayList<Colors>> output = new ArrayList<ArrayList<Colors>>();
        int x1 = getCornersRow(x)[0] , x2 = getCornersRow(x)[1];
        int y1 = getCornersColumn(y)[0] , y2 = getCornersColumn(y)[1];
        ArrayList<ArrayList<MapPixel>> field = map.getField(x1 , y1 , x2 , y2);
        for(int i = 0; i < field.size(); i++) {
            output.add(new ArrayList<Colors>());
            for(int j = 0; j < field.get(i).size(); j++)
                output.get(i).add(TypeOfPixel.getColor(field.get(i).get(j).getTypeOfPixel()));
        }
        return output;
    }
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
    private int[] getCornersRow(int x){
        int[] output = new int[2];
        if(x >= map.getSize() - 1) {
            output[1] = map.getSize() - 1;
            output[0] = output[1] - 4;
        }
        else if(x <= 2){
            output[0] = 0;
            output[1] = 4;
        }
        else {
            output[0] = x-2;
            output[1] = x+2;
        }
        return output;
    }
    private int[] getCornersColumn(int y){
        int[] output = new int[2];
        if(y >= map.getSize() - 6) {
            output[1] = map.getSize() - 1;
            output[0] = output[1] - 14;
        }
        else if(y <= 7){
            output[0] = 0;
            output[1] = 14;
        }
        else {
            output[0] = y-7;
            output[1] = y+7;
        }
        return output;
    }
<<<<<<< HEAD
    public boolean checkMoveCordinates(int x , int y , int up , int down , int left , int right){
=======
    public ArrayList<ArrayList<Colors>> getMapColorList(int x , int y) {
        ArrayList<ArrayList<Colors>> output = new ArrayList<ArrayList<Colors>>();
        int x1 = getCornersRow(x)[0] , x2 = getCornersRow(x)[1];
        int y1 = getCornersColumn(y)[0] , y2 = getCornersColumn(y)[1];
        ArrayList<ArrayList<MapPixel>> field = map.getField(x1 , y1 , x2 , y2);
        for(int i = 0; i < field.size(); i++) {
            output.add(new ArrayList<Colors>());
            for(int j = 0; j < field.get(i).size(); j++)
                output.get(i).add(TypeOfPixel.getColor(field.get(i).get(j).getTypeOfPixel()));
        }
        return output;
    }
    public boolean checkCordinates(int x , int y , int up , int down , int left , int right){
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
        if(up > 0 || down < 0 || left > 0 || right < 0) return false;
        if(x + up < 0 || x + down >= map.getSize())return false;
        if(y + left < 0 || y + right >= map.getSize())return false;
        return true;
    }
<<<<<<< HEAD
    public boolean checkCordinates(int row , int column){
        if(row < 0 || row >= map.getSize())return false;
        if(column < 0 || column >= map.getSize())return false;
        return true;
    }
    public String getDetails(int row , int column){
        return null;
    }
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
}