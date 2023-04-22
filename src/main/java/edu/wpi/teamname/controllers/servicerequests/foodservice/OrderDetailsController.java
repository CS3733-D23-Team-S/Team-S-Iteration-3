package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  @FXML private VBox allThings;

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
    submit.setDisable(true);

    location1
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submit.setDisable(location1.getValue() == null || empNum.getValue() == null);
            }));

    empNum
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submit.setDisable(location1.getValue() == null || empNum.getValue() == null);
            }));

    totalPrice.setText("$ " + String.format("%.02f", MealDeliveryController.cart.getTotalPrice()));

    for (User u : DBR.getUserDAO().getListOfUsers().values()) {
      empNum.getItems().add(u.getFirstName() + " " + u.getLastName());
    }

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

            // Navigation.navigate(Screen.MEAL_DELIVERY_ORDER_CONFIRMATION);

          } catch (Exception e) {
            e.printStackTrace();
          }
          Label confirm = new Label();
          Label thanks = new Label();
          confirm.setText("Order Submitted!");
          thanks.setText("Thank you for your order!");
          confirm.setStyle("-fx-font-size: 48;");
          thanks.setStyle("-fx-font-size:36; -fx-font-style: italic;");

          allThings.getChildren().clear();
          allThings.getChildren().add(thanks);
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

      ImageView foodImage = new ImageView();
      Image image = new Image(Main.class.getResource(aFood.getImage()).toString());
      foodImage.setImage(image);
      foodImage.setStyle("-fx-background-radius: 10 10 10 10;");

      foodImage.setFitHeight(160);
      foodImage.setFitWidth(160);
      foodImage.setPreserveRatio(false);

      HBox newRow = new HBox();
      newRow.setSpacing(100);
      newRow.setMaxWidth(1000);
      newRow.setMaxHeight(300);

      VBox itemInfo = new VBox();
      itemInfo.setSpacing(10);
      itemInfo.setPrefWidth(1500);
      itemInfo.setMaxHeight(100);

      HBox pricePrep = new HBox();
      pricePrep.setSpacing(40);
      pricePrep.setMaxWidth(1500);
      // pricePrep.setPadding(new Insets(10,50,50,50));

      newItemName.setText(aFood.getFoodName());
      newItemName.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-weight: bold; -fx-font-style: open sans");

      newItemQuantity.setText(String.valueOf("Quantity: " + aFood.getQuantity() + "x"));

      newItemQuantity.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 18px; -fx-font-style: open sans;");

      newItemPrice.setText("$ " + String.format("%.02f", aFood.getFoodPrice()));
      newItemPrice.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 18px; -fx-font-style: open sans");

      newItemPrepTime.setText(String.valueOf(aFood.getFoodPrepTime() + " minutes"));
      newItemPrepTime.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 18px; -fx-font-style: open sans");

      newItemDescription.setText(String.valueOf(aFood.getFoodDescription()));
      newItemDescription.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 18px; -fx-font-style: open sans; -fx-wrap-text: true;");

      // orderVBox.getChildren().add(newRow);

      items.getChildren().add(newRow);
      newRow.getChildren().add(foodImage);
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
