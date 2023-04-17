package edu.wpi.teamname.controllers;

import edu.wpi.teamname.App;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.FoodService.OrderItem;
import edu.wpi.teamname.ServiceRequests.flowers.Cart;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NewHomeController {
  @FXML MFXButton mealRequestsButton;
  @FXML MFXButton reserveRoomButton;
  @FXML MFXButton flowerRequestsButton;
  @FXML MFXButton officeRequestsButton;
  @FXML MFXButton furnitureRequestsButton;
  @FXML MFXButton signageButton;
  @FXML MFXButton navigationButton;
  @FXML MFXButton adminButton;
  @FXML ImageView userIcon;
  @FXML ImageView helpIcon;
  @FXML ImageView mealIcon;
  @FXML ImageView conferenceIcon;
  @FXML ImageView flowerIcon;
  @FXML ImageView officeIcon;
  @FXML ImageView furnitureIcon;

  @FXML MFXButton navbutton;
  @FXML MFXButton navigationbutton1;
  @FXML MFXButton mealbutton;
  @FXML MFXButton roombutton;
  @FXML MFXButton flowerbutton;
  @FXML ImageView homeicon1;
  @FXML ImageView back5;
  @FXML ImageView homeicon;
  // @FXML ImageView backicon;
  @FXML ImageView backicon1;
  @FXML ImageView helpicon;

  @FXML DataBaseRepository DBR = DataBaseRepository.getInstance();

  public static int cartID = 0;

  public static int delID;
  public static int flowDevID;

  public static OrderItem cart;

  public static Cart flowerCart;

  @FXML
  public void initialize() {

    delID = DBR.getLastFoodDevID();
    flowDevID = DBR.flowerGetNewDeliveryID();

    cart = new OrderItem(cartID++);
    flowerCart = new Cart(cartID++);
    // back5.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    mealRequestsButton.setOnMouseClicked(event -> goToMealPage());
    reserveRoomButton.setOnMouseClicked(event -> goToRoomPage());
    flowerRequestsButton.setOnMouseClicked(event -> goToFlowerPage());
    // officeRequestsButton.setOnMouseClicked(event -> goToOfficePage());
    // furnitureRequestsButton.setOnMouseClicked(event -> goToFurniturePage());
    signageButton.setOnMouseClicked(event -> goToSignagePage());
    navigationButton.setOnMouseClicked(event -> goToNavigationPage());
    adminButton.setOnMouseClicked(event -> goToAdminPage());

    Image MealIcon = new Image(Main.class.getResource("HomepageImages/MealIcon.png").toString());
    mealIcon.setImage(MealIcon);

    Image ConferenceIcon =
        new Image(Main.class.getResource("HomepageImages/ConferenceRoom.png").toString());
    conferenceIcon.setImage(ConferenceIcon);

    Image FlowerIcon =
        new Image(Main.class.getResource("HomepageImages/FlowerIcon.png").toString());
    flowerIcon.setImage(FlowerIcon);

    Image OfficeIcon =
        new Image(Main.class.getResource("HomepageImages/OfficeSupplyIcon.png").toString());
    officeIcon.setImage(OfficeIcon);

    Image FurnitureIcon =
        new Image(Main.class.getResource("HomepageImages/FurnitureIcon2.png").toString());
    furnitureIcon.setImage(FurnitureIcon);

    navbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDING));
    navigationbutton1.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    mealbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    roombutton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    flowerbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_DELIVERY));

    homeicon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.HOME);
          event.consume();
        });

    backicon1.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          App.getPrimaryStage().close();
          event.consume();
        });

    /*backicon1.addEventHandler(
       javafx.scene.input.MouseEvent.MOUSE_CLICKED,
       event -> {
         Navigation.navigate(Screen.SIGNAGE_PAGE);
         event.consume();
       });

    */
    back5.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.SIGNAGE_PAGE);
          event.consume();
        });

    // Image UserIcon = new Image(Main.class.getResource("./images/usericon.png").toString());
    // userIcon.setImage(UserIcon);

    // Image HelpIcon =
    //   new Image(Main.class.getResource("./HomepageImages/NewHelpIcon.png").toString());
    // helpIcon.setImage(HelpIcon);
  }

  public void goToRoomPage() {
    Navigation.navigate(Screen.ROOM_BOOKING);
  }

  public void goToMealPage() {
    Navigation.navigate(Screen.MEAL_DELIVERY1);
  }

  public void goToSignagePage() {
    Navigation.navigate(Screen.SIGNAGE_PAGE);
  }

  public void goToNavigationPage() {
    Navigation.navigate(Screen.PATHFINDING);
  }

  public void goToAdminPage() {
    Navigation.navigate(Screen.ADMIN_PAGE);
  }

  public void goToFlowerPage() {
    Navigation.navigate(Screen.FLOWER_DELIVERY);
  }

  //    public void goToOfficePage() {
  //        Navigation.navigate(Screen.MEAL_DELIVERY);
  //    }
  //
  //    public void goToFurniturePage() {
  //        Navigation.navigate(Screen.LOGIN_PAGE);
  //    }

}
