package edu.wpi.teamname.controllers.servicerequests.foodservice;

import static edu.wpi.teamname.controllers.servicerequests.foodservice.MealDeliveryController.clickedFoodID;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.OrderItem;
import edu.wpi.teamname.controllers.NewHomeController;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.tableview2.cell.TextField2TableCell;

public class ProductDetailsController {
  @FXML private MFXButton back3;
  @FXML private MFXButton homeButton;

  @FXML private MFXButton addCart;
  @FXML private MFXButton clear;
  @FXML private MFXTextField quantity;

  @FXML private MFXTextField specialRequest;
  @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();
  @FXML public TextField2TableCell fName;
  @FXML public Label desc1;
  @FXML public Label fPrice;
  @FXML public Label prepTime;

  @FXML MFXButton cancel;

  @FXML MFXButton roomButton5;
  @FXML MFXButton mealbutton;
  @FXML MFXButton signagePage1;
  @FXML MFXButton navigation1;

  public static int orderID;
  public static int itemCount;

  public void initialize() {

    roomButton5.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    mealbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));

    cancel.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));

    back3.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));

    Food currentFood = DBR.retrieveFood(clickedFoodID);

    addCart.setOnMouseClicked(
        event -> {
          try {
            currentFood.setQuantity(
                Integer.parseInt(quantity.getText())); // needs bounds if non int entered

            currentFood.setNote(specialRequest.getText()); // bounds for if non string entered

            OrderItem check = NewHomeController.cart;
            NewHomeController.cart.addFoodItem(currentFood);

            Navigation.navigate(Screen.MEAL_DELIVERY1);

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    clearFields();
    clear.setOnMouseClicked(event -> clearFields());
    clearFields();
    selectedFood();
    foodNamer();
    foodDescription();
    foodPrice();
    foodPrep();
  }

  public void count(String x) {

    itemCount = Integer.parseInt(x);
    System.out.println(itemCount);
  }

  public void clearFields() {
    quantity.clear();
    specialRequest.clear();
  }

  public Food selectedFood() {
    System.out.println("working selected");
    return DBR.retrieveFood(clickedFoodID);
  }

  public void foodNamer() {

    fName.setId(selectedFood().getFoodDescription());
    fName.setText(selectedFood().getFoodName().toString());
    fName.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
  }

  public void foodDescription() {

    desc1.setId(selectedFood().getFoodDescription());
    desc1.setText(selectedFood().getFoodDescription().toString());
    desc1.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
  }

  public void foodPrice() {
    fPrice.setId(Double.toString(selectedFood().getFoodPrice()));
    fPrice.setText("$ " + (selectedFood().getFoodPrice()));
    fPrice.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
  }

  public void foodPrep() {

    prepTime.setId(Double.toString(selectedFood().getFoodPrepTime()));
    prepTime.setText(Double.toString(selectedFood().getFoodPrepTime()) + " minutes");
    prepTime.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
  }
}
