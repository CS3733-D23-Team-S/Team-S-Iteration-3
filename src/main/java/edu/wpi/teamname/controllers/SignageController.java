package edu.wpi.teamname.controllers;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class SignageController {
  @FXML MFXButton signageButton;
  @FXML MFXButton signageBack;

  @FXML
  public void initialize() {
    signageBack.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));
  }
}
