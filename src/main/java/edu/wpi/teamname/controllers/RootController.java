package edu.wpi.teamname.controllers;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class RootController {
  @FXML ImageView homeIcon;
  @FXML ImageView userIcon;
  @FXML ImageView exitIcon;
  @FXML ImageView backIcon;
  @FXML Label navLabel;
  @FXML Label signageLabel;
  @FXML Label mealLabel;
  @FXML Label roomLabel;
  @FXML Label flowerLabel;
  @FXML ImageView helpIcon;

  public void initialize() {
    homeIcon.setOnMouseClicked(event -> gotoHomePage());
    exitIcon.setOnMouseClicked(event -> exitApp());
    // backIcon.setOnMouseClicked(event -> back());
    navLabel.setOnMouseClicked(event -> gotoNavigation());
    signageLabel.setOnMouseClicked(event -> gotoSignage());
    mealLabel.setOnMouseClicked(event -> gotoMealRequest());
    roomLabel.setOnMouseClicked(event -> gotoRoomBooking());
    flowerLabel.setOnMouseClicked(event -> gotoFlowerRequest());
    helpIcon.setOnMouseClicked(event -> gotoHelpPage());
  }

  public void gotoHomePage() {
    Navigation.navigate(Screen.HOME);
  }

  public void exitApp() {
    Platform.exit();
  }

  // public void back(){Navigation.navigate(Screen.);}
  // depends on each page
  public void gotoNavigation() {
    Navigation.navigate(Screen.PATHFINDING);
  }

  public void gotoSignage() {
    Navigation.navigate(Screen.SIGNAGE_PAGE);
  }

  public void gotoMealRequest() {
    Navigation.navigate(Screen.MEAL_DELIVERY1);
  }

  public void gotoRoomBooking() {
    Navigation.navigate(Screen.ROOM_BOOKING);
  }

  public void gotoFlowerRequest() {
    Navigation.navigate(Screen.FLOWER_DELIVERY);
  }

  public void gotoHelpPage() {
    Navigation.navigate(Screen.HELP_PAGE);
  }
}
