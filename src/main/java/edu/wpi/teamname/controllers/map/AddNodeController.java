package edu.wpi.teamname.controllers.map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;

public class AddNodeController {

  @FXML @Getter private Button addButton;

  @FXML @Getter private TextField buildingEnter;

  @FXML @Getter private TextField nodeIDEnter;
  @FXML @Getter private Label warning;

  public AddNodeController() {}

  public boolean checkFieldsFilled() {
    if (buildingEnter.getText().isEmpty() || nodeIDEnter.getText().isEmpty()) {
      warning.setText("Error: Both fields must be filled in!");
      return false;
    }
    warning.setText("");
    return true;
  }
}
