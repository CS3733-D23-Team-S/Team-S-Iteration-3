package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerDeliveryController.flowerID;
import static edu.wpi.teamname.navigation.Screen.*;
import static edu.wpi.teamname.navigation.Screen.HELP_PAGE;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.flowers.Cart;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class FlowerOrderDetailsController {
  public static Cart flowerCart = new Cart(1);
  public static String recipient;

  @FXML MFXButton addtocartbutton;
  @FXML ImageView backicon;
  @FXML MFXButton clearbutton;
  @FXML MFXTextField custommessagefield;
  @FXML Label descriptionlabel;
  @FXML ImageView exiticon;
  @FXML MFXButton flowerbutton;
  @FXML ImageView helpicon;
  @FXML ImageView homeicon;
  @FXML MFXButton mealbutton;
  @FXML Label namelabel;
  @FXML MFXButton navigationbutton;
  @FXML Label pricelabel;
  @FXML MFXTextField quantityfield;
  @FXML MFXButton roombutton;
  @FXML MFXButton signagebutton;
  @FXML MFXTextField sizefield;
  @FXML ImageView topbarlogo;
  @FXML Label sizelabel;

  @FXML TextField recipientfield;

  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {
    addtocartbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
    backicon.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
    exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
    homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

    addtocartbutton.setOnMouseClicked(event -> createDelivery());
    clearbutton.setOnMouseClicked(event -> clearFields());

    navigationbutton.setOnMouseClicked(event -> Navigation.navigate(PATHFINDING));
    signagebutton.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    mealbutton.setOnMouseClicked(event -> Navigation.navigate(MEAL_DELIVERY1));
    roombutton.setOnMouseClicked(event -> Navigation.navigate(ROOM_BOOKING));
    flowerbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));

    showInfo();
  }

  private void showInfo() {
    namelabel.setText(dbr.getFlowerDAO().get(flowerID).getName());
    descriptionlabel.setText(dbr.getFlowerDAO().get(flowerID).getDescription());
    pricelabel.setText("$" + dbr.getFlowerDAO().get(flowerID).getPrice());
    sizelabel.setText(dbr.getFlowerDAO().get(flowerID).getSize().toString());
  }

  private void createDelivery() {
    Flower flower = dbr.getFlowerDAO().get(flowerID);
    flower.setQuantity(Integer.parseInt(quantityfield.getText()));
    flower.setMessage(custommessagefield.getText());
    flowerCart.addFlowerItem(flower);
    Navigation.navigate(FLOWER_DELIVERY);

    // room = .getText get room from drop down box in the thingy
    recipient = recipientfield.getText();
  }

  public void clearFields() {
    quantityfield.clear();
    sizefield.clear();
    custommessagefield.clear();
  }
}
