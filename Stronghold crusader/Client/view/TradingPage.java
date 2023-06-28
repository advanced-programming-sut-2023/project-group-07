package Client.view;

import Client.model.Resources;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TradingPage {
    private Resources resource;
    private HBox menu;
    private Rectangle image;
    private Text itemAmountText ;
    public TradingPage(Resources resource) {
        this.resource = resource;

        initializeMenu();
        setImage(resource);
//        setButtons();
        // TODO: 6/28/2023 set buttons
        setItemAmounts();
//        addButtonsToMenu();
        // TODO: 6/28/2023 add + and - buttons
    }
    private void setItemAmounts() {
        itemAmountText = new Text("0");
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
    }

    public HBox menu() {
        return menu;
    }

    public Resources resource() {
        return resource;
    }
    public void setItemAmount(Integer amount){
        itemAmountText.setText(amount.toString());
    }

}
