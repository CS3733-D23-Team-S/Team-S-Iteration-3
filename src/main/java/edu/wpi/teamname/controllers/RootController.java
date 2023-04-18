package edu.wpi.teamname.controllers;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class RootController {

  // menu items
  @FXML Pane menunavigation;
  @FXML Pane menusignage;
  @FXML Pane menumeal;
  @FXML Pane menuroom;
  @FXML Pane menuflower;
  @FXML Pane menugame;

  // icons
  @FXML ImageView navIcon;
  @FXML ImageView signageIcon;
  @FXML ImageView mealIcon;
  @FXML ImageView roomIcon;
  @FXML ImageView flowerIcon;

  // top bar icons
  @FXML ImageView homeLogo1;
  @FXML ImageView homeLogo2;
  @FXML ImageView homeIcon;
  @FXML ImageView userIcon;
  @FXML ImageView exitIcon;
  @FXML ImageView backIcon;

  String backPath = "views/Home.fxml";

  public void initialize() {

    menunavigation.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDING));
    menusignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    menumeal.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    menuroom.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    menuflower.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_DELIVERY));
    menugame.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    homeLogo1.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.HOME);
          event.consume();
        });
    homeLogo2.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.HOME);
          event.consume();
        });
    homeIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.HOME);
          event.consume();
        });
    userIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.HOME);
          event.consume();
        });
    exitIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Platform.exit();
          event.consume();
        });
    backIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.valueOf(backPath));
          event.consume();
        });
  }
}
