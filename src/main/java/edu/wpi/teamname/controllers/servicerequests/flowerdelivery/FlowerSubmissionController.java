package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.flowerCart;
import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.recipient;
import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

public class FlowerSubmissionController {
  public static String deliveryRoom;

  @FXML ImageView backicon;
  @FXML VBox cartvbox;
  @FXML MFXButton clearbutton;
  @FXML ImageView exiticon;
  @FXML MFXButton flowerbutton;
  @FXML ImageView helpicon;
  @FXML ImageView homeicon;
  @FXML SearchableComboBox locationdrop;
  @FXML MFXButton mealbutton;
  @FXML MFXButton navigationbutton;
  @FXML MFXTextField requestfield;
  @FXML MFXButton roombutton;
  @FXML MFXButton signagebutton;
  @FXML MFXButton submitbutton;
  @FXML MFXButton submitreqbutton;
  @FXML ImageView topbarlogo;
  @FXML Text itemnametext;
  @FXML Text quantitytext;
  @FXML Text pricetext;
  @FXML Text requesttext;
  @FXML Text recipienttext;
  @FXML VBox itemvbox;
  @FXML VBox quantityvbox;
  @FXML VBox pricevbox;
  @FXML VBox requestvbox;
  @FXML VBox recipientvbox;
  @FXML TextField employeefield;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {
    submitbutton.setOnMouseClicked(
        event -> {
          try {
            String Emp = employeefield.getText();
            deliveryRoom = locationdrop.getValue().toString();

            Navigation.navigate(FLOWER_CONFIRMATION);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    backicon.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
    exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
    homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));
    displayCart();
    locationdrop.getItems().addAll(dbr.getListOfEligibleRooms());
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
      quantityvbox.getChildren().add(quantity);
      pricevbox.getChildren().add(price);
      requestvbox.getChildren().add(message);
      recipientvbox.getChildren().add(recipientLabel);
    }
  }
}
