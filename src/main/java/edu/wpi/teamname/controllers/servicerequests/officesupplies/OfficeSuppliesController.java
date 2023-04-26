package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import static edu.wpi.teamname.navigation.Screen.*;
import static javafx.geometry.Pos.CENTER;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.*;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class OfficeSuppliesController {
  DecimalFormat df = new DecimalFormat("0.00");
  public static int suppliesID;
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
  public static int osDevID;
  public static OfficeSupplyCart Cart;

  public void initialize() {

    Cart = new OfficeSupplyCart(cartID++);
    osDevID = dbr.officeSupplyGetNewDeliveryID();
    lowerCart.setVisible(false);

    Cart.getCartItems()
        .addListener(
            (ListChangeListener<OfficeSupply>)
                change -> {
                  displayCart();
                });

    openCart();

    proceed.setOnMouseClicked(event -> checkOutHandler());

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
                      || locationdrop.getValue() == null);
            }));

    locationdrop
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitButton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null);
            }));

    for (User u : dbr.getUserDAO().getListOfUsers().values()) {
      employeedrop.getItems().add(u.getFirstName() + " " + u.getLastName());
    }

    locationdrop.getItems().addAll(dbr.getListOfEligibleRooms());

    clearSubmit.setOnMouseClicked(event -> clearCheckoutFields());

    submitButton.setOnMouseClicked(event -> submitHandler());
  }

  public void noFilter() {
    flowpane.getChildren().clear();
    for (OfficeSupply f : dbr.getOfficeSupplyDAO().getSupplies().values()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);

      view.setPreserveRatio(true);
      view.setFitHeight(80);
      view.setFitWidth(80);
      // When Actual Images like Food
      // view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(14,14,12,0.8), 10, 0, 0, 5);");

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
            store(f.getOfficesupplyid());
          });
    }
  }

  public void openCart() {
    totalPrice.setText(String.valueOf("Total Price: $" + df.format(Cart.getTotalPrice())));
    lowerCart.setVisible(true);
    cartPane.getChildren().clear();
    displayCart();
  }

  public void store(int x) {
    suppliesID = x;
    Navigation.launchPopUp(OFFICE_SUPPLIES_POPUP);
  }

  public void checkOutHandler() {
    if (Cart.getTotalPrice() != 0) {
      checkOutBox.setVisible(true);
      cartBox.getChildren().clear();
    }
  }

  public void displayCart() {
    System.out.println("Displaying foods");
    cartPane.getChildren().clear();
    totalPrice.setText(String.valueOf("Total Price: $" + df.format(Cart.getTotalPrice())));

    if (Cart.getCartItems().size() == 0) {

    } else {
      for (OfficeSupply officeSupply : Cart.getCartItems()) {

        System.out.println("works");

        HBox newRow = new HBox();
        newRow.setSpacing(5);
        newRow.setMaxHeight(300);
        newRow.setMaxWidth(276);

        ImageView officeSupplyImage = new ImageView();
        Image image = new Image(Main.class.getResource(officeSupply.getImage()).toString());
        officeSupplyImage.setImage(image);
        officeSupplyImage.setStyle("-fx-background-radius: 10 10 10 10;");

        ImageView delete = new ImageView();
        Image imageDelete =
            new Image(String.valueOf(Main.class.getResource("images/TrashCan.png")));
        delete.setImage(imageDelete);
        delete.setStyle("-fx-background-radius: 10 10 10 10;");

        officeSupplyImage.setFitHeight(60);
        officeSupplyImage.setFitWidth(60);
        officeSupplyImage.setPreserveRatio(false);

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
        qLabel.setText(String.valueOf(officeSupply.getQuantity()));
        qLabel.setStyle(
            "-fx-background-color: #FFFFFF; -fx-background-radius: 10 10 10 10; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");

        quantityChange.getChildren().add(decreaseB);
        quantityChange.getChildren().add(qLabel);
        quantityChange.getChildren().add(increaseB);

        decreaseB.setOnMouseClicked(
            event -> {
              qLabel.setText(Integer.toString(officeSupply.getQuantity()));
              if (officeSupply.getQuantity() > 1) {
                officeSupply.setQuantity(officeSupply.getQuantity() - 1);
              }
              displayCart();
            });

        increaseB.setOnMouseClicked(
            event -> {
              officeSupply.setQuantity(officeSupply.getQuantity() + 1);
              qLabel.setText(Integer.toString(officeSupply.getQuantity()));
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

        name.setText(officeSupply.getName());
        name.setStyle(
            "-fx-text-fill: #000000; -fx-font-size: 16px; -fx-font-weight: bold; -fx-font-style: open sans");

        quantity.setText(String.valueOf("QTY: " + officeSupply.getQuantity() + "x"));
        quantity.setStyle(
            "-fx-text-fill: #000000; -fx-font-size: 16px; -fx-font-style: open sans;");

        cartPane.getChildren().add(newRow);
        cartPane.setSpacing(10);
        newRow.getChildren().add(officeSupplyImage);
        newRow.getChildren().add(itemInfo);
        newRow.getChildren().add(delete);

        itemInfo.getChildren().add(name);
        // itemInfo.getChildren().add(priceQ);
        itemInfo.getChildren().add(quantityChange);

        priceQ.getChildren().add(quantity);

        delete.setOnMouseClicked(event -> deleteOfficeSupply(officeSupply));
      }
    }
  }

  public void deleteOfficeSupply(OfficeSupply officeSupply) {
    Cart.removeOfficeSupplyItem(officeSupply);
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

      OfficeSupplyDelivery currentOSDev =
          new OfficeSupplyDelivery(
              OfficeSuppliesController.osDevID++,
              OfficeSuppliesController.Cart.toString(),
              d,
              t,
              deliveryRoom,
              ActiveUser.getInstance().getCurrentUser().getUserName(),
              Emp,
              "Recieved",
              OfficeSuppliesController.Cart.getTotalPrice(),
              n);

      dbr.getOfficeSupplyDeliveryDAO().add(currentOSDev);

      checkOutBox.getChildren().clear();

      Label confirm = new Label();
      ImageView checkMark = new ImageView();
      Label thanks = new Label();
      Image checkMark1 = new Image(String.valueOf(Main.class.getResource("images/checkMark.png")));
      checkMark.setImage(checkMark1);
      checkMark.setStyle("-fx-background-radius: 10 10 10 10;");

      checkMark.setFitHeight(180);
      checkMark.setFitWidth(180);
      checkMark.setPreserveRatio(false);
      confirm.setText("Order Submitted!");
      thanks.setText("Thank you for your order!");
      confirm.setStyle("-fx-font-size: 30;");
      thanks.setStyle("-fx-font-size:18; -fx-font-style: italic;");
      checkOutBox.setAlignment(Pos.CENTER);
      checkOutBox.getChildren().add(confirm);
      checkOutBox.getChildren().add(checkMark);
      checkOutBox.getChildren().add(thanks);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void clearCart() {
    Cart.removeAll();
    displayCart();
  }
}
