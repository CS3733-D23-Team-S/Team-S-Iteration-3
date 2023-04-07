package edu.wpi.teamname.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class KitchenStaffController {

  @FXML MFXButton kitchenStaffToAdminButton;
  @FXML TableView kitchenStaffTable;
  @FXML TableColumn orderIDCol;
  @FXML TableColumn orderDetailsCol;
  @FXML TableColumn specialRequestsCol;
  @FXML TableColumn timeOrderedCol;
  @FXML TableColumn etDeliveryCol;
  @FXML TableColumn statusCol;

  public void initialize() {
    // kitchenStaffToAdminButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ADMIN));

  }
}
