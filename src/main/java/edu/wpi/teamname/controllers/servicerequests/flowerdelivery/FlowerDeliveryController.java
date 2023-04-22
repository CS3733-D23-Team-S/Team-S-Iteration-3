package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;
import static javafx.geometry.Pos.CENTER;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.flowers.Cart;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FlowerDeliveryController {
  public static int flowerID;
  @FXML MenuButton sizedrop;
  @FXML MFXButton viewcartbutton;
  @FXML MenuItem sizesmall;
  @FXML MenuItem sizenormal;
  @FXML MenuItem sizelarge;
  @FXML MFXButton clearfilter;
  @FXML FlowPane flowpane;
  @FXML VBox cartPane;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  private int cartID = 1;
  public static int flowDevID;
  public static int flowercounter;
  public static Cart flowerCart;
  public boolean cartOpen = false;

  public void initialize() {

    flowerCart = new Cart(cartID++);
    flowDevID = dbr.flowerGetNewDeliveryID();
    cartPane.setVisible(false);

    viewcartbutton.setOnMouseClicked(event -> openCart());

    sizesmall.setOnAction(event -> filterSmall());
    sizenormal.setOnAction(event -> filterMedium());
    sizelarge.setOnAction(event -> filterLarge());

    clearfilter.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));

    noFilter();
  }

  public void filterSmall() {
    flowpane.getChildren().clear();
    for (Flower f : dbr.getFlowerDAO().getListOfSize("small")) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(true);
      view.setFitHeight(150);
      view.setFitWidth(150);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-spacing: 10;");
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(25);
      flowpane.setVgap(25);

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
      view.setFitHeight(150);
      view.setFitWidth(150);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
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
      view.setFitHeight(150);
      view.setFitWidth(150);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
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
      view.setPreserveRatio(true);
      view.setFitHeight(120);
      view.setFitWidth(120);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(200);
      btn1.setPrefHeight(150);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
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
    if (!cartOpen) {
      cartPane.setVisible(true);
      displayCart();
      cartOpen = true;
    } else {
      cartPane.setVisible(false);
      cartPane.getChildren().clear();
      cartOpen = false;
    }
  }

  public void store(int x) {
    flowerID = x;
    Navigation.launchPopUp(FLOWER_POPUP);
  }

  public void displayCart() {
    System.out.println("Displaying flowers");
    System.out.println(FlowerDeliveryController.flowerCart.getCartItems().get(0));

    for (Flower flower : FlowerDeliveryController.flowerCart.getCartItems()) {

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
      Image imageDelete = new Image(String.valueOf(Main.class.getResource("images/TrashCan.png")));
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
      qLabel.setText("1");
      qLabel.setStyle(
          "-fx-background-color: #FFFFFF; -fx-background-radius: 10 10 10 10; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");

      quantityChange.getChildren().add(decreaseB);
      quantityChange.getChildren().add(qLabel);
      quantityChange.getChildren().add(increaseB);

      decreaseB.setOnMouseClicked(
          event -> {
            qLabel.setText(Integer.toString(flowercounter));
            if (flowercounter > 1) {
              flowercounter--;
            }
          });

      increaseB.setOnMouseClicked(
          event -> {
            flowercounter++;
            qLabel.setText(Integer.toString(flowercounter));
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
      quantity.setStyle("-fx-text-fill: #000000; -fx-font-size: 16px; -fx-font-style: open sans;");

      cartPane.getChildren().add(newRow);
      cartPane.setSpacing(10);
      newRow.getChildren().add(flowerImage);
      newRow.getChildren().add(itemInfo);
      newRow.getChildren().add(delete);

      itemInfo.getChildren().add(name);
      // itemInfo.getChildren().add(priceQ);
      itemInfo.getChildren().add(quantityChange);

      priceQ.getChildren().add(quantity);

      delete.setOnMouseClicked(event -> flowerCart.removeFlowerItem(flower));
    }
  }
}
