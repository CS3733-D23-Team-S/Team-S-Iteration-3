package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import static edu.wpi.teamname.controllers.servicerequests.officesupplies.OfficeSuppliesController.suppliesID;
import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupply;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupplyCart;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class OfficeSuppliesDetailsController {
  public static OfficeSupplyCart officeSuppliesCart = new OfficeSupplyCart(1);
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

    addtocartbutton.setOnMouseClicked(event -> Navigation.navigate(OFFICE_SUPPLIES_DELIVERY));
    backicon.setOnMouseClicked(event -> Navigation.navigate(OFFICE_SUPPLIES_DELIVERY));
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
    namelabel.setText(dbr.getOfficeSupplyDAO().get(suppliesID).getName());
    descriptionlabel.setText(dbr.getOfficeSupplyDAO().get(suppliesID).getDescription());
    pricelabel.setText("$" + dbr.getOfficeSupplyDAO().get(suppliesID).getPrice());
  }

  private void createDelivery() {
    dbConnection connection = dbConnection.getInstance();
    OfficeSupply officeSupply = dbr.getOfficeSupplyDAO().get(suppliesID);
    officeSupply.setQuantity(Integer.parseInt(quantityfield.getText()));
    // officeSupply.setMessage(custommessagefield.getText());
    // officeSuppliesCart.addOfficeSupplyItem(officeSupply);
    Navigation.navigate(OFFICE_SUPPLIES_DELIVERY);

    // room = .getText get room from drop down box in the thingy
    recipient = recipientfield.getText();
  }

  public void clearFields() {
    quantityfield.clear();
    sizefield.clear();
    custommessagefield.clear();
  }
}
