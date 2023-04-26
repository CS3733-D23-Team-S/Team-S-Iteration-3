package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.controllers.PopUpController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ProductDetailsController extends PopUpController {

  @FXML private MFXButton addCart;
  @FXML private ImageView foodImage;
  @FXML private Label quantitylabel;

  @FXML private Text PriceText;
  @FXML private Text foodDescription;
  @FXML private MFXButton cancel;

  @FXML private MFXTextField specialRequest;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();
  @FXML public Label foodName;

  @FXML public MFXButton add;
  @FXML public MFXButton minus;

  @FXML public Label productName;

  public static int supplycounter;

  public void initialize() {

    showInfo();
    addCart.setOnMouseClicked(event -> createDelivery());

    cancel.setOnMouseClicked(event -> stage.close());

    supplycounter = 1;
    add.setOnMouseClicked(event -> addquantity());
    minus.setOnMouseClicked(event -> subtractquantity());
  }

  private void createDelivery() {
    try {
      Food food = dbr.getFoodDAO().get(MealDeliveryController.mealDevID);
      food.setQuantity(supplycounter);
      MealDeliveryController.mealCart.addFoodItem(food);
      food.setNote(specialRequest.getText()); // bounds for if non string entered

      stage.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showInfo() {
    foodName.setText(dbr.getFoodDAO().get(MealDeliveryController.mealDevID).getFoodName());
    foodDescription.setText(
        dbr.getFoodDAO().get(MealDeliveryController.mealDevID).getFoodDescription());

    PriceText.setText(
        String.valueOf(dbr.getFoodDAO().get(MealDeliveryController.mealDevID).getFoodPrice()));

    Image image =
        new Image(
            Main.class
                .getResource(dbr.getFoodDAO().get(MealDeliveryController.mealDevID).getImage())
                .toString());
    foodImage.setImage(image);
  }

  public void addquantity() {
    supplycounter++;
    quantitylabel.setText(Integer.toString(supplycounter));
  }

  public void subtractquantity() {
    supplycounter--;
    if (supplycounter < 1) {
      supplycounter = 1;
    }

    quantitylabel.setText(Integer.toString(supplycounter));
  }
}
