package controller;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;

import model.*;
import model.Map;
import view.CaptchaPrinter;
import view.MenuPrinter;

public class Controller {
    public static User currentUser;
    public static Game currentGame;
    public static final MenuPrinter menuPrinter = new MenuPrinter();
    private static Random random;

    public static int randomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public static char getRandomChar(char c, int max) {
        return (char) (c + randomNumber(max));
    }

    public static String trimmer(String string) {
        if (string.length() == 0) return string;
        if (string.charAt(0) == '\"')
            return string.substring(1, string.length() - 1);
        return string;
    }

    public static String generateCaptcha(CaptchaPrinter captchaPrinter) {
        String captcha = "", captchaPrint = "";
        int length = randomNumber(5) + 4;
        for (int i = 0; i < length; i++) {
            char toBeAdded = getRandomChar('A', 26);
            captcha += toBeAdded;
            captchaPrint += toBeAdded;
            captchaPrint += ' ';
        }
        captchaPrinter.print(captchaPrint);
        return captcha;
    }

    public static String toSHA256(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(string.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hex = String.format("%064x", new BigInteger(1, digest));
        return hex;
    }

    public static String checkCoordinatesFormat(Matcher rowMatcher, Matcher columnMatcher) {
        if (rowMatcher == null)
            return "Enter the row number!";
        if (columnMatcher == null)
            return "Enter the column number!";
        return null;
    }

    public static String checkRegionCoordinatesFormat(Matcher x1Matcher, Matcher y1Matcher, Matcher x2Matcher,
                                                      Matcher y2Matcher) {
        if (x1Matcher == null)
            return "Enter first row number!";
        if (y1Matcher == null)
            return "Enter first column number!";
        if (x2Matcher == null)
            return "Enter second row number!";
        if (y2Matcher == null)
            return "Enter second column number!";
        return null;
    }


    public static ArrayList<Unit> getNearOpponentsUnits(int x, int y, int range, Government owner) {
        ArrayList<Unit> opponentsUnits = new ArrayList<>();
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                if (i * i + j * j > range * range) continue;
                int xi = x + i, yj = y + j;
                for (Person person : currentGame.getPersonOfAPixel(xi, yj)) {
                    if (person instanceof Unit && !person.getGovernment().equals(owner)) {
                        opponentsUnits.add((Unit) person);
                    }
                }
            }
        }
        return opponentsUnits;
    }

    public static void sendToOilSmelter(Engineer engineer) {
        GameMenuController.sendToOilSmelter(engineer);
    }

    public static void sendToCoordinate(int x, int y, Person person) {
        int currentX = person.getCurrentLocation()[0],
                currentY = person.getCurrentLocation()[1];
        ArrayList<int[]> path = currentGame.getMap().getPathList(currentX, currentY, x, y);
        person.setMovePattern(path);
    }

    public static boolean isThereWall(int x, int y) {
        Map map = currentGame.getMap();
        MapPixel pixel = map.getMapPixel(x, y);
        for (Building building : pixel.getBuildings()) {
            TypeOfBuilding type = building.getTypeOfBuilding();
            if (type.equals(TypeOfBuilding.WALL_STAIRS) ||
                    type.equals(TypeOfBuilding.LOW_WALL) ||
                    type.equals(TypeOfBuilding.CRENELATED_WALL) ||
                    type.equals(TypeOfBuilding.STONE_WALL))
                return true;

        }
        return false;
    }

    public static void breakWall(int x, int y) {
        Map map = currentGame.getMap();
        MapPixel pixel = map.getMapPixel(x, y);
        for (Building building : pixel.getBuildings()) {
            TypeOfBuilding type = building.getTypeOfBuilding();
            if (type.equals(TypeOfBuilding.WALL_STAIRS) ||
                    type.equals(TypeOfBuilding.LOW_WALL) ||
                    type.equals(TypeOfBuilding.CRENELATED_WALL) ||
                    type.equals(TypeOfBuilding.STONE_WALL))
                building.destroy();

        }
    }

    public static ArrayList<int[]> getPathForTunneler(int x, int y, Government owner) {
        //todo : write path for tunneler
        return null;
    }
    public static Random getRandom(){
        if (random == null) random = new Random();
        return random;
    }
}
