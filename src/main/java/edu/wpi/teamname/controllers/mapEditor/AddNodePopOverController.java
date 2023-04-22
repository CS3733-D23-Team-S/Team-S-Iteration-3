package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddNodePopOverController {

  DataBaseRepository repo = DataBaseRepository.getInstance();
  MapEditorController mainController;
  @FXML private Button addButton;
  @FXML private TextField buildingEnter, nodeIDEnter;
  @FXML private Label warning;
  GridPane addNodeMenu;

  public void AddNodeController(MapEditorController controller) {
    this.mainController = controller;
  }

  public void initialize(MapEditorController controller) throws IOException {
    this.mainController = controller;
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/addNodePopUp.fxml"));
    addNodeMenu = loader.load();
    mainController.popOver.setContentNode(addNodeMenu);
  }

  void launchPopUp() {
    mainController.popOver.setContentNode(addNodeMenu);
  }

  public boolean checkFieldsFilled() {
    if (buildingEnter.getText().isEmpty() || nodeIDEnter.getText().isEmpty()) {
      warning.setText("Error: Both fields must be filled in!");
      return false;
    }
    warning.setText("");
    return true;
  }
}
