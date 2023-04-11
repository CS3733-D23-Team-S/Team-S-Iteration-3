package edu.wpi.teamname.controllers.mainpages;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class WelcomeController {

  @FXML MFXButton loginButton;
  @FXML MFXButton signageButton;
  @FXML MFXButton navButton;

  @FXML
  public void initialize() {

    loginButton.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));
    signageButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    navButton.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDING));
  }
}
