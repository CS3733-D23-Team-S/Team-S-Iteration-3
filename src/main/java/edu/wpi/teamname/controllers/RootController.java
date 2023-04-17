package edu.wpi.teamname.controllers;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class RootController {

  // menu items
  @FXML Pane menunavigation;
  @FXML Pane menusignage;
  @FXML Pane menumeal;
  @FXML Pane menuroom;
  @FXML Pane menuflower;

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

    // invert icons
    menunavigation.setOnMouseEntered(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(250);
            // colorAdjust.setHue(400);

            navIcon.setEffect(colorAdjust);

            menunavigation.setStyle("-fx-background-color:#000000;");
          }
        });

    menunavigation.setOnMouseExited(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0.1);

            menunavigation.setStyle("-fx-background-color:#dae7f3;");
          }
        });
  }
}
