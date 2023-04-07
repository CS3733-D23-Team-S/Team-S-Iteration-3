package edu.wpi.teamname.controllers.ServiceRequestControllers;

import static edu.wpi.teamname.controllers.ServiceRequestControllers.MealDeliveryController.clickedFoodID;

import edu.wpi.teamname.Database.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.Database.ServiceRequests.FoodService.FoodDAOImpl;
import edu.wpi.teamname.Database.ServiceRequests.FoodService.OrderItem;
import edu.wpi.teamname.controllers.HomeController;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ProductDetailsController {
  @FXML MFXButton back3;
  @FXML MFXButton addCart;
  @FXML MFXButton clear;
  @FXML MFXTextField quantity;

  @FXML MFXTextField request;
  @FXML FoodDAOImpl foodDAO = FoodDAOImpl.getInstance();
  @FXML HBox foodName;
  @FXML HBox fDescription;
  @FXML HBox fPrice;
  static int orderID;

  public static int itemCount;

  static OrderItem cart = new OrderItem(HomeController.cartID);

  public void initialize() {

    back3.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));

    Food currentFood = foodDAO.retrieveFood(clickedFoodID);
    cart.addFoodItem(currentFood);

    addCart.setOnMouseClicked(
        event -> {
          try {
            cart.getTheCart()
                .get(HomeController.cartID)
                .setQuantity(
                    Integer.parseInt(quantity.getText())); // needs bounds if non int entered

            cart.getTheCart()
                .get(HomeController.cartID)
                .setNote(request.getText()); // bounds for if non string entered
            System.out.println(request.getText());

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    // addCart.setOnMouseClicked(event -> addToOrder());
    clearFields();
    clear.setOnMouseClicked(event -> clearFields());
    clearFields();
    selectedFood();
    foodNamer();
    foodDescription();
    foodPrice();
    addCart.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
  }

  public void count(String x) {

    itemCount = Integer.parseInt(x);
    System.out.println(itemCount);
  }

  public void clearFields() {
    quantity.clear();
    request.clear();
  }

  public Food selectedFood() {
    return foodDAO.retrieveFood(clickedFoodID);
  }

  public void foodNamer() {

    Label fName = new Label();

    fName.setId(selectedFood().getFoodDescription());

    fName.setText(selectedFood().getFoodName().toString());

    fName.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
    foodName.getChildren().add(fName);
  }

  public void foodDescription() {

    Label fDescription1 = new Label();
    fDescription1.setId(selectedFood().getFoodDescription());
    fDescription1.setText(selectedFood().getFoodDescription().toString());
    fDescription1.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

    fDescription.getChildren().add(fDescription1);
  }

  public void foodPrice() {

    Label fPrice1 = new Label();
    fPrice1.setId(Double.toString(selectedFood().getFoodPrice()));
    fPrice1.setText(Double.toString(selectedFood().getFoodPrice()));
    fPrice1.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
    fPrice.getChildren().add(fPrice1);
  }
}
