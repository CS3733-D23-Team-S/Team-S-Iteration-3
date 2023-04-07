package edu.wpi.teamname.controllers.ServiceRequestControllers;

import edu.wpi.teamname.Database.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.Database.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.Database.ServiceRequests.FoodService.FoodDeliveryDAOImp;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderDetailsController {

  @FXML MFXButton back2;
  @FXML MFXButton submit;
  @FXML MFXButton clear2;
  @FXML VBox orderVBox;
  @FXML MFXTextField roomNum;
  @FXML MFXTextField empNum;

  static FoodDeliveryDAOImp foodreq = FoodDeliveryDAOImp.getInstance();

  @FXML
  public void initialize() {
    System.out.println(ProductDetailsController.cart.toString());

    clearFields2();
    addedOrder();
    // multSelectedFood();
    back2.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    submit.setOnMouseClicked(event -> Navigation.navigate(Screen.ORDER_CONFIRMATION));

    java.sql.Date d = new java.sql.Date(2023, 4, 6);
    java.sql.Time t = new java.sql.Time(11, 35, 45);

    // FoodDelivery currentFoodDev;

    submit.setOnMouseClicked(
        event -> {
          try {
            String Emp = empNum.getText();

            FoodDelivery currentFoodDev =
                new FoodDelivery(
                    1,
                    ProductDetailsController.cart,
                    d,
                    t,
                    5,
                    "John Doe",
                    Emp,
                    "In Progress",
                    "These are not the notes you are looking for");
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
    clear2.setOnMouseClicked(event -> clearFields2());

    // foodNamer1();
  }

  public void clearFields2() {
    roomNum.clear();
    empNum.clear();
  }

  public void addedOrder() {

    for (Food aFood : ProductDetailsController.cart.getTheCart().values()) {
      System.out.println("works");
      Label newItemName = new Label();
      Label newItemQuantity = new Label();
      Label newItemPrice = new Label();
      Label newItemRequest = new Label();

      HBox newRow = new HBox();
      newRow.setSpacing(200);
      newRow.setMaxWidth(1000);
      // newRow.setStyle("-fx-background-color : red");

      newItemName.setText(aFood.getFoodName());
      newItemName.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

      newItemQuantity.setText(String.valueOf(aFood.getQuantity()));
      newItemQuantity.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

      newItemPrice.setText(String.valueOf(aFood.getFoodPrice()));
      newItemPrice.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

      newItemRequest.setText(String.valueOf(aFood.getNote()));
      newItemRequest.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

      orderVBox.getChildren().add(newRow);
      newRow.getChildren().add(newItemName);

      newRow.getChildren().add(newItemQuantity);
      newRow.getChildren().add(newItemPrice);
      newRow.getChildren().add(newItemRequest);
    }
  }
}
