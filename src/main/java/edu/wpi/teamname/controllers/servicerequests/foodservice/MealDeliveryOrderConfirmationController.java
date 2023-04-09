package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class MealDeliveryOrderConfirmationController {

  @FXML private MFXButton home1;

  @FXML
  public void initialize() {
    home1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
  }
}
