package edu.wpi.teamname.controllers.mainpages;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomeController {

  @FXML MFXButton mealDeliveryButton;
  @FXML MFXButton reserveRoomButton;

  @FXML MFXButton logoutButton;

  @FXML ButtonBar buttonBar;

  @FXML MenuButton app;
  @FXML Button buttonBarPathfinding;
  @FXML Button buttonBarReserveRoom;
  @FXML Button buttonBarMealDelivery;
  @FXML MFXButton exportButton;
  @FXML MenuItem exitOption;

  @FXML ImageView helpIcon;

  @FXML ImageView hospitalBg;
  @FXML ImageView pathfindingIcon;
  @FXML ImageView roomreserveIcon;
  @FXML ImageView mealdeliveryIcon;

  @FXML MFXButton homeToPathfindingButton;
  @FXML MFXButton goflower;

  @FXML DataBaseRepository DBR = DataBaseRepository.getInstance();

  // public static int cartID = 0;

  // public static int delID;

  // public static OrderItem cart;

  @FXML
  public void initialize() {

    // delID = DBR.getLastFoodDevID();

    //  cart = new OrderItem(cartID++);

    // Adding the menu option to exit application

    mealDeliveryButton.setOnMouseClicked(event -> goToMealPage());
    reserveRoomButton.setOnMouseClicked(event -> goToRoomPage());
    logoutButton.setOnMouseClicked(event -> goToLoginPage());
    // **helpIcon.setOnMouseClicked(event -> goToHelpPage());
    exitOption.setOnAction(event -> exitApplication());
    homeToPathfindingButton.setOnMouseClicked(event -> Navigation.navigate(PATHFINDING));
    exportButton.setOnMouseClicked(event -> Navigation.launchPopUp(CSV_MANAGE));
    goflower.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));

    // Menu bar button handlers
    buttonBarPathfinding.setOnMouseClicked(event -> Navigation.navigate(PATHFINDING));
    buttonBarMealDelivery.setOnMouseClicked(event -> goToMealPage());
    buttonBarReserveRoom.setOnMouseClicked(event -> goToRoomPage());

    Image hospitalBackground =
        new Image(
            Main.class
                .getResource("./HomepageImages/BrighamandWomensHospitalImage.jpeg")
                .toString());
    hospitalBg.setImage(hospitalBackground);

    Image pathfindIcon =
        new Image(Main.class.getResource("./HomepageImages/PathfindIcon.png").toString());
    pathfindingIcon.setImage(pathfindIcon);

    Image mealIcon = new Image(Main.class.getResource("./HomepageImages/Meal_Icon.jpg").toString());
    mealdeliveryIcon.setImage(mealIcon);

    Image roomIcon =
        new Image(Main.class.getResource("./HomepageImages/Conference_room_icon.jpg").toString());
    roomreserveIcon.setImage(roomIcon);

    Image helpbutton =
        new Image(
            Main.class
                .getResource("./HomepageImages/1200px-Icon-round-Question_mark.svg.png")
                .toString());
    helpIcon.setImage(helpbutton);

    buttonBarPathfinding.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
    mealDeliveryButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY));

    reserveRoomButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
  }

  public void goToRoomPage() {
    Navigation.navigate(Screen.ROOM_BOOKING);
  }

  public void goToMealPage() {
    Navigation.navigate(Screen.MEAL_DELIVERY);
  }

  public void goToLoginPage() {
    Navigation.navigate(Screen.LOGIN_PAGE);
  }

  public void exitApplication() {
    Platform.exit();
  }

  public void goToHelpPage() {
    Navigation.navigate(Screen.HELP_PAGE);
  }
}
