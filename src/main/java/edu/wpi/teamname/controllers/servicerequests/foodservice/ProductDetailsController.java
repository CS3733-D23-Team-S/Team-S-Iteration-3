package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.OrderItem;
import edu.wpi.teamname.controllers.PopUpController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.tableview2.cell.TextField2TableCell;

public class ProductDetailsController extends PopUpController {
  // @FXML private MFXButton back3;
  // @FXML private MFXButton homeButton;

  @FXML private MFXButton addCart;
  @FXML private ImageView foodImage;
  @FXML private MFXButton clear;
  @FXML private Label quantityLabel;

  @FXML private Label productPrice;
  @FXML private Label productDescription;
  @FXML private MFXButton cancel;
  @FXML private MFXTextField quantity;

  @FXML private MFXTextField specialRequest;
  @FXML private final DataBaseRepository DBR = DataBaseRepository.getInstance();
  @FXML public TextField2TableCell fName;

  @FXML public MFXButton add;
  @FXML public MFXButton minus;

  // @FXML public Label desc1;
  // @FXML public Label fPrice;
  // @FXML public Label prepTime;

  @FXML public Label productName;

  // @FXML public Label quantityLabel;

  // @FXML MFXButton cancel;

  // @FXML MFXButton roomButton5;
  // @FXML MFXButton mealbutton;
  // @FXML MFXButton signagePage1;
  // @FXML MFXButton navigation1;

  public static int orderID;
  public static int itemCount;

  public void initialize() {
    itemCount = 1;
    // roomButton5.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    // homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    // mealbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));

    //  cancel.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));

    // back3.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));

    Food currentFood = DBR.getFoodDAO().get(MealDeliveryController.clickedFoodID);

    add.setOnMouseClicked(event -> addQuantity());
    minus.setOnMouseClicked(event -> subtractQuantity());

    addCart.setOnMouseClicked(
        event -> {
          try {
            currentFood.setQuantity(itemCount); // needs bounds if non int entered

            currentFood.setNote(specialRequest.getText()); // bounds for if non string entered

            OrderItem check = MealDeliveryController.cart;
            MealDeliveryController.cart.addFoodItem(currentFood);

            stage.close();

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    clearFields();
    clear.setOnMouseClicked(event -> clearFields());
    cancel.setOnMouseClicked(event -> stage.close());
    clearFields();
    selectedFood();
    foodNamer();
    foodDescription();
    foodPrice();
    foodDescription();
    showImage();
  }

  public void count(String x) {

    itemCount = Integer.parseInt(x);
    System.out.println(itemCount);
  }

  public void clearFields() {
    // quantity.clear();          ////*************FIX
    specialRequest.clear();
  }

  public Food selectedFood() {
    System.out.println("working selected");
    return DBR.getFoodDAO().get(MealDeliveryController.clickedFoodID);
  }

  public void foodNamer() {

    productName.setId(selectedFood().getFoodDescription());
    productName.setText(selectedFood().getFoodName());
    productName.setStyle("-fx-text-fill: #122e59; -fx-font-size: 36px; ");
  }

  public void foodDescription() {

    productDescription.setId(selectedFood().getFoodDescription());
    productDescription.setText(selectedFood().getFoodDescription());
    productDescription.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
  }

  public void foodPrice() {
    productPrice.setId(Double.toString(selectedFood().getFoodPrice()));
    productPrice.setText("$ " + String.format("%.02f", selectedFood().getFoodPrice()));
    productPrice.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
  }

  public void foodQuantity() {
    quantityLabel.setId(Double.toString(selectedFood().getFoodPrice()));
    quantityLabel.setText(String.valueOf(itemCount));
  }

  public void addQuantity() {
    itemCount++;
    quantityLabel.setText(Integer.toString(itemCount));
  }

  public void subtractQuantity() {
    if (itemCount > 1) {
      itemCount--;
      quantityLabel.setText(Integer.toString(itemCount));
    }
  }

  public void showImage() {
    Image fImage = new Image(Main.class.getResource(selectedFood().getImage()).toString());
    foodImage.setImage(fImage);
  }

  /*
   public void foodDescr() {

     productDescription.setId(Double.toString(selectedFood().getFoodDescription()));
     productDescription.setText(Double.toString(selectedFood().getFoodPrepTime()) + " minutes");
     productDescription.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
   }

  */
}
