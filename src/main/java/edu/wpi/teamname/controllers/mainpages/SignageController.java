package edu.wpi.teamname.controllers.mainpages;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SignageController {
  @FXML Label tapButton;

  //  @FXML
  //  public void initialize() {
  //    signageBack.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));
  //  }

  public void initialize() {
    // label button
    tapButton.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));
  }
}
