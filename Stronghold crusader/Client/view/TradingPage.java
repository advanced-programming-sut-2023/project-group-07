package Client.view;

import Client.controller.TradeMenuController;
import Client.model.LordColor;
import Client.model.Resources;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TradingPage {
    private TradeMenuController tradeMenuController = new TradeMenuController();
    private Resources resource;
    private HBox menu;
    private Rectangle image;
    private Text itemAmountText;
    private Text tradingAmountText;
    private Integer tradingAmount;
    private TextField messageTextField;
    private TextField goldAmountTextField;
    private Button requestButton;
    private ComboBox<LordColor> usersComboBox;


    public TradingPage(Resources resource) {
        this.resource = resource;

        initializeMenu();
        setImage(resource);
        setActions();
        setItemAmountTexts();
    }

    private void setActions() {
        addTradingAmountNodes();
        setTextFields();
        setRequestButton();
    }

    private void setRequestButton() {
        VBox lastVBox = setChoosingUser();
        requestButton = new Button("request");
        lastVBox.getChildren().add(requestButton);
    }

    private VBox setChoosingUser() {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        usersComboBox = new ComboBox<>();
        usersComboBox.getItems().addAll(tradeMenuController.getUserColors());
        vBox.getChildren().add(usersComboBox);
        menu().getChildren().add(vBox);
        return vBox;
    }

    private void addTradingAmountNodes() {
        VBox vBox = new VBox();
        menu().getChildren().add(vBox);
        vBox.setSpacing(20);
        tradingAmount = 0;
        tradingAmountText = new Text("0");
        vBox.getChildren().add(new HBox(new Text("trading amount\t"), tradingAmountText));
        setAddAmountButton(vBox);
        setMinusAmountButton(vBox);
    }

    private void setTextFields() {
        messageTextField = new TextField();
        messageTextField.setPromptText("write your message here");
        goldAmountTextField = new TextField();
        goldAmountTextField.setPromptText("enter gold amount");
        goldAmountTextField.textProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal.matches("\\d*"))
                return;
            if (!newVal.matches("\\d*") && oldVal.matches("\\d*"))
                goldAmountTextField.setText(oldVal);
            else
                goldAmountTextField.setText("0");

        });
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.getChildren().add(messageTextField);
        vBox.getChildren().add(goldAmountTextField);
        menu().getChildren().add(vBox);
    }

    private void setMinusAmountButton(VBox vBox) {
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

    private void setAddAmountButton(VBox vBox) {
        Button addButton = new Button();
        addButton.setText("+");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                increaseTradingAmount();
            }
        });
        vBox.getChildren().add(addButton);
    }

    private void decreaseTradingAmount() {
        if (tradingAmount <= 0) tradingAmount = 0;
        else tradingAmount--;
        updateTradingAmount();
    }

    private void increaseTradingAmount() {
        tradingAmount++;
        updateTradingAmount();
    }

    private void updateTradingAmount() {
        tradingAmountText.setText(tradingAmount.toString());
    }

    private void setItemAmountTexts() {
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
        menu.setTranslateX(10);
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

    public Button requestButton() {
        return requestButton;
    }

    public LordColor lordColor() {
        return usersComboBox.getValue();
    }

    public String message() {
        return messageTextField.getText();
    }

    public Integer tradingAmount() {
        return tradingAmount;
    }

    public int goldAmount() {
        if (goldAmountTextField.getText().matches("\\d+")) return Integer.parseInt(goldAmountTextField.getText());
        else return 0;
    }
}
