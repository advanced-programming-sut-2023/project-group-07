package view;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Resources;

public class TradingPage {
    private Resources resource;
    private HBox menu;
    private Rectangle image;
    private Text itemAmountText;
    private Text tradingAmountText;
    private Integer tradingAmount;

    public TradingPage(Resources resource) {
        this.resource = resource;

        initializeMenu();
        setImage(resource);
        setActions();
//        setButtons();
        // TODO: 6/28/2023 set buttons
        setItemAmounts();
//        addButtonsToMenu();
        // TODO: 6/28/2023 add + and - buttons
    }

    private void setActions() {
        VBox vBox = new VBox();
        menu().getChildren().add(vBox);
        vBox.setSpacing(20);
        tradingAmount = 0;
        tradingAmountText = new Text("0");
        vBox.getChildren().add(new HBox(new Text("trading amount\t"), tradingAmountText));

        Button addButton = new Button();
        addButton.setText("+");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                increaseTradingAmount();
            }
        });
        vBox.getChildren().add(addButton);
        Button minusButton = new Button();
        minusButton.setText("-");
        minusButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                decreaseTradingAmount();
            }
        });
        vBox.getChildren().add(minusButton);
    }

    private void decreaseTradingAmount() {
        if (tradingAmount <= 0) return;
        tradingAmount--;
        updateTradingAmount();
    }

    private void increaseTradingAmount() {
        tradingAmount++;
        updateTradingAmount();
    }

    private void updateTradingAmount(){
        tradingAmountText.setText(tradingAmount.toString());
    }

    private void setItemAmounts() {
        itemAmountText = new Text("0");
        menu.getChildren().add(new Text("current amount"));
        menu.getChildren().add(itemAmountText);
    }

    private void setImage(Resources resource) {
        image = new Rectangle(60, 60);
        image.setFill(new ImagePattern(new Image(ShoppingPage.class.getResource(
                "/Images/Game/market/items/" + resource.toString() + ".png").toExternalForm())));
        menu.getChildren().add(image);
    }

    private void initializeMenu() {
        menu = new HBox();
        menu.setSpacing(20);
        menu.setTranslateY(40);
    }

    private void updateImage() {
        image.setFill(new ImagePattern(new Image(ShoppingPage.class.getResource(
                "/Images/Game/market/items/" + resource.toString() + ".png").toExternalForm())));
    }

    public void setResource(Resources resource) {
        this.resource = resource;
        updateImage();
        tradingAmount = 0;
        updateTradingAmount();
    }

    public HBox menu() {
        return menu;
    }

    public Resources resource() {
        return resource;
    }

    public void setItemAmount(Integer amount) {
        itemAmountText.setText(amount.toString());
    }

}
