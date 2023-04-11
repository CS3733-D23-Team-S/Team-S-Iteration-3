package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.controllers.mainpages.HomeController;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class OrderDetailsController {

  @FXML private MFXButton back2;
  @FXML private MFXButton submit;
  @FXML private MFXButton clear2;
  @FXML private VBox orderVBox;
  @FXML private SearchableComboBox location1;
  @FXML private MFXTextField empNum;

  @FXML private MFXButton submitted1;
  @FXML private MFXTextField request1;

  @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();

  @FXML
  public void initialize() {

    System.out.println(HomeController.cart.toString());

    clearFields2();
    addedOrder();

    back2.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));

    Location temp = DBR.getLocation("Neuroscience Waiting Room");

    String whoOrdered = "George Washington"; // eventually linked to account ordering

    String stat = "Received";

    location1.getItems().addAll(DBR.getListOfEligibleRooms());

    submit.setOnMouseClicked(
        event -> {
          try {
            String Emp = empNum.getText();
            String theNote = request1.getText();

            String loc = location1.getValue().toString();

            Date currentd = Date.valueOf(LocalDate.now());
            Time currentt = Time.valueOf(LocalTime.now());

            FoodDelivery currentFoodDev =
                new FoodDelivery(
                    HomeController.delID++,
                    HomeController.cart.toString(),
                    currentd,
                    currentt,
                    loc,
                    whoOrdered,
                    Emp,
                    stat,
                    HomeController.cart.getTotalPrice(),
                    theNote);

            DBR.addFoodRequest(currentFoodDev);

            Navigation.navigate(Screen.ORDER_CONFIRMATION);

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    submitted1.setOnMouseClicked(event -> Navigation.navigate(Screen.SUBMITTED_MEALS));

    clear2.setOnMouseClicked(event -> clearFields2());
  }

  public void clearFields2() {
    location1.valueProperty().set(null);
    empNum.clear();
  }

  public void addedOrder() {

    for (Food aFood : HomeController.cart.getTheCart().values()) {
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
      newItemName.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      newItemQuantity.setText(String.valueOf(aFood.getQuantity()));
      newItemQuantity.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      newItemPrice.setText(String.valueOf(aFood.getFoodPrice()));
      newItemPrice.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      newItemRequest.setText(String.valueOf(aFood.getNote()));
      newItemRequest.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      orderVBox.getChildren().add(newRow);
      newRow.getChildren().add(newItemName);

      newRow.getChildren().add(newItemQuantity);
      newRow.getChildren().add(newItemPrice);
      newRow.getChildren().add(newItemRequest);
    }
  }
  /*
   public void addEligibleRooms() {
     for (int i = 0; i < DataBaseRepository.getListOfEligibleRooms.size(); i++) {
         location1.getItems().add(i.getText());
     }
   }

  */

}
