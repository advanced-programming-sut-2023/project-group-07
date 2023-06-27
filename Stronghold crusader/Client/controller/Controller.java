package Client.controller;

import java.awt.*;
import java.io.File;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;

import Client.view.LoginMenuGraphics;
import Client.view.Main;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import Client.model.*;
import Client.model.Map;
import Client.view.GameGraphics;
import Client.view.MenuPrinter;
import javafx.scene.text.Text;

public class Controller {
    public static User currentUser = User.getUsers().get(0); // TODO: 6/8/2023
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

    public static ArrayList<int[]> getPathForTunneler(int x, int y, Government owner) {
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
    }

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

    public static DropShadow getBorderGlow(Color color, double height) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(color);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(30);
        return borderGlow;
    }

    public static Alert information(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }

    public static Alert error(String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }

    public static String giveARandomAvatar() {
        try {
            String path = GameGraphics.class.getResource("/Images/Avatars").toString();
            path = path.replaceAll("%20", " ");
            path = path.substring("file:".length());
            File file2 = new File(path);
            File[] files = file2.listFiles();
            System.out.println(files);
            ArrayList<String> defaultAvatarsNames = new ArrayList<>();
            for (File file : files) {
                if (file.getName().matches("\\d+\\.[^.]+")) defaultAvatarsNames.add(file.getName()); //default avatars names contains just number
            }
            return defaultAvatarsNames.get(randomNumber(defaultAvatarsNames.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static VBox createChatMenu(Pane pane) {
        ScrollPane scrollPane = new ScrollPane();
        Button button = new Button("Send");
        button.getStylesheets().add(Controller.class.getResource("/CSS/SignUp.css").toExternalForm());
        button.setScaleX(0.75);
        button.setScaleY(0.75);
        scrollPane.setPrefSize(300,600);
        VBox vBox = new VBox(5);
        vBox.setPrefWidth(300);
        vBox.setMinHeight(600);
        for(int i=0;i<100;i++)
            vBox.getChildren().add(new Text("KIR"+i));
        vBox.setStyle("-fx-background-color: linear-gradient(to top,#a07759,#ca9964,rgb(75,216,137) );");
        vBox.getChildren().add(new Text(""));
        scrollPane.setContent(vBox);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVvalue(1);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        VBox chatBox = new VBox(5);
        pane.getChildren().addAll(chatBox);
        TextField textField = new TextField();
        textField.setPromptText("Enter Text");
        textField.setPrefWidth(230);
        hBox.getChildren().addAll(textField,button);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(textField.getText().length()>0){
                    vBox.getChildren().add(vBox.getChildren().size()-1,new Text(textField.getText()));
                    textField.setText("");
                    scrollPane.setVvalue(1);
                }
            }
        });
        ImageView imageView = new ImageView(new Image(Controller.class.getResource("/Images/Game/chatButton.png").toExternalForm()));
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);
        pane.getChildren().add(imageView);
        imageView.setLayoutX(Main.getScreenWidth()-70);
        imageView.setLayoutY(650);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                chatBox.setVisible(!chatBox.isVisible());
            }
        });
        chatBox.getChildren().addAll(scrollPane,hBox);
        chatBox.setLayoutX(Main.getScreenWidth()-300);
        return chatBox;
    }

}
