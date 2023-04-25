package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;
import static javafx.geometry.Pos.CENTER;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.flowers.Cart;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.ServiceRequests.flowers.FlowerDelivery;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class FlowerDeliveryController {
  public static int flowerID;
  @FXML MenuButton sizedrop;
  @FXML MFXButton viewcartbutton;
  @FXML MenuItem sizesmall;
  @FXML MenuItem sizenormal;
  @FXML MenuItem sizelarge;
  @FXML MFXButton clearfilter;
  @FXML FlowPane flowpane;
  @FXML VBox cartBox;
  @FXML VBox checkOutBox;
  @FXML VBox cartPane;
  @FXML MFXButton clearCart;
  @FXML MFXButton proceed;
  @FXML Label totalPrice;
  @FXML VBox lowerCart;
  @FXML SearchableComboBox employeedrop;
  @FXML SearchableComboBox locationdrop;
  @FXML MFXTextField requestfield;
  @FXML MFXButton submitButton;
  @FXML MFXButton clearSubmit;

  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  private int cartID = 1;
  public static int flowDevID;
  public static Cart flowerCart;

  public void initialize() {

    flowerCart = new Cart(cartID++);
    flowDevID = dbr.flowerGetNewDeliveryID();
    lowerCart.setVisible(false);

    flowerCart
        .getCartItems()
        .addListener(
            (ListChangeListener<Flower>)
                change -> {
                  displayCart();
                });

    viewcartbutton.setOnMouseClicked(event -> openCart());

    sizesmall.setOnAction(event -> filterSmall());
    sizenormal.setOnAction(event -> filterMedium());
    sizelarge.setOnAction(event -> filterLarge());
    proceed.setOnMouseClicked(event -> checkOutHandler());

    clearfilter.setOnMouseClicked(event -> noFilter());
    clearCart.setOnMouseClicked(event -> clearCart());
    checkOutBox.setVisible(false);

    noFilter();

    submitButton.setDisable(true);

    employeedrop
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitButton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    locationdrop
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitButton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    requestfield
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitButton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    for (User u : dbr.getUserDAO().getListOfUsers().values()) {
      employeedrop.getItems().add(u.getFirstName() + " " + u.getLastName());
    }

    locationdrop.getItems().addAll(dbr.getListOfEligibleRooms());

    clearSubmit.setOnMouseClicked(event -> clearCheckoutFields());

    submitButton.setOnMouseClicked(event -> submitHandler());
  }

  public void filterSmall() {
    flowpane.getChildren().clear();
    for (Flower f : dbr.getFlowerDAO().getListOfSize("small")) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(true);
      view.setFitHeight(80);
      view.setFitWidth(80);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(150);
      btn1.setPrefHeight(100);
      btn1.setStyle(
          "-fx-background-radius:10 10 10 10; -fx-font-size: 10;-fx-effect: dropshadow(three-pass-box, rgba(42,42,38,0.35), 10, 0, 0, 5);");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getID());
          });
    }
  }

  public void filterMedium() {
    flowpane.getChildren().clear();
    for (Flower f : dbr.getFlowerDAO().getListOfSize("medium")) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(true);
      view.setFitHeight(80);
      view.setFitWidth(80);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(150);
      btn1.setPrefHeight(100);
      btn1.setStyle(
          "-fx-background-radius:10 10 10 10; -fx-font-size: 10;-fx-effect: dropshadow(three-pass-box, rgba(42,42,38,0.35), 10, 0, 0, 5);");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getID());
          });
    }
  }

  public void filterLarge() {
    flowpane.getChildren().clear();

    flowpane.getChildren().clear();
    for (Flower f : dbr.getFlowerDAO().getListOfSize("large")) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(true);
      view.setFitHeight(80);
      view.setFitWidth(80);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(160);
      btn1.setPrefHeight(100);
      btn1.setStyle(
          "-fx-background-radius:10 10 10 10; -fx-font-size: 10;-fx-effect: dropshadow(three-pass-box, rgba(42,42,38,0.35), 10, 0, 0, 5);");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getID());
          });
    }
  }

  public void noFilter() {
    flowpane.getChildren().clear();
    for (Flower f : dbr.getFlowerDAO().getFlowers().values()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(false);
      view.setFitHeight(80);
      view.setFitWidth(80);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(175);
      btn1.setPrefHeight(100);
      btn1.setStyle(
          "-fx-background-radius:10 10 10 10; -fx-font-size: 12;-fx-effect: dropshadow(three-pass-box, rgba(42,42,38,0.35), 10, 0, 0, 5);");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getID());
          });
    }
  }

  public void openCart() {
    if (!lowerCart.isVisible()) {
      totalPrice.setText(String.valueOf("Total Price: $" + flowerCart.getTotalPrice()));
      lowerCart.setVisible(true);
      cartPane.getChildren().clear();
      viewcartbutton.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color:  #B5C5EE");
      displayCart();
    } else {
      lowerCart.setVisible(false);
      viewcartbutton.setStyle("-fx-background-radius: 5 5 5 5; -fx-background-color:  #B5C5EE");
      cartPane.getChildren().clear();
    }
  }

  public void store(int x) {
    flowerID = x;
    Navigation.launchPopUp(FLOWER_POPUP);
  }

  public void checkOutHandler() {
    if (flowerCart.getTotalPrice() != 0) {
      checkOutBox.setVisible(true);
      cartBox.getChildren().clear();
    }
  }

  public void displayCart() {
    System.out.println("Displaying flowers");
    cartPane.getChildren().clear();
    totalPrice.setText(String.valueOf("Total Price: $" + flowerCart.getTotalPrice()));

    if (flowerCart.getCartItems().size() == 0) {

    } else {
      for (Flower flower : flowerCart.getCartItems()) {

        System.out.println("works");

        HBox newRow = new HBox();
        newRow.setSpacing(20);
        newRow.setMaxHeight(300);
        newRow.setMaxWidth(300);

        ImageView flowerImage = new ImageView();
        Image image = new Image(Main.class.getResource(flower.getImage()).toString());
        flowerImage.setImage(image);
        flowerImage.setStyle("-fx-background-radius: 10 10 10 10;");

        ImageView delete = new ImageView();
        Image imageDelete =
            new Image(String.valueOf(Main.class.getResource("images/TrashCan.png")));
        delete.setImage(imageDelete);
        delete.setStyle("-fx-background-radius: 10 10 10 10;");

        flowerImage.setFitHeight(60);
        flowerImage.setFitWidth(60);
        flowerImage.setPreserveRatio(false);

        delete.setFitHeight(20);
        delete.setFitWidth(20);
        delete.setPreserveRatio(false);

        VBox itemInfo = new VBox();
        itemInfo.setSpacing(5);
        itemInfo.setPrefWidth(300);
        itemInfo.setMaxHeight(300);

        HBox quantityChange = new HBox();
        quantityChange.setAlignment(CENTER);
        quantityChange.setSpacing(5);

        MFXButton increaseB = new MFXButton();
        increaseB.setStyle(
            "-fx-background-color: transparent; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");
        increaseB.setText("+");

        MFXButton decreaseB = new MFXButton();
        decreaseB.setStyle(
            "-fx-background-color: transparent; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");
        decreaseB.setText("-");

        Label qLabel = new Label();
        qLabel.setAlignment(CENTER);
        qLabel.setMinWidth(30);
        qLabel.setText(String.valueOf(flower.getQuantity()));
        qLabel.setStyle(
            "-fx-background-color: #FFFFFF; -fx-background-radius: 10 10 10 10; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");

        quantityChange.getChildren().add(decreaseB);
        quantityChange.getChildren().add(qLabel);
        quantityChange.getChildren().add(increaseB);

        decreaseB.setOnMouseClicked(
            event -> {
              qLabel.setText(Integer.toString(flower.getQuantity()));
              if (flower.getQuantity() > 1) {
                flower.setQuantity(flower.getQuantity() - 1);
              }
              displayCart();
            });

        increaseB.setOnMouseClicked(
            event -> {
              flower.setQuantity(flower.getQuantity() + 1);
              qLabel.setText(Integer.toString(flower.getQuantity()));
              displayCart();
            });

        HBox priceQ = new HBox();
        priceQ.setSpacing(5);
        priceQ.setMaxWidth(300);

        VBox deleteBox = new VBox();
        deleteBox.setSpacing(5);
        deleteBox.setPrefWidth(300);
        deleteBox.setMaxHeight(300);

        Label name = new Label();
        Label quantity = new Label();

        name.setText(flower.getName());
        name.setStyle(
            "-fx-text-fill: #000000; -fx-font-size: 16px; -fx-font-weight: bold; -fx-font-style: open sans");

        quantity.setText(String.valueOf("QTY: " + flower.getQuantity() + "x"));
        quantity.setStyle(
            "-fx-text-fill: #000000; -fx-font-size: 16px; -fx-font-style: open sans;");

        cartPane.getChildren().add(newRow);
        cartPane.setSpacing(10);
        newRow.getChildren().add(flowerImage);
        newRow.getChildren().add(itemInfo);
        newRow.getChildren().add(delete);

        itemInfo.getChildren().add(name);
        // itemInfo.getChildren().add(priceQ);
        itemInfo.getChildren().add(quantityChange);

        priceQ.getChildren().add(quantity);

        delete.setOnMouseClicked(event -> deleteFlower(flower));
      }
    }
  }

  public void deleteFlower(Flower flower) {
    flowerCart.removeFlowerItem(flower);
    displayCart();
  }

  public void clearCheckoutFields() {
    requestfield.clear();
    employeedrop.valueProperty().set(null);
    locationdrop.valueProperty().set(null);
  }

  public void submitHandler() {
    try {
      String Emp = employeedrop.getValue().toString();
      String deliveryRoom = locationdrop.getValue().toString();

      String n = requestfield.getText();

      Date d = Date.valueOf(LocalDate.now());
      Time t = Time.valueOf(LocalTime.now());

      FlowerDelivery currentFlowDev =
          new FlowerDelivery(
              FlowerDeliveryController.flowDevID++,
              FlowerDeliveryController.flowerCart.toString(),
              d,
              t,
              deliveryRoom,
              ActiveUser.getInstance().getCurrentUser().getUserName(),
              Emp,
              "Recieved",
              FlowerDeliveryController.flowerCart.getTotalPrice(),
              n);

      dbr.getFlowerDeliveryDAO().add(currentFlowDev);

      checkOutBox.getChildren().clear();

      Label confirm = new Label();
      Label thanks = new Label();
      confirm.setText("Order Submitted!");
      thanks.setText("Thank you for your order!\n\n\n\n\n\n");
      confirm.setStyle("-fx-font-size: 30;");
      thanks.setStyle("-fx-font-size:18; -fx-font-style: italic;");
      checkOutBox.setAlignment(Pos.CENTER);
      checkOutBox.getChildren().add(confirm);
      checkOutBox.getChildren().add(thanks);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void clearCart() {
    flowerCart.removeAll();
    displayCart();
  }
}
