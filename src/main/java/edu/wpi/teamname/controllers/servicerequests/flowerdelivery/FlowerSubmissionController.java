package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
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
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    displayCart();

    totalprice.setText("$ " + String.valueOf(NewHomeController.flowerCart.getTotalPrice()));

    String stat = "Recieved";

    employeedrop.getItems().add("Nick Ho");
    employeedrop.getItems().add("Nikesh Walling");
    employeedrop.getItems().add("Prahladh Raja");
    employeedrop.getItems().add("Tyler Brown");
    employeedrop.getItems().add("Ryan Wright");
    employeedrop.getItems().add("Jake Olsen");
    employeedrop.getItems().add("Sarah Kogan");
    employeedrop.getItems().add("Kashvi Singh");
    employeedrop.getItems().add("Anthony Ticombe");
    employeedrop.getItems().add("Nat Rubin");

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
                    "Abraham Lincoln",
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

    // submitreqbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_REQTABLE));
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
      itemInfo.setMaxWidth(1000);
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
          "-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-style: open sans; -fx-wrap-text: true;");

      quantity.setText(String.valueOf("Quantity: " + flower.getQuantity() + "x"));
      quantity.setStyle("-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-style: open sans;");

      price.setText(String.valueOf("$ " + flower.getPrice()));
      price.setStyle("-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-style: open sans");

      /*message.setText(String.valueOf(flower.getMessage()));
      message.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");*/

      /*recipientLabel.setText(String.valueOf(recipient));
      recipientLabel.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");*/

      itemvbox.getChildren().add(newRow);
      itemvbox.setSpacing(20);
      newRow.getChildren().add(flowerImage);
      newRow.getChildren().add(itemInfo);

      itemInfo.getChildren().add(name);
      itemInfo.getChildren().add(description);
      description.setStyle("-fx-wrap-text: true;");
      itemInfo.getChildren().add(priceQ);

      priceQ.getChildren().add(price);
      priceQ.getChildren().add(quantity);
    }
  }

  public void clearFields() {
    requestfield.clear();
  }
}
