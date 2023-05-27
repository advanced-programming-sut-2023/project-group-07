package controller;

import java.io.File;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;

import model.*;
import model.Map;
import view.MenuPrinter;

public class Controller {
    public static User currentUser = User.getUsers().get(0); // todo remove initializer
    public static Game currentGame;
    public static final MenuPrinter menuPrinter = new MenuPrinter();
    private static Random random;
    private static ArrayList<Integer> captchaNumbers;

    static {
        captchaNumbers = new ArrayList<>();
        captchaNumbers.addAll(List.of(1181, 1381, 1491, 1722, 1959,
                2163, 2177, 2723, 2785, 3541, 3847, 3855,
                3876, 3967, 4185, 4310, 4487, 4578,
                4602, 4681, 4924, 5326, 5463, 5771));
    }

    public static int randomNumber(int max) {
        return getRandom().nextInt(max);
    }

    public static double randomDouble(double min, double max) {
        return getRandom().nextDouble() * (max - min) + min;
    }

    public static char getRandomChar(char c, int max) {
        return (char) (c + randomNumber(max));
    }

    public static String trimmer(String string) {
        if (string.length() == 0)
            return string;
        if (string.charAt(0) == '\"')
            return string.substring(1, string.length() - 1);
        return string;
    }

    public static int getRandomCaptcha() {
        return captchaNumbers.get(randomNumber(captchaNumbers.size()));
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
                if (i * i + j * j > range * range)
                    continue;
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
        boolean isAssassin = (person instanceof Unit && ((Unit) person).getType().equals(UnitTypes.ASSASSIN));
        ArrayList<int[]> path = map().getPathList(currentX, currentY, x, y, isAssassin);
        person.setMovePattern(path);
    }

    public static boolean isThereOpponentWall(int x, int y, Government government) {
        MapPixel pixel = map().getMapPixel(x, y);
        for (Building building : pixel.getBuildings()) {
            TypeOfBuilding type = building.getTypeOfBuilding();
            if (!building.getGovernment().equals(government) && type.getType().equals("wall"))
                return true;

        }
        return false;
    }

    public static boolean isThereOpponentTower(int x, int y, Government government) {
        MapPixel pixel = map().getMapPixel(x, y);
        for (Building building : pixel.getBuildings()) {
            TypeOfBuilding type = building.getTypeOfBuilding();
            if (!building.getGovernment().equals(government) && type.getType().equals("tower"))
                return true;

        }
        return false;
    } // todo : combine these methods

    public static void breakOpponentWall(int x, int y, Government government) {
        MapPixel pixel = map().getMapPixel(x, y);
        for (Building building : pixel.getBuildings()) {
            TypeOfBuilding type = building.getTypeOfBuilding();
            if (!building.getGovernment().equals(government) && type.getType().equals("wall"))
                building.destroy();
        }
    }

    public static void damageOpponentTower(int x, int y, Government government, int damage) {
        MapPixel pixel = map().getMapPixel(x, y);
        for (Building building : pixel.getBuildings()) {
            TypeOfBuilding type = building.getTypeOfBuilding();
            if (!building.getGovernment().equals(government) && type.getType().equals("tower"))
                building.changeHp(-damage);
        }
    }

    public static ArrayList<int[]> getPathForTunneler(int x, int y, Government owner) {// todo : test this
        Building targetBuilding = null;

        for (int range = 0; range < size(); range++) {
            Building buildingInRange = getDefendingOpponentBuildingInRange(x, y, range, owner);
            if (buildingInRange != null) {
                targetBuilding = buildingInRange;
                break;
            }
        }
        if (targetBuilding != null) {
            int targetX = targetBuilding.row(), targetY = targetBuilding.column();
            return map().getStraightPathList(x, y, targetX, targetY);
        }
        return null;
    }

    private static Building getDefendingOpponentBuildingInRange(int x, int y, int range, Government owner) {
        for (int targetX : new int[]{x - range, x + range}) {
            if (targetX < 0 || targetX >= size())
                continue;
            for (int targetY = y - range; targetY <= y + range; targetY++) {
                if (targetY < 0 || targetY >= size())
                    continue;
                Building building = getDefendingOpponentBuildingOnCoordinates(owner, targetX, targetY);
                if (building != null)
                    return building;
            }
        }
        for (int targetY : new int[]{y - range, y + range}) {
            if (targetY < 0 || targetY > size())
                continue;
            for (int targetX = x - range + 1; targetX <= x + range - 1; targetX++) {
                if (targetX < 0 || targetX >= size())
                    continue;
                Building building = getDefendingOpponentBuildingOnCoordinates(owner, targetX, targetY);
                if (building != null)
                    return building;
            }
        }
        return null;
    }// todo : test the method

    private static Building getDefendingOpponentBuildingOnCoordinates(Government owner, int x, int y) {
        for (Building building : map().getMapPixel(x, y).getBuildings()) {
            TypeOfBuilding type = building.getTypeOfBuilding();
            if (!building.getGovernment().equals(owner) &&
                    (type.getType().equals("wall") || type.getType().equals("tower")))
                return building;
        }
        return null;
    }

    public static int size() {
        Map map = map();
        int size = map.getSize();
        return size;
    }

    public static Map map() {
        return currentGame.getMap();
    }

    public static Random getRandom() {
        if (random == null)
            random = new Random();
        return random;
    }

    public static void releaseDogs(int numberOfDogs, int x, int y) {
        currentGame.releaseDogs(numberOfDogs, x, y, currentGame.getCurrentGovernment());
    }

    public static String giveARandomAvatar() {
        try {
            String avatarFolderAddress =
                    Objects.requireNonNull(Controller.class.getResource("/Images/Avatars")).getPath();
            File avatarFolder = new File(avatarFolderAddress);
            File[] files = avatarFolder.listFiles();
            ArrayList<String> defaultAvatarsNames = new ArrayList<>();
            for (File file : files) {
                if (file.getName().matches("\\d+\\.[^.]+")) defaultAvatarsNames.add(file.getName()); //default avatars names contains just number
            }
            return defaultAvatarsNames.get(randomNumber(defaultAvatarsNames.size()));
        } catch (Exception e) {
            return null;
        }
    }
}
