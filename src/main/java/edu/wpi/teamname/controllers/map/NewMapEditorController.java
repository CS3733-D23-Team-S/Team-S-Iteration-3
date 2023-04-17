package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.App;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Floor;
import edu.wpi.teamname.DAOs.orms.Node;
import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PopupControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

public class NewMapEditorController {

  enum Move {
    ADD_REMOVE,
    MOVE,
    REMOVE
  }

  Node nodeToCircle;
  boolean editingNodes = true;
  boolean edgeShow = false;
  DataBaseRepository repo = DataBaseRepository.getInstance();
  Move mode;
  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();
  PopOver popOver = new PopOver();

  @FXML ComboBox<String> floorSelect;
  @FXML MFXButton addNode;
  @FXML MFXButton moveNode;
  @FXML MFXButton removeNode;
  @FXML GesturePane mapPane;
  @FXML ToggleSwitch editToggle;
  @FXML ToggleSwitch showEdges;
  @FXML GridPane addNodeMenu;
  ImageView floor;
  Floor currFloor = Floor.Floor1;
  Circle prevSelection;
  HashMap<Circle, Node> listOfCircles = new HashMap<>();
  List<Node> clonedNodes;
  AddNodeController addNodeController;
  List<Line> lines = new ArrayList<>();
  Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
  Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
  Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));
  Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
  Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));

  public void initialize() throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("views/addNodePopUp.fxml"));
    addNodeMenu = loader.load();
    addNodeController = loader.getController();
    //    loader.setLocation(App.class.getResource());
    initPopOver();
    clonedNodes = List.copyOf(repo.getNodeDAO().getAll());
    stackPane.setPrefSize(1200.0, 742.0);
    floor = new ImageView(floor1);
    floor.setImage(floor1);
    stackPane.getChildren().add(floor);
    stackPane.getChildren().add(anchorPane);
    anchorPane.setBackground(Background.fill(Color.TRANSPARENT));
    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
    mapPane.setMaxScale(0.9);
    mapPane.zoomTo(.5, new Point2D(2000, 1500));

    floorSelect.getItems().add("Lower 1");
    floorSelect.getItems().add("Lower 2");
    floorSelect.getItems().add("Floor 1");
    floorSelect.getItems().add("Floor 2");
    floorSelect.getItems().add("Floor 3");
    mapPane.setOnMouseClicked(
        event -> {
          if (event.isShiftDown()) {
            editingNodes = false;
            rightClickMap(event);
          }
        });
    showEdges.setOnMouseClicked(
        event -> {
          edgeShow = !edgeShow;
          drawEdges();
        });
    addNode.setOnMouseClicked(
        event -> {
          resetColors();
          addNode.setStyle("-fx-background-color: #122E59");
          mode = Move.ADD_REMOVE;
        });
    moveNode.setOnMouseClicked(
        event -> {
          resetColors();
          moveNode.setStyle("-fx-background-color: #122E59");
          mode = Move.MOVE;
        });
    removeNode.setOnMouseClicked(
        event -> {
          resetColors();
          removeNode.setStyle("-fx-background-color: #122E59");
          mode = Move.REMOVE;
        });
    generateFloorNodes();
    floorSelect.setOnAction(
        event -> {
          int index = floorSelect.getSelectionModel().getSelectedIndex();
          floor.setImage(swapFloor(index));
          mapPane.centreOn(new Point2D(2000, 1500));
          generateFloorNodes();
          anchorPane.getChildren().removeAll(lines);
          lines.clear();
          drawEdges();
        });
  }

  private void rightClickMap(javafx.scene.input.MouseEvent event) {
    updatePopOver(event);
  }

  public void addNode() {
    Node node = new Node(1, 1, 1, Floor.Floor1, "  ");
    repo.getNodeDAO().getAll().add(node);
    clonedNodes.add(node);
  }

  public void deleteNode() {
    Node node = new Node(1, 1, 1, Floor.Floor1, "  ");
    repo.getNodeDAO().getAll().remove(node);
    clonedNodes.remove(node);
  }

  public void addLocation() {}

  public void deleteLocation() {}

  private void generateFloorNodes() {
    anchorPane.getChildren().removeAll(listOfCircles.keySet());
    listOfCircles.clear();
    for (Node floorNode : clonedNodes) {
      if (!floorNode.getFloor().equals(currFloor)) continue;
      Circle newCircle = new Circle(floorNode.getXCoord(), floorNode.getYCoord(), 10.0, Color.RED);
      newCircle.setViewOrder(0);
      newCircle.setMouseTransparent(false);
      newCircle.setOnMouseClicked(
          event -> {
            circleHighlight(newCircle);
            updateNodePopOver(newCircle);
          });
      newCircle.setOnMouseDragged(event -> circleDrag(newCircle, event));
      newCircle.setOnMouseReleased(
          event -> {
            mapPane.setGestureEnabled(true);
            newCircle.setStroke(Color.TRANSPARENT);
          });
      listOfCircles.put(newCircle, floorNode);
      prevSelection = newCircle;
    }
    anchorPane.getChildren().addAll(listOfCircles.keySet());
  }

  private void circleHighlight(Circle circle) {
    circle.setStroke(Color.YELLOW);
    circle.setStrokeWidth(3);
    prevSelection.setStroke(Color.TRANSPARENT);
    prevSelection = circle;
    nodeToCircle = listOfCircles.get(circle);
  }

  private void rightClickedCircle(Circle circle) {}

  private void circleDrag(Circle circle, javafx.scene.input.MouseEvent event) {
    if (mode == Move.MOVE) {
      mapPane.setGestureEnabled(false);
      int currX = (int) event.getX();
      int currY = (int) event.getY();
      circle.setCenterX(currX);
      circle.setCenterY(currY);
      circle.setStroke(Color.YELLOW);
      circle.setStrokeWidth(3);
      Node temp = listOfCircles.get(circle);
      temp.setXCoord(currX);
      temp.setYCoord(currY);
      anchorPane.getChildren().removeAll(lines);
      lines.clear();
      drawEdges();
    }
  }

  private void drawEdges() {
    if (!edgeShow) {
      anchorPane.getChildren().removeAll(lines);
      lines.clear();
      return;
    }
    HashMap<Integer, HashSet<Integer>> edges = repo.getEdgeDAO().getNeighbors();
    for (Node floorNode : clonedNodes) {
      if (!floorNode.getFloor().equals(currFloor)) continue;
      HashSet<Integer> currNeighbors = edges.get(floorNode.getNodeID());
      for (Integer currNeighbor : currNeighbors) {
        Node curr = repo.getNodeDAO().get(currNeighbor);
        if (!curr.getFloor().equals(currFloor)) continue;
        Line line =
            new Line(
                floorNode.getXCoord(), floorNode.getYCoord(), curr.getXCoord(), curr.getYCoord());
        line.setFill(Color.BLACK);
        line.setStrokeWidth(5.0);
        line.setViewOrder(1);
        lines.add(line);
      }
    }
    anchorPane.getChildren().addAll(lines);
  }

  private Image swapFloor(int index) {
    switch (index) {
      case 0:
        currFloor = Floor.FloorL1;
        return floorL1;
      case 1:
        currFloor = Floor.FloorL2;
        return floorL2;
      case 2:
        currFloor = Floor.Floor1;
        return floor1;
      case 3:
        currFloor = Floor.Floor2;
        return floor2;
      case 4:
        currFloor = Floor.Floor3;
        return floor3;
    }
    return floor1;
  }

  private void resetColors() {
    addNode.setStyle("-fx-background-color: #1D3D94");
    moveNode.setStyle("-fx-background-color: #1D3D94");
    removeNode.setStyle("-fx-background-color: #1D3D94");
  }

  private void updatePopOver(javafx.scene.input.MouseEvent event) {
    popOver.setTitle("Add Node?");
    popOver.setContentNode(addNodeMenu);
    popOver.show(mapPane, event.getScreenX(), event.getScreenY());
  }

  private void initPopOver() {
    popOver.setTitle("Screen Information");
    popOver.setHeaderAlwaysVisible(true);
    popOver.setAutoHide(true);
    popOver.setPrefSize(10, 30);
    popOver.setMaxSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
    popOver.setMinSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
    popOver.setDetachable(false);
  }

  private void updateNodePopOver(Circle circle) {
    if (mode == Move.ADD_REMOVE) {
      popOver.setTitle("Node Information");

      popOver.show(circle);
      editingNodes = true;
    }
  }
}
