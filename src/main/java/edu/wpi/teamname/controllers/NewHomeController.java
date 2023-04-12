package edu.wpi.teamname.controllers;

import edu.wpi.teamname.Main;
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
    @FXML ImageView userIcon;
    @FXML ImageView helpIcon;
    @FXML ImageView mealIcon;
    @FXML ImageView conferenceIcon;
    @FXML ImageView flowerIcon;
    @FXML ImageView officeIcon;
    @FXML ImageView furnitureIcon;


    @FXML
    public void initialize() {
        mealRequestsButton.setOnMouseClicked(event -> goToMealPage());
        reserveRoomButton.setOnMouseClicked(event -> goToRoomPage());
       // flowerRequestsButton.setOnMouseClicked(event -> goToFlowerPage());
       // officeRequestsButton.setOnMouseClicked(event -> goToOfficePage());
       // furnitureRequestsButton.setOnMouseClicked(event -> goToFurniturePage());
        signageButton.setOnMouseClicked(event -> goToSignagePage());
        navigationButton.setOnMouseClicked(event -> goToNavigationPage());

        Image MealIcon =
                new Image(Main.class.getResource("./HomepageImages/MealIcon.png").toString());
        mealIcon.setImage(MealIcon);

        Image ConferenceIcon =
                new Image(Main.class.getResource("./HomepageImages/ConferenceRoom.png").toString());
        conferenceIcon.setImage(ConferenceIcon);

        Image FlowerIcon =
                new Image(Main.class.getResource("./HomepageImages/FlowerIcon.png").toString());
        flowerIcon.setImage(FlowerIcon);

        Image OfficeIcon =
                new Image(Main.class.getResource("./HomepageImages/OfficeSupplyIcon.png").toString());
        officeIcon.setImage(OfficeIcon);

        Image FurnitureIcon =
                new Image(Main.class.getResource("./HomepageImages/FurnitureIcon2.png").toString());
        furnitureIcon.setImage(FurnitureIcon);

        Image UserIcon =
                new Image(Main.class.getResource("./images/usericon.png").toString());
        userIcon.setImage(UserIcon);

        Image HelpIcon =
                new Image(Main.class.getResource("./HomepageImages/NewHelpIcon.png").toString());
        helpIcon.setImage(HelpIcon);


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

//    public void goToFlowerPage() {
//        Navigation.navigate(Screen.);
//    }

//    public void goToOfficePage() {
//        Navigation.navigate(Screen.MEAL_DELIVERY);
//    }
//
//    public void goToFurniturePage() {
//        Navigation.navigate(Screen.LOGIN_PAGE);
//    }


}
