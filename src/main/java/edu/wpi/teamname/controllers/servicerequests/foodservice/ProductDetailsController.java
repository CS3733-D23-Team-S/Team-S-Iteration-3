package edu.wpi.teamname.controllers.servicerequests.foodservice;

import static edu.wpi.teamname.controllers.servicerequests.foodservice.MealDeliveryController.clickedFoodID;

import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import edu.wpi.teamname.ServiceRequests.FoodService.OrderItem;
import edu.wpi.teamname.controllers.mainpages.HomeController;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ProductDetailsController {
  @FXML private MFXButton back3;
  @FXML private MFXButton addCart;
  @FXML private MFXButton clear;
  @FXML private MFXTextField quantity;

  @FXML private MFXTextField request;
  @FXML private FoodDAOImpl foodDAO = FoodDAOImpl.getInstance();
  @FXML private HBox foodName;
  @FXML private HBox fDescription;
  @FXML private HBox fPrice;

  public static int orderID;

  public static int itemCount;

  public static OrderItem cart = new OrderItem(HomeController.cartID);

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

    fName.setText(selectedFood().getFoodName());

    fName.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
    foodName.getChildren().add(fName);
  }

  public void foodDescription() {

    Label fDescription1 = new Label();
    fDescription1.setId(selectedFood().getFoodDescription());
    fDescription1.setText(selectedFood().getFoodDescription());
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
