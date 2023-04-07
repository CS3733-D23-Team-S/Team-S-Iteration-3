package edu.wpi.teamname.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class FacilitiesStaffController {

  @FXML MFXButton facilitiesStaffToAdminButton;
  @FXML TableView facilitiesStaffTable;
  @FXML TableColumn roomCol;
  @FXML TableColumn eventNameCol;
  @FXML TableColumn setupCol;
  @FXML TableColumn reservationDateCol;
  @FXML TableColumn reservationTimeCol;
  @FXML TableColumn statusCol;

  public void initialize() {
    // facilitiesStaffToAdminButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ADMIN));
  }
}
