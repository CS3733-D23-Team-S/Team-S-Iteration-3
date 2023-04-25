package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.controllers.PopUpController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import org.controlsfx.control.SearchableComboBox;

public class PathfindingMovePopUpController extends PopUpController {

  @FXML SearchableComboBox locationBox;
  @FXML MFXDatePicker datePick;
  @FXML MFXComboBox nodeCombo;

  @FXML MFXButton cancelAdd;

  @FXML MFXButton confirmAdd;

  public void initialize() {

    cancelAdd.setOnMouseClicked(event -> stage.close());
    confirmAdd.setOnMouseClicked(event -> stage.close());
  }
}
