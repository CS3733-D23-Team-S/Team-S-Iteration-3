package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.IDataPack;
import edu.wpi.teamname.DAOs.orms.Edge;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;

public class QueueManager {

  DataBaseRepository repo = DataBaseRepository.getInstance();
  MapEditorController controller;
  List<IDataPack> queue;
  List<IDataPack> deleteQueue;
  Label selectedQueueItem;

  public QueueManager(MapEditorController controller) {
    deleteQueue = new ArrayList<>();
    queue = new ArrayList<>();
    this.controller = controller;
  }

  void addToDeleteQueue(IDataPack data) {
    Label label = new Label();
    String name = data.getClass().getSimpleName();
    if (name.equals("Edge")) {
      Edge edge = (Edge) data;
      label.setText(
          "Deleted "
              + name.toLowerCase()
              + " between "
              + edge.getStartNode().getNodeID()
              + " and "
              + edge.getEndNode().getNodeID());
    } else if (name.equals("Move")) {
      Move move = (Move) data;
      label.setText("Deleted " + move.toString());
    } else if (name.equals("Node")) {
      Node node = (Node) data;
      label.setText("Deleted " + node.toString());
    } else if (name.equals("Location")) {
      Location location = (Location) data;
      label.setText("Deleted " + location.toString());
    } else {
      return;
    }
    controller.queuePane.getChildren().add(label);
    deleteQueue.add(data);
  }

  void addNodeMoveToQueue(Node node) {
    Label label = new Label();
    label.setText("Moved node " + node.getNodeID() + " to another location");
    initQueueItem(label);
    controller.queuePane.getChildren().add(label);
    queue.add(node);
  }

  void addToQueue(IDataPack data) {
    Label label = new Label();
    String name = data.getClass().getSimpleName();
    if (name.equals("Edge")) {
      Edge edge = (Edge) data;
      label.setText(
          name
              + " between "
              + edge.getStartNode().getNodeID()
              + " and "
              + edge.getEndNode().getNodeID());
    } else if (name.equals("Move")) {
      Move move = (Move) data;
      label.setText(move.toString());
    } else if (name.equals("Node")) {
      Node node = (Node) data;
      label.setText(node.toString());
    } else if (name.equals("Location")) {
      Location location = (Location) data;
      label.setText(location.toString());
    } else {
      return;
    }
    controller.queuePane.getChildren().add(label);
    queue.add(data);
  }

  void initQueueItem(Label item) {
    item.setOnMouseClicked(
        event -> {
          selectedQueueItem = item;
          item.setStyle("-fx-border-color:\"122E59\"");
        });
  }

  List<Node> getCurrentNodeChanges() {
    List<Node> local = new ArrayList<>(List.copyOf(repo.getAllNodes()));
    for (IDataPack data : queue) {
      if (data.getClass().equals(Node.class)) {
        local.add((Node) data);
      }
    }
    for (IDataPack data : deleteQueue) {
      if (data.getClass().equals(Node.class)) {
        local.remove((Node) data);
      }
    }
    return local;
  }

  List<Edge> getCurrentEdgeChanges() {
    List<Edge> local = new ArrayList<>(List.copyOf(repo.getAllEdges()));
    for (IDataPack data : queue) {
      if (data.getClass().equals(Edge.class)) {
        local.add((Edge) data);
      }
    }
    for (IDataPack data : deleteQueue) {
      if (data.getClass().equals(Edge.class)) {
        local.remove((Edge) data);
      }
    }
    return local;
  }
}
