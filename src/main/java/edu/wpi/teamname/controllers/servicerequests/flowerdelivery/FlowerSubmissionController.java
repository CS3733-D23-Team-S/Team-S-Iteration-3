package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
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
  @FXML SearchableComboBox locationdrop;
  @FXML Text pricetext;
  @FXML MFXTextField requestfield;
  @FXML MFXButton submitbutton;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    displayCart();

    submitbutton.setOnMouseClicked(
        event -> {
          try {
            // String Emp = employeefield.getText();
            deliveryRoom = locationdrop.getValue().toString();

            Navigation.navigate(FLOWER_CONFIRMATION);
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
    System.out.println(FlowerPopupController.flowerCart.getCartItems().get(0));

    for (Flower flower : FlowerPopupController.flowerCart.getCartItems()) {

      System.out.println("works");

      HBox newRow = new HBox();
      newRow.setSpacing(100);
      newRow.setMaxWidth(1000);

      ImageView flowerImage = new ImageView();
      Image image = new Image(Main.class.getResource(flower.getImage()).toString());
      flowerImage.setImage(image);

      flowerImage.setFitHeight(150);
      flowerImage.setFitWidth(150);
      flowerImage.setPreserveRatio(true);

      VBox itemInfo = new VBox();
      itemInfo.setSpacing(20);
      itemInfo.setMaxWidth(1000);

      HBox priceQ = new HBox();
      priceQ.setSpacing(80);
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
          "-fx-text-fill: #000000; -fx-font-size: 18px; -fx-font-style: open sans; -fx-wrap-text: true;");

      /*quantity.setText(String.valueOf(flower.getQuantity()));
      quantity.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");*/

      price.setText(String.valueOf("$ " + flower.getPrice()));
      price.setStyle("-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-style: open sans");

      /*message.setText(String.valueOf(flower.getMessage()));
      message.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");*/

      /*recipientLabel.setText(String.valueOf(recipient));
      recipientLabel.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");*/

      itemvbox.getChildren().add(newRow);
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
  }
}
