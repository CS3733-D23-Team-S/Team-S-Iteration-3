package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
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
  public static int supplycounter;

  public void initialize() {
    showInfo();
    addcartbutton.setOnMouseClicked(event -> createDelivery());

    supplycounter = 1;
    incrementbutton.setOnMouseClicked(event -> addquantity());
    decrementbutton.setOnMouseClicked(event -> subtractquantity());
  }

  private void showInfo() {
    officesupplyname.setText(
        dbr.getOfficeSupplyDAO().get(OfficeSuppliesController.suppliesID).getName());
    officesupplydescription.setText(
        dbr.getOfficeSupplyDAO().get(OfficeSuppliesController.suppliesID).getDescription());

    PriceText.setText(
        String.valueOf(
            dbr.getOfficeSupplyDAO().get(OfficeSuppliesController.suppliesID).getPrice()));

    Image image =
        new Image(
            Main.class
                .getResource(
                    dbr.getOfficeSupplyDAO().get(OfficeSuppliesController.suppliesID).getImage())
                .toString());
    officesupplyimage.setImage(image);
  }

  private void createDelivery() {
    OfficeSupply officeSupply = dbr.getOfficeSupplyDAO().get(OfficeSuppliesController.suppliesID);
    officeSupply.setQuantity(supplycounter);
    OfficeSuppliesController.Cart.addOfficeSupplyItem(officeSupply);
    stage.close();
  }

  public void addquantity() {
    supplycounter++;
    quantitylabel.setText(Integer.toString(supplycounter));
  }

  public void subtractquantity() {
    supplycounter--;
    if (supplycounter < 1) {
      supplycounter = 1;
    }

    quantitylabel.setText(Integer.toString(supplycounter));
  }
}
