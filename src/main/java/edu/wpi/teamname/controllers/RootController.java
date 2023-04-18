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
  @FXML Pane menuoffice;

  // icons
  @FXML ImageView navIcon;
  @FXML ImageView signageIcon;
  @FXML ImageView mealIcon;
  @FXML ImageView roomIcon;
  @FXML ImageView flowerIcon;
  @FXML ImageView menuIcon;

  // top bar icons
  @FXML ImageView homeLogo1;
  @FXML ImageView homeLogo2;
  @FXML ImageView homeIcon;
  @FXML ImageView userIcon;
  @FXML ImageView exitIcon;
  @FXML ImageView backIcon;

  public void initialize() {

    menunavigation.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDING));
    menusignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    menumeal.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    menuroom.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    menuflower.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_DELIVERY));
    menuoffice.setOnMouseClicked(event -> Navigation.navigate(Screen.OFFICE_SUPPLIES_DELIVERY));

    homeLogo1.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.ADMIN_PAGE);
          event.consume();
        });
    homeLogo2.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.ADMIN_PAGE);
          event.consume();
        });
    homeIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.ADMIN_PAGE);
          event.consume();
        });
    userIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.USER_PROFILE);
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
          Navigation.navigate(Screen.ADMIN_PAGE);
          event.consume();
        });
  }
}
