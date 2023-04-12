package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;
import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.flowerCart;
import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.deliveryRoom;
import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.recipient;

import edu.wpi.teamname.ServiceRequests.flowers.FlowerDelivery;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Status;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlowerConfirmationController {
  @FXML ImageView backicon;
  @FXML ImageView exiticon;
  @FXML MFXButton flowerbutton;
  @FXML ImageView helpicon;
  @FXML ImageView homeicon;
  @FXML MFXButton mealbutton;
  @FXML MFXButton navigationbutton;
  @FXML MFXButton roombutton;
  @FXML MFXButton signagebutton;
  @FXML ImageView topbarlogo;

  public void initialize() {
    backicon.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
    exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
    homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

    addFlowerDelivery();
  }

  void addFlowerDelivery() {
    FlowerDelivery fd = new FlowerDelivery(flowerCart, LocalDate.now(), LocalTime.now(), deliveryRoom, "user", "name", recipient, )
  }

}
