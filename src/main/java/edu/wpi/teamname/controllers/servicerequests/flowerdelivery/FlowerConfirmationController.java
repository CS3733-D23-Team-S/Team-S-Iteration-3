package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.flowerCart;
import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerOrderDetailsController.recipient;
import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerSubmissionController.deliveryRoom;
import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Status;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.ServiceRequests.flowers.FlowerDelivery;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

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
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {
    backicon.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
    exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
    homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

    navigationbutton.setOnMouseClicked(event -> Navigation.navigate(PATHFINDING));
    signagebutton.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    mealbutton.setOnMouseClicked(event -> Navigation.navigate(MEAL_DELIVERY1));
    roombutton.setOnMouseClicked(event -> Navigation.navigate(ROOM_BOOKING));
    flowerbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));

    addFlowerDelivery();
    clearCart();
  }

  private void addFlowerDelivery() {
    System.out.println(
        dbr.flowerGetNewDeliveryID()
            + " "
            + flowerCart.toString()
            + " "
            + new java.sql.Date(System.currentTimeMillis())
            + " "
            + new java.sql.Time(System.currentTimeMillis())
            + " "
            + deliveryRoom
            + " "
            + "user"
            + " "
            + recipient
            + " "
            + Status.InProgress
            + " "
            + calculateTotalCost());

    FlowerDelivery fd =
        new FlowerDelivery(
            dbr.flowerGetNewDeliveryID(),
            flowerCart.toString(),
            new java.sql.Date(System.currentTimeMillis()),
            new java.sql.Time(System.currentTimeMillis()),
            deliveryRoom,
            "user",
            recipient,
            Status.InProgress.toString(),
            calculateTotalCost());

    dbr.flowerDeliveryAdd(fd);
  }

  private int calculateTotalCost() {
    int totalCost = 0;
    for (Flower flower : flowerCart.getCartItems()) {
      totalCost += flower.getPrice();
    }

    return totalCost;
  }

  private void clearCart() {
    flowerCart.setCartItems(new ArrayList<>());
    deliveryRoom = null;
    recipient = null;
  }
}
