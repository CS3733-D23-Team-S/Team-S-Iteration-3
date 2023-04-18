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
            updateEdgeBox();
          }
        });
    addLocation.setOnMouseClicked(
        event -> {
          String longName = locationField.getValue();
          LocalDate date = dateSelect.getValue();
          if (!longName.isEmpty()) {
            if (repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()) != null)
              for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()))
                if (longName.equals(move.getLocationName()) && date.isEqual(move.getDate())) return;
          }
          Move newMove = new Move(this.node, repo.getLocationDAO().get(longName), date);
          addedMoves.add(newMove);
          System.out.println("Added the move: " + newMove);
          locationBox.getChildren().add(new Label(longName));
          Label label =
              new Label(newMove.getLocationName() + " on " + newMove.getDate().toString());
          label.setWrapText(true);
          label.maxWidth(70);
          moveBox.getChildren().add(label);
        });
    checkBox.setOnMouseClicked(
        event -> {
          showClosest = checkBox.isSelected();
          updateEdgeBox();
        });
    submitUpdate.setOnMouseClicked(
        event -> { //					System.out.println("Tried to update");
          for (int id : addedEdges) {
            Edge newEdge = new Edge(this.node, repo.getNodeDAO().get(id));
            System.out.println(newEdge);
            repo.getEdgeDAO().add(newEdge);
          }
          for (Move move : addedMoves) {
            System.out.println("adding the move " + move.toString());
            repo.getMoveDAO().add(move);
          }
          if (node.getNodeID() != Integer.parseInt(nodeIDLabel.getText()))
            repo.getNodeDAO()
                .updateNodeID(this.node.getNodeID(), Integer.parseInt(nodeIDLabel.getText()));
          this.node.setNodeID(Integer.parseInt(nodeIDLabel.getText()));
          DataBaseRepository.getInstance().forceUpdate();
        });
  }

  private void update() {
    edgeField.getItems().removeAll();
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
    edgeField.getItems().removeAll();
    if (!showClosest) {
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
    addedEdges.clear();
    addedMoves.clear();
    update();
    nodeIDLabel.clear();
    edgeBox.getChildren().clear();
    moveBox.getChildren().clear();
    locationBox.getChildren().clear();
    nodeIDLabel.setText(String.valueOf(node.getNodeID()));
    if (repo.getEdgeDAO().getNeighbors().get(node.getNodeID()) != null)
      for (Integer neighbor : repo.getEdgeDAO().getNeighbors().get(node.getNodeID())) {
        edgeBox.getChildren().add(new Label(String.valueOf(neighbor)));
      }
    if (repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()) != null) {
      List<String> sortedLocations = new ArrayList<>();
      for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID())) {
        Label label = new Label();
        label.setText(move.getLocationName() + " on " + move.getDate().toString());
        label.setWrapText(true);
        label.maxWidth(70);
        moveBox.getChildren().add(label);
        sortedLocations.add(
            move.getLocationName() + " - " + move.getLocation().getNodeType().toString());
      }
      Collections.sort(sortedLocations);
      for (String name : sortedLocations) {
        Label label = new Label();
        label.setText(name);
        locationBox.getChildren().add(label);
      }
    }
  }
}
