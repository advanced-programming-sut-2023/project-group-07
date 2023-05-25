package view;

import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MenuFadeTransition extends Transition {
    private Node node;
    private Pane pane;
    private boolean rightDirection;
    private double centerX;
    private boolean isFading=false;
    private int fadeIn;
    private int fadeOut;

    public MenuFadeTransition(Pane pane, Node node, boolean rightDirection, double centerX, int fadeIn, int fadeOut){
        this.node=node;
        this.pane = pane;
        this.rightDirection = rightDirection;
        this.centerX = centerX;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        setCycleDuration(Duration.INDEFINITE);
        setCycleCount(-1);
    }
    @Override
    protected void interpolate(double v) {
        move();
        if(!isFading){
            fade(fadeIn,true);
            fade(fadeOut,false);
            ((HBox)node).getChildren().get(fadeIn).setVisible(true);
            isFading=true;
        }
    }
    private void move() {
        if(!rightDirection && node.getBoundsInParent().getCenterX()>= centerX-400)
            node.setLayoutX(node.getLayoutX()-5);
        if((!rightDirection && node.getBoundsInParent().getCenterX()< centerX-400) || (rightDirection && node.getBoundsInParent().getCenterX()> centerX+400)){
            this.stop();
            ((HBox)node).getChildren().get(fadeOut).setVisible(false);
        }
        if(rightDirection && node.getBoundsInParent().getCenterX()<= centerX+400) {
            node.setLayoutX(node.getLayoutX()+5);
        }
    }

    private void fade(int i,boolean fadeIn) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500));
        fadeTransition.setNode(((HBox)node).getChildren().get(i));
        if(!fadeIn){
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
        }
        else {
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
        }
        fadeTransition.play();
    }

    private void setVisibility(HBox hBox,int i) {
        for(Node node : hBox.getChildren())
            node.setVisible(false);
    }
}
