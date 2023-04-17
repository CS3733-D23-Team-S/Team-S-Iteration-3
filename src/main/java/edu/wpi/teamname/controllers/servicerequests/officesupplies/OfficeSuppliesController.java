package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupply;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class OfficeSuppliesController {
  public static int suppliesID;
  @FXML MFXButton viewcartbutton;
  @FXML MFXButton clearfilter;
  @FXML FlowPane flowpane;
  @FXML MFXScrollPane scrollpane;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    viewcartbutton.setOnMouseClicked(event -> Navigation.navigate(OFFICE_SUPPLIES_CART));

    clearfilter.setOnMouseClicked(event -> Navigation.navigate(OFFICE_SUPPLIES_DELIVERY));

    noFilter();
  }

  public void noFilter() {
    flowpane.getChildren().clear();
    for (OfficeSupply o : dbr.getOfficeSupplyDAO().getSupplies().values()) {
      Image image = new Image(Main.class.getResource(o.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(true);
      view.setFitHeight(150);
      view.setFitWidth(150);

      MFXButton btn1 = new MFXButton();
      btn1.setId(o.toString());
      btn1.setText(o.getName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);

      btn1.setOnMouseClicked(
          event -> {
            store(o.getId());
          });
    }
  }

  public void store(int x) {
    suppliesID = x;
    Navigation.launchPopUp(OFFICE_SUPPLIES_POPUP);
  }
}
