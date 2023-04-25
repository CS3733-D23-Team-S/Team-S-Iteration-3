package edu.wpi.teamname.controllers.map;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NodeDetailsController {
  @FXML MFXButton backButton;
  @FXML Label nodeIDLBL;
  @FXML Label xcoordLBL;
  @FXML Label ycoordLBL;
  @FXML Label floorLBL;
  @FXML Label buildingLBL;

  public void initialize() {
    //    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.BETTER_MAP_EDITOR));

    nodeIDLBL.setText(String.valueOf(BetterMapEditorController.thisNode.getNodeID()));
    xcoordLBL.setText(String.valueOf(BetterMapEditorController.thisNode.getXCoord()));
    ycoordLBL.setText(String.valueOf(BetterMapEditorController.thisNode.getYCoord()));
    floorLBL.setText(BetterMapEditorController.thisNode.getFloor().name());
    buildingLBL.setText(String.valueOf(BetterMapEditorController.thisNode.getBuilding()));
  }
}
