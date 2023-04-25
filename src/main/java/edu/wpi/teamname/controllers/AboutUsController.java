package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AboutUsController extends PopUpController {

  @FXML Text projManager;
  @FXML Text prodOwner;
  @FXML Text fullEng;

  @FXML MFXButton homeBtn;
  // @FXML Text scrum;

  public void initialize() {

    Screen current;
    if (ActiveUser.getInstance().getCurrentUser().getPermission() == Permission.STAFF) {
      current = Screen.STAFF;
    } else {
      current = Screen.NEW_ADMIN_PAGE;
    }
    ActiveUser.getInstance().getCurrentUser().getPermission();
    homeBtn.setOnMouseClicked(event -> Navigation.navigate(current));

    projManager.setStyle("-fx-font-weight: BOLD");
    prodOwner.setStyle("-fx-font-weight: bold");
    fullEng.setStyle("-fx-font-weight: bold");
    // scrum.setStyle("-fx-font-weight: bold");
  }
}
