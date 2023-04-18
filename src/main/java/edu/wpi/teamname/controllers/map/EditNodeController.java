package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Edge;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;
import java.time.LocalDate;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class EditNodeController {

  DataBaseRepository repo = DataBaseRepository.getInstance();

  @FXML private Button addEdge;

  @FXML private Button addLocation;

  @FXML private VBox edgeBox;
  @FXML private CheckBox checkBox;
  private boolean showClosest;

  @FXML private SearchableComboBox<Integer> edgeField;

  @FXML private SearchableComboBox<String> locationField;

  @FXML private VBox locationBox;

  @FXML private VBox moveBox;

  @FXML private TextField nodeIDLabel;

  @FXML private Button submitUpdate;
  @FXML private DatePicker dateSelect;

  Node node;

  List<Integer> addedEdges = new ArrayList<>();
  List<Move> addedMoves = new ArrayList<>();

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
            edgeField.getItems().remove(id);
          }
        });
    addLocation.setOnMouseClicked(
        event -> {
          String longName = locationField.getValue();
          if (!longName.isEmpty()) {
            for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()))
              if (longName.equals(move.getLocationName())) return;
          }
          LocalDate date = dateSelect.getValue();
          Move newMove = new Move(this.node, repo.getLocationDAO().get(longName), date);
          addedMoves.add(newMove);
          locationBox.getChildren().add(new Label(longName));
          Label label =
              new Label(newMove.getLocationName() + " on " + newMove.getDate().toString());
          label.setWrapText(true);
          label.maxWidth(70);
          moveBox.getChildren().add(label);
          update();
        });
    checkBox.setOnMouseClicked(
        event -> {
          showClosest = checkBox.isSelected();
          updateEdgeBox();
        });
    submitUpdate.setOnMouseClicked(
        event -> {
          System.out.println("Tried to update");
          repo.getNodeDAO()
              .updateNodeID(this.node.getNodeID(), Integer.parseInt(nodeIDLabel.getText()));
          for (int id : addedEdges) {
            Edge newEdge = new Edge(this.node, repo.getNodeDAO().get(id));
            repo.getEdgeDAO().add(newEdge);
          }
          for (Move move : addedMoves) {
            repo.getMoveDAO().add(move);
          }
        });
  }

  private void update() {
    addedEdges.clear();
    addedMoves.clear();
    for (Node potentialEdge : repo.getNodeDAO().getAll()) {
      if (calcWeight(potentialEdge) < 60
          && !edgeField.getItems().contains(potentialEdge.getNodeID()))
        edgeField.getItems().add(potentialEdge.getNodeID());
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

  private void updateEdgeBox() {
    if (showClosest) {
      edgeField.setPromptText("Nearest Nodes");
      for (Node potentialEdge : repo.getNodeDAO().getAll()) {
        if (calcWeight(potentialEdge) < 60) edgeField.getItems().add(potentialEdge.getNodeID());
      }
    } else {
      edgeField.setPromptText("All Nodes");
      for (Node potentialEdge : repo.getNodeDAO().getAll())
        edgeField.getItems().add(potentialEdge.getNodeID());
    }
  }

  public void setInfo(Node node) {
    this.node = node;
    update();
    nodeIDLabel.clear();
    try {
      edgeBox.getChildren().clear();
      moveBox.getChildren().clear();
      locationBox.getChildren().clear();
    } catch (IndexOutOfBoundsException ignored) {
    }
    nodeIDLabel.setText(String.valueOf(node.getNodeID()));
    if (repo.getEdgeDAO().getNeighbors().get(node.getNodeID()) == null) return;
    for (Integer neighbor : repo.getEdgeDAO().getNeighbors().get(node.getNodeID())) {
      edgeBox.getChildren().add(new Label(String.valueOf(neighbor)));
    }
    if (repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()) == null) return;
    List<String> sortedLocations = new ArrayList<>();
    Label label = new Label();
    for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID())) {
      label.setText(move.getLocationName() + " on " + move.getDate().toString());
      label.setWrapText(true);
      label.maxWidth(70);
      moveBox.getChildren().add(label);
      sortedLocations.add(
          move.getLocationName() + " - " + move.getLocation().getNodeType().toString());
    }
    Collections.sort(sortedLocations);
    for (String name : sortedLocations) {
      label.setText(name);
      locationBox.getChildren().add(label);
    }
  }
}
