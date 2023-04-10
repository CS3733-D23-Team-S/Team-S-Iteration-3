package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDeliveryDAOImp;
import edu.wpi.teamname.controllers.mainpages.HomeController;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.sql.Time;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderDetailsController {

  @FXML private MFXButton back2;
  @FXML private MFXButton submit;
  @FXML private MFXButton clear2;
  @FXML private VBox orderVBox;
  @FXML private MFXTextField roomNum;
  @FXML private MFXTextField empNum;

  @FXML private MFXButton submitted1;
  @FXML private MFXTextField request1;

  public static FoodDeliveryDAOImp foodreq = FoodDeliveryDAOImp.getInstance();

  @FXML
  public void initialize() {
    System.out.println(ProductDetailsController.cart.toString());

    FoodDeliveryDAOImp foodev = FoodDeliveryDAOImp.getInstance();

    clearFields2();
    addedOrder();
    // multSelectedFood();
    back2.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    submitted1.setOnMouseClicked(event -> Navigation.navigate(Screen.SUBMITTED_MEALS));

    java.util.Date current = new java.util.Date();

    Location temp = LocationDoaImpl.getInstance().getLocation("Neuroscience Waiting Room");

    String whoOrdered = "George Washington";

    submit.setOnMouseClicked(
        event -> {
          try {

            // Get the time when button is clicked
            int year = current.getYear();
            int month = current.getMonth();
            int day = current.getDay();

            java.sql.Date theDate = new Date(year, month, day);

            int hour = current.getHours();
            int minute = current.getMinutes();
            int second = current.getSeconds();

            java.sql.Time theTime = new Time(hour, minute, second);

            // Get the employee
            String Emp = empNum.getText();
            String theNote = request1.getText();

            FoodDelivery currentFoodDev =
                new FoodDelivery(
                    (HomeController.numRequests++),
                    ProductDetailsController.cart,
                    theDate,
                    theTime,
                    temp,
                    whoOrdered,
                    Emp,
                    "In Progress",
                    theNote);

            foodev.addRequest(currentFoodDev);

            Navigation.navigate(Screen.ORDER_CONFIRMATION);

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
