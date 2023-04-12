package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class MealDeliveryOrderConfirmationController {

  @FXML private MFXButton homeButton;
  // @FXML private MFXButton flowerbutton1;
  @FXML private MFXButton roomButton1;
  @FXML private MFXButton mealbutton;
  @FXML private MFXButton signagePage1;
  // @FXML private MFXButton navigation1;
  @FXML private MFXButton backButton4;
  @FXML private MFXButton cancel;

  @FXML
  public void initialize() {

    homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    // flowerbutton1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    roomButton1.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    mealbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    signagePage1.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    // navigation1.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    backButton4.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    cancel.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));
  }
}
