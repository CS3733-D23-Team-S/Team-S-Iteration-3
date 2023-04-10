package edu.wpi.teamname.controllers;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminController {
  @FXML ImageView homeIcon;
  @FXML ImageView profileIcon;
  @FXML ImageView helpIcon;
  @FXML ImageView backIcon;
  @FXML MFXButton submittedMealButton;
  @FXML MFXButton submittedRoomButton;
  @FXML MFXButton mapEditorButton;
  @FXML MFXButton navigationButton;
  @FXML MFXButton signageButton;
  @FXML MFXButton mealButton;
  @FXML MFXButton roomButton;
  @FXML MFXButton flowerButton;


  @FXML

  public void initialize() {
    homeIcon.setOnMouseClicked(event -> goToHomePage());
    helpIcon.setOnMouseClicked(event -> goToHelpPage());
    backIcon.setOnMouseClicked(event -> goToLoginPage());
    submittedMealButton.setOnMouseClicked(event -> goToSubmittedMealRequestsPage());
    submittedRoomButton.setOnMouseClicked(event -> goToSubmittedRoomRequestsPage());
    mapEditorButton.setOnMouseClicked(event -> goToMapEditorPage());

    Image HomeIcon = new Image(Main.class.getResource("./images/Home-icon.svg.png").toString());
    homeIcon.setImage(HomeIcon);

    Image HelpIcon =
        new Image(
            Main.class
                .getResource("./HomepageImages/1200px-Icon-round-Question_mark.svg.png")
                .toString());
    helpIcon.setImage(HelpIcon);

    Image ProfileIcon = new Image(Main.class.getResource("./images/ProfileIcon.png").toString());
    profileIcon.setImage(ProfileIcon);
  }

  public void goToHomePage() {
    Navigation.navigate(Screen.HOME);
  }

  public void goToHelpPage() {
    Navigation.navigate(Screen.HELP_PAGE);
  }

  public void goToSubmittedMealRequestsPage() {
    System.out.println("i am running");
    Navigation.navigate(Screen.SUBMITTED_MEAL_REQUESTS);
  }

  public void goToSubmittedRoomRequestsPage() {
    Navigation.navigate(Screen.SUBMITTED_ROOM_REQUESTS);
  }

  public void goToMapEditorPage() {
    Navigation.navigate(Screen.MAP_EDITOR);
  }
  public void goToLoginPage() {
    Navigation.navigate(Screen.LOGIN_PAGE);
  }
}
