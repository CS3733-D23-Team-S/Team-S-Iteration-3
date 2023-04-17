package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import static edu.wpi.teamname.controllers.servicerequests.flowerdelivery.FlowerDeliveryController.flowerID;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.flowers.Cart;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.controllers.PopUpController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class OfficeSuppliesPopupController extends PopUpController {
  @FXML MFXButton addcartbutton;
  @FXML MFXButton decrementbutton;
  @FXML ImageView officesupplyimage;
  @FXML MFXButton incrementbutton;
  @FXML Label quantitylabel;
  @FXML Label officesupplyname;
  @FXML Text officesupplydescription;

  @FXML Text PriceText;

  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();
  public static Cart officeSupplyCart = new Cart(1);
  public static String recipient;

  public void initialize() {
    showInfo();
    addcartbutton.setOnMouseClicked(event -> createDelivery());
  }

  private void showInfo() {
    officesupplyname.setText(dbr.getFlowerDAO().get(flowerID).getName());
    officesupplydescription.setText(dbr.getFlowerDAO().get(flowerID).getDescription());

    PriceText.setText(String.valueOf(dbr.getFlowerDAO().get(flowerID).getPrice()));

    Image image =
        new Image(Main.class.getResource(dbr.getFlowerDAO().get(flowerID).getImage()).toString());
    officesupplyimage.setImage(image);
  }

  private void createDelivery() {
    Flower flower = dbr.getFlowerDAO().get(flowerID);
    officeSupplyCart.addFlowerItem(flower);
    stage.close();
  }
}
