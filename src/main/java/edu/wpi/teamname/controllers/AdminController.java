package edu.wpi.teamname.controllers;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminController {
  @FXML ImageView homeIcon;
  @FXML ImageView userIcon;
  @FXML ImageView helpIcon;
  @FXML ImageView backIcon;
  @FXML ImageView exitIcon;
  @FXML ImageView topbarlogo;
  @FXML ImageView hospitalLogo;
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
    exitIcon.setOnMouseClicked(event -> exitApplication());
    submittedMealButton.setOnMouseClicked(event -> goToSubmittedMealRequestsPage());
    submittedRoomButton.setOnMouseClicked(event -> goToSubmittedRoomRequestsPage());
    mapEditorButton.setOnMouseClicked(event -> goToMapEditorPage());
    navigationButton.setOnMouseClicked(event -> goToPathfindingPage());
    signageButton.setOnMouseClicked(event -> goToSignagePage());
    mealButton.setOnMouseClicked(event -> goToMealDeliveryPage());
    roomButton.setOnMouseClicked(event -> goToRoomReservationPage());

    Image HomeIcon = new Image(Main.class.getResource("./images/homeicon.png").toString());
    homeIcon.setImage(HomeIcon);

    Image HelpIcon = new Image(Main.class.getResource("./images/helpicon.png").toString());
    helpIcon.setImage(HelpIcon);

    Image ProfileIcon = new Image(Main.class.getResource("./images/usericon.png").toString());
    userIcon.setImage(ProfileIcon);

    Image BackIcon = new Image(Main.class.getResource("./images/backicon.png").toString());
    backIcon.setImage(BackIcon);

    Image TopBarIcon = new Image(Main.class.getResource("./images/topbarlogo.png").toString());
    topbarlogo.setImage(TopBarIcon);
  }
  // functions to go to pages
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

  public void exitApplication() {
    Platform.exit();
  }

  public void goToSignagePage() {
    Navigation.navigate(Screen.SIGNAGE_PAGE);
  }

  public void goToPathfindingPage() {
    Navigation.navigate(Screen.PATHFINDING);
  }

  public void goToMealDeliveryPage() {
    Navigation.navigate(Screen.MEAL_DELIVERY1);
  }

  public void goToRoomReservationPage() {
    Navigation.navigate(Screen.ROOM_BOOKING);
  }
}
