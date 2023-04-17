package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.flowerCart;
import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.recipient;
import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

public class FlowerSubmissionController {
  public static String deliveryRoom;

  @FXML MFXButton clearbutton;
  @FXML Text descriptiontext;
  @FXML SearchableComboBox<?> employeedrop;
  @FXML ImageView flowerimage;
  @FXML Text flowernametext;
  @FXML VBox itemvbox;
  @FXML SearchableComboBox locationdrop;
  @FXML Text pricetext;
  @FXML MFXTextField requestfield;
  @FXML MFXButton submitbutton;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {
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

    displayCart();
    locationdrop.getItems().addAll(dbr.getListOfEligibleRooms());
    clearbutton.setOnMouseClicked(event -> clearFields());

    // submitreqbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_REQTABLE));
  }

  public void displayCart() {
    System.out.println("Displaying flowers");
    for (Flower flower : flowerCart.getCartItems()) {
      Label name = new Label();
      Label quantity = new Label();
      Label price = new Label();
      Label message = new Label();
      Label recipientLabel = new Label();

      HBox newRow = new HBox();
      newRow.setSpacing(200);
      newRow.setMaxWidth(1000);
      // newRow.setStyle("-fx-background-color : red");

      name.setText(flower.getName());
      name.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      quantity.setText(String.valueOf(flower.getQuantity()));
      quantity.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      price.setText(String.valueOf(flower.getPrice()));
      price.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      message.setText(String.valueOf(flower.getMessage()));
      message.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      recipientLabel.setText(String.valueOf(recipient));
      recipientLabel.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      itemvbox.getChildren().add(name);
      /*quantityvbox.getChildren().add(quantity);
      pricevbox.getChildren().add(price);
      requestvbox.getChildren().add(message);
      recipientvbox.getChildren().add(recipientLabel);*/
    }
  }

  public void clearFields() {
    requestfield.clear();
  }
}
