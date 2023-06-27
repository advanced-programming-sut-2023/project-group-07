package view;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Resources;

public class ShoppingPage {
    private Resources resource;
    private HBox menu;
    private Rectangle image;
    private HBox buyingPrice;
    private Text buyingPriceNumber;
    private HBox sellingPrice;
    private Text sellingPriceNumber;
    private Button buyingButton;
    private Button sellingButton;
    private Text itemAmountText ;

    public ShoppingPage(Resources resource) {
        this.resource = resource;

        initializeMenu();
        setImage(resource);
        settingPrices(resource);
        setButtons();
        setItemAmounts();
    }

    private void setItemAmounts() {
        itemAmountText = new Text("0");
        menu.getChildren().add(itemAmountText);
    }

    private void setButtons() {
        VBox buttonsVBox = new VBox();
        buttonsVBox.setSpacing(20);
        buyingButton = new Button();
        buyingButton.setText("buy");
        sellingButton = new Button();
        sellingButton.setText("sell");
        buttonsVBox.getChildren().add(buyingButton);
        buttonsVBox.getChildren().add(sellingButton);
        menu.getChildren().add(buttonsVBox);
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

    private void settingPrices(Resources resource) {
        VBox pricesVBox = new VBox();
        pricesVBox.setSpacing(20);
        buyingPrice = new HBox();
        buyingPriceNumber = new Text(resource.getBuyingPrice() + "");
        buyingPrice.getChildren().add(new Text("buying price : \t"));
        buyingPrice.getChildren().add(buyingPriceNumber);
        sellingPrice = new HBox();
        sellingPriceNumber = new Text(resource.getSellingPrice() + "");
        sellingPrice.getChildren().add(new Text("selling price : \t"));
        sellingPrice.getChildren().add(sellingPriceNumber);
        pricesVBox.getChildren().add(buyingPrice);
        pricesVBox.getChildren().add(sellingPrice);
        menu.getChildren().add(pricesVBox);
    }

    private void updatePrices() {
        buyingPriceNumber.setText(resource.getBuyingPrice() + "");
        sellingPriceNumber.setText(resource.getSellingPrice() + "");
    }

    private void updateImage() {
        image.setFill(new ImagePattern(new Image(ShoppingPage.class.getResource(
                "/Images/Game/market/items/" + resource.toString() + ".png").toExternalForm())));
    }

    public void setResource(Resources resource) {
        this.resource = resource;
        updateImage();
        updatePrices();
    }

    public HBox menu() {
        return menu;
    }

    public Resources resource() {
        return resource;
    }

    public Button buyingButton() {
        return buyingButton;
    }

    public Button sellingButton() {
        return sellingButton;
    }

    public void setItemAmount(Integer amount){
        itemAmountText.setText(amount.toString());
    }
}
