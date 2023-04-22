package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.ServiceRequests.flowers.FlowerDelivery;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

public class FlowerSubmissionController {
  public static String deliveryRoom;
  @FXML MFXButton clearbutton;
  @FXML Text descriptiontext;
  @FXML SearchableComboBox employeedrop;
  @FXML ImageView flowerimage;
  @FXML Text flowernametext;
  @FXML VBox itemvbox;
  @FXML VBox sidepanel;
  @FXML SearchableComboBox locationdrop;
  @FXML Text pricetext;
  @FXML MFXTextField requestfield;
  @FXML MFXButton submitbutton;
  @FXML Label totalprice;
  @FXML MFXScrollPane scroll;
  @FXML private final DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    submitbutton.setDisable(true);

    employeedrop
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitbutton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    locationdrop
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitbutton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    requestfield
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitbutton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    displayCart();

    totalprice.setText(
        "$ " + String.format("%.02f", FlowerDeliveryController.flowerCart.getTotalPrice()));

    String stat = "Recieved";

    for (User u : dbr.getUserDAO().getListOfUsers().values()) {
      employeedrop.getItems().add(u.getFirstName() + " " + u.getLastName());
    }

    submitbutton.setOnMouseClicked(
        event -> {
          try {
            String Emp = employeedrop.getValue().toString();
            deliveryRoom = locationdrop.getValue().toString();

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
                    stat,
                    FlowerDeliveryController.flowerCart.getTotalPrice(),
                    n);

            dbr.getFlowerDeliveryDAO().add(currentFlowDev);

            sidepanel.getChildren().clear();

            Label confirm = new Label();
            Label thanks = new Label();
            confirm.setText("Order Submitted!");
            thanks.setText("Thank you for your order!");
            confirm.setStyle("-fx-font-size: 48;");
            thanks.setStyle("-fx-font-size:36; -fx-font-style: italic;");
            sidepanel.setAlignment(Pos.CENTER);
            sidepanel.getChildren().add(confirm);
            sidepanel.getChildren().add(thanks);

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    locationdrop.getItems().addAll(dbr.getListOfEligibleRooms());
    clearbutton.setOnMouseClicked(event -> clearFields());
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

      quantity.setText("Quantity: " + flower.getQuantity() + "x");
      quantity.setStyle("-fx-text-fill: #000000; -fx-font-size: 20px; -fx-font-style: open sans;");

      price.setText("$ " + String.format("%.02f", flower.getPrice()));
      price.setStyle("-fx-text-fill: #000000; -fx-font-size: 20px; -fx-font-style: open sans");

      itemvbox.getChildren().add(newRow);
      itemvbox.setSpacing(20);
      newRow.getChildren().add(flowerImage);
      newRow.getChildren().add(itemInfo);

      itemInfo.getChildren().add(name);
      itemInfo.getChildren().add(description);
      itemInfo.getChildren().add(priceQ);

      priceQ.getChildren().add(price);
      priceQ.getChildren().add(quantity);
    }
  }

  public void clearFields() {
    requestfield.clear();
    employeedrop.valueProperty().set(null);
    locationdrop.valueProperty().set(null);
  }
}
