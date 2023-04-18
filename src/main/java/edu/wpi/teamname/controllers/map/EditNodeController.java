package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Edge;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class EditNodeController {

  DataBaseRepository repo = DataBaseRepository.getInstance();

  @FXML private Button addEdge;

  @FXML private Button addLocation;

  @FXML private VBox edgeBox;

  @FXML private SearchableComboBox<Integer> edgeField;

  @FXML private SearchableComboBox<String> locationField;

  @FXML private VBox locationBox;

  @FXML private VBox moveBox;

  @FXML private TextField nodeIDLabel;

  @FXML private Button submitUpdate;

  Node node;

  List<Integer> addedEdges = new ArrayList<>();
  List<String> addedLocations = new ArrayList<>();

  public EditNodeController() {}

  public void initialize() {
    for (Location loc : repo.getLocationDAO().getAll()) {
      locationField.getItems().add(loc.getLongName());
    }

    addEdge.setOnMouseClicked(
        event -> {
          int id = edgeField.getSelectionModel().getSelectedItem();
          if (repo.getNodeDAO().get(id) != null) {
            edgeBox.getChildren().add(new Label(String.valueOf(id)));
            addedEdges.add(id);
          }
        });
    addLocation.setOnMouseClicked(
        event -> {
          String longName = locationField.getValue();
          if (!longName.isEmpty()) {
            for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()))
              if (longName.equals(move.getLocationName())) return;
          }
          addedLocations.add(longName);
          locationBox.getChildren().add(new Label(longName));
        });
    submitUpdate.setOnMouseClicked(
        event -> {
          this.node.setNodeID(Integer.parseInt(nodeIDLabel.getText()));
          for (int id : addedEdges) {
            Edge newEdge = new Edge(this.node, repo.getNodeDAO().get(id));
            repo.getEdgeDAO().getNeighbors().get(this.node.getNodeID()).add(id);
            repo.getEdgeDAO().getNeighbors().get(id).add(this.node.getNodeID());
          }
          for (String location : addedLocations) {
            Location loc = repo.getLocationDAO().get(location);
            Move newMove = new Move(this.node, loc, LocalDate.now());
            repo.getMoveDAO().add(newMove);
          }
        });
  }

  private void update() {
    edgeField.getItems().clear();
    for (Node potentialEdge : repo.getNodeDAO().getAll()) {
      if (calcWeight(potentialEdge) < 50) edgeField.getItems().add(potentialEdge.getNodeID());
    }
    for (Location loc : repo.getLocationDAO().getAll()) {
      if (!locationField.getItems().contains(loc.getLongName()))
        locationField.getItems().add(loc.getLongName());
    }
  }

  private double calcWeight(Node node) {
    return Math.sqrt(
        Math.pow((this.node.getXCoord() - node.getXCoord()), 2)
            + Math.pow((this.node.getYCoord() - node.getYCoord()), 2));
  }

  public void setInfo(Node node) {
    this.node = node;
    update();
    nodeIDLabel.clear();
    edgeBox.getChildren().clear();
    moveBox.getChildren().clear();
    locationBox.getChildren().clear();
    nodeIDLabel.setText(String.valueOf(node.getNodeID()));
    if (repo.getEdgeDAO().getNeighbors().get(node.getNodeID()) == null) return;
    for (Integer neighbor : repo.getEdgeDAO().getNeighbors().get(node.getNodeID())) {
      edgeBox.getChildren().add(new Label(String.valueOf(neighbor)));
    }
    if (repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()) == null) return;
    for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID())) {
      moveBox
          .getChildren()
          .add(new Label(move.getLocationName() + " on " + move.getDate().toString()));
        locationBox.getChildren().add(new Label(move.getLocationName()));
    }
  }
}
