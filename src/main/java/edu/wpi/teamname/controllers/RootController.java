package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
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
  @FXML ImageView helpIcon;

  // top bar icons
  @FXML ImageView homeLogo1;
  @FXML ImageView homeLogo2;
  @FXML ImageView homeIcon;
  @FXML ImageView userIcon;
  @FXML ImageView logoutIcon;
  @FXML ImageView backIcon;

  public void initialize() {

    //   ft.setAutoReverse(true);

    // NAVIGATION BAR

    menunavigation.setOnMouseClicked(
        event -> {
          changeMenuItem(menunavigation);
          Image i =
              new Image(
                  String.valueOf(
                      Main.class.getResource("templateIcons/invertedmenu/navinverted.png")));
          navIcon.setImage(i);
          Navigation.navigate(Screen.PATHFINDING);
        });

    menusignage.setOnMouseClicked(
        event -> {
          changeMenuItem(menusignage);
          Image i =
              new Image(
                  String.valueOf(
                      Main.class.getResource("templateIcons/invertedmenu/signageinverted.png")));
          signageIcon.setImage(i);
          Navigation.navigate(Screen.SIGNAGE_PAGE);
        });

    menumeal.setOnMouseClicked(
        event -> {
          changeMenuItem(menumeal);
          Image i =
              new Image(
                  String.valueOf(
                      Main.class.getResource("templateIcons/invertedmenu/mealinverted.png")));
          mealIcon.setImage(i);
          Navigation.navigate(Screen.MEAL_DELIVERY);
        });

    menuroom.setOnMouseClicked(
        event -> {
          changeMenuItem(menuroom);
          Image i =
              new Image(
                  String.valueOf(
                      Main.class.getResource("templateIcons/invertedmenu/roominverted.png")));
          roomIcon.setImage(i);
          Navigation.navigate(Screen.ROOM_BOOKING);
        });

    menuflower.setOnMouseClicked(
        event -> {
          changeMenuItem(menuflower);
          Image i =
              new Image(
                  String.valueOf(
                      Main.class.getResource("templateIcons/invertedmenu/flowerinverted.png")));
          flowerIcon.setImage(i);
          Navigation.navigate(Screen.FLOWER_DELIVERY);
        });

    menuoffice.setOnMouseClicked(
        event -> {
          changeMenuItem(menuoffice);
          Image i =
              new Image(
                  String.valueOf(
                      Main.class.getResource("templateIcons/invertedmenu/peninverted.png")));
          officeIcon.setImage(i);
          Navigation.navigate(Screen.OFFICE_SUPPLIES_DELIVERY);
        });

    helpIcon.setOnMouseClicked(
        event -> {
          // changeMenuItem(menuoffice);
          Image i = new Image(String.valueOf(Main.class.getResource("templateIcons/helpicon.png")));
          helpIcon.setImage(i);
          Navigation.navigate(Screen.HELP_PAGE);
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
            Navigation.navigate(Screen.STAFFHOME);
          }

          event.consume();
        });

    userIcon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.USER_PROFILE);
          event.consume();
        });

    // Logout Button
    logoutIcon.setOnMouseClicked(
        event -> {
          Navigation.launchPopUp(Screen.ROOT_LOGOUT_POPUP);
        });

    userIcon.setCursor(Cursor.HAND);
    homeIcon.setCursor(Cursor.HAND);
    logoutIcon.setCursor(Cursor.HAND);

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
    Image h = new Image(String.valueOf(Main.class.getResource("templateIcons/navicon.png")));
    navIcon.setImage(h);
    Image j = new Image(String.valueOf(Main.class.getResource("templateIcons/signageicon.png")));
    signageIcon.setImage(j);
    Image k = new Image(String.valueOf(Main.class.getResource("templateIcons/mealicon.png")));
    mealIcon.setImage(k);
    Image l = new Image(String.valueOf(Main.class.getResource("templateIcons/roomicon.png")));
    roomIcon.setImage(l);
    Image m = new Image(String.valueOf(Main.class.getResource("templateIcons/flowericon.png")));
    flowerIcon.setImage(m);
    Image n = new Image(String.valueOf(Main.class.getResource("templateIcons/pen.png")));
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
