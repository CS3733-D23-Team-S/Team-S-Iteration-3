package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

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
  @FXML ImageView officeIcon;

  // top bar icons
  @FXML ImageView homeLogo1;
  @FXML ImageView homeLogo2;
  @FXML ImageView homeIcon;
  @FXML ImageView userIcon;
  @FXML ImageView exitIcon;
  @FXML ImageView backIcon;

  public void initialize() {

    //   ft.setAutoReverse(true);

    // NAVIGATION BAR

    menunavigation.setOnMouseClicked(
        event -> {
          changeMenuItem(menunavigation);
          Image i = new Image("@../../edu/wpi/teamname/templateIcons/invertedmenu/navinverted.png");
          navIcon.setImage(i);
          Navigation.navigate(Screen.PATHFINDING);
        });

    menusignage.setOnMouseClicked(
        event -> {
          changeMenuItem(menusignage);
          Image i =
              new Image("@../../edu/wpi/teamname/templateIcons/invertedmenu/signageinverted.png");
          signageIcon.setImage(i);
          Navigation.navigate(Screen.SIGNAGE_PAGE);
        });

    menumeal.setOnMouseClicked(
        event -> {
          changeMenuItem(menumeal);
          Image i =
              new Image("@../../edu/wpi/teamname/templateIcons/invertedmenu/mealinverted.png");
          mealIcon.setImage(i);
          Navigation.navigate(Screen.MEAL_DELIVERY1);
        });

    menuroom.setOnMouseClicked(
        event -> {
          changeMenuItem(menuroom);
          Image i =
              new Image("@../../edu/wpi/teamname/templateIcons/invertedmenu/roominverted.png");
          roomIcon.setImage(i);
          Navigation.navigate(Screen.ROOM_BOOKING);
        });

    menuflower.setOnMouseClicked(
        event -> {
          changeMenuItem(menuflower);
          Image i =
              new Image("@../../edu/wpi/teamname/templateIcons/invertedmenu/flowerinverted.png");
          flowerIcon.setImage(i);
          Navigation.navigate(Screen.FLOWER_DELIVERY);
        });

    menuoffice.setOnMouseClicked(
        event -> {
          changeMenuItem(menuoffice);
          Image i = new Image("@../../edu/wpi/teamname/templateIcons/invertedmenu/peninverted.png");
          officeIcon.setImage(i);
          Navigation.navigate(Screen.OFFICE_SUPPLIES_DELIVERY);
        });

    // TOP BAR
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
          if (ActiveUser.getInstance().getCurrentUser().getPermission() == Permission.ADMIN) {
            System.out.println("Going to Admin page");
            Navigation.navigate(Screen.ADMIN_PAGE);
          }
          if (ActiveUser.getInstance().getCurrentUser().getPermission() == Permission.STAFF) {
            System.out.println("Going to Staff page");
            Navigation.navigate(Screen.STAFF);
          }

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

    // BACK ICON
    /*
    backIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.ADMIN_PAGE); // TODO fix!!
          event.consume();
        });

       */
  }

  public void clearMenuClasses() {
    // clear menu items
    menunavigation.getStyleClass().remove("selectedtab");
    menusignage.getStyleClass().remove("selectedtab");
    menumeal.getStyleClass().remove("selectedtab");
    menuroom.getStyleClass().remove("selectedtab");
    menuflower.getStyleClass().remove("selectedtab");
    menuoffice.getStyleClass().remove("selectedtab");

    // reset photos
    Image h = new Image("@../../edu/wpi/teamname/templateIcons/navicon.png");
    navIcon.setImage(h);
    Image j = new Image("@../../edu/wpi/teamname/templateIcons/signageicon.png");
    signageIcon.setImage(j);
    Image k = new Image("@../../edu/wpi/teamname/templateIcons/mealicon.png");
    mealIcon.setImage(k);
    Image l = new Image("@../../edu/wpi/teamname/templateIcons/roomicon.png");
    roomIcon.setImage(l);
    Image m = new Image("@../../edu/wpi/teamname/templateIcons/flowericon.png");
    flowerIcon.setImage(m);
    Image n = new Image("@../../edu/wpi/teamname/templateIcons/pen.png");
    officeIcon.setImage(n);
  }

  public void hoverTransition(Pane pane) {
    clearMenuClasses();
    FadeTransition ft = new FadeTransition(Duration.millis(300), pane);
    ft.setFromValue(0.3);
    ft.setToValue(0.5);
    ft.play();
  }

  public void changeMenuItem(Pane pane) {
    clearMenuClasses();
    FadeTransition ft = new FadeTransition(Duration.millis(1000), pane);
    ft.setFromValue(0.5);
    ft.setToValue(1.0);
    ft.play();
    pane.getStyleClass().add("selectedtab");
  }
}
