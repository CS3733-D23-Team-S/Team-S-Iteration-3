package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.*;
import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PopupControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

public class MapEditorController {
  DataBaseRepository repo = DataBaseRepository.getInstance();

  // FXML elements for this page
  @FXML ComboBox<String> floorSelect;
  @FXML MFXButton addAndRemove, moveNode, addLocation;
  @FXML GesturePane mapPane;
  @FXML ToggleSwitch showEdges, showNames;

  // Map generation
  ImageView floor;
  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();

  enum MoveState {
    ADD_REMOVE,
    MOVE,
    REMOVE
  }

  // Keeps track of the current state of the application
  MoveState mode;

  // Images
  Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
  Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
  Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));
  Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
  Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));
  Floor currFloor = Floor.Floor1;

  // Drawing nodes and labels management
  double imageX, imageY; // Stores the location of mouse clicks
  Node nodeToCircle; // Stores the node of the
  Circle prevSelection; // Facilitates the deleting of circles on the map
  // Prevents null pointer errors when deleting circles. Invisible circle that is always drawn
  private final Circle deleteReferenceCircle = new Circle(0, 0, 0, Color.TRANSPARENT);

  HashMap<Circle, Node> listOfCircles =
      new HashMap<>(); // Linked a circle on the map to the associated node
  List<Text> listOfLabels = new ArrayList<>(); // Stores a list of all the node ID labels
  List<Text> listOfShortNames = new ArrayList<>(); // Stores a list of all the short names of nodes
  List<Line> lines = new ArrayList<>(); // Stores the list of lines that represents edges

  // Popover controllers
  PopOver popOver = new PopOver();
  AddLocationPopOverController addLocationPopOverController = new AddLocationPopOverController();
  AddNodePopOverController addNodePopOverController = new AddNodePopOverController();
  EditNodePopOverController editNodePopOverController = new EditNodePopOverController();

  public void initialize() throws IOException {
    initPopOver();
    floor = new ImageView(floor1);
    floor.setImage(floor1);
    stackPane.getChildren().addAll(floor, anchorPane);
    anchorPane.setBackground(Background.fill(Color.TRANSPARENT));
    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
    mapPane.setMaxScale(0.9);
    mapPane.zoomTo(.5, new Point2D(2000, 1500));
    floorSelect.getItems().add("Lower 2");
    floorSelect.getItems().add("Lower 1");
    floorSelect.getItems().add("Floor 1");
    floorSelect.getItems().add("Floor 2");
    floorSelect.getItems().add("Floor 3");

    showEdges.setOnMouseClicked(event -> drawEdges());
    showNames.setOnMouseClicked(event -> showShortNames());
    addLocationPopOverController.initialize(this);
    addNodePopOverController.initialize(this);
    editNodePopOverController.initialize(this);

    anchorPane.setOnContextMenuRequested(event -> editNodePopOverController.launchPopUp());
    addLocation.setOnMouseClicked(event -> addLocationPopOverController.launchPopUp());
    addAndRemove.setOnMouseClicked(
        event -> {
          addAndRemove.setStyle("-fx-background-color: #122E59");
          mode = MoveState.ADD_REMOVE;
        });
    moveNode.setOnMouseClicked(
        event -> {
          moveNode.setStyle("-fx-background-color: #122E59");
          mode = MoveState.MOVE;
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

  /**
   * Creates all the circle representations of the nodes on the current floor. Then it initializes
   * all the circles to make them usable and then also stores them in a hashmap to associate the
   * circle to the node it represents
   */
  private void generateFloorNodes() {
    anchorPane.getChildren().removeAll(listOfCircles.keySet());
    listOfCircles.clear();
    for (Node floorNode : repo.getNodeDAO().getAll()) {
      if (!floorNode.getFloor().equals(currFloor)) continue;
      Circle newCircle = new Circle(floorNode.getXCoord(), floorNode.getYCoord(), 10.0, Color.RED);
      initCircle(newCircle);
      listOfCircles.put(newCircle, floorNode);
      prevSelection = newCircle;
    }
    anchorPane.getChildren().addAll(listOfCircles.keySet());
  }

  private void drawEdges() {
    boolean edgeShow = showEdges.isSelected();
    anchorPane.getChildren().removeAll(lines);
    lines.clear();
    if (!edgeShow) {
      return;
    }
    for (Node floorNode : repo.getNodeDAO().getAll()) {
      if (!floorNode.getFloor().equals(currFloor)) continue;
      HashSet<Integer> currNeighbors = repo.getEdgeDAO().getNeighbors(floorNode.getNodeID());
      if (currNeighbors == null) continue;
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

  /** Draws the names of the displayed nodes the map */
  private void showShortNames() {
    if (!showNames.isSelected()) {
      anchorPane.getChildren().removeAll(listOfShortNames);
      listOfShortNames.clear();
      return;
    }
    for (Circle circle : listOfCircles.keySet()) {
      Node node = listOfCircles.get(circle);
      if (node.getFloor() != currFloor) continue;
      List<Move> list = repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID());
      if (list == null) continue;
      for (Move move : list) {
        if (move.getDate().isAfter(LocalDate.now())) break;
        Location loc = move.getLocation();
        if (loc.getNodeType() == NodeType.HALL) break;
        Text newText = new Text(loc.getShortName());
        //        System.out.println(loc.getShortName());
        newText.setFill(Color.WHITE);
        newText.setStroke(Color.BLACK);
        newText.setX(circle.getCenterX() - 20);
        newText.setY(circle.getCenterY() + 20);
        listOfShortNames.add(newText);
      }
    }
    anchorPane.getChildren().addAll(listOfShortNames);
  }

  /**
   * Sets all the lambda functions needed in order to make the circles actually responsive on the
   * map
   *
   * @param circle the circle to initialize
   */
  private void initCircle(Circle circle) {
    circle.setViewOrder(0);
    circle.setMouseTransparent(false);
    circle.setOnContextMenuRequested(
        event -> {
          System.out.println("Circled clicked");
          editNodePopOverController.launchPopUp();
          event.consume();
        });
    circle.setOnMouseClicked(
        event -> {
          circle.setStroke(Color.YELLOW);
          circle.setStrokeWidth(3);
          prevSelection.setStroke(Color.TRANSPARENT);
          prevSelection = circle;
          nodeToCircle = listOfCircles.get(circle);
        });
    circle.setOnMouseDragged(
        event -> {
          if (mode == MoveState.MOVE) {
            prevSelection = circle;
            int currX = (int) Math.round(event.getX());
            int currY = (int) Math.round(event.getY());
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
            event.consume();
          }
        });
    circle.setOnMouseReleased(
        event -> {
          if (mode == MoveState.MOVE && listOfCircles.get(prevSelection) != null) {
            repo.getNodeDAO().updateNodeLocation(listOfCircles.get(prevSelection));
            System.out.println("Sent node location update");
            event.consume();
          }
        });
  }

  /** Sets up the popover menu for the page before it gets set by other things */
  private void initPopOver() {
    popOver.setHeaderAlwaysVisible(true);
    popOver.setAutoHide(true);
    popOver.setOnAutoHide(
        event -> {
          anchorPane.getChildren().removeAll(listOfLabels);
          listOfLabels.clear();
          generateFloorNodes();
        });
    popOver.setPrefSize(10, 30);
    popOver.setMaxSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
    popOver.setMinSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
    popOver.setDetachable(false);
  }
}
