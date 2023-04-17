package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import static edu.wpi.teamname.controllers.servicerequests.officesupplies.OfficeSuppliesController.suppliesID;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupplyCart;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupply;
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
  public static OfficeSupplyCart officeSupplyCart = new OfficeSupplyCart(1);
  public static String recipient;

  public void initialize() {
    showInfo();
    addcartbutton.setOnMouseClicked(event -> createDelivery());
  }

  private void showInfo() {
    officesupplyname.setText(dbr.getOfficeSupplyDAO().get(suppliesID).getName());
    officesupplydescription.setText(dbr.getOfficeSupplyDAO().get(suppliesID).getDescription());

    PriceText.setText(String.valueOf(dbr.getOfficeSupplyDAO().get(suppliesID).getPrice()));

    Image image =
        new Image(Main.class.getResource(dbr.getOfficeSupplyDAO().get(suppliesID).getImage()).toString());
    officesupplyimage.setImage(image);
  }

  private void createDelivery() {
    OfficeSupply officeSupply = dbr.getOfficeSupplyDAO().get(suppliesID);
    officeSupplyCart.addOfficeSupplyItem(officeSupply);
    stage.close();
  }
}
