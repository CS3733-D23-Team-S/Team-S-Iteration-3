package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class AboutUsController extends PopUpController {

  @FXML MFXButton homeBtn;
  // @FXML Text scrum;

  public void initialize() {

    Screen current;
    if (ActiveUser.getInstance().getCurrentUser().getPermission() == Permission.STAFF) {
      current = Screen.STAFFHOME;
    } else {
      current = Screen.NEW_ADMIN_PAGE;
    }
    ActiveUser.getInstance().getCurrentUser().getPermission();
    homeBtn.setOnMouseClicked(
        event -> {
          Navigation.navigate(current);
          stage.close();
        });

    // scrum.setStyle("-fx-font-weight: bold");
  }
}
