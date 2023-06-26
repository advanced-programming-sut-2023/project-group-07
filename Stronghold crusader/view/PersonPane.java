package view;

import controller.GameMenuController;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Person;
import model.Unit;
import model.UnitTypes;

public class PersonPane extends StackPane {
    private ImageView personImage;
    private ProgressBar healthBar;
    private Person person;
    private boolean isMoving =false;

    public PersonDirection getPersonDirection() {
        return personDirection;
    }

    private PersonDirection personDirection;

    public PersonMove getPersonMove() {
        return personMove;
    }

    private PersonMove personMove;
    private GameMenuController gameMenuController;

    public PersonPane(String name,Person person,GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
        personDirection = new PersonDirection(GameGraphics.class.getResource("/Images/Game/Soldiers/"+name+"/").toExternalForm(),this,"left");
        personMove = new PersonMove(this,gameMenuController);
        this.person = person;
        personImage = new ImageView(new Image(GameGraphics.class.getResource("/Images/Game/Soldiers/"+name+"/down/anim1.png").toExternalForm()));
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        Double a=0.0;
        if(UnitTypes.getUnitTypeFromString(name)!=null){
            healthBar = new ProgressBar(1);
            healthBar.setMaxWidth(25);
            healthBar.setMaxHeight(5);
            healthBar.getStylesheets().add(getClass().getResource("/CSS/HealthBar.css").toString());
            vBox.getChildren().addAll(healthBar,personImage);
        }
        else {
            vBox.getChildren().add(personImage);
        }
        this.getChildren().add(vBox);
        this.setLayoutX(person.getCurrentLocation()[1]*40);
        this.setLayoutY(person.getCurrentLocation()[0]*40);
        personImage.setFitWidth(25);
        personImage.setPreserveRatio(true);
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }

    public ImageView getPersonImage() {
        return personImage;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
