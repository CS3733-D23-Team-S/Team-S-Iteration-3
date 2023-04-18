package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
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

  // @FXML private MFXButton back2;
  // @FXML private MFXButton flowerbutton1;
  @FXML private MFXButton submit;
  @FXML private MFXButton clear2;

  @FXML private SearchableComboBox location1;
  @FXML private SearchableComboBox empNum;
  // @FXML private MFXTextField empNum;
  @FXML private VBox items;

  @FXML private MFXTextField request1;
  @FXML private Label totalPrice;

  // @FXML private MFXButton roomButton1;
  // @FXML private MFXButton mealButton2;
  // @FXML private MFXButton signagePage1;
  // @FXML private MFXButton navigation1;
  // @FXML private MFXButton cancel;
  // @FXML private MFXButton homeButton;

  @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();

  @FXML
  public void initialize() {

    // totalPrice.setText("$ "+ String.valueOf().getTotalPrice());

    // flowerbutton1.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_DELIVERY));
    // roomButton1.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    // signagePage1.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    // mealButton2.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    // navigation1.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDING));
    // cancel.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));
    // homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    clearFields2();
    addedOrder();

    // back2.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));

    String whoOrdered = ActiveUser.getInstance().getCurrentUser().getUserName();

    String stat = "Received";

    location1.getItems().addAll(DBR.getListOfEligibleRooms());

    submit.setOnMouseClicked(
        event -> {
          try {
            String Emp = empNum.getValue().toString();
            String theNote = request1.getText();

            String loc = location1.getValue().toString();

            Date currentd = Date.valueOf(LocalDate.now());
            Time currentt = Time.valueOf(LocalTime.now());

            FoodDelivery currentFoodDev =
                new FoodDelivery(
                    MealDeliveryController.delID++,
                    MealDeliveryController.cart.toString(),
                    currentd,
                    currentt,
                    loc,
                    whoOrdered,
                    Emp,
                    stat,
                    MealDeliveryController.cart.getTotalPrice(),
                    theNote);

            DBR.getFoodDeliveryDAO().add(currentFoodDev);

            Navigation.navigate(Screen.MEAL_DELIVERY_ORDER_CONFIRMATION);

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    clear2.setOnMouseClicked(event -> clearFields2());
  }

  public void clearFields2() {
    location1.valueProperty().set(null);
    empNum.valueProperty().set(null);
  }

  public void addedOrder() {

    System.out.println(MealDeliveryController.cart.getTheCart().size());
    for (Food aFood : MealDeliveryController.cart.getTheCart().values()) {

      Label newItemName = new Label();
      Label newItemQuantity = new Label();
      Label newItemPrice = new Label();
      Label newItemPrepTime = new Label();
      Label newItemDescription = new Label();

      HBox newRow = new HBox();
      newRow.setSpacing(200);
      newRow.setMaxWidth(1000);

      VBox itemInfo = new VBox();
      itemInfo.setSpacing(10);
      itemInfo.setMaxWidth(1000);

      HBox pricePrep = new HBox();
      pricePrep.setSpacing(80);
      pricePrep.setMaxWidth(1000);

      newItemName.setText(aFood.getFoodName());
      newItemName.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px; -fx-font-weight: bold");

      newItemQuantity.setText(String.valueOf(aFood.getQuantity()));
      newItemQuantity.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      newItemPrice.setText(String.valueOf("$ " + aFood.getFoodPrice()));
      newItemPrice.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      newItemPrepTime.setText(String.valueOf(aFood.getFoodPrepTime() + " minutes"));
      newItemPrepTime.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      newItemDescription.setText(String.valueOf(aFood.getFoodDescription()));
      newItemDescription.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");

      // orderVBox.getChildren().add(newRow);

      items.getChildren().add(newRow);
      newRow.getChildren().add(itemInfo);

      itemInfo.getChildren().add(newItemName);
      itemInfo.getChildren().add(newItemDescription);
      itemInfo.getChildren().add(pricePrep);

      pricePrep.getChildren().add(newItemPrice);
      pricePrep.getChildren().add(newItemPrepTime);
      pricePrep.getChildren().add(newItemQuantity);
    }
  }
}
