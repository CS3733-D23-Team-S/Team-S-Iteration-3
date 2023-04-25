package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Node;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;
import lombok.Setter;

public class AddNodePopOverController {

  DataBaseRepository repo = DataBaseRepository.getInstance();
  @Setter MapEditorController mainController;
  @FXML @Setter private GridPane addMenu;
  @FXML private Button addButton;
  @FXML private TextField buildingEnter, nodeIDEnter;
  @FXML private Label warning;
  private Point2D potentialNodLoc;

  public void initialize() {
    addButton.setOnMouseClicked(
        event -> {
          if (checkFieldsFilled()) {
            Node node =
                new Node(
                    Integer.parseInt(nodeIDEnter.getText()),
                    (int) Math.round(potentialNodLoc.getX()),
                    (int) Math.round(potentialNodLoc.getY()),
                    mainController.currFloor,
                    buildingEnter.getText());
            mainController.queueManager.addToQueue(node);
            mainController.generateFloorNodes();
          }
        });
  }

  public void launchPopup(ContextMenuEvent event) {
    mainController.popOver.setContentNode(this.addMenu);
    potentialNodLoc = new Point2D(event.getX(), event.getY());
    mainController.popOver.show(mainController.anchorPane, event.getScreenX(), event.getScreenY());
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
