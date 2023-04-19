package edu.wpi.teamname.controllers.mainpages;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.Permission;
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
    tapButton.setOnMouseClicked(
        event -> {
          if (ActiveUser.getInstance().isLoggedIn()) {
            System.out.println(ActiveUser.getInstance().getPermission().name());
            if (ActiveUser.getInstance().getPermission() == Permission.ADMIN) {
              Navigation.navigate(Screen.ADMIN_PAGE);
            } else {
              Navigation.navigate(Screen.STAFF);
            }
          } else Navigation.navigate(Screen.LOGIN_PAGE);
        });
  }
}
