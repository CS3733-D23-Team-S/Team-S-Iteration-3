package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RootLogoutPopupController extends PopUpController {
  @FXML private Button buttonYes;
  @FXML private Button buttonNo;

  public void initialize() {

    buttonYes.setOnMouseClicked(
        event -> {
          ActiveUser.getInstance().setCurrentUser(null);
          ActiveUser.getInstance().setLoggedIn(false);
          stage.close();
          Navigation.navigate(Screen.SIGNAGE_PAGE);
        });

    buttonNo.setOnMouseClicked(
        event -> {
          stage.close();
        });
  }
}
