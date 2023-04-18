package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class EditNodeController {

  DataBaseRepository repo = DataBaseRepository.getInstance();
  @FXML private VBox parent;
  @FXML private TextField nodeIDLabel;
  @FXML private VBox edgeBox;

  @FXML private Accordion infoAccordian;

  @FXML private VBox locationBox;

  @FXML private VBox moveBox;

  @FXML private Button submitUpdate;

  public EditNodeController() {}

  public void setInfo(Node node) {
    nodeIDLabel.clear();
    edgeBox.getChildren().clear();
    moveBox.getChildren().clear();
    locationBox.getChildren().clear();
    nodeIDLabel.setText(String.valueOf(node.getNodeID()));
    for (Integer neighbor : repo.getEdgeDAO().getNeighbors().get(node.getNodeID())) {
      edgeBox.getChildren().add(new Label(String.valueOf(neighbor)));
    }
    for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID())) {
      moveBox
          .getChildren()
          .add(new Label(move.getLocationName() + " on " + move.getDate().toString()));
      if (move.getDate().toEpochDay() < LocalDate.now().toEpochDay()) {
        locationBox.getChildren().add(new Label(move.getLocationName()));
      }
    }
  }
}
