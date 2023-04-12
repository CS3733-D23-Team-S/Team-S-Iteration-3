package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerDeliveryController.flowerID;
import static edu.wpi.teamname.navigation.Screen.*;
import static edu.wpi.teamname.navigation.Screen.HELP_PAGE;

import edu.wpi.teamname.ServiceRequests.flowers.Cart;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class FlowerOrderDetailsController {
  public static Cart flowerCart = new Cart(1);
  public static int flowerID;

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

  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {
    addtocartbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
    backicon.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
    exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
    homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

    addtocartbutton.setOnMouseClicked(event -> createDelivery());
    clearbutton.setOnMouseClicked(event -> clearFields());

    showInfo();
  }

  private void showInfo() {
    namelabel.setText(dbr.flowerRetrieve(flowerID).getName());
    descriptionlabel.setText(dbr.flowerRetrieve(flowerID).getDescription());
    pricelabel.setText("$" + dbr.flowerRetrieve(flowerID).getPrice());
    sizelabel.setText(dbr.flowerRetrieve(flowerID).getSize().toString());
  }

  private void createDelivery() {
    Flower flower = dbr.flowerRetrieve(flowerID);
    flower.setQuantity(Integer.parseInt(quantityfield.getText()));
    flower.setMessage(custommessagefield.getText());
    flowerCart.addFlowerItem(flower);
  }

  public void clearFields() {
    quantityfield.clear();
    sizefield.clear();
    custommessagefield.clear();
  }
}
