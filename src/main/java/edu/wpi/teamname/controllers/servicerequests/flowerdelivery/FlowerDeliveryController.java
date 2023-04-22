package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;

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
      newRow.setSpacing(50);
      newRow.setMaxHeight(300);
      newRow.setMaxWidth(1000);

      ImageView flowerImage = new ImageView();
      Image image = new Image(Main.class.getResource(flower.getImage()).toString());
      flowerImage.setImage(image);
      flowerImage.setStyle("-fx-background-radius: 10 10 10 10;");

      flowerImage.setFitHeight(160);
      flowerImage.setFitWidth(160);
      flowerImage.setPreserveRatio(false);

      VBox itemInfo = new VBox();
      itemInfo.setSpacing(20);
      itemInfo.setPrefWidth(800);
      itemInfo.setMaxHeight(300);

      HBox priceQ = new HBox();
      priceQ.setSpacing(30);
      priceQ.setMaxWidth(1000);

      Label name = new Label();
      Label price = new Label();
      Label description = new Label();
      Label quantity = new Label();

      name.setText(flower.getName());
      name.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-weight: bold; -fx-font-style: open sans");

      description.setText(flower.getDescription());
      description.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 20px; -fx-font-style: open sans; -fx-wrap-text: true; -fx-font-style: italic;");

      quantity.setText(String.valueOf("Quantity: " + flower.getQuantity() + "x"));
      quantity.setStyle("-fx-text-fill: #000000; -fx-font-size: 20px; -fx-font-style: open sans;");

      price.setText("$ " + String.format("%.02f", flower.getPrice()));
      price.setStyle("-fx-text-fill: #000000; -fx-font-size: 20px; -fx-font-style: open sans");

      cartPane.getChildren().add(newRow);
      cartPane.setSpacing(20);
      newRow.getChildren().add(flowerImage);
      newRow.getChildren().add(itemInfo);

      itemInfo.getChildren().add(name);
      itemInfo.getChildren().add(description);
      itemInfo.getChildren().add(priceQ);

      priceQ.getChildren().add(price);
      priceQ.getChildren().add(quantity);
    }
  }
}
