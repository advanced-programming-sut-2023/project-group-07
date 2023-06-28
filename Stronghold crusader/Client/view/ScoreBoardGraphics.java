package Client.view;

import Client.controller.ScoreBoardController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Client.model.User;

import java.util.ArrayList;

public class ScoreBoardGraphics extends Application {
    private static Stage stage;
    private static Scene scene;
    private static ScoreBoardController controller = null;
    private static final int scoreboardWidth = 480 - 125;
    private static final int scoreboardHeight = 360 - 45;
    private static final int screenWidth = 600;
    private static final int screenHeight = 400;
    private static ScoreBoardController controller() {
        if (controller == null) controller = new ScoreBoardController();
        return controller;
    }

    private GridPane createScoreboard() {
        ArrayList<User> users = controller().sortedUsers();
        GridPane scoresGrid = new GridPane();
        scoresGrid.setAlignment(Pos.CENTER);
        scoresGrid.setHgap(10);
        scoresGrid.setVgap(10);
        scoresGrid.add(new Text("rank"), 0, 0);
        scoresGrid.add(new Text("username"), 1, 0);
        scoresGrid.add(new Text("score"), 2, 0);
        int i = 1;
        for (User user : users) {
            Label playerRankLabel = new Label("" + user.getRank());
            playerRankLabel.getStyleClass().add("label-style");
            scoresGrid.add(playerRankLabel, 0, i);

            Label playerNameLabel = new Label("Player " + user.getUsername());
            playerNameLabel.getStyleClass().add("label-style");
            scoresGrid.add(playerNameLabel, 1, i);

            Label scoreLabel = new Label(user.getHighScore() + "");
            scoreLabel.getStyleClass().add("label-style");
            scoresGrid.add(scoreLabel, 2, i);
            i++;
        }
        return scoresGrid;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        BorderPane root = new BorderPane();
        AnchorPane anchorPane = new AnchorPane();
        root.setCenter(anchorPane);
        GridPane scoreboard = createScoreboard();
        setScoreBoardDetails(scoreboard);
        ScrollPane scrollPane = createScrollPane(scoreboard);
        anchorPane.getChildren().add(scrollPane);
        setBackButton(anchorPane);
        setBackground(anchorPane);
        scene = new Scene(root, screenWidth, screenHeight);
        setTextStyle();
        primaryStage.setScene(scene);
        primaryStage.setTitle("scoreboard");
        primaryStage.show();
    }

    private void setTextStyle() {
        scene.getStylesheets().add(ScoreBoardGraphics.class.getResource("/CSS/Scoreboard.css").toExternalForm());
    }


    private static void setScoreBoardDetails(GridPane scoreboard) {
        scoreboard.setPrefWidth(scoreboardWidth);
        scoreboard.setPrefHeight(scoreboardHeight);
        scoreboard.setStyle("-fx-background-color: rgb(123,97,57);");
    }

    private static ScrollPane createScrollPane(GridPane scoreboard) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(scoreboard);
        scrollPane.setLayoutX(125);
        scrollPane.setLayoutY(45);
        scrollPane.setPrefWidth(scoreboardWidth);
        scrollPane.setPrefHeight(scoreboardHeight);

        scrollPane.setStyle("-fx-border-color: rgb(123,97,57);");
        return scrollPane;
    }

    private void setBackButton(AnchorPane anchorPane) {
        Rectangle backButton = new Rectangle();
        backButton.setLayoutX(50);
        backButton.setLayoutY(350);
        backButton.setWidth(104);
        backButton.setHeight(45);
        backButton.setFill(new ImagePattern(new Image(ScoreBoardGraphics.class.getResource("/Images/Icon/hand pointing left.png").toExternalForm())));
        backButton.setOnMousePressed(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                back();
            }
        });
        anchorPane.getChildren().add(backButton);
    }

    private static void setBackground(AnchorPane anchorPane) {
        Image image = new Image(ScoreBoardGraphics.class.getResource
                ("/Images/Background/scoreboard background.png").toString(),
                screenWidth, screenHeight, false, false);
        Background background = new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT));
        anchorPane.setBackground(background);
    }


    private void back() {
        stage.close();
    }
}
