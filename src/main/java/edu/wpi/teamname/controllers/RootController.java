package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.Permission;
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

  String backPath = "views/Home.fxml";

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
          if (ActiveUser.getInstance().getCurrentUser().getPermission() == Permission.ADMIN) {
            System.out.println("Going to Admin page");
            Navigation.navigate(Screen.HOME);
          }
          if (ActiveUser.getInstance().getCurrentUser().getPermission() == Permission.STAFF) {
            System.out.println("Going to Staff page");
            Navigation.navigate(Screen.HOME);
          }
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
