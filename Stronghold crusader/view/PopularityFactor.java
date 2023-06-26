package view;

import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PopularityFactor {
    private final String name;
    private Integer amount;
    private HBox hBox = null;

    public PopularityFactor(String name, Integer amount){
        this.name = name;
        this.amount = amount;
    }
    public HBox getHBox(){
        if (hBox == null) makeHBox();
        return hBox;
    }

    private void makeHBox() {
        hBox = new HBox();
        hBox.setSpacing(20);
        hBox.getChildren().add(new Text(amount.toString()));
        hBox.getChildren().add(getMask());
        hBox.getChildren().add(new Text(name));
    }

    private Rectangle getMask() {
        Rectangle mask = new Rectangle(20, 20); // TODO: 6/26/2023 add masks
        mask.setFill(new ImagePattern(getMaskImage()));
        return mask;
    }

    private Image getMaskImage() {
        String fileName = "poker";
        if (amount > 0) fileName = "happy";
        else if (amount < 0) fileName = "sad";
        return new Image(PopularityFactor.class.getResource("/Images/Game/Menu/impression masks/"+fileName+".png").toExternalForm());
    }

    public void setAmount(Integer amount){
        this.amount = amount;
        ((Text) getHBox().getChildren().get(0)).setText(amount.toString());
        getHBox().getChildren().set(1,getMask());
    }

}
