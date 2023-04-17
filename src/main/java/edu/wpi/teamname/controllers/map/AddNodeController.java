package edu.wpi.teamname.controllers.map;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;

public class AddNodeController {

  @FXML @Getter private MFXButton addButton;

  @FXML @Getter private TextField buildingEnter;

  @FXML @Getter private TextField nodeIDEnter;

  public void initialize() {
    //		addButton.setOnMouseClicked(event -> return);
  }
}
